package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.dto.AppExamPostDto;
import com.javastudy.vocabease_common.entity.po.AppExam;
import com.javastudy.vocabease_common.entity.query.AppExamQuery;
import com.javastudy.vocabease_common.entity.query.AppQuestion4examQuery;
import com.javastudy.vocabease_common.entity.vo.ExamQuestionVO;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 测试表 业务接口
 */
public interface AppExamService {

	/**
	 * 根据条件查询列表
	 */
	List<AppExam> findListByParam(AppExamQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(AppExamQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<AppExam> findListByPage(AppExamQuery param);

	/**
	 * 新增
	 */
	Integer add(AppExam bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<AppExam> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<AppExam> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(AppExam bean,AppExamQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(AppExamQuery param);

	/**
	 * 根据ExamId查询对象
	 */
	AppExam getAppExamByExamId(Integer examId);


	/**
	 * 根据ExamId修改
	 */
	Integer updateAppExamByExamId(AppExam bean,Integer examId);

	/**
	 * 根据ExamId删除
	 */
	Integer deleteAppExamByExamId(Integer examId);

	AppExam addExam(String categoryIds, AppAccountDto dto);

	AppExam checkAppExam(AppAccountDto dto, Integer examId);

	List<ExamQuestionVO> getAppExamQuestion(AppQuestion4examQuery query);

	AppExam endExam(AppAccountDto accountDto, AppExamPostDto examPostDto);

	void cancelExam(AppAccountDto accountDto, Integer examId);

}