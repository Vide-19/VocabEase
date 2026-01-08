package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.config.AppConfig;
import com.javastudy.vocabease_common.entity.enums.AccountStatusEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.enums.VerifyRegexEnum;
import com.javastudy.vocabease_common.entity.po.Account;
import com.javastudy.vocabease_common.entity.query.AccountQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AccountService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Controller
 */
@RestController("accountController")
@RequestMapping("/settings")
public class AccountController extends com.javastudy.vocabease_admin.controller.ABaseController {

	@Resource
	private AccountService accountService;
	@Resource
	private AppConfig appConfig;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadAccountList")
	public ResponseVO loadAccountList(AccountQuery query){
		query.setOrderBy("create_time desc");
		query.setRolesQuery(true);
		return getSuccessResponseVO(accountService.findListByPage(query));
	}
	/**
	 * 保存新增或更改的用户
	 */
	@RequestMapping("/saveAccount")
	public ResponseVO saveAccount(@VerifyParam Account account){
		this.accountService.saveAccount(account);
		return getSuccessResponseVO(null);
	}
	/**
	 * 删除用户
	 */
	@DeleteMapping("/deleteAccount")
	public ResponseVO deleteAccount(@VerifyParam(required = true) Integer userId){
		Account account = this.accountService.getAccountByUserId(userId);
		if (!StringTools.isEmpty(appConfig.getSuperAdminPhone()) &&
				ArrayUtils.contains(appConfig.getSuperAdminPhone().split(","), account.getPhone()))
			throw new BusinessException("超管无法被删除！");
		this.accountService.deleteAccountByUserId(userId);
		return getSuccessResponseVO(null);
	}
	/**
	 * 修改密码
	 */
	@PostMapping("/updatePassword")
	public ResponseVO updatePassword(@VerifyParam Integer userId,
									 @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD) String password){
		Account account = new Account();
		account.setPassword(StringTools.encodeByMd5(password));
		this.accountService.updateAccountByUserId(account, userId);
		return getSuccessResponseVO(null);
	}
	/**
	 * 修改状态
	 */
	@PostMapping("/updateStatus")
	public ResponseVO updateStatus(@VerifyParam Integer userId,
									 @VerifyParam(required = true) Integer status){
		AccountStatusEnum accountStatusEnum = AccountStatusEnum.getByStatus(status);
		if (accountStatusEnum == null)
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		Account account = new Account();
		account.setStatus(status);
		this.accountService.updateAccountByUserId(account, userId);
		return getSuccessResponseVO(null);
	}
}