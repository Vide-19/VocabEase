package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;
import com.javastudy.vocabease_common.entity.po.Category;
import com.javastudy.vocabease_common.entity.query.CategoryQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类表 Controller
 */
@RestController("categoryController")
@RequestMapping("/category")
public class CategoryController extends ABaseController{

	@Resource
	private CategoryService categoryService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadCategoryList")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.CATEGORY_LIST)
	public ResponseVO loadCategoryList(CategoryQuery query){
		query.setOrderBy("sort asc");
		return getSuccessResponseVO(categoryService.findListByParam(query));
	}
	/**
	 * 新增分类
	 */
	@RequestMapping("/saveCategory")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.CATEGORY_EDIT)
	public ResponseVO saveCategory(Category category){
		this.categoryService.saveCategory(category);
		return getSuccessResponseVO(null);
	}
	/**
	 * 删除分类
	 */
	@RequestMapping("/deleteCategory")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.CATEGORY_DELETE)
	public ResponseVO deleteCategory(@VerifyParam(required = true) Integer categoryId){
		this.categoryService.deleteCategoryByCategoryId(categoryId);
		return getSuccessResponseVO(null);
	}
	/**
	 * 修改排序
	 */
	@RequestMapping("/updateSort")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.CATEGORY_EDIT)
	public ResponseVO updateSort(@VerifyParam(required = true) String  categoryIds){
		this.categoryService.updateSort(categoryIds);
		return getSuccessResponseVO(null);
	}
	/**
	 * 展示不同type的不同分类
	 */
	@RequestMapping("/loadCategoryByType")
	@GlobalInterceptor(permissionCode = PermissionCodeEnum.CATEGORY_LIST)
	public ResponseVO loadCategoryByType(@VerifyParam(required = true) Integer categoryType){
		List<Category> categoryList = categoryService.getCategoryListByType(categoryType);
		return getSuccessResponseVO(categoryList);
	}
}