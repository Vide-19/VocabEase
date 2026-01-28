package com.javastudy.vocabease_common.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 文章表 数据库操作接口
 */
public interface ArticleMapper<T,P> extends BaseMapper<T,P> {
	/**
	 * 根据ArticleId更新
	 */
	 Integer updateByArticleId(@Param("bean") T t,@Param("articleId") Integer articleId);
	/**
	 * 根据ArticleId删除
	 */
	 Integer deleteByArticleId(@Param("articleId") Integer articleId);
	/**
	 * 根据ArticleId获取对象
	 */
	 T selectByArticleId(@Param("articleId") Integer articleId);
	/**
	 * 根据ArticleIds删除多个对象
	 */
	void deleteByArticleIds(@Param("articleIds") String[] articleIds, @Param("status") Integer status, @Param("userId") Integer userId);

	T showArticleNext(@Param("query") P p);

	Integer updateCount(@Param("readCount") Integer readCount, @Param("collectCount") Integer collectCount, @Param("articleId") Integer articleId);

}
