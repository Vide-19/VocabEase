package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.CollectTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppCollect;
import com.javastudy.vocabease_common.entity.query.AppCollectQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.AppCollectMapper;
import com.javastudy.vocabease_common.service.AppCollectService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户收藏表 业务接口实现
 */
@Service("appCollectService")
public class AppCollectServiceImpl implements AppCollectService {

	@Resource
	private AppCollectMapper<AppCollect, AppCollectQuery> appCollectMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<AppCollect> findListByParam(AppCollectQuery param) {
		return this.appCollectMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(AppCollectQuery param) {
		return this.appCollectMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<AppCollect> findListByPage(AppCollectQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<AppCollect> list = this.findListByParam(param);
		PaginationResultVO<AppCollect> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(AppCollect bean) {
		return this.appCollectMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<AppCollect> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.appCollectMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<AppCollect> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.appCollectMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(AppCollect bean, AppCollectQuery param) {
		StringTools.checkParam(param);
		return this.appCollectMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(AppCollectQuery param) {
		StringTools.checkParam(param);
		return this.appCollectMapper.deleteByParam(param);
	}

	/**
	 * 根据CollectId获取对象
	 */
	@Override
	public AppCollect getAppCollectByCollectId(Integer collectId) {
		return this.appCollectMapper.selectByCollectId(collectId);
	}

	/**
	 * 根据CollectId修改
	 */
	@Override
	public Integer updateAppCollectByCollectId(AppCollect bean, Integer collectId) {
		return this.appCollectMapper.updateByCollectId(bean, collectId);
	}

	/**
	 * 根据CollectId删除
	 */
	@Override
	public Integer deleteAppCollectByCollectId(Integer collectId) {
		return this.appCollectMapper.deleteByCollectId(collectId);
	}

	@Override
	public void addCollect(String userId, String objectId, Integer collectType) {
		CollectTypeEnum type = CollectTypeEnum.getEnum(collectType);
		if (type == null)
			throw new BusinessException(ResponseCodeEnum.CODE_400);
		AppCollectQuery query = new AppCollectQuery();
		query.setUserId(userId);
		query.setObjectId(objectId);
		query.setCollectType(collectType);
		if (this.appCollectMapper.selectCount(query) != 0)
			return;
		AppCollect appCollect = new AppCollect();
		appCollect.setUserId(userId);
		appCollect.setObjectId(objectId);
		appCollect.setCollectType(collectType);
		this.appCollectMapper.insert(appCollect);
	}

	@Override
	public void cancelCollect(String userId, String objectId, Integer collectType) {
		CollectTypeEnum type = CollectTypeEnum.getEnum(collectType);
		if (type == null)
			throw new BusinessException(ResponseCodeEnum.CODE_400);
		AppCollectQuery query = new AppCollectQuery();
		query.setUserId(userId);
		query.setObjectId(objectId);
		query.setCollectType(collectType);
		AppCollect collect = this.appCollectMapper.selectByParam(query);
		this.appCollectMapper.deleteByCollectId(collect.getCollectId());
	}

	@Override
	public AppCollect showCollectNext(AppCollectQuery query, Integer nextType, Integer currentId) {
		if (nextType == null)
			query.setCollectId(currentId);
		else {
			query.setNextType(nextType);
			query.setCurrentId(currentId);
		}
		AppCollect collect = this.appCollectMapper.showCollectNext(query);
		if (collect == null && nextType == null)
			throw new BusinessException("抱歉，没有更多了");
		else if (collect == null && nextType == -1)
			throw new BusinessException("已经在第一页");
		else if (collect == null && nextType == 1)
			throw new BusinessException("已经在最后一页");
		return collect;
	}


}