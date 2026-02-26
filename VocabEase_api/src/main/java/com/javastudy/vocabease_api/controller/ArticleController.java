package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.CollectTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.po.Article;
import com.javastudy.vocabease_common.entity.po.Article2category;
import com.javastudy.vocabease_common.entity.query.AppCollectQuery;
import com.javastudy.vocabease_common.entity.query.Article2categoryQuery;
import com.javastudy.vocabease_common.entity.query.ArticleQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.AppCollectService;
import com.javastudy.vocabease_common.service.Article2categoryService;
import com.javastudy.vocabease_common.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController("articleController")
@RequestMapping("/article")
public class ArticleController extends ABaseController {

    @Resource
    private Article2categoryService a2cService;
    @Resource
    private ArticleService articleService;
    @Resource
    private AppCollectService appCollectService;

    /**
     * 加载文章
     */
    @RequestMapping("/loadArticle")
    @GlobalInterceptor
    public ResponseVO<PaginationResultVO<Article>> loadArticle(@VerifyParam(required = true) Integer pageNum,
                                                               @VerifyParam(required = true) Integer categoryId) {
        Article2categoryQuery a2cQuery = new Article2categoryQuery();
        a2cQuery.setCategoryId(categoryId);
        List<Article2category> a2cList = this.a2cService.findListByParam(a2cQuery);
        if (a2cList == null || a2cList.isEmpty())
            return getServerErrorResponseVO(null);
        // 提取 articleId 列表
        String[] articleIds = a2cList.stream()
                .map(Article2category::getArticleId)
                .filter(Objects::nonNull)
                .map(String::valueOf) // Integer → String
                .toArray(String[]::new);
        ArticleQuery query = new ArticleQuery();
        query.setArticleIds(articleIds);
        query.setPageNo(pageNum);
        query.setOrderBy("article_id desc");
        query.setQueryBodyContent(false);
        query.setStatus(PostStatusEnum.IS_POST.getStatus());
        return getSuccessResponseVO(this.articleService.findListByPage(query));
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
}