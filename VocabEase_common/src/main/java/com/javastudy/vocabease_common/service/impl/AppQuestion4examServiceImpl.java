package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.AppQuestion4exam;
import com.javastudy.vocabease_common.entity.query.AppQuestion4examQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.mappers.AppQuestion4examMapper;
import com.javastudy.vocabease_common.service.AppQuestion4examService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 测试问题表 业务接口实现
 */
@Service("appQuestion4examService")
public class AppQuestion4examServiceImpl implements AppQuestion4examService {

	@Resource
	private AppQuestion4examMapper<AppQuestion4exam, AppQuestion4examQuery> appQuestion4examMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<AppQuestion4exam> findListByParam(AppQuestion4examQuery param) {
		return this.appQuestion4examMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(AppQuestion4examQuery param) {
		return this.appQuestion4examMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<AppQuestion4exam> findListByPage(AppQuestion4examQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<AppQuestion4exam> list = this.findListByParam(param);
		PaginationResultVO<AppQuestion4exam> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(AppQuestion4exam bean) {
		return this.appQuestion4examMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<AppQuestion4exam> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.appQuestion4examMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<AppQuestion4exam> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.appQuestion4examMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(AppQuestion4exam bean, AppQuestion4examQuery param) {
		StringTools.checkParam(param);
		return this.appQuestion4examMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(AppQuestion4examQuery param) {
		StringTools.checkParam(param);
		return this.appQuestion4examMapper.deleteByParam(param);
	}

	/**
	 * 根据Id获取对象
	 */
	@Override
	public AppQuestion4exam getAppQuestion4examById(Integer id) {
		return this.appQuestion4examMapper.selectById(id);
	}

	/**
	 * 根据Id修改
	 */
	@Override
	public Integer updateAppQuestion4examById(AppQuestion4exam bean, Integer id) {
		return this.appQuestion4examMapper.updateById(bean, id);
	}

	/**
	 * 根据Id删除
	 */
	@Override
	public Integer deleteAppQuestion4examById(Integer id) {
		return this.appQuestion4examMapper.deleteById(id);
	}
}