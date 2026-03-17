package com.javastudy.vocabease_common.mappers;

import com.javastudy.vocabease_common.entity.po.Category;
import com.javastudy.vocabease_common.entity.query.CategoryQuery;
import org.apache.ibatis.annotations.Param;

/**
 * 分类表 数据库操作接口
 */
public interface CategoryMapper extends BaseMapper<Category, CategoryQuery> {

	/**
	 * 根据CategoryId更新
	 */
	 Integer updateByCategoryId(@Param("bean") Category t,@Param("categoryId") Integer categoryId);

	/**
	 * 根据CategoryId删除
	 */
	 Integer deleteByCategoryId(@Param("categoryId") Integer categoryId);

	/**
	 * 根据CategoryId获取对象
	 */
	Category selectByCategoryId(@Param("categoryId") Integer categoryId);

	/**
	 * 根据CategoryName获取对象
	 */
	Integer selectCategoryIdByCategoryName(@Param("categoryName") String categoryName);

	 void updateCategoryName(@Param("categoryId") Integer categoryId,@Param("categoryName") String categoryName);

	 Integer selectMaxSortByType(Integer type);



}
