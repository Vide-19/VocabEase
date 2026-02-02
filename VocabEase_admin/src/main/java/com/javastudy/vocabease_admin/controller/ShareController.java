package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_common.entity.po.Share;
import com.javastudy.vocabease_common.entity.query.ShareQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.ShareService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 笔记表 Controller
 */
@RestController("shareController")
@RequestMapping("/share")
public class ShareController extends ABaseController{

	@Resource
	private ShareService shareService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(ShareQuery query){
		return getSuccessResponseVO(shareService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(Share bean) {
		shareService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<Share> listBean) {
		shareService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<Share> listBean) {
		shareService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ShareId查询对象
	 */
	@RequestMapping("/getShareByShareId")
	public ResponseVO getShareByShareId(Integer shareId) {
		return getSuccessResponseVO(shareService.getShareByShareId(shareId));
	}

	/**
	 * 根据ShareId修改对象
	 */
	@RequestMapping("/updateShareByShareId")
	public ResponseVO updateShareByShareId(Share bean,Integer shareId) {
		shareService.updateShareByShareId(bean,shareId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据ShareId删除
	 */
	@RequestMapping("/deleteShareByShareId")
	public ResponseVO deleteShareByShareId(Integer shareId) {
		shareService.deleteShareByShareId(shareId);
		return getSuccessResponseVO(null);
	}
}