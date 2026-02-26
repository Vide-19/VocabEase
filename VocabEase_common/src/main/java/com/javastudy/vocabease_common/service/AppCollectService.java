package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.AppCollect;
import com.javastudy.vocabease_common.entity.query.AppCollectQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 用户收藏表 业务接口
 */
public interface AppCollectService {

	/**
	 * 根据条件查询列表
	 */
	List<AppCollect> findListByParam(AppCollectQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(AppCollectQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<AppCollect> findListByPage(AppCollectQuery param);

	/**
	 * 新增
	 */
	Integer add(AppCollect bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<AppCollect> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<AppCollect> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(AppCollect bean,AppCollectQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(AppCollectQuery param);

	/**
	 * 根据CollectId查询对象
	 */
	AppCollect getAppCollectByCollectId(Integer collectId);


	/**
	 * 根据CollectId修改
	 */
	Integer updateAppCollectByCollectId(AppCollect bean,Integer collectId);


	/**
	 * 根据CollectId删除
	 */
	Integer deleteAppCollectByCollectId(Integer collectId);

	void addCollect(String userId, String objectId, Integer collectType);

	void cancelCollect(String userId, String objectId, Integer collectType);

	AppCollect showCollectNext(AppCollectQuery query, Integer nextType, Integer currentId);

}