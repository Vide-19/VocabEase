package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.CollectTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.Question;
import com.javastudy.vocabease_common.entity.query.AppCollectQuery;
import com.javastudy.vocabease_common.entity.query.QuestionQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppCollectService;
import com.javastudy.vocabease_common.service.QuestionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * App端 - 问题表 Controller
 * 注意：区别于后台管理的 QuestionController，此 Controller 专供前端用户使用
 */
@RestController("appQuestionController")
@RequestMapping("/question")
public class QuestionController extends ABaseController {

    @Resource
    private QuestionService questionService;

    @Resource
    private AppCollectService appCollectService;

    /**
     * 获取题目详情
     * 逻辑：查询题目 -> 检查状态 -> (若登录) 检查收藏状态 -> 返回
     */
    @RequestMapping("/getQuestionDetail")
    @GlobalInterceptor
    public ResponseVO<Question> getQuestionDetail(@RequestHeader(value = "token", required = false) String token,
                                                  @VerifyParam(required = true) Integer questionId) {
        Question question = this.questionService.getAppQuestionByQuestionId(questionId);
        if (question == null || !PostStatusEnum.IS_POST.getStatus().equals(question.getStatus()))
            return getBusinessErrorResponseVO(new BusinessException("题目不存在或已下架"), null);
        handleCollectStatus(token, question, CollectTypeEnum.QUESTION);
        // 可选：增加阅读量 (建议异步处理或简单 +1)
        // questionService.updateReadCount(questionId);
        return getSuccessResponseVO(question);
    }

    /**
     * 获取上一题/下一题
     * 逻辑：根据 currentId 和 nextType 查找相邻题目 -> 检查状态 -> (若登录) 检查收藏状态
     */
    @RequestMapping("/getQuestionNext")
    @GlobalInterceptor
    public ResponseVO<Question> getQuestionNext(@RequestHeader(value = "token", required = false) String token,
                                                @VerifyParam(required = true) Integer currentId, Integer nextType) {
        QuestionQuery query = new QuestionQuery();
        query.setStatus(PostStatusEnum.IS_POST.getStatus());
        Question question = this.questionService.showQuestionNext(query, currentId, nextType);
        if (question == null) {
            // 根据 nextType 返回更友好的提示，或者直接返回 null 让前端处理
            String msg = "没有更多题目了";
            if (nextType != null)
                msg = nextType == 1 ? "已经是最后一题了" : "已经是第一题了";
            return getBusinessErrorResponseVO(new BusinessException(msg), null);
        }
        // 处理收藏状态
        handleCollectStatus(token, question, CollectTypeEnum.QUESTION);
        return getSuccessResponseVO(question);
    }

    /**
     * 获取收藏列表中的下一题
     * 仅限登录用户
     */
    @GetMapping("/getNextCollectedQuestion")
    public ResponseVO<Question> getNextCollectedQuestion(@RequestHeader(value = "token", required = false) String token,
                                                         Integer currentId, Integer nextType) {
        try {
            // 1. 强制校验登录
            AppAccountDto dto = getTokenUserAdminDto(token);
            if (dto == null)
                return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401), null);
            // 2. 调用 Service 获取收藏列表中的相邻题目
            // 注意：QuestionService 中需要实现 showNextCollectedQuestion 方法
            Question question = this.questionService.showNextCollectedQuestion(dto.getUserId(), currentId, nextType);
            if (question == null)
                return getBusinessErrorResponseVO(new BusinessException("没有更多收藏的题目了"), null);
            // 3. 收藏列表里的题目必然已收藏，但为了统一前端逻辑，依然设为 true
            question.setCollect(true);
            return getSuccessResponseVO(question);
        } catch (BusinessException e) {
            return getBusinessErrorResponseVO(e, null);
        } catch (Exception e) {
            e.printStackTrace();
            return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_500), null);
        }
    }

    /**
     * 通用方法：处理收藏状态
     * 如果 token 有效，查询数据库并设置 question.isCollect
     */
    private void handleCollectStatus(String token, Question question, CollectTypeEnum typeEnum) {
        if (question == null) return;
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto != null) {
            AppCollectQuery cQuery = new AppCollectQuery();
            cQuery.setUserId(dto.getUserId());
            cQuery.setObjectId(question.getQuestionId().toString());
            cQuery.setCollectType(typeEnum.getType());
            // 如果计数 > 0，说明已收藏
            Integer count = this.appCollectService.findCountByParam(cQuery);
            question.setCollect(count != null && count > 0);
        } else
            // 未登录默认未收藏
            question.setCollect(false);
    }
}