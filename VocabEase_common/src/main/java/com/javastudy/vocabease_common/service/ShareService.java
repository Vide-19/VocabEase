package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.Share;
import com.javastudy.vocabease_common.entity.query.ShareQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 笔记表 业务接口
 */
public interface ShareService {

	/**
	 * 根据条件查询列表
	 */
	List<Share> findListByParam(ShareQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(ShareQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Share> findListByPage(ShareQuery param);

	/**
	 * 新增
	 */
	Integer add(Share bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Share> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Share> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Share bean,ShareQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(ShareQuery param);

	/**
	 * 根据ShareId查询对象
	 */
	Share getShareByShareId(Integer shareId);


	/**
	 * 根据ShareId修改
	 */
	Integer updateShareByShareId(Share bean,Integer shareId);


	/**
	 * 根据ShareId删除
	 */
	Integer deleteShareByShareId(Integer shareId);

	/**
	 * 保存/新增分享
	 */
	void saveShare(Share share, Boolean isAdmin);
	/**
	 * 删除分享
	 */
	void deleteShareByShareIds(String shareIds, String userId);
	/**
	 * 修改发布状态
	 */
	void updateShareStatus(String shareIds, Integer status);

	Share showShareNext(ShareQuery shareQuery, Integer currentId, Integer nextType, Boolean isUpdateReadCount);

	Share showNextCollectedShare(String userId, Integer currentId, Integer nextType);

	void updateCollectCountById(String shareId);

	void updateReadCountById(Integer shareId);
}