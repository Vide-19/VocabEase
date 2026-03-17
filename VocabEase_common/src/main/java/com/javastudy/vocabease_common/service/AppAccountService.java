package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.AppAccount;
import com.javastudy.vocabease_common.entity.query.AppAccountQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 小程序用户表 业务接口
 */
public interface AppAccountService {

	/**
	 * 根据条件查询列表
	 */
	List<AppAccount> findListByParam(AppAccountQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(AppAccountQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<AppAccount> findListByPage(AppAccountQuery param);

	/**
	 * 新增
	 */
	Integer add(AppAccount bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<AppAccount> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<AppAccount> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(AppAccount bean,AppAccountQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(AppAccountQuery param);

	/**
	 * 根据UserId查询对象
	 */
	AppAccount getAppAccountByUserId(String userId);


	/**
	 * 根据UserId修改
	 */
	Integer updateAppAccountByUserId(AppAccount bean,String userId);


	/**
	 * 根据UserId删除
	 */
	Integer deleteAppAccountByUserId(String userId);

	void register(AppAccount appAccount);

	String login(String email, String password, String ip, String deviceId, String deviceBrand);
	String autoLogin(String token, String ip, String deviceId, String deviceBrand);
	AppAccount getAccountByOpenId(String openId);
	void registerByWechat(AppAccount account);
	String generateToken(AppAccount account, String ip, String deviceId, String deviceBrand);

	void updateAccountDevice(AppAccount account);
}