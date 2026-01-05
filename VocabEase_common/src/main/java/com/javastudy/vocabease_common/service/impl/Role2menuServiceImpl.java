package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.Role2menu;
import com.javastudy.vocabease_common.entity.query.Role2menuQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.mappers.Role2menuMapper;
import com.javastudy.vocabease_common.service.Role2menuService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 角色菜单表 业务接口实现
 */
@Service("role2menuService")
public class Role2menuServiceImpl implements Role2menuService {

	@Resource
	private Role2menuMapper<Role2menu, Role2menuQuery> role2menuMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Role2menu> findListByParam(Role2menuQuery param) {
		return this.role2menuMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(Role2menuQuery param) {
		return this.role2menuMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Role2menu> findListByPage(Role2menuQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Role2menu> list = this.findListByParam(param);
		PaginationResultVO<Role2menu> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Role2menu bean) {
		return this.role2menuMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Role2menu> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.role2menuMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Role2menu> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.role2menuMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Role2menu bean, Role2menuQuery param) {
		StringTools.checkParam(param);
		return this.role2menuMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(Role2menuQuery param) {
		StringTools.checkParam(param);
		return this.role2menuMapper.deleteByParam(param);
	}

	/**
	 * 根据RoleIdAndMenuId获取对象
	 */
	@Override
	public Role2menu getRole2menuByRoleIdAndMenuId(Integer roleId, Integer menuId) {
		return this.role2menuMapper.selectByRoleIdAndMenuId(roleId, menuId);
	}

	/**
	 * 根据RoleIdAndMenuId修改
	 */
	@Override
	public Integer updateRole2menuByRoleIdAndMenuId(Role2menu bean, Integer roleId, Integer menuId) {
		return this.role2menuMapper.updateByRoleIdAndMenuId(bean, roleId, menuId);
	}

	/**
	 * 根据RoleIdAndMenuId删除
	 */
	@Override
	public Integer deleteRole2menuByRoleIdAndMenuId(Integer roleId, Integer menuId) {
		return this.role2menuMapper.deleteByRoleIdAndMenuId(roleId, menuId);
	}
}