package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.Article2category;
import com.javastudy.vocabease_common.entity.query.Article2categoryQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 文章-分类对应表 业务接口
 */
public interface Article2categoryService {

	/**
	 * 根据条件查询列表
	 */
	List<Article2category> findListByParam(Article2categoryQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(Article2categoryQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Article2category> findListByPage(Article2categoryQuery param);

	/**
	 * 新增
	 */
	Integer add(Article2category bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Article2category> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Article2category> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Article2category bean,Article2categoryQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(Article2categoryQuery param);

	/**
	 * 根据ArticleIdAndCategoryId查询对象
	 */
	Article2category getArticle2categoryByArticleIdAndCategoryId(Integer articleId,Integer categoryId);


	/**
	 * 根据ArticleIdAndCategoryId修改
	 */
	Integer updateArticle2categoryByArticleIdAndCategoryId(Article2category bean,Integer articleId,Integer categoryId);


	/**
	 * 根据ArticleIdAndCategoryId删除
	 */
	Integer deleteArticle2categoryByArticleIdAndCategoryId(Integer articleId,Integer categoryId);

	Integer getCategoryIdByArticleId(Integer articleId);

	void deleteArticle2categoryByArticleIds(String[] articleIds);
}