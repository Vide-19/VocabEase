package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.dto.AppExamPostDto;
import com.javastudy.vocabease_common.entity.enums.*;
import com.javastudy.vocabease_common.entity.po.*;
import com.javastudy.vocabease_common.entity.query.*;
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
                item.setResult(appQuestion4exam.getResult());
                item.setUserAnswer(appQuestion4exam.getAnswer());
            }
        }
        return examQuestionVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppExam endExam(AppAccountDto accountDto, AppExamPostDto examPostDto) {
        // 1. 校验考试是否存在，且属于当前用户
        AppExam appExam = this.checkAppExam(accountDto, examPostDto.getExamId());
        if (appExam == null || !Objects.equals(appExam.getUserId(), accountDto.getUserId()))
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        // 2. 校验考试状态：仅允许提交“未完成”的考试
        if (!appExam.getStatus().equals(ExamStatusEnum.NO_FINISH.getStatus()))
            throw new BusinessException("该测试已被提交");
        // 3. 获取用户提交的答题记录列表
        List<AppQuestion4exam> userAnswerList = examPostDto.getAppQuestion4examList();
        if (userAnswerList == null || userAnswerList.isEmpty())
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        // 4. 查询数据库中该考试关联的所有题目（用于校验提交的题目是否合法）
        AppQuestion4examQuery query = new AppQuestion4examQuery();
        query.setExamId(examPostDto.getExamId());
        List<AppQuestion4exam> questionListDB = this.appQuestion4examMapper.selectList(query);
        // 5. 提取数据库中题目的 ID 列表（转为 String，适配 QuestionQuery 的入参要求）
        List<String> questionIdDB = questionListDB.stream().map(item ->
                item.getQuestionId().toString()).toList();
        // 6. 批量查询这些题目的标准答案（用于自动判分）
        QuestionQuery questionQuery = new QuestionQuery();
        questionQuery.setQuestionIds(questionIdDB.toArray(new String[questionIdDB.size()]));
        questionQuery.setQueryAnswer(true);// 确保返回 answer 字段
        List<Question> questionList = this.questionMapper.selectList(questionQuery);
        // 7. 构建题目 ID -> 题目实体的映射，便于快速查找标准答案
        Map<Integer, Question> itemMap = questionList.stream().collect(
                Collectors.toMap(Question::getQuestionId, Function.identity(), (data1, data2) -> data2));
        // 8. 遍历用户提交的每一道题，进行判分并补充必要字段
        for (AppQuestion4exam item : userAnswerList) {
            Question question = itemMap.get(item.getQuestionId());
            // 校验：提交的题目必须属于本次考试（防止伪造题号）
            if (question == null)
                throw new BusinessException(ResponseCodeEnum.CODE_400);
            // 补充考试ID和用户ID（前端可能未传，由服务端填充）
            item.setExamId(appExam.getExamId());
            item.setUserId(accountDto.getUserId());
            // 自动判分：比对用户答案与标准答案
            if (question.getAnswer().equals(item.getAnswer()))
                item.setResult(ExamStatusEnum.TRUE.getStatus());
            else
                item.setResult(ExamStatusEnum.FALSE.getStatus());
        }
        // 9. 批量保存或更新用户的答题记录（支持覆盖重答）
        this.appQuestion4examMapper.insertOrUpdateBatch(userAnswerList);
        // 10. 更新考试主记录状态为“已完成”，并设置结束时间
        // 注意：此处先构造更新对象，但实际更新在下一步通过条件更新完成
        Date currentDate = new Date();
        AppExam updateExam = new AppExam();
        updateExam.setStatus(ExamStatusEnum.IS_FINISH.getStatus());
        updateExam.setEndTime(currentDate);
        updateExam.setRemark(examPostDto.getRemark());
        // 11. 【关键】再次校验：确保数据库中仍存在一条“未完成”状态的考试记录（防止并发重复提交）
        AppExamQuery appExamQuery = new AppExamQuery();
        appExamQuery.setExamId(appExam.getExamId());
        appExamQuery.setUserId(accountDto.getUserId());
        appExamQuery.setStatus(ExamStatusEnum.NO_FINISH.getStatus());
        Integer count = this.appExamMapper.selectCount(appExamQuery);
        if (count == 0)
            throw new BusinessException("测试提交失败");
        // 12. 执行考试状态更新
        Integer update = this.appExamMapper.updateByExamId(updateExam, appExam.getExamId());
        if (update != 1)
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        // remark 由前端传入，直接设置到原对象（后续用于返回）👇remark可以不要
        // 13. 返回更新后的考试信息
        appExam.setRemark(examPostDto.getRemark());
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
}