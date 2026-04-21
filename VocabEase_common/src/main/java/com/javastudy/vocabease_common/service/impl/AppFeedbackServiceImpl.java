package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.FeedbackEnum;
import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppFeedback;
import com.javastudy.vocabease_common.entity.query.AppFeedbackQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.AppFeedbackMapper;
import com.javastudy.vocabease_common.service.AppFeedbackService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * 问题反馈表 业务接口实现
 */
@Service("appFeedbackService")
public class AppFeedbackServiceImpl implements AppFeedbackService {

	@Resource
	private AppFeedbackMapper<AppFeedback, AppFeedbackQuery> appFeedbackMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<AppFeedback> findListByParam(AppFeedbackQuery param) {
		return this.appFeedbackMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(AppFeedbackQuery param) {
		return this.appFeedbackMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<AppFeedback> findListByPage(AppFeedbackQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<AppFeedback> list = this.findListByParam(param);
		PaginationResultVO<AppFeedback> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}
	/**
	 * 回复反馈
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void replyFeedback(AppFeedback appFeedback) {
		AppFeedback pFeedback = this.appFeedbackMapper.selectByFeedbackId(appFeedback.getpFeedbackId());
		if (pFeedback == null)
			throw new BusinessException(ResponseCodeEnum.CODE_400);
		appFeedback.setCreateTime(new Date());
		appFeedback.setpFeedbackId(pFeedback.getFeedbackId());
		appFeedback.setStatus(FeedbackEnum.NO_REPLY.getCode());
		appFeedback.setSendType(FeedbackEnum.ADMIN.getCode());
		appFeedback.setLastSendTime(new Date());
		this.appFeedbackMapper.insert(appFeedback);
		//状态:已回复
		pFeedback.setStatus(FeedbackEnum.IS_REPLY.getCode());
		this.appFeedbackMapper.updateByFeedbackId(pFeedback, pFeedback.getFeedbackId());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveNewFeedback(AppFeedback appFeedback) {
		Date currentDate = new Date();
		if (appFeedback.getpFeedbackId() != null && appFeedback.getpFeedbackId() != 0) {
			AppFeedback pFeedbackDB = this.appFeedbackMapper.selectByFeedbackId(appFeedback.getpFeedbackId());
			if (pFeedbackDB == null)
				throw new BusinessException(ResponseCodeEnum.CODE_400);
			AppFeedback pFeedbackUpdate = new AppFeedback();
			pFeedbackUpdate.setLastSendTime(currentDate);
			pFeedbackUpdate.setStatus(FeedbackEnum.NO_REPLY.getCode());
			this.appFeedbackMapper.updateByFeedbackId(pFeedbackUpdate, pFeedbackDB.getFeedbackId());
		} else
			appFeedback.setpFeedbackId(0);
		appFeedback.setStatus(FeedbackEnum.NO_REPLY.getCode());
		appFeedback.setCreateTime(currentDate);
		appFeedback.setSendType(FeedbackEnum.CLIENT.getCode());
		appFeedback.setLastSendTime(currentDate);
		this.appFeedbackMapper.insert(appFeedback);
	}
}