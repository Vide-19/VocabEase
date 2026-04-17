package com.javastudy.vocabease_api.controller; // 假设在 api 模块

import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppFeedback;
import com.javastudy.vocabease_common.entity.query.AppFeedbackQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppFeedbackService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 小程序端 - 问题反馈 Controller
 */
@RestController("appFeedbackClientController")
@RequestMapping("/appFeedback")
public class AppFeedbackController extends ABaseController {

    @Resource
    private AppFeedbackService appFeedbackService;

    /**
     * 查询我的反馈列表
     */
    @RequestMapping("/loadMyFeedbackList")
    public ResponseVO<PaginationResultVO<AppFeedback>> loadMyFeedbackList(@RequestHeader("token") String token) {
        AppAccountDto user = getTokenUserAdminDto(token);
        if (user == null)
            return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401), null);
        AppFeedbackQuery query = new AppFeedbackQuery();
        query.setUserId(user.getUserId());
        query.setpFeedbackId(0); // 只查主贴
        query.setOrderBy("feedback_id desc"); // 按ID倒序，最新的在最前
        return getSuccessResponseVO(this.appFeedbackService.findListByPage(query));
    }

    /**
     * 查询反馈详情
     */
    @RequestMapping("/loadFeedbackDetail")
    public ResponseVO<List<AppFeedback>> loadFeedbackDetail(@RequestHeader("token") String token,
                                                            @VerifyParam(required = true) Integer feedbackId) {
        List<AppFeedback> allMessages = new ArrayList<>();
        // 1. 查询主贴信息
        AppFeedbackQuery mainQuery = new AppFeedbackQuery();
        mainQuery.setFeedbackId(feedbackId);
        List<AppFeedback> mainList = this.appFeedbackService.findListByParam(mainQuery);

        if (mainList == null || mainList.isEmpty())
            throw new BusinessException(ResponseCodeEnum.CODE_400.getMsg());

        AppFeedback mainFeedback = mainList.get(0);
        // 权限校验：确保是当前用户查看自己的反馈
        AppAccountDto user = getTokenUserAdminDto(token);
        if (user == null)
            return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401), null);
        if (!mainFeedback.getUserId().equals(user.getUserId()))
            throw new BusinessException(ResponseCodeEnum.CODE_403.getMsg());

        allMessages.add(mainFeedback);

        // 2. 查询所有回复记录
        AppFeedbackQuery replyQuery = new AppFeedbackQuery();
        replyQuery.setpFeedbackId(feedbackId);
        replyQuery.setOrderBy("feedback_id asc"); // 按时间正序

        List<AppFeedback> replies = this.appFeedbackService.findListByParam(replyQuery);
        if (replies != null && !replies.isEmpty()) {
            allMessages.addAll(replies);
        }

        // 3. 整体按时间排序 (虽然主贴肯定在最前，但为了严谨)
        allMessages.sort(Comparator.comparing(AppFeedback::getCreateTime));

        return getSuccessResponseVO(allMessages);
    }

    /**
     * 提交反馈
     */
    @RequestMapping("/saveNewFeedback")
    public ResponseVO<Void> saveNewFeedback(@RequestHeader("token") String token,
                                            @VerifyParam(required = true) String content,
                                            String pFeedbackId) {
        // 1. 构建反馈对象
        AppFeedback appFeedback = new AppFeedback();
        appFeedback.setContent(content);

        // 2. 获取当前登录用户的 ID (根据你的项目实际方式获取)
        // 假设你的拦截器会把用户信息放在 RequestHolder 或 Header 里
        // 如果还没做登录拦截，这里可以先写死测试，或者从参数里传 userId
        AppAccountDto user = getTokenUserAdminDto(token);
        if (user == null)
            return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401), null);
        appFeedback.setUserId(user.getUserId());
        appFeedback.setNickName(user.getNickName());
        if (pFeedbackId != null && !pFeedbackId.isEmpty())
            // 这是一个回复
            appFeedback.setpFeedbackId(Integer.parseInt(pFeedbackId));
        else
            // 这是一个新建的主贴
            appFeedback.setpFeedbackId(0);
        // 3. 调用 Service 保存
        // 注意：这里调用的是 service 层的 saveNewFeedback 方法，不是 replyFeedback
        this.appFeedbackService.saveNewFeedback(appFeedback);
        return getSuccessResponseVO(null);
    }

}