package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.Role;
import com.javastudy.vocabease_common.entity.query.RoleQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.mappers.RoleMapper;
import com.javastudy.vocabease_common.service.RoleService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *  业务接口实现
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleMapper<Role, RoleQuery> roleMapper;

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
		return this.roleMapper.selectByRoleId(roleId);
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
	public Integer deleteRoleByRoleId(Integer roleId) {
		return this.roleMapper.deleteByRoleId(roleId);
	}
}