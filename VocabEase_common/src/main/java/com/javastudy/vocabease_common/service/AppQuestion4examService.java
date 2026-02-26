package com.javastudy.vocabease_common.service;

import java.util.List;

import com.javastudy.vocabease_common.entity.query.AppQuestion4examQuery;
import com.javastudy.vocabease_common.entity.po.AppQuestion4exam;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;


/**
 * 测试问题表 业务接口
 */
public interface AppQuestion4examService {

	/**
	 * 根据条件查询列表
	 */
	List<AppQuestion4exam> findListByParam(AppQuestion4examQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(AppQuestion4examQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<AppQuestion4exam> findListByPage(AppQuestion4examQuery param);

	/**
	 * 新增
	 */
	Integer add(AppQuestion4exam bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<AppQuestion4exam> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<AppQuestion4exam> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(AppQuestion4exam bean,AppQuestion4examQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(AppQuestion4examQuery param);

	/**
	 * 根据Id查询对象
	 */
	AppQuestion4exam getAppQuestion4examById(Integer id);


	/**
	 * 根据Id修改
	 */
	Integer updateAppQuestion4examById(AppQuestion4exam bean,Integer id);


	/**
	 * 根据Id删除
	 */
	Integer deleteAppQuestion4examById(Integer id);

}