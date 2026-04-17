package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.CollectTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppCarousel;
import com.javastudy.vocabease_common.entity.po.Share;
import com.javastudy.vocabease_common.entity.query.AppCarouselQuery;
import com.javastudy.vocabease_common.entity.query.AppCollectQuery;
import com.javastudy.vocabease_common.entity.query.ShareQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppCarouselService;
import com.javastudy.vocabease_common.service.AppCollectService;
import com.javastudy.vocabease_common.service.ShareService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController("shareController")
@RequestMapping("/share")
public class ShareController extends ABaseController {

    @Resource
    private ShareService shareService;
    @Resource
    private AppCollectService appCollectService;
    @Resource
    private AppCarouselService appCarouselService;

    /**
     * 加载分享
     */
    @RequestMapping("/loadShare")
    @GlobalInterceptor
    public ResponseVO<PaginationResultVO<Share>> loadShare(@RequestHeader(value = "token", required = false) String token,
                                                           @VerifyParam(required = true) Integer pageNum,
                                                           @VerifyParam(required = false) String sortField,
                                                           @VerifyParam(required = false) String searchKey) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        ShareQuery query = new ShareQuery();
        query.setPageNo(pageNum);
        query.setQueryContent(false);
        query.setStatusList(Arrays.asList(PostStatusEnum.IS_POST.getStatus(), PostStatusEnum.IS_TOP.getStatus()));
        if (dto != null){
            query.setUserId(dto.getUserId());
            query.setCollectType(CollectTypeEnum.SHARE.getType());
        }
        if ("collect".equals(sortField))
            query.setOrderBy("status desc, collect_count desc"); // 按收藏数降序
        else if ("read".equals(sortField))
            query.setOrderBy("status desc, read_count desc"); // 按阅读量降序
        else
            query.setOrderBy("status desc, share_id desc");
        if (!searchKey.equals("null"))
            query.setTitleFuzzy(searchKey);
        return getSuccessResponseVO(this.shareService.findListByPage(query));
    }

    /**
     * 加载分享
     */
    @RequestMapping("/loadMyShare")
    @GlobalInterceptor
    public ResponseVO<PaginationResultVO<Share>> loadMyShare(@RequestHeader(value = "token", required = false) String token,
                                                           @VerifyParam(required = true) Integer pageNum,
                                                           @VerifyParam(required = false) String searchKey) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto == null)
            throw new BusinessException(ResponseCodeEnum.CODE_401);
        ShareQuery query = new ShareQuery();
        query.setPageNo(pageNum);
        query.setQueryContent(false);
        query.setCreaterId(dto.getUserId());
        query.setUserId(dto.getUserId());
        query.setCollectType(CollectTypeEnum.SHARE.getType());
        query.setOrderBy("share_id desc");
        if (!searchKey.equals("null"))
            query.setTitleFuzzy(searchKey);
        return getSuccessResponseVO(this.shareService.findListByPage(query));
    }

    /**
     * 加载轮播图
     */
    @RequestMapping("/loadCarouselList")
    @GlobalInterceptor
    public ResponseVO<List<AppCarousel>> loadCarouselList() {
        AppCarouselQuery query = new AppCarouselQuery();
        query.setObjectType(CollectTypeEnum.SHARE.getType());
        query.setOrderBy("sort desc");
        return getSuccessResponseVO(this.appCarouselService.findListByParam(query));//👈改前端
    }

    /**
     * 下一分享
     */
    @RequestMapping("/getShareNext")
    @GlobalInterceptor
    public ResponseVO<Share> getShareNext(@RequestHeader(value = "token", required = false) String token,
                                          @VerifyParam(required = true) Integer currentId, Integer nextType) {
        ShareQuery query = new ShareQuery();
        query.setStatusList(Arrays.asList(PostStatusEnum.IS_POST.getStatus(), PostStatusEnum.IS_TOP.getStatus()));
        Share share = this.shareService.showShareNext(query, currentId, nextType, true);
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto != null) {
            AppCollectQuery cQuery = new AppCollectQuery();
            cQuery.setUserId(dto.getUserId());
            cQuery.setObjectId(share.getShareId().toString());
            cQuery.setCollectType(CollectTypeEnum.SHARE.getType());
            if (this.appCollectService.findCountByParam(cQuery) != null)
                share.setCollect(true);
        }
        share.setContent(resetContentImg(share.getContent()));
        return getSuccessResponseVO(share);
    }

    @RequestMapping("/listByWordId")
    public ResponseVO<PaginationResultVO<Share>> listByWordId(@RequestHeader(value = "token", required = false) String token,
                                                              @VerifyParam(required = true) Integer wordId,
                                                              @VerifyParam(required = true) Integer pageNum) {
        ShareQuery query = new ShareQuery();
        query.setRelateId(wordId);
        query.setRelateType(CollectTypeEnum.WORD.getType());
        query.setStatusList(Arrays.asList(PostStatusEnum.IS_POST.getStatus(), PostStatusEnum.IS_TOP.getStatus()));
        query.setPageNo(pageNum);
        query.setOrderBy("create_time desc");
        query.setQueryContent(true);
        // 🆕 关键：解析 Token 获取 userId，传入 Query 以便 SQL 判断收藏状态
        if (token != null && !token.isEmpty()) {
            try {
                AppAccountDto user = getTokenUserAdminDto(token);
                if (user != null) {
                    query.setUserId(user.getUserId());
                    query.setCollectType(CollectTypeEnum.SHARE.getType());
                }
            } catch (BusinessException e) {
                return getBusinessErrorResponseVO(e, null);
            }
        }
        return getSuccessResponseVO(this.shareService.findListByPage(query));
    }

    @RequestMapping("/listByArticleId")
    public ResponseVO<PaginationResultVO<Share>> listByArticleId(@VerifyParam(required = true) Integer articleId,
                                                              @VerifyParam(required = true) Integer pageNum) {
        ShareQuery query = new ShareQuery();
        query.setRelateId(articleId);
        query.setRelateType(CollectTypeEnum.ARTICLE.getType());
        query.setStatusList(Arrays.asList(PostStatusEnum.IS_POST.getStatus(), PostStatusEnum.IS_TOP.getStatus()));
        query.setPageNo(pageNum);
        query.setOrderBy("create_time desc");
        query.setQueryContent(true);
        return getSuccessResponseVO(this.shareService.findListByPage(query));
    }

    @RequestMapping("/listByQuestionId")
    public ResponseVO<PaginationResultVO<Share>> listByQuestionId(@VerifyParam(required = true) Integer questionId,
                                                              @VerifyParam(required = true) Integer pageNum) {
        ShareQuery query = new ShareQuery();
        query.setRelateId(questionId);
        query.setRelateType(CollectTypeEnum.QUESTION.getType());
        query.setStatusList(Arrays.asList(PostStatusEnum.IS_POST.getStatus(), PostStatusEnum.IS_TOP.getStatus()));
        query.setPageNo(pageNum);
        query.setOrderBy("create_time desc");
        query.setQueryContent(true);
        return getSuccessResponseVO(this.shareService.findListByPage(query));
    }

    /**
     * 获取笔记详情
     */
    @RequestMapping("/getShareDetail")
    @GlobalInterceptor
    public ResponseVO<Share> getShareDetail(@RequestHeader(value = "token", required = false) String token,
                                                @VerifyParam(required = true) Integer shareId) {
        Share share = this.shareService.getShareByShareId(shareId);
        if (share == null || share.getStatus().equals(PostStatusEnum.NO_POST.getStatus()))
            return getBusinessErrorResponseVO(new BusinessException("笔记不存在或未发布"), null);
        this.shareService.updateReadCountById(shareId);
        share.setReadCount(share.getReadCount() + 1);
        // 处理收藏状态
        AppAccountDto dto = getTokenUserAdminDto(token);
        if (dto != null) {
            AppCollectQuery cQuery = new AppCollectQuery();
            cQuery.setUserId(dto.getUserId());
            cQuery.setObjectId(share.getShareId().toString());
            cQuery.setCollectType(CollectTypeEnum.SHARE.getType()); // 确保枚举正确
            if (this.appCollectService.findCountByParam(cQuery) > 0)
                share.setCollect(true);
        }
        return getSuccessResponseVO(share);
    }

    /**
     * 获取收藏列表中的下一个笔记
     */
    @GetMapping("/getNextCollectedShare")
    public ResponseVO<Share> getNextCollectedShare(@RequestHeader(value = "token", required = false) String token,
                                                       Integer currentId, Integer nextType) {
        try {
            AppAccountDto dto = getTokenUserAdminDto(token);
            if (dto == null)
                return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401), null);
            Share share = this.shareService.showNextCollectedShare(dto.getUserId(), currentId, nextType);
            return getSuccessResponseVO(share);
        } catch (BusinessException e) {
            return getBusinessErrorResponseVO(e, null);
        } catch (Exception e) {
            e.printStackTrace();
            return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_500), null);
        }
    }

    /**
     * 新增/修改笔记
     */
    @RequestMapping("/saveShare")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Void> saveShare(@RequestHeader("token") String token,
                                      Integer relateId,
                                      @VerifyParam(required = true) String content,
                                      String title, Integer type, Integer shareId) {
        AppAccountDto user = getTokenUserAdminDto(token);
        if (user == null)
            return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401), null);
        Share share = new Share();
        share.setShareId(shareId);
        share.setTitle(StringTools.isEmpty(title) ? content.substring(0, Math.min(20, content.length())) : title);
        share.setContent(content);
        share.setRelateId(relateId);
        share.setRelateType(type);
        share.setCreaterId(user.getUserId());
        this.shareService.saveShare(share, false);
        return getSuccessResponseVO(null);
    }

    /**
     * 删除笔记
     */
    @RequestMapping("/deleteShare")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Void> deleteShare(@RequestHeader("token") String token,
                                        @VerifyParam(required = true) Integer shareId) {
        AppAccountDto user = getTokenUserAdminDto(token);
        if (user == null) return getBusinessErrorResponseVO(new BusinessException(ResponseCodeEnum.CODE_401), null);

        // 调用 Service 层删除 (Service 层已有权限校验逻辑)
        this.shareService.deleteShareByShareIds(String.valueOf(shareId), user.getUserId());
        return getSuccessResponseVO(null);
    }
}