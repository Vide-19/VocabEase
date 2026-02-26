package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.CollectTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.po.Share;
import com.javastudy.vocabease_common.entity.query.AppCollectQuery;
import com.javastudy.vocabease_common.entity.query.ShareQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.AppCollectService;
import com.javastudy.vocabease_common.service.ShareService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("shareController")
@RequestMapping("/share")
public class ShareController extends ABaseController {

    @Resource
    private ShareService shareService;
    @Resource
    private AppCollectService appCollectService;

    /**
     * 加载分享
     */
    @RequestMapping("/loadShare")
    @GlobalInterceptor
    public ResponseVO<PaginationResultVO<Share>> loadShare(@VerifyParam(required = true) Integer pageNum) {
        ShareQuery query = new ShareQuery();
        query.setPageNo(pageNum);
        query.setOrderBy("share_id desc");
        query.setQueryContent(false);
        query.setStatus(PostStatusEnum.IS_POST.getStatus());
        return getSuccessResponseVO(this.shareService.findListByPage(query));
    }

    /**
     * 下一分享
     */
    @RequestMapping("/getShareNext")
    @GlobalInterceptor
    public ResponseVO<Share> getShareNext(@RequestHeader(value = "token", required = false) String token,
                                          @VerifyParam(required = true) Integer currentId, Integer nextType) {
        ShareQuery query = new ShareQuery();
        query.setStatus(PostStatusEnum.IS_POST.getStatus());
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
}