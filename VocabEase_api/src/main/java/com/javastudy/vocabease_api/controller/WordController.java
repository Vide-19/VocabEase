package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.CollectTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.Word;
import com.javastudy.vocabease_common.entity.po.Word2category;
import com.javastudy.vocabease_common.entity.query.AppCollectQuery;
import com.javastudy.vocabease_common.entity.query.Word2categoryQuery;
import com.javastudy.vocabease_common.entity.query.WordQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppCollectService;
import com.javastudy.vocabease_common.service.Word2categoryService;
import com.javastudy.vocabease_common.service.WordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController("wordController")
@RequestMapping("/word")
public class WordController extends ABaseController {

    @Resource
    private Word2categoryService a2cService;
    @Resource
    private WordService wordService;
    @Resource
    private AppCollectService appCollectService;

    /**
     * 加载单词
     */
    @RequestMapping("/loadWord")
    //@GlobalInterceptor
    public ResponseVO<PaginationResultVO<Word>> loadWord(@VerifyParam(required = true) Integer pageNum,
                                                         @VerifyParam(required = true) Integer categoryId) {
        Word2categoryQuery a2cQuery = new Word2categoryQuery();
        a2cQuery.setCategoryId(categoryId);
        List<Word2category> a2cList = this.a2cService.findListByParam(a2cQuery);
        if (a2cList == null || a2cList.isEmpty())
            return getServerErrorResponseVO(null);
        // 提取 wordId 列表
        String[] wordIds = a2cList.stream()
                .map(Word2category::getWordId)
                .filter(Objects::nonNull)
                .map(String::valueOf) // Integer → String
                .toArray(String[]::new);
        WordQuery query = new WordQuery();
        query.setWordIds(wordIds);
        query.setPageNo(pageNum);
        query.setOrderBy("word_id desc");
        query.setStatus(PostStatusEnum.IS_POST.getStatus());
        return getSuccessResponseVO(this.wordService.findListByPage(query));
    }

    /**
     * 下一单词
     */
    @RequestMapping("/getWordNext")
    @GlobalInterceptor
    public ResponseVO<Word> getWordNext(@RequestHeader(value = "token", required = false) String token,
                                        @VerifyParam(required = true) Integer currentId,
                                        Integer nextType, Integer difficulty) {
        WordQuery query = new WordQuery();
        query.setStatus(PostStatusEnum.IS_POST.getStatus());
        query.setLevel(difficulty);
        Word word = this.wordService.showWordNext(query, currentId, nextType);
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto != null) {
            AppCollectQuery cQuery = new AppCollectQuery();
            cQuery.setUserId(dto.getUserId());
            cQuery.setObjectId(word.getWordId().toString());
            cQuery.setCollectType(CollectTypeEnum.WORD.getType());
            if (this.appCollectService.findCountByParam(cQuery) != null)
                word.setCollect(true);
        }
        return getSuccessResponseVO(word);
    }

    /**
     * 单词详情
     */
    @RequestMapping("/getWordDetail")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Word> getWordDetail(@RequestHeader(value = "token", required = false) String token,
                                          @VerifyParam(required = true) Integer wordId) {
        // 1. 获取单词基本信息 (含 Category Name)
        Word word = this.wordService.getWordByWordId(wordId);
        if (word == null)
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        // 2. 检查登录用户是否收藏了该单词
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto != null) {
            AppCollectQuery cQuery = new AppCollectQuery();
            cQuery.setUserId(dto.getUserId());
            cQuery.setObjectId(wordId.toString());
            cQuery.setCollectType(CollectTypeEnum.WORD.getType());
            // 如果 count > 0 说明已收藏
            Integer count = this.appCollectService.findCountByParam(cQuery);
            if (count != null && count > 0)
                word.setCollect(true);
            // 如果需要 cancelCollect 的 ID，可以查出 collectId
            // 这里简化处理，cancelCollect 接口通常只需要 objectId
        }
        return getSuccessResponseVO(word);
    }

    /**
     * 【新增】获取收藏列表中的下一个单词
     * URL: /word/getNextCollectedWord
     */
    @GetMapping("/getNextCollectedWord")
    public ResponseVO<Word> getNextCollectedWord(@RequestHeader(value = "token", required = false) String token,
                                                 Integer currentId, Integer nextType) {
        try {
            AppAccountDto dto = getTokenUserAdminDto(token);
            if (dto == null)
                return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401), null);
            Word word = this.wordService.showNextCollectedWord(dto.getUserId(), currentId, nextType);
            return getSuccessResponseVO(word);
        } catch (BusinessException e) {
            return getBusinessErrorResponseVO(e, null);
        } catch (Exception e) {
            e.printStackTrace();
            return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_500), null);
        }
    }

    /**
     * 获取下一批单词 (支持断点续传)
     */
    @GetMapping("/getStudyList")
    public ResponseVO<List<Word>> getStudyList(@RequestParam Integer difficulty,
                                               @RequestParam(required = false) Integer lastWordId,
                                               @RequestParam(defaultValue = "20") Integer limit) {
        List<Word> words = this.wordService.getStudyList(difficulty, lastWordId, limit);
        return getSuccessResponseVO(words);
    }

    /**
     * 获取第一个单词 (用于全新开始学习)
     */
    @RequestMapping("/getFirst")
    @GlobalInterceptor
    public ResponseVO<Word> getFirstWord(@RequestHeader(value = "token", required = false) String token,
                                         Integer difficulty) { // difficulty 可选，根据你的业务决定是否必填

        WordQuery query = new WordQuery();
        query.setStatus(PostStatusEnum.IS_POST.getStatus());
        query.setLevel(difficulty);
        // 关键点：传入 currentId 为 0，nextType 为 1 (下一个)
        // 这样逻辑上就是 "从头开始的下一个单词"
        Word word = this.wordService.getFirst(query);
        // ... (后续的收藏逻辑，同 getWordNext)
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto != null && word != null) {
            AppCollectQuery cQuery = new AppCollectQuery();
            cQuery.setUserId(dto.getUserId());
            cQuery.setObjectId(word.getWordId().toString());
            cQuery.setCollectType(CollectTypeEnum.WORD.getType());
            if (this.appCollectService.findCountByParam(cQuery) != null)
                word.setCollect(true);
        }
        return getSuccessResponseVO(word);
    }
}