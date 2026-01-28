package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.dto.ImportErrorItem;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.po.Article;
import com.javastudy.vocabease_common.entity.query.ArticleQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 文章表 业务接口
 */
public interface ArticleService {

	/**
	 * 根据条件查询列表
	 */
	List<Article> findListByParam(ArticleQuery param);
	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(ArticleQuery param);
	/**
	 * 分页查询
	 */
	PaginationResultVO<Article> findListByPage(ArticleQuery param);
	/**
	 * 新增
	 */
	Integer add(Article bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<Article> listBean);
	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Article> listBean);
	/**
	 * 多条件更新
	 */
	Integer updateByParam(Article bean, ArticleQuery param);
	/**
	 * 多条件删除
	 */
	Integer deleteByParam(ArticleQuery param);
	/**
	 * 根据ArticleId查询对象
	 */
	Article getArticleByArticleId(Integer articleId);
	/**
	 * 根据ArticleId修改
	 */
	Integer updateArticleByArticleId(Article bean, Integer articleId);
	/**
	 * 根据ArticleId删除
	 */
	Integer deleteArticleByArticleId(Integer articleId);
	/**
	 * 保存文章
	 */
	void saveArticle(Article article, Boolean isAdmin);
	/**
	 * 删除多个文章
	 */
	void deleteArticleByArticleIds(String articleIds, Integer userId);

	Integer getCategoryIdByArticleId(Integer articleId);

	void updateArticleStatus(String articleIds, Integer status);

	List<ImportErrorItem> importArticle(SessionUserAdminDto sessionUserAdminDto, MultipartFile file);

	Article showArticleNext(ArticleQuery articleQuery, Integer currentId, Integer nextType, Boolean isUpdateReadCount);
}