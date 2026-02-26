package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.AppFeedback;
import com.javastudy.vocabease_common.entity.query.AppFeedbackQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 问题反馈表 业务接口
 */
public interface AppFeedbackService {

	/**
	 * 根据条件查询列表
	 */
	List<AppFeedback> findListByParam(AppFeedbackQuery param);
	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(AppFeedbackQuery param);
	/**
	 * 分页查询
	 */
	PaginationResultVO<AppFeedback> findListByPage(AppFeedbackQuery param);
	/**
	 * 回复问题
	 */
	void replyFeedback(AppFeedback appFeedback);
}