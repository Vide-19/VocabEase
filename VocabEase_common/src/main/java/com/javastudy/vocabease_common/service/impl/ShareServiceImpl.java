package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.Share;
import com.javastudy.vocabease_common.entity.query.ShareQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.ShareMapper;
import com.javastudy.vocabease_common.service.ShareService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * 笔记表 业务接口实现
 */
@Service("shareService")
public class ShareServiceImpl implements ShareService {

	@Resource
	private ShareMapper<Share, ShareQuery> shareMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Share> findListByParam(ShareQuery param) {
		return this.shareMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(ShareQuery param) {
		return this.shareMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Share> findListByPage(ShareQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Share> list = this.findListByParam(param);
		PaginationResultVO<Share> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Share bean) {
		return this.shareMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Share> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.shareMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Share> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.shareMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Share bean, ShareQuery param) {
		StringTools.checkParam(param);
		return this.shareMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(ShareQuery param) {
		StringTools.checkParam(param);
		return this.shareMapper.deleteByParam(param);
	}

	/**
	 * 根据ShareId获取对象
	 */
	@Override
	public Share getShareByShareId(Integer shareId) {
		return this.shareMapper.selectByShareId(shareId);
	}

	/**
	 * 根据ShareId修改
	 */
	@Override
	public Integer updateShareByShareId(Share bean, Integer shareId) {
		return this.shareMapper.updateByShareId(bean, shareId);
	}

	/**
	 * 根据ShareId删除
	 */
	@Override
	public Integer deleteShareByShareId(Integer shareId) {
		return this.shareMapper.deleteByShareId(shareId);
	}
	/**
	 * 保存/新增分享
	 */
	@Override
	public void saveShare(Share share, Boolean isAdmin) {
		//新增
		if (share.getShareId() == null) {
			share.setCreateTime(new Date());
			this.shareMapper.insert(share);
		}
		//修改
		else {
			Share shareDB = this.shareMapper.selectByShareId(share.getShareId());
			if (!shareDB.getShareId().equals(share.getShareId()) && !isAdmin)
				throw new BusinessException("非管理员或作者无法修改当前笔记");
			share.setCreaterId(null);
			share.setCreateTime(null);
			this.shareMapper.updateByShareId(share, share.getShareId());
		}
	}
	/**
	 * 删除分享
	 */
	@Override
	public void deleteShareByShareIds(String shareIds, Integer userId) {
		String[] shareIdArray = shareIds.split(",");
		if (userId != null) {
			ShareQuery query = new ShareQuery();
			query.setShareIds(shareIdArray);
			List<Share> shareList = this.findListByParam(query);
			List<Share> currentUserList = shareList.stream().filter(item ->
					!item.getCreaterId().equals(String.valueOf(userId))).toList();
			if (!currentUserList.isEmpty())
				throw new BusinessException("非管理员或作者无法删除当前笔记");
		}
		ShareQuery query = new ShareQuery();
		query.setShareIds(shareIdArray);
		this.shareMapper.deleteByParam(query);
	}
	/**
	 * 修改发布状态
	 */
	public void updateShareStatus(String shareIds, Integer status) {
		ShareQuery shareQuery = new ShareQuery();
		shareQuery.setShareIds(shareIds.split(","));
		Share share = new Share();
		share.setStatus(status);
		this.updateByParam(share, shareQuery);
	}

	@Override
	public Share showShareNext(ShareQuery shareQuery, Integer currentId,
							   Integer nextType, Boolean isUpdateReadCount) {
		if (nextType == null)
			shareQuery.setShareId(currentId);
		else {
			shareQuery.setShareId(nextType);
			shareQuery.setCurrentId(currentId);
		}
		Share share = this.shareMapper.showShareNext(shareQuery);
		if (share == null && nextType == null)
			throw new BusinessException("抱歉，没有更多了");
		else if (share == null && nextType == -1)
			throw new BusinessException("已经在第一页");
		else if (share == null && nextType == 1)
			throw new BusinessException("已经在最后一页");
		if (isUpdateReadCount && share != null) {
			this.shareMapper.updateCount(1, null, currentId);
			share.setReadCount(share.getReadCount() + 1);
		}
		return share;
	}


}