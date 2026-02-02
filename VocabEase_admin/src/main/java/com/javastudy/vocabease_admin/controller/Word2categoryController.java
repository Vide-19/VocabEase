package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_common.entity.po.Word2category;
import com.javastudy.vocabease_common.entity.query.Word2categoryQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.Word2categoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 单词-分类对应表 Controller
 */
@RestController("word2categoryController")
@RequestMapping("/word2category")
public class Word2categoryController extends ABaseController{

	@Resource
	private Word2categoryService word2categoryService;
	/**
	 * 根据条件分页查询
	 */
	@RequestMapping("/loadDataList")
	public ResponseVO loadDataList(Word2categoryQuery query){
		return getSuccessResponseVO(word2categoryService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("/add")
	public ResponseVO add(Word2category bean) {
		word2categoryService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("/addBatch")
	public ResponseVO addBatch(@RequestBody List<Word2category> listBean) {
		word2categoryService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增/修改
	 */
	@RequestMapping("/addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<Word2category> listBean) {
		word2categoryService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据WordIdAndCategoryId查询对象
	 */
	@RequestMapping("/getWord2categoryByWordIdAndCategoryId")
	public ResponseVO getWord2categoryByWordIdAndCategoryId(Integer wordId,Integer categoryId) {
		return getSuccessResponseVO(word2categoryService.getWord2categoryByWordIdAndCategoryId(wordId,categoryId));
	}

	/**
	 * 根据WordIdAndCategoryId修改对象
	 */
	@RequestMapping("/updateWord2categoryByWordIdAndCategoryId")
	public ResponseVO updateWord2categoryByWordIdAndCategoryId(Word2category bean,Integer wordId,Integer categoryId) {
		word2categoryService.updateWord2categoryByWordIdAndCategoryId(bean,wordId,categoryId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据WordIdAndCategoryId删除
	 */
	@RequestMapping("/deleteWord2categoryByWordIdAndCategoryId")
	public ResponseVO deleteWord2categoryByWordIdAndCategoryId(Integer wordId,Integer categoryId) {
		word2categoryService.deleteWord2categoryByWordIdAndCategoryId(wordId,categoryId);
		return getSuccessResponseVO(null);
	}
}