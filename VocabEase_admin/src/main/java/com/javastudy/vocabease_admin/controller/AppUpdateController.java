package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.po.AppUpdate;
import com.javastudy.vocabease_common.entity.query.AppUpdateQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.AppUpdateService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 小程序发布表 Controller
 */
@RestController("appUpdateController")
@RequestMapping("/appUpdate")
public class AppUpdateController extends ABaseController{

	@Resource
	private AppUpdateService appUpdateService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadUpdateList")
	public ResponseVO<PaginationResultVO<AppUpdate>> loadUpdateList(AppUpdateQuery query){
		query.setOrderBy("id desc");
		return getSuccessResponseVO(appUpdateService.findListByPage(query));
	}
	/**
	 * 新增/修改更新
	 */
	@RequestMapping("/saveAppUpdate")
	public ResponseVO<Void> saveAppUpdate(Integer id, @VerifyParam(required = true) String version,
										  @VerifyParam(required = true) String description,
										  @VerifyParam(required = true) Integer updateType,
										  MultipartFile file) {
		AppUpdate appUpdate = new AppUpdate();
		appUpdate.setId(id);
		appUpdate.setVersion(version);
		appUpdate.setUpdateDesc(description);
		appUpdate.setUpdateType(updateType);
		this.appUpdateService.saveAppUpdate(appUpdate, file);
		return getSuccessResponseVO(null);
	}
	/**
	 * 取消更新
	 */
	@RequestMapping("/deleteAppUpdate")
	public ResponseVO<Void> deleteAppUpdate(@VerifyParam(required = true) Integer id) {
		this.appUpdateService.deleteAppUpdateById(id);
		return getSuccessResponseVO(null);
	}
	/**
	 * 发布更新
	 */
	@RequestMapping("/postAppUpdate")
	public ResponseVO<Void> postAppUpdate(@VerifyParam(required = true) Integer id,
										  @VerifyParam(required = true) Integer status,
										  String grayscaleDevice) {
		this.appUpdateService.postAppUpdate(id, status, grayscaleDevice);
		return getSuccessResponseVO(null);
	}
}