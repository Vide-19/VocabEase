package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.CategoryTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.Article2category;
import com.javastudy.vocabease_common.entity.po.Category;
import com.javastudy.vocabease_common.entity.po.Question2category;
import com.javastudy.vocabease_common.entity.query.Article2categoryQuery;
import com.javastudy.vocabease_common.entity.query.CategoryQuery;
import com.javastudy.vocabease_common.entity.query.Question2categoryQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.Article2categoryMapper;
import com.javastudy.vocabease_common.mappers.CategoryMapper;
import com.javastudy.vocabease_common.mappers.Question2categoryMapper;
import com.javastudy.vocabease_common.service.CategoryService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 分类表 业务接口实现
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Resource
	private CategoryMapper<Category, CategoryQuery> categoryMapper;
	@Resource
	private Article2categoryMapper<Article2category, Article2categoryQuery> article2categoryMapper;
	@Resource
	private Question2categoryMapper<Question2category, Question2categoryQuery> question2categoryMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Category> findListByParam(CategoryQuery param) {
		return this.categoryMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(CategoryQuery param) {
		return this.categoryMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Category> findListByPage(CategoryQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Category> list = this.findListByParam(param);
		PaginationResultVO<Category> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Category bean) {
		return this.categoryMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Category> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.categoryMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Category> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.categoryMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Category bean, CategoryQuery param) {
		StringTools.checkParam(param);
		return this.categoryMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(CategoryQuery param) {
		StringTools.checkParam(param);
		return this.categoryMapper.deleteByParam(param);
	}

	/**
	 * 根据CategoryId获取对象
	 */
	@Override
	public Category getCategoryByCategoryId(Integer categoryId) {
		return this.categoryMapper.selectByCategoryId(categoryId);
	}

	/**
	 * 根据CategoryId修改
	 */
	@Override
	public Integer updateCategoryByCategoryId(Category bean, Integer categoryId) {
		return this.categoryMapper.updateByCategoryId(bean, categoryId);
	}

	/**
	 * 根据CategoryId删除
	 */
	@Override
	public Integer deleteCategoryByCategoryId(Integer categoryId) {
		Article2categoryQuery article2categoryQuery = new Article2categoryQuery();
		article2categoryQuery.setCategoryId(categoryId);
		Question2categoryQuery question2categoryQuery = new Question2categoryQuery();
		question2categoryQuery.setCategoryId(categoryId);
		int count = this.article2categoryMapper.selectCount(article2categoryQuery) + this.question2categoryMapper.selectCount(question2categoryQuery);
		if (count > 0)
			throw new BusinessException("该分类已被使用，暂无法删除");
		return this.categoryMapper.deleteByCategoryId(categoryId);
	}
	/**
	 * 新增Category
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveCategory(Category category) {
		// 1. 【统一校验】分类名称是否重复（同 type 下唯一）
		CategoryQuery query = new CategoryQuery();
		query.setCategoryName(category.getCategoryName());
		query.setType(category.getType());
		// 如果是更新，排除当前记录自身
		if (category.getCategoryId() != null) {
			query.setExcludeCategoryId(category.getCategoryId()); // 需 Query 支持此字段
		}
		if (this.findCountByParam(query) > 0) {
			throw new BusinessException("该分类名称已存在");
		}
		// 2. 处理新增：设置 sort
		if (category.getCategoryId() == null) {
			// 查询当前 type 下的最大 sort 值
			Integer maxSort = this.categoryMapper.selectMaxSortByType(category.getType());
			category.setSort(maxSort == null ? 1 : maxSort + 1);
			this.categoryMapper.insert(category);
		} else {
			// 更新：直接覆盖（假设 updateByCategoryId 更新所有字段）
			this.categoryMapper.updateByCategoryId(category, category.getCategoryId());
		}
	}
	/**
	 * 修改分类排序
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateSort(String categoryIds) {
		String[] categoryIdArray = categoryIds.split(",");
		Integer index = 1;
		for (String categoryIdStr : categoryIdArray) {
			Integer categoryId = Integer.parseInt(categoryIdStr);
			Category category = new Category();
			category.setSort(index);
			categoryMapper.updateByCategoryId(category, categoryId);
			index++;
		}
	}

	@Override
	public List<Category> getCategoryListByType(Integer type) {
		CategoryTypeEnum categoryTypeFromEnum = CategoryTypeEnum.getEnumByType(type);
		if (categoryTypeFromEnum == null)
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		CategoryQuery query = new CategoryQuery();
		query.setTypes(new Integer[] {categoryTypeFromEnum.getType(), CategoryTypeEnum.ARTICLE_QUESTION.getType()});
		query.setOrderBy("sort asc");


		return List.of();
	}

}
