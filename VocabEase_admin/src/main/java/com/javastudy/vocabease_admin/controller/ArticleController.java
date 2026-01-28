package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.ImportErrorItem;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.po.Article;
import com.javastudy.vocabease_common.entity.query.ArticleQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.ArticleService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文章表 Controller
 */
@RestController("articleController")
@RequestMapping("/article")
public class ArticleController extends ABaseController{

	@Resource
	private ArticleService articleService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.ARTICLE_LIST)
	public ResponseVO loadDataList(ArticleQuery query){
		query.setOrderBy("article_id desc)");
		query.setQueryBodyContent(true);
		return getSuccessResponseVO(articleService.findListByPage(query));
	}
	/**
	 * 新增文章
	 */
	@RequestMapping("/saveArticle")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.ARTICLE_EDIT)
	public ResponseVO<Void> saveArticle(HttpSession session, Article article){
		SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
		article.setCreaterId(sessionUserAdminDto.getUserId());
		articleService.saveArticle(article, sessionUserAdminDto.getSuperAdmin());
		return getSuccessResponseVO(null);
	}
	/**
	 * 删除文章
	 */
	@RequestMapping("/deleteArticle")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.ARTICLE_DELETE)
	public ResponseVO<Void> deleteArticle(HttpSession session,
										  @VerifyParam(required = true) Integer articleId){
		SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
		this.articleService.deleteArticleByArticleIds(String.valueOf(articleId), sessionUserAdminDto.getSuperAdmin()?null:sessionUserAdminDto.getUserId());
		return getSuccessResponseVO(null);
	}
	/**
	 * 批量删除文章
	 */
	@RequestMapping("/deleteArticleBatch")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.ARTICLE_DELETE_BATCH)
	public ResponseVO<Void> deleteArticleBatch(@VerifyParam(required = true) String articleIds){
		this.articleService.deleteArticleByArticleIds(articleIds, null);
		return getSuccessResponseVO(null);
	}
	/**
	 * 文章发布
	 */
	@RequestMapping("/postArticle")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.ARTICLE_POST)
	public ResponseVO<Void> postArticle(@VerifyParam(required = true) String articleIds, SessionStatus sessionStatus){
		this.articleService.updateArticleStatus(articleIds, PostStatusEnum.IS_POST.getStatus());
		return getSuccessResponseVO(null);
	}
	/**
	 * 文章下架
	 */
	@RequestMapping("/cancelPostArticle")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.ARTICLE_POST)
	public ResponseVO<Void> cancelPostArticle(@VerifyParam(required = true) String articleIds,
											  SessionStatus sessionStatus){
		this.articleService.updateArticleStatus(articleIds, PostStatusEnum.NO_POST.getStatus());
		return getSuccessResponseVO(null);
	}
	/**
	 * 文章导入
	 */
	@RequestMapping("/importArticleByExcel")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.ARTICLE_IMPORT)
	public ResponseVO importArticleByExcel(HttpSession session, MultipartFile file){
		SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
		List<ImportErrorItem> errorItemList = this.articleService.importArticle(sessionUserAdminDto, file);
		return getSuccessResponseVO(errorItemList);
	}
	/**
	 * 下一篇
	 */
	@RequestMapping("/showNextArticle")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.ARTICLE_LIST)
	public ResponseVO<Article> showNextArticle(ArticleQuery articleQuery,
									  @VerifyParam(required = true) Integer currentId, Integer nextType){
		Article article = this.articleService.showArticleNext(articleQuery, currentId, nextType, false);
		return getSuccessResponseVO(article);
	}


}