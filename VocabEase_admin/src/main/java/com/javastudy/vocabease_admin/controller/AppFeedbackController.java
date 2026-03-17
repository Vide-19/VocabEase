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

import java.util.ArrayList;
import java.util.Comparator;
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
		query.setpFeedbackId(0);
		return getSuccessResponseVO(this.appFeedbackService.findListByPage(query));
	}
	/**
	 * 查询回复
	 */
	@RequestMapping("/loadReplyList")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_FEEDBACK_REPLY)
	public ResponseVO<List<AppFeedback>> loadReplyList(@VerifyParam(required = true) Integer pFeedbackId){
		List<AppFeedback> allMessages = new ArrayList<>();

		// 1. 【关键】先查询主贴 (即用户的原始反馈)
		AppFeedbackQuery mainQuery = new AppFeedbackQuery();
		mainQuery.setFeedbackId(pFeedbackId);
		// 确保只查这一条
		List<AppFeedback> mainList = this.appFeedbackService.findListByParam(mainQuery);

		AppFeedback mainFeedback = null;
		if (mainList != null && !mainList.isEmpty()) {
			mainFeedback = mainList.get(0);
			// 将主贴加入列表首位
			allMessages.add(mainFeedback);
		} else {
			// 如果连主贴都找不到，返回空
			return getSuccessResponseVO(new ArrayList<>());
		}

		// 2. 查询所有的回复记录 (pFeedbackId = 主贴ID)
		// 注意：这里查出来的是管理员的回复，或者用户后续的追加反馈(如果有此业务)
		AppFeedbackQuery replyQuery = new AppFeedbackQuery();
		replyQuery.setOrderBy("feedback_id asc"); // 按时间正序
		replyQuery.setpFeedbackId(pFeedbackId);

		List<AppFeedback> replies = this.appFeedbackService.findListByParam(replyQuery);
		if (replies != null && !replies.isEmpty()) {
			allMessages.addAll(replies);
		}

		// 3. 再次整体排序 (以防万一)，确保主贴在最前 (通常主贴ID最小)
		allMessages.sort(Comparator.comparing(AppFeedback::getCreateTime));

		return getSuccessResponseVO(allMessages);
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