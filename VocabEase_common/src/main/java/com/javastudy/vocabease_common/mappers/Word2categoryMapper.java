package com.javastudy.vocabease_common.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 单词-分类对应表 数据库操作接口
 */
public interface Word2categoryMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据WordIdAndCategoryId更新
	 */
/*
	 void updateByWordId(@Param("wordId") Integer wordId,@Param("categoryId") Integer categoryId);
*/
	/**
	 * 根据WordIdAndCategoryId删除
	 */
	 Integer deleteByWordIdAndCategoryId(@Param("wordId") Integer wordId,@Param("categoryId") Integer categoryId);
	/**
	 * 根据WordIdAndCategoryId获取对象
	 */
	 T selectByWordIdAndCategoryId(@Param("wordId") Integer wordId,@Param("categoryId") Integer categoryId);
	/**
	 * 通过单词id获取分类id
	 */
	Integer selectCategoryIdByWordId(@Param("wordId") Integer wordId);
	/**
	 * 根据WordIds删除多个对象
	 */
	void deleteByWordIds(@Param("wordIds") String[] wordIds);


}
