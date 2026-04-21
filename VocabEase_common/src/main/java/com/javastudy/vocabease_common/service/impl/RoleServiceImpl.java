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
		PaginationResultVO<Role> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
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
		Role role = roleMapper.selectByRoleId(roleId);
		if (role != null) {
			// 只查叶子节点（实际 role2menu 存的就是叶子）
			List<Integer> menuIds = role2menuMapper.selectMenuIdsByRoleIds(
					new String[]{String.valueOf(roleId)}
			);
			role.setMenuIds(menuIds); // 前端用这个初始化 el-tree
		}
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
	public void savaRole(Role role, String menuIds) {
		Integer roleId = role.getRoleId();
		boolean isNew = (roleId == null);

		// 先校验角色名唯一性（修复逻辑）
		RoleQuery query = new RoleQuery();
		query.setRoleName(role.getRoleName());
		Integer count = findCountByParam(query);

		// 新增：count > 0 则重复；编辑：count > 1 则重复（排除自己）
		if ((isNew && count > 0) || (!isNew && count > 1)) {
			throw new BusinessException("该角色名称已存在");
		}

		if (isNew) {
			Date now = new Date();
			role.setCreateTime(now);
			role.setLastUpdateTime(now);
			roleMapper.insert(role);
			roleId = role.getRoleId();
		} else {
			role.setLastUpdateTime(new Date());
			roleMapper.updateByRoleId(role, roleId);
		}

		// 只有传入了menuIds才更新菜单权限
		if (!StringTools.isEmpty(menuIds)) {
			saveRole2Menu(roleId, menuIds);
		}
	}

	public void saveRole2Menu(Integer roleId, String menuIds) {
		if (roleId == null || StringTools.isEmpty(menuIds)) {
			throw new BusinessException(ResponseCodeEnum.CODE_400);
		}

		// 删除旧关联
		Role2menuQuery delQuery = new Role2menuQuery();
		delQuery.setRoleId(roleId);
		role2menuMapper.deleteByParam(delQuery);

		// 批量插入新关联（全部视为叶子节点）
		String[] ids = menuIds.split(",");
		List<Role2menu> list = new ArrayList<>();
		for (String id : ids) {
			String trimId = id.trim();
			if (!trimId.isEmpty()) {
				try {
					Role2menu r2m = new Role2menu();
					r2m.setRoleId(roleId);
					r2m.setMenuId(Integer.parseInt(trimId));
					list.add(r2m);
				} catch (NumberFormatException e) {
					throw new BusinessException("菜单ID格式错误");
				}
			}
		}
		if (!list.isEmpty()) {
			role2menuMapper.insertBatch(list);
		}
	}

	private static List<Role2menu> getRole2MenuList(Integer roleId, String[] menuIdsArray) {
		List<Role2menu> role2MenuList = new ArrayList<>();
		for (String menuId : menuIdsArray) {
			Role2menu role2menu = new Role2menu();
			role2menu.setMenuId(Integer.parseInt(menuId));
			role2menu.setRoleId(roleId);
			role2menu.setCheckType(MenuCheckTypeEnum.ALL.getCheckTypeCode());
			role2MenuList.add(role2menu);
		}
		return role2MenuList;
	}

}