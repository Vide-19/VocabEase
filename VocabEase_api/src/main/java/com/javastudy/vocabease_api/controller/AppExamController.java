package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.dto.AppExamPostDto;
import com.javastudy.vocabease_common.entity.enums.ExamStatusEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppExam;
import com.javastudy.vocabease_common.entity.query.AppExamQuery;
import com.javastudy.vocabease_common.entity.query.AppQuestion4examQuery;
import com.javastudy.vocabease_common.entity.vo.AppExamVO;
import com.javastudy.vocabease_common.entity.vo.ExamQuestionVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppExamService;
import com.javastudy.vocabease_common.utils.CopyUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 测试表 Controller
 */
@RestController("appExamController")
@RequestMapping("/appExam")
public class AppExamController extends ABaseController {

    @Resource
    private AppExamService appExamService;

    /**
     * 查询未完成测试
     */
    @RequestMapping("/loadMyExam")
    @GlobalInterceptor
    public ResponseVO<List<AppExam>> loadMyExam(@RequestHeader(value = "token", required = false) String token) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto == null)
            return getSuccessResponseVO(null);
        AppExamQuery query = new AppExamQuery();
        query.setStatus(ExamStatusEnum.IS_FINISH.getStatus());
        query.setUserId(dto.getUserId());
        query.setOrderBy("exam_id desc");
        List<AppExam> examList = this.appExamService.findListByParam(query);
        return getSuccessResponseVO(examList);
    }

    /**
     * 查询未完成测试
     */
    @RequestMapping("/loadExamNotFinish")
    @GlobalInterceptor
    public ResponseVO<List<AppExam>> loadExamNotFinish(@RequestHeader(value = "token", required = false) String token) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto == null)
            return getSuccessResponseVO(null);
        AppExamQuery query = new AppExamQuery();
        query.setStatus(ExamStatusEnum.NO_FINISH.getStatus());
        query.setUserId(dto.getUserId());
        query.setOrderBy("exam_id desc");
        List<AppExam> examList = this.appExamService.findListByParam(query);
        return getSuccessResponseVO(examList);
    }

    /**
     * 新增
     */
    @RequestMapping("/addExam")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<AppExam> addExam(@RequestHeader(value = "token", required = false) String token,
                                       @VerifyParam(required = true) String categoryIds) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        return getSuccessResponseVO(this.appExamService.addExam(categoryIds, dto));
    }

    /**
     * 测试问题
     */
    @RequestMapping("/getExamQuestion")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<AppExamVO> getExamQuestion(@RequestHeader(value = "token", required = false) String token,
                                                 @VerifyParam(required = true) Integer examId) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        AppExam appExam = this.appExamService.getAppExamByExamId(examId);
        boolean showAnswer = ExamStatusEnum.IS_FINISH.getStatus().equals(appExam.getStatus());
        AppExamVO appExamVO = CopyUtil.copy(appExam, AppExamVO.class);
        AppQuestion4examQuery query = new AppQuestion4examQuery();
        query.setExamId(examId);
        query.setUserId(dto.getUserId());
        query.setShowAnswer(showAnswer);
        List<ExamQuestionVO> questionVOList = this.appExamService.getAppExamQuestion(query);
        for (ExamQuestionVO item : questionVOList) {
            item.setQuestion(resetContentImg(item.getQuestion()));
            item.setAnswerAnalysis(resetContentImg(item.getAnswerAnalysis()));
        }
        appExamVO.setExamQuestionList(questionVOList);
        return getSuccessResponseVO(appExamVO);
    }

    /**
     * 开始测试
     */
    @RequestMapping("/startExam")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Date> startExam(@RequestHeader(value = "token", required = false) String token,
                                      @VerifyParam(required = true) Integer examId) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        this.appExamService.checkAppExam(dto, examId);
        Date currenDate = new Date();
        AppExam newExam = new AppExam();
        newExam.setStartTime(currenDate);
        AppExamQuery query = new AppExamQuery();
        query.setExamId(examId);
        query.setUserId(dto.getUserId());
        this.appExamService.updateByParam(newExam, query);
        return getSuccessResponseVO(currenDate);
    }

    /**
     * 结束测试
     */
    @RequestMapping("/endExam")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Map<String, Object>> endExam(@RequestHeader(value = "token", required = false) String token,
                                       @RequestBody AppExamPostDto appExamPostDto) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        AppExam appExam = this.appExamService.endExam(dto, appExamPostDto);
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("examId", appExam.getExamId());
        resultData.put("score", appExam.getScore());
        resultData.put("endTime", appExam.getEndTime());
        return getSuccessResponseVO(resultData);
    }

    /**
     * 退出测试
     */
    @RequestMapping("/cancelExam")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Void> cancelExam(@RequestHeader(value = "token", required = false) String token,
                                       @VerifyParam(required = true) Integer examId) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        this.appExamService.cancelExam(dto, examId);
        return getSuccessResponseVO(null);
    }

    /**
     * 【新增】自动组卷接口
     * 根据用户传入的难度 (difficulty) 查询题目并创建考试
     */
    @RequestMapping("/generatePaper")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<AppExamVO> generatePaper(@RequestHeader(value = "token", required = false) String token,
                                               @VerifyParam(required = true) Integer difficulty,
                                               @VerifyParam(required = true) Integer pageIndex) { // 接收前端传来的难度参数
        AppAccountDto dto = getTokenUserAdminDto(token);
        // 调用 Service 层的自动组卷逻辑
        AppExamVO examVO = this.appExamService.generateExamPaper(dto, difficulty, pageIndex);
        return getSuccessResponseVO(examVO);
    }

    /**
     * 获取考试报告详情
     * 用于展示分数、用时以及错题解析
     */
    @RequestMapping("/getReport")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<AppExamVO> getReport(@RequestHeader(value = "token", required = false) String token,
                                           @VerifyParam(required = true) Integer examId) {
        AppAccountDto dto = getTokenUserAdminDto(token);

        // 1. 获取考试基本信息（包含分数）
        AppExam appExam = this.appExamService.getAppExamByExamId(examId);

        // 安全校验：只能查看自己的报告
        if (!appExam.getUserId().equals(dto.getUserId())) {
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        }

        // 2. 组装 VO 对象
        AppExamVO appExamVO = CopyUtil.copy(appExam, AppExamVO.class);

        // 3. 获取错题列表（用于前端展示解析）
        AppQuestion4examQuery query = new AppQuestion4examQuery();
        query.setExamId(examId);
        query.setUserId(dto.getUserId());
        query.setShowAnswer(true);
        // 假设你的 Service 或 Mapper 有一个方法专门查错题，或者查所有题前端自己过滤
        // 这里为了演示，我们查所有题目，前端根据 result 字段判断对错
        List<ExamQuestionVO> questionVOList = this.appExamService.getAppExamQuestion(query);

        // 4. 处理图片路径（如果有）
        for (ExamQuestionVO item : questionVOList) {
            item.setQuestion(resetContentImg(item.getQuestion()));
            item.setAnswerAnalysis(resetContentImg(item.getAnswerAnalysis()));
        }

        appExamVO.setExamQuestionList(questionVOList);

        List<ExamQuestionVO> wrongQuestions = questionVOList.stream()
                .filter(q -> q.getResult() == 3) // 假设 0 代表答错，1 代表答对
                .collect(Collectors.toList());
        appExamVO.setWrongQuestions(wrongQuestions);

        return getSuccessResponseVO(appExamVO);
    }
}