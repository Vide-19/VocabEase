package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.dto.ImportErrorItem;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.po.Word;
import com.javastudy.vocabease_common.entity.query.WordQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 单词表 业务接口
 */
public interface WordService {

	/**
	 * 根据条件查询列表
	 */
	List<Word> findListByParam(WordQuery param);
	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(WordQuery param);
	/**
	 * 分页查询
	 */
	PaginationResultVO<Word> findListByPage(WordQuery param);
	/**
	 * 新增
	 */
	Integer add(Word bean);
	/**
	 * 批量新增
	 */
	Integer addBatch(List<Word> listBean);
	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<Word> listBean);
	/**
	 * 多条件更新
	 */
	Integer updateByParam(Word bean,WordQuery param);
	/**
	 * 多条件删除
	 */
	Integer deleteByParam(WordQuery param);
	/**
	 * 根据WordId查询对象
	 */
	Word getWordByWordId(Integer wordId);
	/**
	 * 根据WordId修改
	 */
	Integer updateWordByWordId(Word bean,Integer wordId);
	/**
	 * 根据WordId删除
	 */
	Integer deleteWordByWordId(Integer wordId);
	/**
	 * 保存单词
	 */
	void saveWord(Word word, Boolean isAdmin);
	/**
	 * 删除多个单词
	 */
	void deleteWordByWordIds(String wordIds, Integer userId);

	Integer getCategoryIdByWordId(Integer wordId);

	void updateWordStatus(String wordIds, Integer status);

	List<ImportErrorItem> importWord(SessionUserAdminDto sessionUserAdminDto, MultipartFile file);

	Word showWordNext(WordQuery wordQuery, Integer currentId, Integer nextType);

	Word showNextCollectedWord(String userId, Integer currentId, Integer nextType);

	List<Word> getStudyList(Integer difficulty, Integer lastWordId, Integer limit);

	Word getFirst(WordQuery wordQuery);
}