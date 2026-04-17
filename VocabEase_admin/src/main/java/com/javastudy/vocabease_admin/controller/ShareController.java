package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.po.Share;
import com.javastudy.vocabease_common.entity.query.ShareQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.ShareService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;

/**
 * 笔记表 Controller
 */
@RestController("shareController")
@RequestMapping("/share")
public class ShareController extends ABaseController {

    @Resource
    private ShareService shareService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadDataList")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SHARE_LIST)
    public ResponseVO<PaginationResultVO<Share>> loadDataList(ShareQuery query) {
        query.setOrderBy("share_id desc");
        query.setQueryContent(true);
        return getSuccessResponseVO(this.shareService.findListByPage(query));
    }

    /**
     * 新增/修改分享
     */
    @RequestMapping("/saveShare")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SHARE_EDIT)
    public ResponseVO<Void> saveShare(HttpSession session, @RequestBody @VerifyParam(required = true) Share share) {
        SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
        share.setCreaterId(sessionUserAdminDto.getUserId().toString());
        this.shareService.saveShare(share, sessionUserAdminDto.getSuperAdmin());
        return getSuccessResponseVO(null);
    }

    /**
     * 删除分享
     */
    @RequestMapping("/deleteShare")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SHARE_DELETE)
    public ResponseVO<Void> deleteShare(HttpSession session, @VerifyParam(required = true) String shareIds) {
        SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
        this.shareService.deleteShareByShareIds(shareIds, String.valueOf(sessionUserAdminDto.getSuperAdmin() ? null : sessionUserAdminDto.getUserId()));
        return getSuccessResponseVO(null);
    }

    /**
     * 批量删除分享
     */
    @RequestMapping("/deleteShareBatch")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SHARE_DELETE_BATCH)
    public ResponseVO<Void> deleteShareBatch(@VerifyParam(required = true) String shareIds) {
        this.shareService.deleteShareByShareIds(shareIds, null);
        return getSuccessResponseVO(null);
    }

    /**
     * 发布笔记
     */
    @RequestMapping("/postShare")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SHARE_POST)
    public ResponseVO<Void> postShare(@VerifyParam(required = true) String shareIds,
                                      SessionStatus sessionStatus) {
        this.shareService.updateShareStatus(shareIds, PostStatusEnum.IS_POST.getStatus());
        return getSuccessResponseVO(null);
    }

    /**
     * 下架笔记
     */
    @RequestMapping("/cancelPostShare")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SHARE_POST)
    public ResponseVO<Void> cancelPostShare(@VerifyParam(required = true) String shareIds,
                                            SessionStatus sessionStatus) {
        this.shareService.updateShareStatus(shareIds, PostStatusEnum.NO_POST.getStatus());
        return getSuccessResponseVO(null);
    }

    /**
     * 笔记置顶
     */
    @RequestMapping("/topShare")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SHARE_POST)
    public ResponseVO<Void> topShare(@VerifyParam(required = true) String shareIds,
                                       SessionStatus sessionStatus) {
        this.shareService.updateShareStatus(shareIds, PostStatusEnum.IS_TOP.getStatus());
        return getSuccessResponseVO(null);
    }

    /**
     * 下一篇
     */
    @RequestMapping("/showNextShare")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SHARE_LIST)
    public ResponseVO<Share> showNextShare(ShareQuery shareQuery,
                                           @VerifyParam(required = true) Integer currentId, Integer nextType) {
        Share share = this.shareService.showShareNext(shareQuery, currentId, nextType, false);
        return getSuccessResponseVO(share);
    }
    //👇

}