package com.javastudy.vocabease_common.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 问题反馈表 数据库操作接口
 */
public interface AppFeedbackMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据FeedbackId更新
	 */
	 void updateByFeedbackId(@Param("bean") T t,@Param("feedbackId") Integer feedbackId);
	/**
	 * 根据FeedbackId获取对象
	 */
	 T selectByFeedbackId(@Param("feedbackId") Integer feedbackId);
}
