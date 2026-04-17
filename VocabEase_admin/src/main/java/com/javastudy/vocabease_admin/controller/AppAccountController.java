package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.enums.AccountStatusEnum;
import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppAccount;
import com.javastudy.vocabease_common.entity.po.AppDevice;
import com.javastudy.vocabease_common.entity.query.AppAccountQuery;
import com.javastudy.vocabease_common.entity.query.AppDeviceQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppAccountService;
import com.javastudy.vocabease_common.service.AppDeviceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序用户表 Controller
 */
@RestController("appAccountController")
@RequestMapping("/appAccount")
public class AppAccountController extends ABaseController{

	@Resource
	private AppAccountService appAccountService;
	@Resource
	private AppDeviceService appDeviceService;

	/**
	 * 根据条件分页查询用户设备
	 */
	@RequestMapping("/loadDeviceList")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_USER_DEVICE)
	public ResponseVO<PaginationResultVO<AppDevice>> loadDataList(AppDeviceQuery query){
		query.setOrderBy("create_time desc");
		return getSuccessResponseVO(this.appDeviceService.findListByPage(query));
	}
	/**
	 * 根据条件分页查询用户
	 */
	@RequestMapping("/loadAccountList")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_USER_DEVICE)
	public ResponseVO<PaginationResultVO<AppAccount>> loadAccountList(@RequestBody AppAccountQuery query){
		query.setOrderBy("create_time desc");
		return getSuccessResponseVO(this.appAccountService.findListByPage(query));
	}
	/**
	 * 更新状态
	 */
	@RequestMapping("/updateStatus")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_USER_EDIT)
	public ResponseVO<Void> updateStatus(@VerifyParam(required = true) String userId,
										 @VerifyParam(required = true) Integer status) {
		AccountStatusEnum accountStatusEnum = AccountStatusEnum.getByStatus(status);
		if (accountStatusEnum == null)
			throw new BusinessException(ResponseCodeEnum.CODE_400);
		AppAccount appAccount = new AppAccount();
		appAccount.setStatus(status);
		this.appAccountService.updateAppAccountByUserId(appAccount, userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 更新我的信息
	 */
	@RequestMapping("/updateAppAccount")
	@GlobalInterceptor(checkLogin = true)//邮箱、昵称👇
	public ResponseVO<Void> updateAppAccount(@RequestBody @VerifyParam(required = true) AppAccount appAccount) {
		this.appAccountService.updateAppAccountByUserId(appAccount, appAccount.getUserId());
		return getSuccessResponseVO(null);
	}
}