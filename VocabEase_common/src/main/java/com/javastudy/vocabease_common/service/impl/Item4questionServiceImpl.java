package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.Item4question;
import com.javastudy.vocabease_common.entity.query.Item4questionQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.mappers.Item4questionMapper;
import com.javastudy.vocabease_common.service.Item4questionService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 问题选项表 业务接口实现
 */
@Service("item4questionService")
public class Item4questionServiceImpl implements Item4questionService {

	@Resource
	private Item4questionMapper<Item4question, Item4questionQuery> item4questionMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Item4question> findListByParam(Item4questionQuery param) {
		return this.item4questionMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(Item4questionQuery param) {
		return this.item4questionMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Item4question> findListByPage(Item4questionQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Item4question> list = this.findListByParam(param);
		PaginationResultVO<Item4question> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Item4question bean) {
		return this.item4questionMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Item4question> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.item4questionMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Item4question> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.item4questionMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Item4question bean, Item4questionQuery param) {
		StringTools.checkParam(param);
		return this.item4questionMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(Item4questionQuery param) {
		StringTools.checkParam(param);
		return this.item4questionMapper.deleteByParam(param);
	}

	/**
	 * 根据ItemId获取对象
	 */
	@Override
	public Item4question getItem4questionByItemId(Integer itemId) {
		return this.item4questionMapper.selectByItemId(itemId);
	}

	/**
	 * 根据ItemId修改
	 */
	@Override
	public Integer updateItem4questionByItemId(Item4question bean, Integer itemId) {
		return this.item4questionMapper.updateByItemId(bean, itemId);
	}

	/**
	 * 根据ItemId删除
	 */
	@Override
	public Integer deleteItem4questionByItemId(Integer itemId) {
		return this.item4questionMapper.deleteByItemId(itemId);
	}
}