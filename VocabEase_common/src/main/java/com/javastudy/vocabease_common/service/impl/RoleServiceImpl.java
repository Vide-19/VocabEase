package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.MenuCheckTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.Account;
import com.javastudy.vocabease_common.entity.po.Role;
import com.javastudy.vocabease_common.entity.po.Role2menu;
import com.javastudy.vocabease_common.entity.query.AccountQuery;
import com.javastudy.vocabease_common.entity.query.Role2menuQuery;
import com.javastudy.vocabease_common.entity.query.RoleQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.AccountMapper;
import com.javastudy.vocabease_common.mappers.Role2menuMapper;
import com.javastudy.vocabease_common.mappers.RoleMapper;
import com.javastudy.vocabease_common.service.RoleService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *  业务接口实现
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleMapper<Role, RoleQuery> roleMapper;

	@Resource
	private Role2menuMapper<Role2menu, Role2menuQuery> role2menuMapper;

	@Resource
	private AccountMapper<Account, AccountQuery> accountMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Role> findListByParam(RoleQuery param) {
		return this.roleMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(RoleQuery param) {
		return this.roleMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Role> findListByPage(RoleQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Role> list = this.findListByParam(param);
		PaginationResultVO<Role> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Role bean) {
		return this.roleMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Role> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.roleMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Role> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.roleMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Role bean, RoleQuery param) {
		StringTools.checkParam(param);
		return this.roleMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(RoleQuery param) {
		StringTools.checkParam(param);
		return this.roleMapper.deleteByParam(param);
	}

	/**
	 * 根据RoleId获取对象
	 */
	@Override
	public Role getRoleByRoleId(Integer roleId) {
		Role role = this.roleMapper.selectByRoleId(roleId);
		List<Integer> selectMenuIds = this.role2menuMapper.selectMenuIdsByRoleIds(new String[]{String.valueOf(roleId)});
		role.setMenuIds(selectMenuIds);
		return role;
	}

	/**
	 * 根据RoleId修改
	 */
	@Override
	public Integer updateRoleByRoleId(Role bean, Integer roleId) {
		return this.roleMapper.updateByRoleId(bean, roleId);
	}

	/**
	 * 根据RoleId删除
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer deleteRoleByRoleId(Integer roleId) {
		AccountQuery accountQuery = new AccountQuery();
		accountQuery.setRoles(String.valueOf(roleId));
		Integer count = this.accountMapper.selectCount(accountQuery);
		if (count > 0)
			throw new BusinessException("该角色仍被使用，无法进行删除");
		count = this.roleMapper.deleteByRoleId(roleId);
		Role2menuQuery role2menuQuery = new Role2menuQuery();
		role2menuQuery.setRoleId(roleId);
		this.role2menuMapper.deleteByParam(role2menuQuery);
		return count;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void savaRole(Role role, String menuIds, String halfMenuIds) {
		Integer roleId = role.getRoleId();
		Boolean addMenu = false;
		//新增
		if (roleId == null) {
			Date date = new Date();
			role.setCreateTime(date);
			role.setLastUpdateTime(date);
			this.roleMapper.insert(role);
			addMenu = true;
			roleId = role.getRoleId();
		}
		//修改
		else {
			role.setCreateTime(null);
			this.roleMapper.updateByRoleId(role, roleId);
		}
		RoleQuery roleQuery = new RoleQuery();
		roleQuery.setRoleName(role.getRoleName());
		Integer count = this.findCountByParam(roleQuery);
		if (count > 1)
			throw new BusinessException("该角色已存在，请重新设置");
		if (addMenu) {
			this.saveRole2Menu(roleId, menuIds, halfMenuIds);
		}
	}

	public void saveRole2Menu(Integer roleId, String menuIds, String halfMenuIds) {
		if (roleId == null || menuIds == null)
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		Role2menuQuery role2menuQuery = new Role2menuQuery();
		role2menuQuery.setRoleId(roleId);
		this.role2menuMapper.deleteByParam(role2menuQuery);
		String[] menuIdsArray = menuIds.split(",");
		String[] halfMenuIdsArray = halfMenuIds.split(",");
		List<Role2menu> role2menuList = getRole2MenuList(roleId, menuIdsArray, halfMenuIdsArray);
		if (!role2menuList.isEmpty())
			this.role2menuMapper.insertBatch(role2menuList);
	}

	private static List<Role2menu> getRole2MenuList(Integer roleId, String[] menuIdsArray, String[] halfMenuIdsArray) {
		List<Role2menu> role2MenuList = new ArrayList<>();
		for (String menuId : menuIdsArray) {
			Role2menu role2menu = new Role2menu();
			role2menu.setMenuId(Integer.parseInt(menuId));
			role2menu.setRoleId(roleId);
			role2menu.setCheckType(MenuCheckTypeEnum.ALL.getCheckTypeCode());
			role2MenuList.add(role2menu);
		}
		for (String menuId : halfMenuIdsArray) {
			Role2menu role2menu = new Role2menu();
			role2menu.setMenuId(Integer.parseInt(menuId));
			role2menu.setRoleId(roleId);
			role2menu.setCheckType(MenuCheckTypeEnum.HALF.getCheckTypeCode());
			role2MenuList.add(role2menu);
		}
		return role2MenuList;
	}

}