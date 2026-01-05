package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.Menu;
import com.javastudy.vocabease_common.entity.query.MenuQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 菜单表 业务接口
 */
public interface MenuService {

	/**
	 * 根据条件查询列表
	 */
	List<Menu> findListByParam(MenuQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(MenuQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Menu> findListByPage(MenuQuery param);

	/**
	 * 新增
	 */
	Integer add(Menu bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Menu> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Menu> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Menu bean,MenuQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(MenuQuery param);

	/**
	 * 根据MenuId查询对象
	 */
	Menu getMenuByMenuId(Integer menuId);


	/**
	 * 根据MenuId修改
	 */
	Integer updateMenuByMenuId(Menu bean,Integer menuId);


	/**
	 * 根据MenuId删除
	 */
	Integer deleteMenuByMenuId(Integer menuId);

}