package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.AppWrongWord;
import com.javastudy.vocabease_common.entity.po.Word;
import com.javastudy.vocabease_common.entity.query.AppWrongWordQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 用户错题本表 业务接口
 */
public interface AppWrongWordService {

	/**
	 * 根据条件查询列表
	 */
	List<AppWrongWord> findListByParam(AppWrongWordQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(AppWrongWordQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<AppWrongWord> findListByPage(AppWrongWordQuery param);

	/**
	 * 根据Id删除
	 */
	void deleteAppWrongWordById(Integer id);

	void addWrongWord(String userId, Integer wordId);

	Word getNextWrongWord(AppWrongWordQuery query);

	void updateWrong(String userId, Integer wordId);
}