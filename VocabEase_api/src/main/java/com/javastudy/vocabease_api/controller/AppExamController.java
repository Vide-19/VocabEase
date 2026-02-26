package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.dto.AppExamPostDto;
import com.javastudy.vocabease_common.entity.enums.ExamStatusEnum;
import com.javastudy.vocabease_common.entity.po.AppExam;
import com.javastudy.vocabease_common.entity.query.AppExamQuery;
import com.javastudy.vocabease_common.entity.query.AppQuestion4examQuery;
import com.javastudy.vocabease_common.entity.vo.AppExamVO;
import com.javastudy.vocabease_common.entity.vo.ExamQuestionVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.AppExamService;
import com.javastudy.vocabease_common.utils.CopyUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

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
    public ResponseVO<AppExam> endExam(@RequestHeader(value = "token", required = false) String token,
                                       @RequestBody AppExamPostDto appExamPostDto) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        AppExam appExam = this.appExamService.endExam(dto, appExamPostDto);
        return getSuccessResponseVO(appExam);
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
}