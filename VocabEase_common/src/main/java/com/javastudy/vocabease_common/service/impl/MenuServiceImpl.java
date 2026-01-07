package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.Menu;
import com.javastudy.vocabease_common.entity.query.MenuQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.mappers.MenuMapper;
import com.javastudy.vocabease_common.service.MenuService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 菜单表 业务接口实现
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {
	public static final Integer DEFAULT_ROOT_MENU_ID = 0;
	public static final String DEFAULT_ROOT_MENU_NAME = "所有菜单";
	@Resource
	private MenuMapper<Menu, MenuQuery> menuMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Menu> findListByParam(MenuQuery param) {
		List<Menu> menuList = this.menuMapper.selectList(param);
		if (param.getFormatter2Tree() != null && param.getFormatter2Tree()) {
			Menu root = new Menu();
			root.setMenuId(DEFAULT_ROOT_MENU_ID);
			root.setMenuName(DEFAULT_ROOT_MENU_NAME);
			root.setpId(-1);
			menuList.add(root);
			menuList = convertLine2Tree4Menu(menuList, -1);
		}
		return menuList;
	}
	/**
	 * 线型转为树型
	 */
	@Override
	public List<Menu> convertLine2Tree4Menu(List<Menu> dataList, Integer pid) {
		List<Menu> children = new ArrayList<>();
		for (Menu menu : dataList) {
			if (menu.getMenuId() != null && menu.getpId() != null && menu.getpId().equals(pid)) {
				menu.setChildren(convertLine2Tree4Menu(dataList, menu.getMenuId()));
				children.add(menu);
			}
		}
		return children;
	}

	/**
	 * 保存修改的菜单
	 * @param menu
	 */
	@Override
	public void saveMenu(Menu menu) {
		if (menu.getMenuId() == null)
			this.menuMapper.insert(menu);
		else
			this.menuMapper.updateByMenuId(menu, menu.getMenuId());
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(MenuQuery param) {
		return this.menuMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Menu> findListByPage(MenuQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Menu> list = this.findListByParam(param);
		PaginationResultVO<Menu> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Menu bean) {
		return this.menuMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Menu> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.menuMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Menu> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.menuMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Menu bean, MenuQuery param) {
		StringTools.checkParam(param);
		return this.menuMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(MenuQuery param) {
		StringTools.checkParam(param);
		return this.menuMapper.deleteByParam(param);
	}

	/**
	 * 根据MenuId获取对象
	 */
	@Override
	public Menu getMenuByMenuId(Integer menuId) {
		return this.menuMapper.selectByMenuId(menuId);
	}

	/**
	 * 根据MenuId修改
	 */
	@Override
	public Integer updateMenuByMenuId(Menu bean, Integer menuId) {
		return this.menuMapper.updateByMenuId(bean, menuId);
	}

	/**
	 * 根据MenuId删除
	 */
	@Override
	public Integer deleteMenuByMenuId(Integer menuId) {
		return this.menuMapper.deleteByMenuId(menuId);
	}
}