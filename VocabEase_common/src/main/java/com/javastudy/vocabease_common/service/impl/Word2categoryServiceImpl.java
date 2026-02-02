package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.Word2category;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.query.Word2categoryQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.mappers.Word2categoryMapper;
import com.javastudy.vocabease_common.service.Word2categoryService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 单词-分类对应表 业务接口实现
 */
@Service("word2categoryService")
public class Word2categoryServiceImpl implements Word2categoryService {

	@Resource
	private Word2categoryMapper<Word2category, Word2categoryQuery> word2categoryMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Word2category> findListByParam(Word2categoryQuery param) {
		return this.word2categoryMapper.selectList(param);
	}
	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(Word2categoryQuery param) {
		return this.word2categoryMapper.selectCount(param);
	}
	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Word2category> findListByPage(Word2categoryQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Word2category> list = this.findListByParam(param);
		PaginationResultVO<Word2category> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}
	/**
	 * 新增
	 */
	@Override
	public Integer add(Word2category bean) {
		return this.word2categoryMapper.insert(bean);
	}
	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Word2category> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.word2categoryMapper.insertBatch(listBean);
	}
	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Word2category> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.word2categoryMapper.insertOrUpdateBatch(listBean);
	}
	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Word2category bean, Word2categoryQuery param) {
		StringTools.checkParam(param);
		return this.word2categoryMapper.updateByParam(bean, param);
	}
	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(Word2categoryQuery param) {
		StringTools.checkParam(param);
		return this.word2categoryMapper.deleteByParam(param);
	}
	/**
	 * 根据WordIdAndCategoryId获取对象
	 */
	@Override
	public Word2category getWord2categoryByWordIdAndCategoryId(Integer wordId, Integer categoryId) {
		return this.word2categoryMapper.selectByWordIdAndCategoryId(wordId, categoryId);
	}
	/**
	 * 根据WordIdAndCategoryId修改
	 */
	@Override
	public Integer updateWord2categoryByWordIdAndCategoryId(Word2category bean, Integer wordId, Integer categoryId) {
		return this.word2categoryMapper.updateByWordIdAndCategoryId(bean, wordId, categoryId);
	}
	/**
	 * 根据WordIdAndCategoryId删除
	 */
	@Override
	public Integer deleteWord2categoryByWordIdAndCategoryId(Integer wordId, Integer categoryId) {
		return this.word2categoryMapper.deleteByWordIdAndCategoryId(wordId, categoryId);
	}
	/**
	 * 通过单词id获取分类id
	 */
	@Override
	public Integer getCategoryIdByWordId(Integer wordId) {
		return this.word2categoryMapper.selectCategoryIdByWordId(wordId);
	}
	/**
	 * 根据wordIds删除
	 */
	@Override
	public void deleteWord2categoryByWordIds(String[] wordIds) {
		this.word2categoryMapper.deleteByWordIds(wordIds);
	}
}