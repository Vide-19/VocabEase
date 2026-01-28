package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.config.AppConfig;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.enums.AccountStatusEnum;
import com.javastudy.vocabease_common.entity.enums.MenuTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.Account;
import com.javastudy.vocabease_common.entity.po.Menu;
import com.javastudy.vocabease_common.entity.query.AccountQuery;
import com.javastudy.vocabease_common.entity.query.MenuQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.MenuVO;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.AccountMapper;
import com.javastudy.vocabease_common.service.AccountService;
import com.javastudy.vocabease_common.service.MenuService;
import com.javastudy.vocabease_common.utils.CopyUtil;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 业务接口实现
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountMapper<Account, AccountQuery> accountMapper;
    @Resource
    private MenuService menuService;
    @Resource
    private AppConfig appConfig;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Account> findListByParam(AccountQuery param) {
        return this.accountMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AccountQuery param) {
        return this.accountMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<Account> findListByPage(AccountQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<Account> list = this.findListByParam(param);
        PaginationResultVO<Account> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Account bean) {
        return this.accountMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Account> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.accountMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Account> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.accountMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(Account bean, AccountQuery param) {
        StringTools.checkParam(param);
        return this.accountMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(AccountQuery param) {
        StringTools.checkParam(param);
        return this.accountMapper.deleteByParam(param);
    }

    /**
     * 根据UserId获取对象
     */
    @Override
    public Account getAccountByUserId(Integer userId) {
        return this.accountMapper.selectByUserId(userId);
    }

    /**
     * 根据UserId修改
     */
    @Override
    public Integer updateAccountByUserId(Account bean, Integer userId) {
        return this.accountMapper.updateByUserId(bean, userId);
    }

    /**
     * 根据UserId删除
     */
    @Override
    public Integer deleteAccountByUserId(Integer userId) {
        return this.accountMapper.deleteByUserId(userId);
    }

    /**
     * 根据Phone获取对象
     */
    @Override
    public Account getAccountByPhone(String phone) {
        return this.accountMapper.selectByPhone(phone);
    }

    /**
     * 根据Phone修改
     */
    @Override
    public Integer updateAccountByPhone(Account bean, String phone) {
        return this.accountMapper.updateByPhone(bean, phone);
    }

    /**
     * 根据Phone删除
     */
    @Override
    public Integer deleteAccountByPhone(String phone) {
        return this.accountMapper.deleteByPhone(phone);
    }

    @Override
    public SessionUserAdminDto login(String phone, String password) {
        Account account = this.accountMapper.selectByPhone(phone);
        if (account == null)
            throw new BusinessException("账号或密码错误");
        if (account.getStatus().equals(AccountStatusEnum.DISABLED.getStatus()))
            throw new BusinessException("账号已禁用，请联系管理员");
        if (!StringTools.encodeByMd5(password).equals(account.getPassword()))
            throw new BusinessException("账号或密码错误");
        SessionUserAdminDto sessionUserAdminDto = new SessionUserAdminDto();
        sessionUserAdminDto.setUserId(account.getUserId());
        sessionUserAdminDto.setUserName(account.getUserName());
        List<Menu> menuList;
        if (!StringTools.isEmpty(appConfig.getSuperAdminPhone()) &&
                ArrayUtils.contains(appConfig.getSuperAdminPhone().split(","), phone)) {
            sessionUserAdminDto.setSuperAdmin(true);
            MenuQuery query = new MenuQuery();
            query.setFormatter2Tree(false);
            query.setOrderBy("sort asc");
            menuList = this.menuService.findListByParam(query);
        } else {
            sessionUserAdminDto.setSuperAdmin(false);
            menuList = this.menuService.getMuneListByRoleIds(account.getRoles());
        }
		List<Menu> menuList2 = new ArrayList<>();
        List<String> permissionCodeList = new ArrayList<>();
        for (Menu menu : menuList) {
            if (MenuTypeEnum.MENU.getTypeCode().equals(menu.getMenuType()))
                menuList2.add(menu);
            permissionCodeList.add(menu.getPermissionCode());
        }
		List<MenuVO> menuVOList = new ArrayList<>();
		menuList2 = this.menuService.convertLine2Tree4Menu(menuList2, 1000);
        if (menuList2.isEmpty())
            throw new BusinessException("暂无角色权限，请联系管理员");
		menuList2.forEach(menu -> {
			MenuVO menuVO = CopyUtil.copy(menu, MenuVO.class);
			menuVO.setChildren(CopyUtil.copyList(menu.getChildren(), MenuVO.class));
			menuVOList.add(menuVO);
		});
        sessionUserAdminDto.setMenuList(menuVOList);
        sessionUserAdminDto.setPermissionCodeList(permissionCodeList);
        return sessionUserAdminDto;
    }

    @Override
    public void saveAccount(Account account) {
        Account accountWithPhone = this.accountMapper.selectByPhone(account.getPhone());
        if (accountWithPhone != null && (account.getUserId() == null || !account.getUserId().equals(accountWithPhone.getUserId())))
            throw new BusinessException("该手机号码已被使用");
        //新增
        if (account.getUserId() == null) {
            account.setPassword(StringTools.encodeByMd5(account.getPassword()));
            account.setStatus(AccountStatusEnum.ENABLED.getStatus());
            account.setCreateTime(new Date());
            this.accountMapper.insert(account);
        }
        //修改
        else {
            account.setPassword(null);
            account.setStatus(null);
            this.accountMapper.updateByUserId(account, account.getUserId());
        }
    }
}