package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.Question2category;
import com.javastudy.vocabease_common.entity.query.Question2categoryQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.mappers.Question2categoryMapper;
import com.javastudy.vocabease_common.service.Question2categoryService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 问题-分类对应表 业务接口实现
 */
@Service("question2categoryService")
public class Question2categoryServiceImpl implements Question2categoryService {

	@Resource
	private Question2categoryMapper<Question2category, Question2categoryQuery> question2categoryMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Question2category> findListByParam(Question2categoryQuery param) {
		return this.question2categoryMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(Question2categoryQuery param) {
		return this.question2categoryMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Question2category> findListByPage(Question2categoryQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Question2category> list = this.findListByParam(param);
		PaginationResultVO<Question2category> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Question2category bean) {
		return this.question2categoryMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Question2category> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.question2categoryMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Question2category> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.question2categoryMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Question2category bean, Question2categoryQuery param) {
		StringTools.checkParam(param);
		return this.question2categoryMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(Question2categoryQuery param) {
		StringTools.checkParam(param);
		return this.question2categoryMapper.deleteByParam(param);
	}

	/**
	 * 根据QuestionIdAndCategoryId获取对象
	 */
	@Override
	public Question2category getQuestion2categoryByQuestionIdAndCategoryId(Integer questionId, Integer categoryId) {
		return this.question2categoryMapper.selectByQuestionIdAndCategoryId(questionId, categoryId);
	}

	/**
	 * 根据QuestionIdAndCategoryId修改
	 */
	@Override
	public Integer updateQuestion2categoryByQuestionIdAndCategoryId(Question2category bean, Integer questionId, Integer categoryId) {
		return this.question2categoryMapper.updateByQuestionIdAndCategoryId(bean, questionId, categoryId);
	}

	/**
	 * 根据QuestionIdAndCategoryId删除
	 */
	@Override
	public Integer deleteQuestion2categoryByQuestionIdAndCategoryId(Integer questionId, Integer categoryId) {
		return this.question2categoryMapper.deleteByQuestionIdAndCategoryId(questionId, categoryId);
	}
}