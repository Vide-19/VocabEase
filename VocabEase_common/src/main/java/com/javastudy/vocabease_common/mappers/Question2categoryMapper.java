package com.javastudy.vocabease_common.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 问题-分类对应表 数据库操作接口
 */
public interface Question2categoryMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据QuestionIdAndCategoryId更新
	 */
	 Integer updateByQuestionIdAndCategoryId(@Param("bean") T t,@Param("questionId") Integer questionId,@Param("categoryId") Integer categoryId);
	/**
	 * 根据QuestionIdAndCategoryId删除
	 */
	 Integer deleteByQuestionIdAndCategoryId(@Param("questionId") Integer questionId,@Param("categoryId") Integer categoryId);
	/**
	 * 根据QuestionIdAndCategoryId获取对象
	 */
	 T selectByQuestionIdAndCategoryId(@Param("questionId") Integer questionId,@Param("categoryId") Integer categoryId);
	/**
	 * 通过问题id获取分类id
	 */
	 Integer selectCategoryIdByQuestionId(@Param("questionId") Integer questionId);
	/**
	 * 根据questionIds删除多个对象
	 */
	void deleteByQuestionIds(@Param("questionIds") String[] questionIds);
}
