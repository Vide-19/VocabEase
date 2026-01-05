package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.po.Account;
import com.javastudy.vocabease_common.entity.query.AccountQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 *  业务接口
 */
public interface AccountService {

	/**
	 * 根据条件查询列表
	 */
	List<Account> findListByParam(AccountQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(AccountQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Account> findListByPage(AccountQuery param);

	/**
	 * 新增
	 */
	Integer add(Account bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Account> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Account> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Account bean,AccountQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(AccountQuery param);

	/**
	 * 根据UserId查询对象
	 */
	Account getAccountByUserId(Integer userId);


	/**
	 * 根据UserId修改
	 */
	Integer updateAccountByUserId(Account bean,Integer userId);


	/**
	 * 根据UserId删除
	 */
	Integer deleteAccountByUserId(Integer userId);


	/**
	 * 根据Phone查询对象
	 */
	Account getAccountByPhone(String phone);


	/**
	 * 根据Phone修改
	 */
	Integer updateAccountByPhone(Account bean,String phone);


	/**
	 * 根据Phone删除
	 */
	Integer deleteAccountByPhone(String phone);

	/**
	 * 检查登录
	 *
	 * @param phone
	 * @param password
	 * @return
	 */
	SessionUserAdminDto login(String phone, String password);

}