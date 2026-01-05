package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_common.entity.po.Role;
import com.javastudy.vocabease_common.entity.query.RoleQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  Controller
 */
@RestController("roleController")
@RequestMapping("/role")
public class RoleController extends ABaseController {

	@Resource
	private RoleService roleService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(RoleQuery query){
		return getSuccessResponseVO(roleService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(Role bean) {
		roleService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<Role> listBean) {
		roleService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<Role> listBean) {
		roleService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据RoleId查询对象
	 */
	@RequestMapping("/getRoleByRoleId")
	public ResponseVO getRoleByRoleId(Integer roleId) {
		return getSuccessResponseVO(roleService.getRoleByRoleId(roleId));
	}

	/**
	 * 根据RoleId修改对象
	 */
	@RequestMapping("/updateRoleByRoleId")
	public ResponseVO updateRoleByRoleId(Role bean,Integer roleId) {
		roleService.updateRoleByRoleId(bean,roleId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据RoleId删除
	 */
	@RequestMapping("/deleteRoleByRoleId")
	public ResponseVO deleteRoleByRoleId(Integer roleId) {
		roleService.deleteRoleByRoleId(roleId);
		return getSuccessResponseVO(null);
	}
}