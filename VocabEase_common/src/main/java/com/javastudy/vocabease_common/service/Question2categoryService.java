package com.javastudy.vocabease_common.service;

import java.util.List;

import com.javastudy.vocabease_common.entity.query.Question2categoryQuery;
import com.javastudy.vocabease_common.entity.po.Question2category;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;


/**
 * 问题-分类对应表 业务接口
 */
public interface Question2categoryService {

	/**
	 * 根据条件查询列表
	 */
	List<Question2category> findListByParam(Question2categoryQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(Question2categoryQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Question2category> findListByPage(Question2categoryQuery param);

	/**
	 * 新增
	 */
	Integer add(Question2category bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Question2category> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Question2category> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Question2category bean,Question2categoryQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(Question2categoryQuery param);

	/**
	 * 根据QuestionIdAndCategoryId查询对象
	 */
	Question2category getQuestion2categoryByQuestionIdAndCategoryId(Integer questionId,Integer categoryId);


	/**
	 * 根据QuestionIdAndCategoryId修改
	 */
	Integer updateQuestion2categoryByQuestionIdAndCategoryId(Question2category bean,Integer questionId,Integer categoryId);


	/**
	 * 根据QuestionIdAndCategoryId删除
	 */
	Integer deleteQuestion2categoryByQuestionIdAndCategoryId(Integer questionId,Integer categoryId);

}