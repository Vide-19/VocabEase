package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.CollectTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppWrongWord;
import com.javastudy.vocabease_common.entity.po.Word;
import com.javastudy.vocabease_common.entity.query.AppCollectQuery;
import com.javastudy.vocabease_common.entity.query.AppWrongWordQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppCollectService;
import com.javastudy.vocabease_common.service.AppWrongWordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 用户错题本表 Controller
 */
@RestController("appWrongWordController")
@RequestMapping("/appWrongWord")
public class AppWrongWordController extends ABaseController {

    @Resource
    private AppWrongWordService appWrongWordService;
    @Resource
    private AppCollectService appCollectService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadDataList")
    public ResponseVO<PaginationResultVO<AppWrongWord>> loadDataList(AppWrongWordQuery query) {
        return getSuccessResponseVO(appWrongWordService.findListByPage(query));
    }

    @PostMapping("/addWrongWord")
    @GlobalInterceptor
    public ResponseVO<Void> addWrongWord(@RequestHeader("token") String token,
                                         @RequestParam Integer wordId) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto == null)
            return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401), null);
        this.appWrongWordService.addWrongWord(dto.getUserId(), wordId);
        return getSuccessResponseVO(null);
    }

    /**
     * 根据Id删除
     */
    @RequestMapping("/deleteAppWrongWordById")
    public ResponseVO<Void> deleteAppWrongWordById(Integer id) {
        this.appWrongWordService.deleteAppWrongWordById(id);
        return getSuccessResponseVO(null);
    }

    /**
     * 复习模式：获取下一个错题
     * 逻辑：优先从错题本获取，支持简单的随机或按错误次数排序
     */
    @RequestMapping("/getNext")
    @GlobalInterceptor
    public ResponseVO<Word> getNextWrongWord(@RequestHeader(value = "token", required = false) String token,
                                             @VerifyParam(required = true) String userId) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto == null)
            return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401), null);
        // 2. 构建错题查询条件
        AppWrongWordQuery query = new AppWrongWordQuery();
        query.setUserId(userId); // 使用登录用户的ID
        query.setStatus(PostStatusEnum.IS_POST.getStatus()); // 单词状态正常
        // 3. 调用 Service 层获取下一个错题
        // 注意：这里返回的是 Word 实体，不是 WrongWord 实体
        // 这样前端可以直接复用现有的 wordData 绑定逻辑
        Word word = this.appWrongWordService.getNextWrongWord(query);
        // 4. 如果没有错题了，返回 null (前端收到 null 后会切换到生词库逻辑)
        if (word == null)
            return getSuccessResponseVO(null);
        // 5. 处理收藏状态 (复用你原有的收藏查询逻辑)
        AppCollectQuery cQuery = new AppCollectQuery();
        cQuery.setUserId(dto.getUserId());
        cQuery.setObjectId(word.getWordId().toString());
        cQuery.setCollectType(CollectTypeEnum.WORD.getType());
        // 如果找到了收藏记录，标记为已收藏
        if (this.appCollectService.findCountByParam(cQuery) != null)
            word.setCollect(true);
        return getSuccessResponseVO(word);
    }

    /**
     * 复习模式：用户记住了单词，wrong_count 减 1，并更新复习时间
     */
    @RequestMapping("/updateWrongWord")
    @GlobalInterceptor
    public ResponseVO<Void> updateWrongWord(@RequestHeader(value = "token", required = false) String token,
                                      @VerifyParam(required = true) Integer wordId,
                                      @VerifyParam(required = true) String userId) {
        this.appWrongWordService.updateWrong(userId, wordId);
        return getSuccessResponseVO(null);
    }
}