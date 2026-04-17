package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.CollectTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppCarousel;
import com.javastudy.vocabease_common.entity.po.Article;
import com.javastudy.vocabease_common.entity.query.AppCarouselQuery;
import com.javastudy.vocabease_common.entity.query.AppCollectQuery;
import com.javastudy.vocabease_common.entity.query.ArticleQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppCarouselService;
import com.javastudy.vocabease_common.service.AppCollectService;
import com.javastudy.vocabease_common.service.Article2categoryService;
import com.javastudy.vocabease_common.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController("articleController")
@RequestMapping("/article")
public class ArticleController extends ABaseController {

    @Resource
    private Article2categoryService a2cService;
    @Resource
    private ArticleService articleService;
    @Resource
    private AppCollectService appCollectService;
    @Resource
    private AppCarouselService appCarouselService;

    /**
     * 加载文章
     */
    @RequestMapping("/loadArticle")
    @GlobalInterceptor
    public ResponseVO<PaginationResultVO<Article>> loadArticle(@RequestHeader(value = "token", required = false) String token,
                                                               @VerifyParam(required = true) Integer pageNum,
                                                               @VerifyParam(required = false) String sortField,
                                                               @VerifyParam(required = false) String searchKey) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        ArticleQuery query = new ArticleQuery();
        query.setPageNo(pageNum);
        query.setQueryBodyContent(false);
        query.setStatusList(Arrays.asList(PostStatusEnum.IS_POST.getStatus(), PostStatusEnum.IS_TOP.getStatus()));
        if (dto != null) {
            query.setUserId(dto.getUserId());
            query.setCollectType(CollectTypeEnum.ARTICLE.getType());
        }
        // 设置排序逻辑
        if ("collect".equals(sortField))
            query.setOrderBy("status desc, collect_count desc"); // 按收藏数降序
        else if ("read".equals(sortField))
            query.setOrderBy("status desc, read_count desc"); // 按阅读量降序
        else
            query.setOrderBy("status desc, article_id desc");
        if (!searchKey.equals("null"))
            query.setTitleFuzzy(searchKey);
        return getSuccessResponseVO(this.articleService.findListByPage(query));
    }

    /**
     * 加载轮播图
     */
    @RequestMapping("/loadCarouselList")
    @GlobalInterceptor
    public ResponseVO<List<AppCarousel>> loadCarouselList() {
        AppCarouselQuery query = new AppCarouselQuery();
        query.setObjectType(CollectTypeEnum.ARTICLE.getType());
        query.setOrderBy("sort desc");
        return getSuccessResponseVO(this.appCarouselService.findListByParam(query));
    }

    /**
     * 下一文章
     */
    @RequestMapping("/getArticleNext")
    @GlobalInterceptor
    public ResponseVO<Article> getArticleNext(@RequestHeader(value = "token", required = false) String token,
                                              @VerifyParam(required = true) Integer currentId, Integer nextType) {
        ArticleQuery query = new ArticleQuery();
        query.setStatus(PostStatusEnum.IS_POST.getStatus());
        Article article = this.articleService.showArticleNext(query, currentId, nextType, true);
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto != null) {
            AppCollectQuery cQuery = new AppCollectQuery();
            cQuery.setUserId(dto.getUserId());
            cQuery.setObjectId(article.getArticleId().toString());
            cQuery.setCollectType(CollectTypeEnum.ARTICLE.getType());
            if (this.appCollectService.findCountByParam(cQuery) != null)
                article.setCollect(true);
        }
        return getSuccessResponseVO(article);
    }

    /**
     * 获取文章详情
     */
    @RequestMapping("/getArticleDetail")
    @GlobalInterceptor
    public ResponseVO<Article> getArticleDetail(@RequestHeader(value = "token", required = false) String token,
                                                @VerifyParam(required = true) Integer articleId) {
        Article article = this.articleService.getArticleByArticleId(articleId);
        if (article == null || !article.getStatus().equals(PostStatusEnum.IS_POST.getStatus()))
            return getBusinessErrorResponseVO(new BusinessException("文章不存在或未发布"), null);
        this.articleService.updateReadCountById(articleId);
        article.setReadCount(article.getReadCount() + 1);
        // 处理收藏状态
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto != null) {
            AppCollectQuery cQuery = new AppCollectQuery();
            cQuery.setUserId(dto.getUserId());
            cQuery.setObjectId(article.getArticleId().toString());
            cQuery.setCollectType(CollectTypeEnum.ARTICLE.getType()); // 确保枚举正确
            if (this.appCollectService.findCountByParam(cQuery) > 0)
                article.setCollect(true);
        }
        return getSuccessResponseVO(article);
    }

    /**
     * 获取收藏列表中的下一个文章
     */
    @GetMapping("/getNextCollectedArticle")
    public ResponseVO<Article> getNextCollectedArticle(@RequestHeader(value = "token", required = false) String token,
                                                       Integer currentId, Integer nextType) {
        try {
            AppAccountDto dto = getTokenUserAdminDto(token);
            if (dto == null)
                return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401), null);
            Article article = this.articleService.showNextCollectedArticle(dto.getUserId(), currentId, nextType);
            return getSuccessResponseVO(article);
        } catch (BusinessException e) {
            return getBusinessErrorResponseVO(e, null);
        } catch (Exception e) {
            e.printStackTrace();
            return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_500), null);
        }
    }
}