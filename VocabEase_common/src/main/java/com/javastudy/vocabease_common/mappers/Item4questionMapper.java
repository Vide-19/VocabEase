package com.javastudy.vocabease_common.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问题选项表 数据库操作接口
 */
public interface Item4questionMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据ItemId更新
	 */
	 Integer updateByItemId(@Param("bean") T t,@Param("itemId") Integer itemId);


	/**
	 * 根据ItemId删除
	 */
	 Integer deleteByItemId(@Param("itemId") Integer itemId);

	/**
	 * 根据ItemId批量删除
	 */
	void deleteByItemIdBatch(@Param("itemIdList") List<Integer> itemIdList);

	/**
	 * 根据ItemId获取对象
	 */
	 T selectByItemId(@Param("itemId") Integer itemId);

	 void deleteByQuestionIdBatch(@Param("questionIdArray") String[] questionIdArray, @Param("status") Integer status, @Param("userId") Integer userId);


}
