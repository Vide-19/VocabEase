package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_common.entity.po.Item4question;
import com.javastudy.vocabease_common.entity.query.Item4questionQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.Item4questionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 问题选项表 Controller
 */
@RestController("item4questionController")
@RequestMapping("/item4question")
public class Item4questionController extends ABaseController{

	@Resource
	private Item4questionService item4questionService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(Item4questionQuery query){
		return getSuccessResponseVO(item4questionService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(Item4question bean) {
		item4questionService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<Item4question> listBean) {
		item4questionService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<Item4question> listBean) {
		item4questionService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ItemId查询对象
	 */
	@RequestMapping("/getItem4questionByItemId")
	public ResponseVO getItem4questionByItemId(Integer itemId) {
		return getSuccessResponseVO(item4questionService.getItem4questionByItemId(itemId));
	}

	/**
	 * 根据ItemId修改对象
	 */
	@RequestMapping("/updateItem4questionByItemId")
	public ResponseVO updateItem4questionByItemId(Item4question bean,Integer itemId) {
		item4questionService.updateItem4questionByItemId(bean,itemId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ItemId删除
	 */
	@RequestMapping("/deleteItem4questionByItemId")
	public ResponseVO deleteItem4questionByItemId(Integer itemId) {
		item4questionService.deleteItem4questionByItemId(itemId);
		return getSuccessResponseVO(null);
	}
}