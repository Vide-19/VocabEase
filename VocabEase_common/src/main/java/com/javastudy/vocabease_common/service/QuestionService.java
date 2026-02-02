package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.dto.ImportErrorItem;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.po.Item4question;
import com.javastudy.vocabease_common.entity.po.Question;
import com.javastudy.vocabease_common.entity.query.QuestionQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 问题表 业务接口
 */
public interface QuestionService {

	/**
	 * 根据条件查询列表
	 */
	List<Question> findListByParam(QuestionQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(QuestionQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Question> findListByPage(QuestionQuery param);

	/**
	 * 新增
	 */
	Integer add(Question bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Question> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Question> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(Question bean,QuestionQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(QuestionQuery param);

	/**
	 * 根据QuestionId查询对象
	 */
	Question getQuestionByQuestionId(Integer questionId);


	/**
	 * 根据QuestionId修改
	 */
	Integer updateQuestionByQuestionId(Question bean,Integer questionId);


	/**
	 * 根据QuestionId删除
	 */
	Integer deleteQuestionByQuestionId(Integer questionId);

	void saveQuestion(Question question, List<Item4question> item4questions, Boolean isSuperAdmin);

	void deleteQuestionBatch(String questionIds, Integer userId);

	void updateQuestionStatus(String questionIds, Integer status);

	List<ImportErrorItem> importQuestion(SessionUserAdminDto sessionUserAdminDto, MultipartFile file);

	Question showQuestionNext(QuestionQuery questionQuery, Integer currentId, Integer nextType);


}