package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.config.AppConfig;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.CollectTypeEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.*;
import com.javastudy.vocabease_common.entity.query.*;
import com.javastudy.vocabease_common.entity.vo.AppUserInfoVO;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.*;
import com.javastudy.vocabease_common.utils.CopyUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/myInfo")
public class MyInfoController extends ABaseController {

    @Resource
    private AppAccountService appAccountService;
    @Resource
    private AppCollectService appCollectService;
    @Resource
    private AppExamService appExamService;
    @Resource
    private AppFeedbackService appFeedbackService;
    @Resource
    private AppQuestion4examService appQuestion4examService;
    @Resource
    private AppConfig appConfig;
    @Resource
    private WordService wordService;
    @Resource
    private ArticleService articleService;
    @Resource
    private QuestionService questionService;
    @Resource
    private ShareService shareService;

    /**
     * 我的信息
     */
    @RequestMapping("/loadMyInfo")
    public ResponseVO<AppUserInfoVO> loadMyInfo(@RequestHeader(value = "token", required = false) String token) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto == null)
            return getSuccessResponseVO(null);
        AppAccount appAccount = this.appAccountService.getAppAccountByUserId(dto.getUserId());
        if (appAccount == null)
            return getSuccessResponseVO(null);
        return getSuccessResponseVO(CopyUtil.copy(appAccount, AppUserInfoVO.class));
    }

    /**
     * 我的收藏
     */
    @RequestMapping("/loadMyCollect")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadMyCollect(@RequestHeader(value = "token", required = false) String token,
                                    @VerifyParam(required = true) Integer collectType) {
        CollectTypeEnum typeEnum = CollectTypeEnum.getEnum(collectType);
        if (typeEnum == null)
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        AppAccountDto dto = getTokenUserAdminDto(token);
        AppCollectQuery query = new AppCollectQuery();
        query.setCollectType(collectType);
        query.setUserId(dto.getUserId());
        query.setOrderBy("collect_time desc");
        PaginationResultVO vo = this.appCollectService.findListByPage(query);
        List<AppCollect> appCollectList = vo.getList();
        List<String> objectIdList = appCollectList.stream().map(AppCollect::getObjectId).toList();
        if (objectIdList.isEmpty())
            return getSuccessResponseVO(vo);
        Map<Integer, AppCollect> objectIdMap = appCollectList.stream().collect(Collectors.toMap(item ->
                Integer.parseInt(item.getObjectId()), Function.identity(), (data1, data2) -> data2));
        if (typeEnum.equals(CollectTypeEnum.SHARE)) {
            ShareQuery shareQuery = new ShareQuery();
            shareQuery.setShareIds(objectIdList.toArray(new String[objectIdList.size()]));
            shareQuery.setOrderBy("field(share_id," + StringUtils.join(objectIdList, ",") + ")");
            List<Share> shareList = this.shareService.findListByParam(shareQuery);
            for (Share item : shareList) {
                AppCollect collect = objectIdMap.get(item.getShareId());
                item.setCollectId(collect.getCollectId());
            }
            vo.setList(shareList);
        } else if (typeEnum.equals(CollectTypeEnum.WORD)) {
            WordQuery wordQuery = new WordQuery();
            wordQuery.setWordIds(objectIdList.toArray(new String[objectIdList.size()]));
            wordQuery.setOrderBy("field(word_id," + StringUtils.join(objectIdList, ",") + ")");
            List<Word> wordList = this.wordService.findListByParam(wordQuery);
            for (Word item : wordList) {
                AppCollect collect = objectIdMap.get(item.getWordId());
                item.setCollectId(collect.getCollectId());
            }
            vo.setList(wordList);
        } else if (typeEnum.equals(CollectTypeEnum.ARTICLE)) {
            ArticleQuery articleQuery = new ArticleQuery();
            articleQuery.setArticleIds(objectIdList.toArray(new String[objectIdList.size()]));
            articleQuery.setOrderBy("field(article_id," + StringUtils.join(objectIdList, ",") + ")");
            List<Article> articleList = this.articleService.findListByParam(articleQuery);
            for (Article item : articleList) {
                AppCollect collect = objectIdMap.get(item.getArticleId());
                item.setCollectId(collect.getCollectId());
            }
            vo.setList(articleList);
        } else if (typeEnum.equals(CollectTypeEnum.QUESTION)) {
            QuestionQuery questionQuery = new QuestionQuery();
            questionQuery.setQuestionIds(objectIdList.toArray(new String[objectIdList.size()]));
            questionQuery.setOrderBy("field(question_id," + StringUtils.join(objectIdList, ",") + ")");
            List<Question> questionList = this.questionService.findListByParam(questionQuery);
            for (Question item : questionList) {
                AppCollect collect = objectIdMap.get(item.getQuestionId());
                item.setCollectId(collect.getCollectId());
            }
            vo.setList(questionList);
        }
        return getSuccessResponseVO(vo);
    }

    /**
     * 我的下一个收藏
     */
    @RequestMapping("/getMyCollectNext")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO getMyCollectNext(@RequestHeader(value = "token", required = false) String token,
                                       @VerifyParam(required = true) Integer currentId,
                                       @VerifyParam(required = true) Integer collectType,
                                       Integer nextType) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        CollectTypeEnum typeEnum = CollectTypeEnum.getEnum(collectType);
        if (typeEnum == null)
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        AppCollectQuery query = new AppCollectQuery();
        query.setUserId(dto.getUserId());
        query.setCollectType(collectType);
        AppCollect appCollect = this.appCollectService.showCollectNext(query, nextType, currentId);
        Integer objectId = Integer.parseInt(appCollect.getObjectId());
        if (typeEnum.equals(CollectTypeEnum.SHARE)) {
            Share share = this.shareService.getShareByShareId(objectId);
            share.setCollectId(appCollect.getCollectId());
            return getSuccessResponseVO(share);
        } else if (typeEnum.equals(CollectTypeEnum.WORD)) {
            Word word = this.wordService.getWordByWordId(objectId);
            word.setCollectId(appCollect.getCollectId());
            return getSuccessResponseVO(word);
        } else if (typeEnum.equals(CollectTypeEnum.ARTICLE)) {
            Article article = this.articleService.getArticleByArticleId(objectId);
            article.setCollectId(appCollect.getCollectId());
            return getSuccessResponseVO(article);
        } else if (typeEnum.equals(CollectTypeEnum.QUESTION)) {
            Question question = this.questionService.getQuestionByQuestionId(objectId);
            question.setCollectId(appCollect.getCollectId());
            return getSuccessResponseVO(question);
        }
        return getSuccessResponseVO(null);
    }

    /**
     * 我的测试记录
     */
    @RequestMapping("/getMyExam")
    public ResponseVO<AppUserInfoVO> getMyExam(@RequestHeader(value = "token", required = false) String token) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto == null)
            return getSuccessResponseVO(null);
        //👇


        return getSuccessResponseVO(null);
    }
}
