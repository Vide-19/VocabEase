package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.Word2category;
import com.javastudy.vocabease_common.entity.query.Word2categoryQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 单词-分类对应表 业务接口
 */
public interface Word2categoryService {

	/**
	 * 根据条件查询列表
	 */
	List<Word2category> findListByParam(Word2categoryQuery param);
	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(Word2categoryQuery param);
	/**
	 * 分页查询
	 */
	PaginationResultVO<Word2category> findListByPage(Word2categoryQuery param);
	/**
	 * 新增
	 */
	Integer add(Word2category bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<Word2category> listBean);
	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Word2category> listBean);
	/**
	 * 多条件更新
	 */
	Integer updateByParam(Word2category bean,Word2categoryQuery param);
	/**
	 * 多条件删除
	 */
	Integer deleteByParam(Word2categoryQuery param);
	/**
	 * 根据WordIdAndCategoryId查询对象
	 */
	Word2category getWord2categoryByWordIdAndCategoryId(Integer wordId,Integer categoryId);
	/**
	 * 根据WordIdAndCategoryId修改
	 */
/*
	void updateWord2categoryByWordId(Integer wordId, Integer categoryId);
*/
	/**
	 * 根据WordIdAndCategoryId删除
	 */
	Integer deleteWord2categoryByWordIdAndCategoryId(Integer wordId,Integer categoryId);

	Integer getCategoryIdByWordId(Integer wordId);

	void deleteWord2categoryByWordIds(String[] wordIds);
}