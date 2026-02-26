package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.enums.CollectTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.enums.RequestFrequencyEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.Article;
import com.javastudy.vocabease_common.entity.po.Question;
import com.javastudy.vocabease_common.entity.po.Share;
import com.javastudy.vocabease_common.entity.po.Word;
import com.javastudy.vocabease_common.entity.query.ArticleQuery;
import com.javastudy.vocabease_common.entity.query.QuestionQuery;
import com.javastudy.vocabease_common.entity.query.ShareQuery;
import com.javastudy.vocabease_common.entity.query.WordQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.*;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController extends ABaseController {

    @Resource
    private WordService wordService;
    @Resource
    private ArticleService articleService;
    @Resource
    private QuestionService questionService;
    @Resource
    private Item4questionService item4questionService;
    @Resource
    private ShareService shareService;

    /**
     * 搜索
     */
    @RequestMapping("/search")
    @GlobalInterceptor(frequencyType = RequestFrequencyEnum.MINUTE, requestFrequencyThreshold = 10)
    public ResponseVO search(@VerifyParam(required = true) String str,
                                @VerifyParam(required = true) Integer type,
                                Integer pageNo) {
        CollectTypeEnum typeEnum = CollectTypeEnum.getEnum(type);
        if (typeEnum == null)
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        if (typeEnum == CollectTypeEnum.WORD) {
            WordQuery query = new WordQuery();
            query.setWordFuzzy(str);
            query.setPageNo(pageNo);
            query.setOrderBy("word_id desc");
            query.setStatus(PostStatusEnum.IS_POST.getStatus());
            PaginationResultVO<Word> wordVO = this.wordService.findListByPage(query);
            for (Word word : wordVO.getList())
                word.setImageUrl(resetContentImg(word.getImageUrl()));
            return getSuccessResponseVO(wordVO);
        } else if (typeEnum == CollectTypeEnum.ARTICLE) {
            ArticleQuery query = new ArticleQuery();
            query.setPageNo(pageNo);
            query.setTitleFuzzy(str);
            query.setOrderBy("article_id desc");
            query.setQueryBodyContent(false);
            query.setStatus(PostStatusEnum.IS_POST.getStatus());
            PaginationResultVO<Article> articleVO = this.articleService.findListByPage(query);
            return getSuccessResponseVO(articleVO);
        } else if (typeEnum == CollectTypeEnum.QUESTION) {
            QuestionQuery query = new QuestionQuery();
            query.setPageNo(pageNo);
            query.setTitleFuzzy(str);
            query.setOrderBy("question_id desc");
            query.setStatus(PostStatusEnum.IS_POST.getStatus());
            query.setQueryAnswer(true);
            query.setQuestionItem(true);
            PaginationResultVO<Question> questionVO = this.questionService.findListByPage(query);
            for (Question question : questionVO.getList()) {
                question.setQuestion(resetContentImg(question.getQuestion()));
                question.setAnswerAnalysis(question.getAnswerAnalysis());
            }
            return getSuccessResponseVO(questionVO);
        } else if (typeEnum == CollectTypeEnum.SHARE) {
            ShareQuery query = new ShareQuery();
            query.setPageNo(pageNo);
            query.setTitleFuzzy(str);
            query.setOrderBy("share_id desc");
            query.setStatus(PostStatusEnum.IS_POST.getStatus());
            query.setQueryContent(false);
            PaginationResultVO<Share> shareVO = this.shareService.findListByPage(query);
            for (Share share : shareVO.getList())
                share.setContent(resetContentImg(share.getContent()));
            return getSuccessResponseVO(shareVO);
        } else
            throw new BusinessException(ResponseCodeEnum.CODE_400);
    }
}
