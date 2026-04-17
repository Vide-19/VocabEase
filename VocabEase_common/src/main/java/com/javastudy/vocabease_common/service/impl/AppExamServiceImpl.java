package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.dto.AppExamPostDto;
import com.javastudy.vocabease_common.entity.dto.AppQuestionSubmitDto;
import com.javastudy.vocabease_common.entity.enums.*;
import com.javastudy.vocabease_common.entity.po.*;
import com.javastudy.vocabease_common.entity.query.*;
import com.javastudy.vocabease_common.entity.vo.AppExamVO;
import com.javastudy.vocabease_common.entity.vo.ExamQuestionVO;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.*;
import com.javastudy.vocabease_common.service.AppExamService;
import com.javastudy.vocabease_common.utils.CopyUtil;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 测试表 业务接口实现
 */
@Service("appExamService")
public class AppExamServiceImpl implements AppExamService {

    @Resource
    private AppExamMapper<AppExam, AppExamQuery> appExamMapper;
    @Resource
    private AppQuestion4examMapper<AppQuestion4exam, AppQuestion4examQuery> appQuestion4examMapper;
    @Resource
    private QuestionMapper<Question, QuestionQuery> questionMapper;
    @Resource
    private Item4questionMapper<Item4question, Item4questionQuery> item4questionMapper;
    @Resource
    private AppCollectMapper<AppCollect, AppCollectQuery> appCollectMapper;
    @Resource
    private AppWrongExamMapper<AppWrongExam, AppWrongExamQuery> appWrongExamMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<AppExam> findListByParam(AppExamQuery param) {
        return this.appExamMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AppExamQuery param) {
        return this.appExamMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<AppExam> findListByPage(AppExamQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<AppExam> list = this.findListByParam(param);
        PaginationResultVO<AppExam> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(AppExam bean) {
        return this.appExamMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<AppExam> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appExamMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<AppExam> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appExamMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(AppExam bean, AppExamQuery param) {
        StringTools.checkParam(param);
        return this.appExamMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(AppExamQuery param) {
        StringTools.checkParam(param);
        return this.appExamMapper.deleteByParam(param);
    }

    /**
     * 根据ExamId获取对象
     */
    @Override
    public AppExam getAppExamByExamId(Integer examId) {
        return this.appExamMapper.selectByExamId(examId);
    }

    /**
     * 根据ExamId修改
     */
    @Override
    public Integer updateAppExamByExamId(AppExam bean, Integer examId) {
        return this.appExamMapper.updateByExamId(bean, examId);
    }

    /**
     * 根据ExamId删除
     */
    @Override
    public Integer deleteAppExamByExamId(Integer examId) {
        return this.appExamMapper.deleteByExamId(examId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppExam addExam(String categoryIds, AppAccountDto dto) {
        // 1. 查询当前用户【尚未完成】的考试题目（即之前开始但未提交的题目）
        AppQuestion4examQuery query = new AppQuestion4examQuery();
        query.setUserId(dto.getUserId());
        query.setResult(ExamStatusEnum.NO_FINISH.getStatus());
        List<AppQuestion4exam> questionNotFinishList = this.appQuestion4examMapper.selectList(query);
        // 2. 提取这些未完成题目的 ID 列表
        List<Integer> idNotFinishList = questionNotFinishList.stream().map(AppQuestion4exam::getQuestionId).toList();
        // 3. 构建题目查询条件：
        //    - 指定分类（categoryIds，逗号分隔）
        //    - 排除已存在的未完成题目（避免重复）
        //    - 随机排序（rand()）
        //    - 只查已发布题目（status = IS_POST）
        //    - 分页：第一页，最多50题
        QuestionQuery questionQuery = new QuestionQuery();
        questionQuery.setCategoryIds(categoryIds.split(","));
        questionQuery.setQuestionIdList(idNotFinishList);
        questionQuery.setOrderBy("rand()");
        questionQuery.setStatus(PostStatusEnum.IS_POST.getStatus());
        questionQuery.setPageNo(1);
        questionQuery.setSimplePage(new SimplePage(0, PageSize.SIZE50.getSize()));
        // 4. 执行查询，获取新题目
        List<Question> question = this.questionMapper.selectList(questionQuery);
        if (question.isEmpty())
            throw new BusinessException("恭喜你，该分类所有问题已完成");
        // 5. 创建新的考试记录（AppExam）
        AppExam examNew = new AppExam();
        examNew.setUserId(dto.getUserId());
        examNew.setNickName(dto.getNickName());
        examNew.setCreateTime(new Date());
        examNew.setStatus(ExamStatusEnum.NO_FINISH.getStatus());
        this.appExamMapper.insert(examNew);
        //测试题目
        List<AppQuestion4exam> questionReturnList = new ArrayList<>();
        //未完成+新题目？👇
        for (AppQuestion4exam q : questionNotFinishList) {
            AppQuestion4exam questionNew = new AppQuestion4exam();
            questionNew.setQuestionId(q.getQuestionId());
            questionNew.setExamId(examNew.getExamId());
            questionNew.setUserId(dto.getUserId());
            questionNew.setResult(ExamStatusEnum.NO_FINISH.getStatus());
            questionReturnList.add(questionNew);
        }
        // 7. 批量插入这些“复用”的题目关联记录
        if (!questionReturnList.isEmpty())
            this.appQuestion4examMapper.insertBatch(questionReturnList);
        return examNew;
    }

    @Override
    public AppExam checkAppExam(AppAccountDto dto, Integer examId) {
        AppExam appExam = this.appExamMapper.selectByExamId(examId);
        if (appExam == null || !Objects.equals(appExam.getUserId(), dto.getUserId()))
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        return appExam;
    }

    @Override
    public List<ExamQuestionVO> getAppExamQuestion(AppQuestion4examQuery appQuestion4examQuery) {
        // 1. 查询当前考试下的所有题目关联记录（AppQuestion4exam）
        List<AppQuestion4exam> appQuestion4examList = this.appQuestion4examMapper.selectList(appQuestion4examQuery);
        // 2. 如果需要显示用户答案（如查看历史答题），构建 Map 便于快速查找
        Map<Integer, AppQuestion4exam> appQuestion4examMap = new HashMap<>();
        if (appQuestion4examQuery.getShowAnswer() != null && appQuestion4examQuery.getShowAnswer())
            appQuestion4examMap = appQuestion4examList.stream().
                    collect(Collectors.toMap(AppQuestion4exam::getQuestionId, Function.identity(), (data1, data2) -> data2));
        // 3. 提取所有题目 ID（转为 String，因 QuestionQuery 使用 String[]）
        List<String> questionIdList = appQuestion4examList.stream().map(item ->
                item.getQuestionId().toString()).toList();
        // 4. 查询题目详情（Question）
        QuestionQuery questionQuery = new QuestionQuery();
        questionQuery.setQueryAnswer(appQuestion4examQuery.getShowAnswer());
        questionQuery.setQuestionIds(questionIdList.toArray(new String[questionIdList.size()]));
        List<Question> questionList = this.questionMapper.selectList(questionQuery);
        // 5. 转换为 VO 对象
        List<ExamQuestionVO> examQuestionVOList = CopyUtil.copyList(questionList, ExamQuestionVO.class);
        // 6. 查询每个题目的选项（Item4question）
        Item4questionQuery i4qQuery = new Item4questionQuery();
        i4qQuery.setQuestionIdList(questionIdList);
        List<Item4question> item4questionList = this.item4questionMapper.selectList(i4qQuery);
        Map<Integer, List<Item4question>> item4questionMap = item4questionList.stream().
                collect(Collectors.groupingBy(Item4question::getQuestionId));
        // 7. 查询用户是否收藏了这些题目
        AppCollectQuery appCollectQuery = new AppCollectQuery();
        appCollectQuery.setObjectIdList(questionIdList);
        appCollectQuery.setUserId(appQuestion4examQuery.getUserId());
        appCollectQuery.setCollectType(CollectTypeEnum.QUESTION.getType());
        List<AppCollect> appCollectList = this.appCollectMapper.selectList(appCollectQuery);
        Map<String, AppCollect> appCollectMap = appCollectList.stream().
                collect(Collectors.toMap(AppCollect::getObjectId, Function.identity(), (data1, data2) -> data2));
        // 8. 组装最终 VO
        for (ExamQuestionVO item : examQuestionVOList) {
            // 8.1 设置收藏状态
            item.setCollect(appCollectMap.get(item.getQuestionId().toString()) != null);
            // 8.2 设置考试ID
            item.setExamId(appQuestion4examQuery.getExamId());
            // 8.3 关联选项
            List<Item4question> questionItemList = item4questionMap.get(item.getQuestionId());
            item.setItemList(questionItemList);
            // 8.4 如果需要显示用户答案，则填充 result 和 userAnswer
            if (!appQuestion4examQuery.getShowAnswer())
                continue;
            AppQuestion4exam appQuestion4exam = appQuestion4examMap.get(item.getQuestionId());
            if (appQuestion4exam != null) {
                item.setId(appQuestion4exam.getId());
                item.setResult(appQuestion4exam.getResult());
                item.setUserAnswer(appQuestion4exam.getAnswer());
            }
        }
        return examQuestionVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppExam endExam(AppAccountDto accountDto, AppExamPostDto examPostDto) {
        // 1. 基础校验（用户、状态等）
        AppExam appExam = this.checkAppExam(accountDto, examPostDto.getExamId());
        if (appExam == null || !Objects.equals(appExam.getUserId(), accountDto.getUserId()))
            throw new BusinessException(ResponseCodeEnum.CODE_400);

        if (!appExam.getStatus().equals(ExamStatusEnum.NO_FINISH.getStatus()))
            throw new BusinessException("该测试已被提交");

        List<AppQuestionSubmitDto> userAnswerList = examPostDto.getAppQuestion4examList();
        if (userAnswerList == null || userAnswerList.isEmpty())
            throw new BusinessException(ResponseCodeEnum.CODE_400);

        // 2. 查询题目与标准答案
        AppQuestion4examQuery query = new AppQuestion4examQuery();
        query.setExamId(examPostDto.getExamId());
        List<AppQuestion4exam> questionListDB = this.appQuestion4examMapper.selectList(query);

        List<String> questionIdDB = questionListDB.stream().map(item ->
                item.getQuestionId().toString()).toList();

        QuestionQuery questionQuery = new QuestionQuery();
        questionQuery.setQuestionIds(questionIdDB.toArray(new String[questionIdDB.size()]));
        questionQuery.setQueryAnswer(true);
        List<Question> questionList = this.questionMapper.selectList(questionQuery);

        Map<Integer, Question> itemMap = questionList.stream().collect(
                Collectors.toMap(Question::getQuestionId, Function.identity(), (data1, data2) -> data2));

        int correctCount = 0;
        int totalQuestions = questionList.size();

        // 3. 准备一个列表，用于存放转换后的数据库实体对象
        List<AppQuestion4exam> saveEntityList = new ArrayList<>();

        // 4. 遍历处理
        for (AppQuestionSubmitDto submitDto : userAnswerList) {
            Question question = itemMap.get(submitDto.getQuestionId());
            if (question == null)
                throw new BusinessException(ResponseCodeEnum.CODE_400);

            // --- 核心修改：数据格式转换 ---
            Object answerObj = submitDto.getAnswer();
            String dbAnswerFormat = "";

            if (answerObj instanceof List) {
                List<?> list = (List<?>) answerObj;
                if (!list.isEmpty())
                    dbAnswerFormat = String.join(",", list.stream().map(Object::toString).toArray(String[]::new));
            } else if (answerObj instanceof String)
                dbAnswerFormat = (String) answerObj;

            // --- 核心修改：创建数据库实体对象 ---
            AppQuestion4exam entity = new AppQuestion4exam();

            // 拷贝基础数据
            entity.setId(submitDto.getId());
            entity.setQuestionId(submitDto.getQuestionId());
            entity.setAnswer(dbAnswerFormat); // 设置格式化后的答案
            entity.setExamId(appExam.getExamId());
            entity.setUserId(accountDto.getUserId());

            // 判分
            if (question.getAnswer().equals(dbAnswerFormat)) {
                entity.setResult(ExamStatusEnum.TRUE.getStatus());
                correctCount++;
            } else
                entity.setResult(ExamStatusEnum.FALSE.getStatus());

            saveEntityList.add(entity);
        }

        // 5. 计算分数
        int finalScore = 0;
        if (totalQuestions > 0)
            finalScore = (correctCount * 100) / totalQuestions;

        // 6. 批量保存答题记录
        this.appQuestion4examMapper.insertOrUpdateBatch(saveEntityList);

        // 7. 更新考试主表状态和分数
        Date currentDate = new Date();
        AppExam updateExam = new AppExam();
        updateExam.setStatus(ExamStatusEnum.IS_FINISH.getStatus());
        updateExam.setEndTime(currentDate);
        updateExam.setRemark(examPostDto.getRemark());
        updateExam.setScore(finalScore);

        AppExamQuery appExamQuery = new AppExamQuery();
        appExamQuery.setExamId(appExam.getExamId());
        appExamQuery.setUserId(accountDto.getUserId());
        appExamQuery.setStatus(ExamStatusEnum.NO_FINISH.getStatus());
        Integer count = this.appExamMapper.selectCount(appExamQuery);
        if (count == 0)
            throw new BusinessException("测试提交失败");

        // ==========================================
        // 8. 错题记录逻辑优化 (核心修改部分)
        // ==========================================

        // 8.1 提取本次考试中答错的题目ID列表
        List<Integer> currentWrongQuestionIds = saveEntityList.stream()
                .filter(q -> q.getResult() != null && q.getResult().equals(ExamStatusEnum.FALSE.getStatus()))
                .map(AppQuestion4exam::getQuestionId)
                .collect(Collectors.toList());

        if (!currentWrongQuestionIds.isEmpty()) {
            Date now = new Date();

            // 8.2 查询数据库中该用户已有的错题记录 (只查本次答错的题)
            // 注意：这里假设你的 BaseMapper 支持 selectList 和类似的 Query 对象
            AppWrongExamQuery existQuery = new AppWrongExamQuery();
            existQuery.setUserId(accountDto.getUserId());
            // 将 Integer 列表转换为 String 数组以匹配 Query
            existQuery.setQuestionIds(currentWrongQuestionIds.stream().map(String::valueOf).toArray(String[]::new));

            List<AppWrongExam> existWrongList = this.appWrongExamMapper.selectList(existQuery);

            // 8.3 将数据库已有的错题转为 Map，方便快速查找 (Key: questionId, Value: AppWrongExam实体)
            Map<Integer, AppWrongExam> existWrongMap = existWrongList.stream()
                    .collect(Collectors.toMap(AppWrongExam::getQuestionId, Function.identity()));

            List<AppWrongExam> saveWrongList = new ArrayList<>(); // 用于存放需要新增的记录
            List<AppWrongExam> updateWrongList = new ArrayList<>(); // 用于存放需要更新的记录

            // 8.4 遍历本次答错的题目，决定是新增还是更新
            for (AppQuestion4exam userRecord : saveEntityList) {
                if (userRecord.getResult() != null && userRecord.getResult().equals(ExamStatusEnum.FALSE.getStatus())) {
                    Integer qId = userRecord.getQuestionId();
                    AppWrongExam existItem = existWrongMap.get(qId);

                    if (existItem != null) {
                        // --- 情况A：数据库已存在该错题 -> 准备更新 ---
                        AppWrongExam toUpdate = new AppWrongExam();
                        toUpdate.setId(existItem.getId());
                        toUpdate.setExamId(appExam.getExamId()); // 更新所属试卷（可选，看需求）
                        toUpdate.setQuestionId(qId);
                        toUpdate.setWrongTime(now);             // 更新最后错误时间
                        toUpdate.setUserId(accountDto.getUserId());
                        // 错误次数 + 1
                        Integer currentCount = existItem.getWrongCount();
                        if (currentCount == null)
                            currentCount = 0;
                        toUpdate.setWrongCount(currentCount + 1);

                        updateWrongList.add(toUpdate);
                    } else {
                        if (qId == null)
                            continue; // 跳过这条无效数据，不要存入数据库
                        // --- 情况B：数据库不存在该错题 -> 准备新增 ---
                        AppWrongExam toSave = new AppWrongExam();
                        toSave.setUserId(accountDto.getUserId());
                        toSave.setExamId(appExam.getExamId());
                        toSave.setQuestionId(qId);
                        toSave.setWrongTime(now);
                        toSave.setUserAnswer(userRecord.getAnswer());
                        toSave.setIsReviewed(0);
                        toSave.setWrongCount(1); // 首次错误，次数为1

                        saveWrongList.add(toSave);
                    }
                }
            }

            // 8.5 执行批量操作
            if (!saveWrongList.isEmpty())
                this.appWrongExamMapper.insertBatch(saveWrongList);
            if (!updateWrongList.isEmpty())
                this.appWrongExamMapper.updateBatch(updateWrongList);
        }

        // 9. 执行更新考试主表
        Integer update = this.appExamMapper.updateByParam(updateExam, appExamQuery);
        if (update != 1)
            throw new BusinessException(ResponseCodeEnum.CODE_400);

        // 10. 返回结果
        appExam.setScore(finalScore);
        appExam.setEndTime(currentDate);
        appExam.setStatus(ExamStatusEnum.IS_FINISH.getStatus());
        return appExam;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelExam(AppAccountDto dto, Integer examId) {
        AppExam appExam = this.checkAppExam(dto, examId);
        AppQuestion4examQuery query = new AppQuestion4examQuery();
        query.setExamId(examId);
        query.setUserId(appExam.getUserId());
        this.appQuestion4examMapper.deleteByParam(query);
        this.appExamMapper.deleteByExamId(examId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppExamVO generateExamPaper(AppAccountDto userDto, Integer difficulty, Integer pageIndex) {
        int currentPageNo = (pageIndex != null ? pageIndex : 0) + 1;
        int pageSize = PageSize.SIZE5.getSize();
        if (difficulty == 2)
            pageSize = PageSize.SIZE8.getSize();
        else if (difficulty == 3)
            pageSize = PageSize.SIZE10.getSize();
        QuestionQuery questionQuery = new QuestionQuery();
        questionQuery.setLevel(difficulty); // 核心：筛选指定难度的题目
        questionQuery.setStatus(PostStatusEnum.IS_POST.getStatus()); // 只查已发布的
        questionQuery.setOrderBy("question_id ASC");
        questionQuery.setSimplePage(new SimplePage(currentPageNo * pageSize, pageSize)); // 假设最多50题

        List<Question> questionList = this.questionMapper.selectList(questionQuery);

        if (questionList.isEmpty())
            throw new BusinessException("该难度下暂无题目，请联系管理员");

        // 2. 创建新的考试记录 (AppExam)
        AppExam examNew = new AppExam();
        examNew.setUserId(userDto.getUserId());
        examNew.setNickName(userDto.getNickName());
        examNew.setCreateTime(new Date());
        examNew.setStartTime(new Date());
        examNew.setStatus(ExamStatusEnum.NO_FINISH.getStatus());
        this.appExamMapper.insert(examNew);

        // 3. 构建考试题目关联记录 (AppQuestion4exam)
        List<AppQuestion4exam> questionLinkList = new ArrayList<>();
        for (Question q : questionList) {
            AppQuestion4exam link = new AppQuestion4exam();
            link.setQuestionId(q.getQuestionId());
            link.setExamId(examNew.getExamId());
            link.setUserId(userDto.getUserId());
            link.setResult(ExamStatusEnum.NO_FINISH.getStatus()); // 未作答状态
            questionLinkList.add(link);
        }

        // 4. 批量插入关联记录
        if (!questionLinkList.isEmpty())
            this.appQuestion4examMapper.insertBatch(questionLinkList);

        // 5. 构建返回给前端的 VO (包含考试信息和题目列表)
        AppExamVO examVO = CopyUtil.copy(examNew, AppExamVO.class);

        // 复用 getAppExamQuestion 逻辑来组装题目详情（包含选项）
        AppQuestion4examQuery query = new AppQuestion4examQuery();
        query.setExamId(examNew.getExamId());
        query.setUserId(userDto.getUserId());
        query.setShowAnswer(true); // 组卷新题，不需要显示答案

        List<ExamQuestionVO> questionVOList = getAppExamQuestion(query);//看看里面有没有带上id👈
        examVO.setExamQuestionList(questionVOList);

        return examVO;
    }

    /**
     * 根据 ExamId 查询错题列表
     */
    @Override
    public List<ExamQuestionVO> getWrongQuestionsByExamId(Integer examId, String userId) {
        // 1. 构建查询条件：查询该考试下的错题
        AppQuestion4examQuery query = new AppQuestion4examQuery();
        query.setExamId(examId);
        query.setUserId(userId);
        query.setResult(ExamStatusEnum.FALSE.getStatus()); // 只查答错的题
        query.setShowAnswer(true); // 需要显示答案解析

        // 2. 复用 getAppExamQuestion 逻辑（因为我们只需要题目详情+用户作答结果）
        return this.getAppExamQuestion(query);
    }
}