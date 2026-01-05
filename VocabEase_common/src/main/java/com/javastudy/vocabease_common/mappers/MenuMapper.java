package com.javastudy.vocabease_common.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 菜单表 数据库操作接口
 */
public interface MenuMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据MenuId更新
	 */
	 Integer updateByMenuId(@Param("bean") T t,@Param("menuId") Integer menuId);


	/**
	 * 根据MenuId删除
	 */
	 Integer deleteByMenuId(@Param("menuId") Integer menuId);


	/**
	 * 根据MenuId获取对象
	 */
	 T selectByMenuId(@Param("menuId") Integer menuId);


}
