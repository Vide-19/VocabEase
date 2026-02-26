package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.AppCollectService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("appCollectController")
@RequestMapping("/appCollect")
public class AppCollectController extends ABaseController {

    @Resource
    private AppCollectService appCollectService;

    /**
     * 添加收藏
     */
    @RequestMapping("/addCollect")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Void> addCollect(@RequestHeader(value = "token", required = false) String token,
                                       @VerifyParam(required = true) String objectId,
                                       @VerifyParam(required = true) Integer collectType) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        this.appCollectService.addCollect(dto.getUserId(), objectId, collectType);
        return getSuccessResponseVO(null);
    }
    /**
     * 取消收藏
     */
    @RequestMapping("/cancelCollect")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO<Void> cancelCollect(@RequestHeader(value = "token", required = false) String token,
                                       @VerifyParam(required = true) String objectId,
                                       @VerifyParam(required = true) Integer collectType) {
        AppAccountDto dto = getTokenUserAdminDto(token);
        this.appCollectService.addCollect(dto.getUserId(), objectId, collectType);
        return getSuccessResponseVO(null);
    }
}