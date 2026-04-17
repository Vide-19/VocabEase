package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.AppWrongExam;
import com.javastudy.vocabease_common.entity.query.AppWrongExamQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 我的错题本表 业务接口
 */
public interface AppWrongExamService {

	/**
	 * 根据条件查询列表
	 */
	List<AppWrongExam> findListByParam(AppWrongExamQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(AppWrongExamQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<AppWrongExam> findListByPage(AppWrongExamQuery param);

	/**
	 * 新增
	 */
	Integer add(AppWrongExam bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<AppWrongExam> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<AppWrongExam> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(AppWrongExam bean,AppWrongExamQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(AppWrongExamQuery param);

	/**
	 * 根据Id查询对象
	 */
	AppWrongExam getAppWrongExamById(Integer id);


	/**
	 * 根据Id修改
	 */
	Integer updateAppWrongExamById(AppWrongExam bean,Integer id);


	/**
	 * 根据Id删除
	 */
	Integer deleteAppWrongExamById(String id);

}