package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppCarousel;
import com.javastudy.vocabease_common.entity.query.AppCarouselQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.AppCarouselService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小程序轮播图表 Controller
 */
@RestController("appCarouselController")
@RequestMapping("/appCarousel")
public class AppCarouselController extends ABaseController{

	@Resource
	private AppCarouselService appCarouselService;

	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadCarouselList")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_CAROUSEL_LIST)
	public ResponseVO<List<AppCarousel>> loadCarouselList(AppCarouselQuery query){
		query.setOrderBy("sort desc");
		return getSuccessResponseVO(this.appCarouselService.findListByParam(query));
	}
	/**
	 * 新增/修改轮播图
	 */
	@RequestMapping("/saveCarousel")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_CAROUSEL_EDIT)
	public ResponseVO<Void> saveCarousel(AppCarousel appCarousel) {
		this.appCarouselService.saveAppCarousel(appCarousel);
		return getSuccessResponseVO(null);
	}
	/**
	 * 删除轮播图
	 */
	@RequestMapping("/deleteCarousel")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_CAROUSEL_EDIT)
	public ResponseVO<Void> deleteCarousel(@VerifyParam(required = true) Integer carouselId) {
		this.appCarouselService.deleteAppCarouselByCarouselId(carouselId);
		return getSuccessResponseVO(null);
	}
	/**
	 * 修改轮播图排序
	 */
	@RequestMapping("/updateSort")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.APP_CAROUSEL_EDIT)
	public ResponseVO<Void> updateSort(String carouselIds) {
		this.appCarouselService.updateSort(carouselIds);
		return getSuccessResponseVO(null);
	}
}