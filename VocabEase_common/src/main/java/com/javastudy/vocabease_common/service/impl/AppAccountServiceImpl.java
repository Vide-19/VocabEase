package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.AccountStatusEnum;
import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.AppAccount;
import com.javastudy.vocabease_common.entity.po.AppDevice;
import com.javastudy.vocabease_common.entity.query.AppAccountQuery;
import com.javastudy.vocabease_common.entity.query.AppDeviceQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.AppAccountMapper;
import com.javastudy.vocabease_common.mappers.AppDeviceMapper;
import com.javastudy.vocabease_common.service.AppAccountService;
import com.javastudy.vocabease_common.utils.JWTUtil;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * 小程序用户表 业务接口实现
 */
@Service("appAccountService")
public class AppAccountServiceImpl implements AppAccountService {

	@Resource
	private AppAccountMapper<AppAccount, AppAccountQuery> appAccountMapper;
	@Resource
	private AppDeviceMapper<AppDevice, AppDeviceQuery> appDeviceMapper;
	@Resource
	private JWTUtil<AppAccountDto> jwtUtil;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<AppAccount> findListByParam(AppAccountQuery param) {
		return this.appAccountMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(AppAccountQuery param) {
		return this.appAccountMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<AppAccount> findListByPage(AppAccountQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<AppAccount> list = this.findListByParam(param);
		PaginationResultVO<AppAccount> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(AppAccount bean) {
		return this.appAccountMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<AppAccount> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.appAccountMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<AppAccount> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.appAccountMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(AppAccount bean, AppAccountQuery param) {
		StringTools.checkParam(param);
		return this.appAccountMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(AppAccountQuery param) {
		StringTools.checkParam(param);
		return this.appAccountMapper.deleteByParam(param);
	}

	/**
	 * 根据UserId获取对象
	 */
	@Override
	public AppAccount getAppAccountByUserId(String userId) {
		return this.appAccountMapper.selectByUserId(userId);
	}

	/**
	 * 根据UserId修改
	 */
	@Override
	public Integer updateAppAccountByUserId(AppAccount bean, String userId) {
		return this.appAccountMapper.updateByUserId(bean, userId);
	}

	/**
	 * 根据UserId删除
	 */
	@Override
	public Integer deleteAppAccountByUserId(String userId) {
		return this.appAccountMapper.deleteByUserId(userId);
	}
	/**
	 * 注册
	 */
	@Override
	public void register(AppAccount appAccount) {
		AppAccount account = this.appAccountMapper.selectByEmail(appAccount.getEmail());
		//可根据前项目添加 邮箱验证码👇👇
		if (account != null)
			throw new BusinessException("该邮箱已被使用");
		AppAccountQuery query = new AppAccountQuery();
		query.setNickName(appAccount.getNickName());
		if (this.appAccountMapper.selectCount(query) > 0)
			throw new BusinessException("该昵称已被使用");
		String userId = StringTools.getRandomString(Constants.LENGTH_10);
		Date date = new Date();
		appAccount.setUserId(userId);
		appAccount.setPassword(StringTools.encodeByMd5(appAccount.getPassword()));
		appAccount.setCreateTime(date);
		appAccount.setLastLoginTime(date);
		appAccount.setStatus(AccountStatusEnum.ENABLED.getStatus());
		this.appAccountMapper.insert(appAccount);
		AppDevice device = new AppDevice();
		device.setDeviceId(appAccount.getLastUseDeviceId());
		device.setDeviceBrand(appAccount.getLastUseDeviceBrand());
		device.setCreateTime(date);
		device.setLastUseTime(date);
		device.setLastLoginIp(appAccount.getLastLoginIp());
		this.appDeviceMapper.insert(device);
	}

	@Override
	public String login(String email, String password, String ip, String deviceId, String deviceBrand) {
		AppAccount accountDB = this.appAccountMapper.selectByEmail(email);
		if (accountDB == null)
			throw new BusinessException("该账号或密码错误");
		if (!accountDB.getPassword().equals(StringTools.encodeByMd5(password)))
			throw new BusinessException("该账号或密码错误");
		if (accountDB.getStatus().equals(AccountStatusEnum.ENABLED.getStatus()))
			throw new BusinessException("该账户已被禁用");
		AppAccount account = new AppAccount();
		account.setLastLoginTime(new Date());
		account.setLastUseDeviceId(deviceId);
		account.setLastUseDeviceBrand(deviceBrand);
		account.setLastLoginIp(ip);
		this.appAccountMapper.updateByUserId(account, accountDB.getUserId());
		//JWT👇
		AppAccountDto dto = new AppAccountDto();
		dto.setUserId(account.getUserId());
		dto.setNickName(account.getNickName());
        return jwtUtil.createToken(Constants.JWT_KEY_LOGIN_TOKEN, dto, Constants.JWT_TOKEN_EXPIRE);
	}

	@Override
	public String autoLogin(String token, String ip, String deviceId, String deviceBrand) {//👇
		AppAccountDto dto = jwtUtil.getTokenData(Constants.JWT_KEY_LOGIN_TOKEN, token, AppAccountDto.class);
		if (dto == null)
			return null;
		AppAccount accountDB = this.appAccountMapper.selectByUserId(dto.getUserId());
		if (!accountDB.getStatus().equals(AccountStatusEnum.ENABLED.getStatus()))
			return null;
		AppAccount accountUpdate = new AppAccount();
		accountUpdate.setLastLoginTime(new Date());
		accountUpdate.setLastLoginIp(ip);
		accountUpdate.setLastUseDeviceId(deviceId);
		accountUpdate.setLastUseDeviceBrand(deviceBrand);
		this.appAccountMapper.updateByUserId(accountUpdate, accountDB.getUserId());

        return jwtUtil.createToken(Constants.JWT_KEY_LOGIN_TOKEN, dto, Constants.JWT_TOKEN_EXPIRE);
	}



















}