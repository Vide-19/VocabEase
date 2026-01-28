package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.dto.ImportErrorItem;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.enums.*;
import com.javastudy.vocabease_common.entity.po.Article;
import com.javastudy.vocabease_common.entity.po.Article2category;
import com.javastudy.vocabease_common.entity.po.Category;
import com.javastudy.vocabease_common.entity.query.ArticleQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.ArticleMapper;
import com.javastudy.vocabease_common.service.Article2categoryService;
import com.javastudy.vocabease_common.service.ArticleService;
import com.javastudy.vocabease_common.service.CategoryService;
import com.javastudy.vocabease_common.utils.ExcelUtil;
import com.javastudy.vocabease_common.utils.StringTools;
import com.javastudy.vocabease_common.utils.VerifyUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 文章表 业务接口实现
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

	@Resource
	private ArticleMapper<Article, ArticleQuery> articleMapper;
	@Resource
	private CategoryService categoryService;
	@Resource
	private Article2categoryService article2categoryService;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Article> findListByParam(ArticleQuery param) {
		return this.articleMapper.selectList(param);
	}

	@Override
	public Integer findCountByParam(ArticleQuery param) {
		return this.articleMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Article> findListByPage(ArticleQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Article> list = this.findListByParam(param);
		PaginationResultVO<Article> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Article bean) {
		return this.articleMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Article> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.articleMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Article> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.articleMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Article bean, ArticleQuery param) {
		StringTools.checkParam(param);
		return this.articleMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(ArticleQuery param) {
		StringTools.checkParam(param);
		return this.articleMapper.deleteByParam(param);
	}

	/**
	 * 根据ArticleId获取对象
	 */
	@Override
	public Article getArticleByArticleId(Integer articleId) {
		return this.articleMapper.selectByArticleId(articleId);
	}

	/**
	 * 根据ArticleId修改
	 */
	@Override
	public Integer updateArticleByArticleId(Article bean, Integer articleId) {
		return this.articleMapper.updateByArticleId(bean, articleId);
	}

	@Override
	public Integer deleteArticleByArticleId(Integer articleId) {
		return this.articleMapper.deleteByArticleId(articleId);
	}

	@Override
	public void saveArticle(Article article, Boolean isAdmin) {
		//Category category = categoryService.getCategoryByCategoryId(
		//		article2categoryService.getCategoryIdByArticleId(article.getArticleId()));
		//新增
		if (article.getArticleId() == null) {
			article.setCreateTime(new Date());
			this.articleMapper.insert(article);
		}//更新
		else {
			 Article oldArticle = this.articleMapper.selectByArticleId(article.getArticleId());
			 if (!isAdmin && !oldArticle.getCreaterId().equals(article.getCreaterId()))
				 throw new BusinessException("无法修改其它用户发表的文章");
			 article.setCreaterId(null);
			 article.setCreateTime(null);
			 this.articleMapper.updateByArticleId(article, article.getCreaterId());
		}
	}
	/**
	 * 根据多个ArticleId删除文章
	 */
	@Override
	public void deleteArticleByArticleIds(String articleIds, Integer userId) {
		String[] articleIdArray = articleIds.split(",");
		if (userId != null) {
			ArticleQuery articleQuery = new ArticleQuery();
			articleQuery.setArticleIds(articleIdArray);
			List<Article> articleList = this.articleMapper.selectList(articleQuery);
			List<Article> curUserDataList = articleList.stream().filter(a ->
					!a.getCreaterId().equals(String.valueOf(userId))).collect(Collectors.toList());
			if (!curUserDataList.isEmpty())
				throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		this.articleMapper.deleteByArticleIds(articleIdArray, PostStatusEnum.NO_POST.getStatus(), userId);
		this.article2categoryService.deleteArticle2categoryByArticleIds(articleIdArray);
	}

	@Override
	public Integer getCategoryIdByArticleId(Integer articleId) {
		return 0;
	}

	public void updateArticleStatus(String articleIds, Integer status){
		ArticleQuery articleQuery = new ArticleQuery();
		articleQuery.setArticleIds(articleIds.split(","));
		Article article = new Article();
		article.setStatus(status);
		this.updateByParam(article, articleQuery);
	}

	@Override
	public List<ImportErrorItem> importArticle(SessionUserAdminDto sessionUserAdminDto, MultipartFile file) {
		List<Category> categoryList = this.categoryService.getCategoryListByType(CategoryTypeEnum.ARTICLE.getType());
		Map<String, Category> categoryMap = categoryList.stream().collect(Collectors.toMap(Category::getCategoryName, Function.identity(), (data1, data2) -> data2));
		List<List<String>> dataList = ExcelUtil.readExcel(file, Constants.EXCEL_TITLE_ARTICLE, 1);
		//错误列
		List<ImportErrorItem> errorList = new ArrayList<>();
		//数据列
		List<Article> articleList = new ArrayList<>();
		int dataRowNum = 2;
		List<String> categoryNameList = new ArrayList<>(); // ✅ 新增：保存每行对应的分类名
		for (List<String> row : dataList) {
			if (errorList.size() > Constants.LENGTH_50)
				throw new BusinessException("错误数据过多，超" + Constants.LENGTH_50 + "行，请重新按照模板上传数据");
			dataRowNum++;
			List<String> errorItemList = new ArrayList<>();
			int index = 0;
			// String[]{"标题", "正文", "翻译", "难度", "分类"}
			String title = row.get(index++);
			if (StringTools.isEmpty(title) || title.length() > Constants.LENGTH_150)
				errorItemList.add("标题不能为空，同时长度不能超过" + Constants.LENGTH_150);

			String body = row.get(index++);
			String translation = row.get(index++);
			if (StringTools.isEmpty(translation))
				errorItemList.add("翻译不能为空");

			String difficultyLevel = row.get(index++);
			int difficultyLevelInt = -1;
			if (VerifyUtil.verify(VerifyRegexEnum.POSITIVE_INTEGER, difficultyLevel)) {
				difficultyLevelInt = Integer.parseInt(difficultyLevel);
				if (difficultyLevelInt < 1 || difficultyLevelInt > 4)
					errorItemList.add("难度只能是数字1~4");
			} else
				errorItemList.add("难度只能是正整数1~4");

			String categoryName = row.get(index++);
			Category category = categoryMap.get(categoryName);
			if (category == null)
				errorItemList.add("分类名称不存在");

			if (!errorItemList.isEmpty() || !errorList.isEmpty()) {
				ImportErrorItem errorItem = new ImportErrorItem();
				errorItem.setRowNumber(dataRowNum);
				errorItem.setErrorItemList(errorItemList);
				errorList.add(errorItem);
				continue;
			}

			categoryNameList.add(categoryName);
			Article article = new Article();
			article.setTitle(title);
			article.setBody(body);
			article.setTranslation(translation);
			article.setLevel(difficultyLevelInt);
			article.setCreateTime(new Date());
			article.setStatus(PostStatusEnum.NO_POST.getStatus());
			article.setCreaterId(sessionUserAdminDto.getUserId());
			articleList.add(article);
		}
		if (!articleList.isEmpty())
			this.articleMapper.insertBatch(articleList);

		// 构建 article2category 关联数据
		List<Article2category> a2cList = new ArrayList<>();
		for (int i = 0; i < articleList.size(); i++) {
			Article article = articleList.get(i);
			String catName = categoryNameList.get(i);
			Category cat = categoryMap.get(catName); // 必然存在（已校验）

			Article2category a2c = new Article2category();
			a2c.setArticleId(article.getArticleId());      // ✅ 有值！
			a2c.setCategoryId(cat.getCategoryId());
			a2cList.add(a2c);
		}
		// 批量插入关联表
		if (!a2cList.isEmpty())
			this.article2categoryService.addBatch(a2cList);
		return errorList;
	}

	@Override
	public Article showArticleNext(ArticleQuery articleQuery, Integer currentId,
								   Integer nextType, Boolean isUpdateReadCount) {
		if (nextType == null)
			articleQuery.setArticleId(currentId);
		else {
			articleQuery.setArticleId(nextType);
			articleQuery.setCurrentId(currentId);
		}
		Article article = this.articleMapper.showArticleNext(articleQuery);
		if (article == null && nextType == null)
			throw new BusinessException("抱歉，没有更多了");
		else if (article == null && nextType == -1)
			throw new BusinessException("已经在第一页");
		else if (article == null && nextType == 1)
			throw new BusinessException("已经在最后一页");
		if (isUpdateReadCount && article != null) {
			this.articleMapper.updateCount(1, null, currentId);
			article.setReadCount(article.getReadCount() + 1);
		}
		return article;
	}


}