package com.javastudy.vocabease_admin.controller;

import java.util.List;

import com.javastudy.vocabease_common.entity.query.AccountQuery;
import com.javastudy.vocabease_common.entity.po.Account;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 *  Controller
 */
@RestController("accountController")
@RequestMapping("/account")
public class AccountController extends com.javastudy.vocabease_admin.controller.ABaseController {

	@Resource
	private AccountService accountService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(AccountQuery query){
		return getSuccessResponseVO(accountService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(Account bean) {
		accountService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<Account> listBean) {
		accountService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<Account> listBean) {
		accountService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据UserId查询对象
	 */
	@RequestMapping("/getAccountByUserId")
	public ResponseVO getAccountByUserId(Integer userId) {
		return getSuccessResponseVO(accountService.getAccountByUserId(userId));
	}

	/**
	 * 根据UserId修改对象
	 */
	@RequestMapping("/updateAccountByUserId")
	public ResponseVO updateAccountByUserId(Account bean,Integer userId) {
		accountService.updateAccountByUserId(bean,userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据UserId删除
	 */
	@RequestMapping("/deleteAccountByUserId")
	public ResponseVO deleteAccountByUserId(Integer userId) {
		accountService.deleteAccountByUserId(userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Phone查询对象
	 */
	@RequestMapping("/getAccountByPhone")
	public ResponseVO getAccountByPhone(String phone) {
		return getSuccessResponseVO(accountService.getAccountByPhone(phone));
	}

	/**
	 * 根据Phone修改对象
	 */
	@RequestMapping("/updateAccountByPhone")
	public ResponseVO updateAccountByPhone(Account bean,String phone) {
		accountService.updateAccountByPhone(bean,phone);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Phone删除
	 */
	@RequestMapping("/deleteAccountByPhone")
	public ResponseVO deleteAccountByPhone(String phone) {
		accountService.deleteAccountByPhone(phone);
		return getSuccessResponseVO(null);
	}
}