package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_common.entity.po.Question2category;
import com.javastudy.vocabease_common.entity.query.Question2categoryQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.Question2categoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 问题-分类对应表 Controller
 */
@RestController("question2categoryController")
@RequestMapping("/question2category")
public class Question2categoryController extends ABaseController{

	@Resource
	private Question2categoryService question2categoryService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(Question2categoryQuery query){
		return getSuccessResponseVO(question2categoryService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(Question2category bean) {
		question2categoryService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<Question2category> listBean) {
		question2categoryService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<Question2category> listBean) {
		question2categoryService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据QuestionIdAndCategoryId查询对象
	 */
	@RequestMapping("/getQuestion2categoryByQuestionIdAndCategoryId")
	public ResponseVO getQuestion2categoryByQuestionIdAndCategoryId(Integer questionId,Integer categoryId) {
		return getSuccessResponseVO(question2categoryService.getQuestion2categoryByQuestionIdAndCategoryId(questionId,categoryId));
	}

	/**
	 * 根据QuestionIdAndCategoryId修改对象
	 */
	@RequestMapping("/updateQuestion2categoryByQuestionIdAndCategoryId")
	public ResponseVO updateQuestion2categoryByQuestionIdAndCategoryId(Question2category bean,Integer questionId,Integer categoryId) {
		question2categoryService.updateQuestion2categoryByQuestionIdAndCategoryId(bean,questionId,categoryId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据QuestionIdAndCategoryId删除
	 */
	@RequestMapping("/deleteQuestion2categoryByQuestionIdAndCategoryId")
	public ResponseVO deleteQuestion2categoryByQuestionIdAndCategoryId(Integer questionId,Integer categoryId) {
		question2categoryService.deleteQuestion2categoryByQuestionIdAndCategoryId(questionId,categoryId);
		return getSuccessResponseVO(null);
	}
}