package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.CollectTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.po.Word;
import com.javastudy.vocabease_common.entity.po.Word2category;
import com.javastudy.vocabease_common.entity.query.AppCollectQuery;
import com.javastudy.vocabease_common.entity.query.Word2categoryQuery;
import com.javastudy.vocabease_common.entity.query.WordQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.AppCollectService;
import com.javastudy.vocabease_common.service.Word2categoryService;
import com.javastudy.vocabease_common.service.WordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                                        @VerifyParam(required = true) Integer currentId, Integer nextType) {
        WordQuery query = new WordQuery();
        query.setStatus(PostStatusEnum.IS_POST.getStatus());
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
}