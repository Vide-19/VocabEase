package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.Item4question;
import com.javastudy.vocabease_common.entity.query.Item4questionQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 问题选项表 业务接口
 */
public interface Item4questionService {

	/**
	 * 根据条件查询列表
	 */
	List<Item4question> findListByParam(Item4questionQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(Item4questionQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Item4question> findListByPage(Item4questionQuery param);

	/**
	 * 新增
	 */
	Integer add(Item4question bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Item4question> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Item4question> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Item4question bean,Item4questionQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(Item4questionQuery param);

	/**
	 * 根据ItemId查询对象
	 */
	Item4question getItem4questionByItemId(Integer itemId);


	/**
	 * 根据ItemId修改
	 */
	Integer updateItem4questionByItemId(Item4question bean,Integer itemId);


	/**
	 * 根据ItemId删除
	 */
	Integer deleteItem4questionByItemId(Integer itemId);

}