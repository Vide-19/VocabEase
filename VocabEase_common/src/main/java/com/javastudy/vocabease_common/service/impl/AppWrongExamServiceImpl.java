package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppWrongExam;
import com.javastudy.vocabease_common.entity.query.AppWrongExamQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.AppWrongExamMapper;
import com.javastudy.vocabease_common.service.AppWrongExamService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 我的错题本表 业务接口实现
 */
@Service("appWrongExamService")
public class AppWrongExamServiceImpl implements AppWrongExamService {

	@Resource
	private AppWrongExamMapper<AppWrongExam, AppWrongExamQuery> appWrongExamMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<AppWrongExam> findListByParam(AppWrongExamQuery param) {
		return this.appWrongExamMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(AppWrongExamQuery param) {
		return this.appWrongExamMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<AppWrongExam> findListByPage(AppWrongExamQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<AppWrongExam> list = this.findListByParam(param);
		PaginationResultVO<AppWrongExam> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(AppWrongExam bean) {
		return this.appWrongExamMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<AppWrongExam> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.appWrongExamMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<AppWrongExam> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.appWrongExamMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(AppWrongExam bean, AppWrongExamQuery param) {
		StringTools.checkParam(param);
		return this.appWrongExamMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(AppWrongExamQuery param) {
		StringTools.checkParam(param);
		return this.appWrongExamMapper.deleteByParam(param);
	}

	/**
	 * 根据Id获取对象
	 */
	@Override
	public AppWrongExam getAppWrongExamById(Integer id) {
		return this.appWrongExamMapper.selectById(id);
	}

	/**
	 * 根据Id修改
	 */
	@Override
	public Integer updateAppWrongExamById(AppWrongExam bean, Integer id) {
		return this.appWrongExamMapper.updateById(bean, id);
	}

	/**
	 * 根据Id删除
	 */
	@Override
	public Integer deleteAppWrongExamById(String id) {
		if (id == null)
			throw new BusinessException(ResponseCodeEnum.CODE_400);
		return this.appWrongExamMapper.deleteById(id);
	}
}