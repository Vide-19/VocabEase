package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_common.entity.po.Menu;
import com.javastudy.vocabease_common.entity.query.MenuQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.MenuService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单表 Controller
 */
@RestController("menuController")
@RequestMapping("/settings")
public class MenuController extends ABaseController{

	@Resource
	private MenuService menuService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/menuList")
	public ResponseVO loadDataList(){
		MenuQuery query = new MenuQuery();
		query.setFormatter2Tree(true);
		query.setOrderBy("sort asc");
		List<Menu> menuList = menuService.findListByParam(query);
		return getSuccessResponseVO(menuList);
	}

}