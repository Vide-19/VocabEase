package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.Role2menu;
import com.javastudy.vocabease_common.entity.query.Role2menuQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 角色菜单表 业务接口
 */
public interface Role2menuService {

	/**
	 * 根据条件查询列表
	 */
	List<Role2menu> findListByParam(Role2menuQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(Role2menuQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Role2menu> findListByPage(Role2menuQuery param);

	/**
	 * 新增
	 */
	Integer add(Role2menu bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Role2menu> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Role2menu> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Role2menu bean,Role2menuQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(Role2menuQuery param);

	/**
	 * 根据RoleIdAndMenuId查询对象
	 */
	Role2menu getRole2menuByRoleIdAndMenuId(Integer roleId,Integer menuId);


	/**
	 * 根据RoleIdAndMenuId修改
	 */
	Integer updateRole2menuByRoleIdAndMenuId(Role2menu bean,Integer roleId,Integer menuId);


	/**
	 * 根据RoleIdAndMenuId删除
	 */
	Integer deleteRole2menuByRoleIdAndMenuId(Integer roleId,Integer menuId);

}