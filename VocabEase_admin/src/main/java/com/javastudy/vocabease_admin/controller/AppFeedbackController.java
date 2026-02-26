package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppFeedback;
import com.javastudy.vocabease_common.entity.query.AppFeedbackQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.AppFeedbackService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 问题反馈表 Controller
 */
@RestController("appFeedbackController")
@RequestMapping("/appFeedback")
public class AppFeedbackController extends ABaseController{

	@Resource
	private AppFeedbackService appFeedbackService;
	/**
	 * 查询问题反馈
	 */
	@RequestMapping("/loadFeedbackList")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_FEEDBACK_LIST)
	public ResponseVO<PaginationResultVO<AppFeedback>> loadDataList(AppFeedbackQuery query){
		query.setOrderBy("feedback_id desc");
		query.setFeedbackId(0);
		return getSuccessResponseVO(appFeedbackService.findListByPage(query));
	}
	/**
	 * 查询回复
	 */
	@RequestMapping("/loadReplyList")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_FEEDBACK_REPLY)
	public ResponseVO<List<AppFeedback>> loadReplyList(@VerifyParam(required = true) Integer pFeedbackId){
		AppFeedbackQuery query = new AppFeedbackQuery();
		query.setOrderBy("feedback_id asc");
		query.setpFeedbackId(pFeedbackId);
		return getSuccessResponseVO(this.appFeedbackService.findListByParam(query));
	}
	/**
	 * 回复问题
	 */
	@RequestMapping("/reply")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_FEEDBACK_REPLY)
	public ResponseVO<Void> reply(@VerifyParam(required = true) String content,
							@VerifyParam(required = true) Integer pFeedbackId) {
		AppFeedback feedback = new AppFeedback();
		feedback.setContent(content);
		feedback.setpFeedbackId(pFeedbackId);
		this.appFeedbackService.replyFeedback(feedback);
		return getSuccessResponseVO(null);
	}
}