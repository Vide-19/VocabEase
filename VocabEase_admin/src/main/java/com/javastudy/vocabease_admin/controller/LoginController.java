package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.dto.CreateImagCode;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;
import com.javastudy.vocabease_common.entity.enums.VerifyRegexEnum;
import com.javastudy.vocabease_common.entity.po.Account;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AccountService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LoginController extends ABaseController {
    @Resource
    private AccountService accountService;

    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session) throws IOException {
        //验证码
        CreateImagCode code = new CreateImagCode(130, 38, 5, 10);
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String myCode = code.getCode();
        session.setAttribute(Constants.CHECK_CODE_KEY, myCode);
        code.write(response.getOutputStream());
    }
    /**
     * 登录
     */
    @RequestMapping("/login")
    @GlobalInterceptor(checkLogin = false)
    public ResponseVO login(HttpSession session,
                            @VerifyParam(regex = VerifyRegexEnum.PHONE) String phone,
                            @VerifyParam(required = true) String password,
                            @VerifyParam(required = true) String checkCode) throws BusinessException {
        if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY)))
            throw new BusinessException("验证码错误");
        SessionUserAdminDto sessionUserAdminDto = this.accountService.login(phone, password);
        session.setAttribute(Constants.SESSION_KEY, sessionUserAdminDto);
        return getSuccessResponseVO(sessionUserAdminDto);
    }
    /**
     * 登出
     */
    @RequestMapping("/logout")
    public ResponseVO logout(HttpSession session) {
        session.invalidate();
        return getSuccessResponseVO(null);
    }
    /**
     * 修改个人密码
     */
    @PostMapping("/updateMyPassword")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ACCOUNT_UPDATE_PASSWORD)
    public ResponseVO updateMyPassword(HttpSession session,
                                     @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD) String password){
        SessionUserAdminDto sessionUserAdminDto = getSessionUserAdminDto(session);
        Account account = new Account();
        account.setPassword(StringTools.encodeByMd5(password));
        this.accountService.updateAccountByUserId(account, sessionUserAdminDto.getUserId());
        return getSuccessResponseVO(null);
    }
}
