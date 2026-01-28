package com.javastudy.vocabease_common.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 文章-分类对应表 数据库操作接口
 */
public interface Article2categoryMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据ArticleIdAndCategoryId更新
	 */
	 Integer updateByArticleIdAndCategoryId(@Param("bean") T t,@Param("articleId") Integer articleId,@Param("categoryId") Integer categoryId);


	/**
	 * 根据ArticleIdAndCategoryId删除
	 */
	 Integer deleteByArticleIdAndCategoryId(@Param("articleId") Integer articleId,@Param("categoryId") Integer categoryId);


	/**
	 * 根据ArticleIdAndCategoryId获取对象
	 */
	 T selectByArticleIdAndCategoryId(@Param("articleId") Integer articleId,@Param("categoryId") Integer categoryId);

	 Integer selectCategoryIdByArticleId(@Param("articleId") Integer articleId);
	/**
	 * 根据ArticleIds删除多个对象
	 */
	void deleteByArticleIds(@Param("articleIds") String[] articleIds);

}
