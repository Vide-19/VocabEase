package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;
import com.javastudy.vocabease_common.entity.po.Role;
import com.javastudy.vocabease_common.entity.query.RoleQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  角色Controller
 */
@RestController("roleController")
@RequestMapping("/settings")
public class RoleController extends ABaseController {

	@Resource
	private RoleService roleService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadRoles")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_LIST)
	public ResponseVO loadDataList(RoleQuery query){
		query.setOrderBy("create_time desc");
		return getSuccessResponseVO(roleService.findListByPage(query));
	}
	/**
	 * 新增、修改角色
	 */
	@RequestMapping("/saveRole")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_EDIT)
	public ResponseVO saveRole(@VerifyParam Role role,
							   String menuIds,
							   String halfMenuIds) {
		roleService.savaRole(role, menuIds, halfMenuIds);
		return getSuccessResponseVO(null);
	}
	/**
	 * 保存修改的角色对应的菜单
	 */
	@RequestMapping("/saveRole2Menu")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_EDIT)
	public ResponseVO saveRole2Menu(@VerifyParam(required = true) Integer roleId,
									@VerifyParam(required = true) String menuIds,
							   String halfMenuIds) {
		roleService.saveRole2Menu(roleId, menuIds, halfMenuIds);
		return getSuccessResponseVO(null);
	}
	/**
	 * 找到用户角色
	 */
	@RequestMapping("/getRoleByRoleId")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_LIST)
	public ResponseVO getRoleByRoleId(@VerifyParam(required = true) Integer roleId) {
		Role role = roleService.getRoleByRoleId(roleId);
		return getSuccessResponseVO(role);
	}
	/**
	 * 删除角色
	 */
	@RequestMapping("/deleteRole")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_DELETE)
	public ResponseVO deleteRole(@VerifyParam(required = true) Integer roleId) {
		roleService.deleteRoleByRoleId(roleId);
		return getSuccessResponseVO(null);
	}
	/**
	 * 获取所有角色
	 */
	@RequestMapping("/loadRolesList")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.SETTINGS_ROLE_LIST)
	public ResponseVO loadRolesList(){
		RoleQuery roleQuery = new RoleQuery();
		roleQuery.setOrderBy("create_time desc");
		return getSuccessResponseVO(this.roleService.findListByPage(roleQuery));
	}
}