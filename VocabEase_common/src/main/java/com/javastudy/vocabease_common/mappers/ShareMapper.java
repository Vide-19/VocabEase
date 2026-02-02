package com.javastudy.vocabease_common.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 笔记表 数据库操作接口
 */
public interface ShareMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据ShareId更新
	 */
	 Integer updateByShareId(@Param("bean") T t,@Param("shareId") Integer shareId);


	/**
	 * 根据ShareId删除
	 */
	 Integer deleteByShareId(@Param("shareId") Integer shareId);


	/**
	 * 根据ShareId获取对象
	 */
	 T selectByShareId(@Param("shareId") Integer shareId);


}
