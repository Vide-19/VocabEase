package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.Article2category;
import com.javastudy.vocabease_common.entity.query.Article2categoryQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.mappers.Article2categoryMapper;
import com.javastudy.vocabease_common.service.Article2categoryService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 文章-分类对应表 业务接口实现
 */
@Service("article2categoryService")
public class Article2categoryServiceImpl implements Article2categoryService {

	@Resource
	private Article2categoryMapper<Article2category, Article2categoryQuery> article2categoryMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Article2category> findListByParam(Article2categoryQuery param) {
		return this.article2categoryMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(Article2categoryQuery param) {
		return this.article2categoryMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Article2category> findListByPage(Article2categoryQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Article2category> list = this.findListByParam(param);
		PaginationResultVO<Article2category> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Article2category bean) {
		return this.article2categoryMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Article2category> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.article2categoryMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Article2category> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.article2categoryMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Article2category bean, Article2categoryQuery param) {
		StringTools.checkParam(param);
		return this.article2categoryMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(Article2categoryQuery param) {
		StringTools.checkParam(param);
		return this.article2categoryMapper.deleteByParam(param);
	}

	/**
	 * 根据ArticleIdAndCategoryId获取对象
	 */
	@Override
	public Article2category getArticle2categoryByArticleIdAndCategoryId(Integer articleId, Integer categoryId) {
		return this.article2categoryMapper.selectByArticleIdAndCategoryId(articleId, categoryId);
	}

	/**
	 * 根据ArticleIdAndCategoryId修改
	 */
	@Override
	public Integer updateArticle2categoryByArticleIdAndCategoryId(Article2category bean, Integer articleId, Integer categoryId) {
		return this.article2categoryMapper.updateByArticleIdAndCategoryId(bean, articleId, categoryId);
	}

	/**
	 * 根据ArticleIdAndCategoryId删除
	 */
	@Override
	public Integer deleteArticle2categoryByArticleIdAndCategoryId(Integer articleId, Integer categoryId) {
		return this.article2categoryMapper.deleteByArticleIdAndCategoryId(articleId, categoryId);
	}

	@Override
	public Integer getCategoryIdByArticleId(Integer articleId) {
		return this.article2categoryMapper.selectCategoryIdByArticleId(articleId);
	}
	/**
	 * 根据ArticleIds删除
	 */
	@Override
	public void deleteArticle2categoryByArticleIds(String[] articleIds) {
		this.article2categoryMapper.deleteByArticleIds(articleIds);
	}
}