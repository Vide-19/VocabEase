package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_common.entity.po.Article2category;
import com.javastudy.vocabease_common.entity.query.Article2categoryQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.Article2categoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文章-分类对应表 Controller
 */
@RestController("article2categoryController")
@RequestMapping("/article2category")
public class Article2categoryController extends ABaseController{

	@Resource
	private Article2categoryService article2categoryService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(Article2categoryQuery query){
		return getSuccessResponseVO(article2categoryService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO<Void> add(Article2category bean) {
		article2categoryService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO<Void> addBatch(@RequestBody List<Article2category> listBean) {
		article2categoryService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO<Void> addOrUpdateBatch(@RequestBody List<Article2category> listBean) {
		article2categoryService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ArticleIdAndCategoryId查询对象
	 */
	@RequestMapping("/getArticle2categoryByArticleIdAndCategoryId")
	public ResponseVO getArticle2categoryByArticleIdAndCategoryId(Integer articleId,Integer categoryId) {
		return getSuccessResponseVO(article2categoryService.getArticle2categoryByArticleIdAndCategoryId(articleId,categoryId));
	}

	/**
	 * 根据ArticleIdAndCategoryId修改对象
	 */
	@RequestMapping("/updateArticle2categoryByArticleIdAndCategoryId")
	public ResponseVO<Void> updateArticle2categoryByArticleIdAndCategoryId(Article2category bean,Integer articleId,Integer categoryId) {
		article2categoryService.updateArticle2categoryByArticleIdAndCategoryId(bean,articleId,categoryId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ArticleIdAndCategoryId删除
	 */
	@RequestMapping("/deleteArticle2categoryByArticleIdAndCategoryId")
	public ResponseVO<Void> deleteArticle2categoryByArticleIdAndCategoryId(Integer articleId,Integer categoryId) {
		article2categoryService.deleteArticle2categoryByArticleIdAndCategoryId(articleId,categoryId);
		return getSuccessResponseVO(null);
	}
}