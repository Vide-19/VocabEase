package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.dto.ImportErrorItem;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.enums.*;
import com.javastudy.vocabease_common.entity.po.Category;
import com.javastudy.vocabease_common.entity.po.Item4question;
import com.javastudy.vocabease_common.entity.po.Question;
import com.javastudy.vocabease_common.entity.po.Question2category;
import com.javastudy.vocabease_common.entity.query.Item4questionQuery;
import com.javastudy.vocabease_common.entity.query.QuestionQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.Item4questionMapper;
import com.javastudy.vocabease_common.mappers.QuestionMapper;
import com.javastudy.vocabease_common.service.CategoryService;
import com.javastudy.vocabease_common.service.Question2categoryService;
import com.javastudy.vocabease_common.service.QuestionService;
import com.javastudy.vocabease_common.utils.ExcelUtil;
import com.javastudy.vocabease_common.utils.StringTools;
import com.javastudy.vocabease_common.utils.VerifyUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 问题表 业务接口实现
 */
@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

	@Resource
	private QuestionMapper<Question, QuestionQuery> questionMapper;
	@Resource
	private Item4questionMapper<Item4question, Item4questionQuery> item4questionMapper;
	@Resource
	private Question2categoryService question2categoryService;
	@Resource
	private CategoryService categoryService;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<Question> findListByParam(QuestionQuery param) {
		return this.questionMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(QuestionQuery param) {
		return this.questionMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<Question> findListByPage(QuestionQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<Question> list = this.findListByParam(param);
		PaginationResultVO<Question> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Question bean) {
		return this.questionMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Question> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.questionMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Question> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.questionMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(Question bean, QuestionQuery param) {
		StringTools.checkParam(param);
		return this.questionMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(QuestionQuery param) {
		StringTools.checkParam(param);
		return this.questionMapper.deleteByParam(param);
	}

	/**
	 * 根据QuestionId获取对象
	 */
	@Override
	public Question getQuestionByQuestionId(Integer questionId) {
		return this.questionMapper.selectByQuestionId(questionId);
	}

	@Override
	public Question getAppQuestionByQuestionId(Integer questionId) {
		// 1. 查询主表信息
		Question question = this.questionMapper.selectByQuestionId(questionId);
		if (question == null)
			return null;
		// 2. 🔥 关键修复：查询并组装选项列表 (Item4question)
		// 逻辑与 showQuestionNext 保持一致
		Item4questionQuery itemQuery = new Item4questionQuery();
		itemQuery.setQuestionId(questionId);
		itemQuery.setOrderBy("sort asc"); // 确保选项按 A/B/C/D 顺序
		List<Item4question> itemList = this.item4questionMapper.selectList(itemQuery);
		// 3. 设置到对象中 (即使为空列表也要设置，防止前端 null 报错)
		question.setItemList(itemList != null ? itemList : new ArrayList<>());
		return question;
	}

	/**
	 * 根据QuestionId修改
	 */
	@Override
	public Integer updateQuestionByQuestionId(Question bean, Integer questionId) {
		return this.questionMapper.updateByQuestionId(bean, questionId);
	}

	/**
	 * 根据QuestionId删除
	 */
	@Override
	public Integer deleteQuestionByQuestionId(Integer questionId) {
		return this.questionMapper.deleteByQuestionId(questionId);
	}
	/**
	 * 新增/修改问题
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveQuestion(Question question, List<Item4question> itemList, Boolean isSuperAdmin) {
		Integer questionId = question.getQuestionId();
		// 校验分类
		Category category = this.categoryService.getCategoryByCategoryId(question.getCategoryId());
		if (category == null)
			throw new BusinessException(ResponseCodeEnum.CODE_400);
		// ========== 1. 处理题目主表（Question）==========
		if (questionId == null) {
			// 新增逻辑
			question.setCreateTime(new Date());
			question.setStatus(PostStatusEnum.NO_POST.getStatus());
			question.setReadCount(0);
			question.setCollectCount(0);
			question.setPostType(0);
			this.questionMapper.insert(question);
			// 新增后，questionId 已被 MyBatis 回填
		} else {
			// 修改逻辑
			Question questionDB = this.questionMapper.selectByQuestionId(questionId);
			if (questionDB == null) throw new BusinessException("题目不存在");
			// 权限控制
			if (!questionDB.getCreaterId().equals(question.getCreaterId()) && !isSuperAdmin)
				throw new BusinessException("无法修改当前问题");
			// 更新主表 (注意：这里不要 set null 掉不该空的字段，确保 update 语句只更新非 null 字段或使用动态 SQL)
			// 如果 updateByQuestionId 是动态 SQL (只更新非 null 值)，则直接调用即可
			this.questionMapper.updateByQuestionId(question, questionId);
			// 删除旧关联 (先删后加，确保分类变更生效)
			this.question2categoryService.deleteQuestion2categoryByQuestionIds(new String[]{questionId.toString()});
		}
		// 重新绑定分类
		Question2category question2category = new Question2category();
		question2category.setQuestionId(question.getQuestionId());
		question2category.setCategoryId(question.getCategoryId());
		this.question2categoryService.add(question2category);
		// ========== 2. 同步处理选项（Item4question）==========
		// 如果题目不是选择题，前端传来的 itemList 应该为空，这里做防御性判断
		if (itemList == null)
			itemList = new ArrayList<>();
		// 为所有选项设置关联的题目 ID
		itemList.forEach(item -> item.setQuestionId(question.getQuestionId()));
		// 分离新增和更新
		List<Item4question> addItemList = itemList.stream()
				.filter(item -> item.getItemId() == null).toList();
		List<Item4question> updateItemList = itemList.stream()
				.filter(item -> item.getItemId() != null).toList();
		// 构建“前端希望保留/更新”的 ID 集合 (使用 Set 提高查找效率)
		Set<Integer> frontEndItemIds = updateItemList.stream()
				.map(Item4question::getItemId)
				.collect(Collectors.toSet());
		// 查询数据库中现有的所有选项
		Item4questionQuery itemQuery = new Item4questionQuery();
		itemQuery.setQuestionId(questionId);
		List<Item4question> itemListDB = this.item4questionMapper.selectList(itemQuery);
		// ========== 3. 计算需要删除的选项 (🔥 核心修复点) ==========
		List<Integer> deleteList = new ArrayList<>();
		if (itemListDB != null && !itemListDB.isEmpty())
			for (Item4question dbItem : itemListDB)
				// 如果数据库中的 ID 不在前端传来的 ID 集合中，说明需要删除
				// 无论前端传的是空列表还是部分列表，这个逻辑都成立
				if (!frontEndItemIds.contains(dbItem.getItemId()))
					deleteList.add(dbItem.getItemId());
		// ========== 4. 批量执行数据库操作 ==========
		if (!addItemList.isEmpty())
			this.item4questionMapper.insertBatch(addItemList);
		if (!updateItemList.isEmpty())
			for (Item4question item : updateItemList)
				this.item4questionMapper.updateByItemId(item, item.getItemId());
		if (!deleteList.isEmpty())
			this.item4questionMapper.deleteByItemIdBatch(deleteList);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteQuestionBatch(String questionIds, Integer userId) {
		String[] questionIdArray = questionIds.split(",");
		// 1. 查询题目详情
		QuestionQuery questionQuery = new QuestionQuery();
		questionQuery.setQuestionIds(questionIdArray);
		List<Question> questionList = this.questionMapper.selectList(questionQuery);
		// 2. 检查是否有已发布的题目
		List<String> publishedQuestions = new ArrayList<>();
		for (Question question : questionList) {
			if (question.getStatus() != null &&
					question.getStatus().equals(PostStatusEnum.IS_POST.getStatus())) {
				// 可用题干前20字作为标识，避免太长
				String snippet = question.getQuestion();
				if (snippet != null && snippet.length() > 20)
					snippet = snippet.substring(0, 20) + "...";
				publishedQuestions.add(snippet == null ? "题目" + question.getQuestionId() : snippet);
			}
		}
		if (!publishedQuestions.isEmpty())
			throw new BusinessException("以下题目已发布，禁止删除：" + String.join(", ", publishedQuestions));
		// 3. 权限校验
		if (userId != null) {
			List<Question> notOwnList = questionList.stream()
					.filter(item -> !item.getCreaterId().equals(String.valueOf(userId)))
					.toList();
			if (!notOwnList.isEmpty())
				throw new BusinessException("无法删除非本人创建的问题");
		}
		// 4. 执行删除
		this.item4questionMapper.deleteByQuestionIdBatch(questionIdArray, PostStatusEnum.NO_POST.getStatus(), userId);
		this.question2categoryService.deleteQuestion2categoryByQuestionIds(questionIdArray);
		// 删除主表（可选：其实上面 deleteByQuestionIdBatch 可能已处理，但保留原逻辑）
		QuestionQuery delQuery = new QuestionQuery();
		delQuery.setQuestionIds(questionIdArray);
		delQuery.setStatus(PostStatusEnum.NO_POST.getStatus());
		this.questionMapper.deleteByParam(delQuery);
	}

	@Override
	public void updateQuestionStatus(String questionIds, Integer status) {
		QuestionQuery questionQuery = new QuestionQuery();
		questionQuery.setQuestionIds(questionIds.split(","));
		Question question = new Question();
		question.setStatus(status);
		this.updateByParam(question, questionQuery);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<ImportErrorItem> importQuestion(SessionUserAdminDto sessionUserAdminDto, MultipartFile file) {
		List<Category> categoryList = this.categoryService.getCategoryListByType(
				CategoryTypeEnum.QUESTION.getType());
		Map<String, Category> categoryMap = categoryList.stream().collect(
				Collectors.toMap(Category::getCategoryName, Function.identity(), (data1, data2) -> data2));
		List<List<String>> dataList = ExcelUtil.readExcel(file, Constants.EXCEL_TITLE_QUESTION, 1);
		//错误列
		List<ImportErrorItem> errorList = new ArrayList<>();
		//数据列
		List<Question> questionList = new ArrayList<>();
		int dataRowNum = 2;
		List<String> categoryNameList = new ArrayList<>(); // ✅ 新增：保存每行对应的分类名
		for (List<String> row : dataList) {
			if (errorList.size() > Constants.LENGTH_50)
				throw new BusinessException("错误数据过多，超" + Constants.LENGTH_50 + "行，请重新按照模板上传数据");
			dataRowNum++;
			List<String> errorItemList = new ArrayList<>();
			int index = 0;
			// String[]{"标题", "问题描述", "问题类型", "问题选项", "答案解析", "答案", "难度", "分类"}
			String title = row.get(index++);
			if (StringTools.isEmpty(title) || title.length() > Constants.LENGTH_150)
				errorItemList.add("标题不能为空，同时长度不能超过" + Constants.LENGTH_150);

			String questionItself = row.get(index++);

			String questionType = row.get(index++);
			QuestionTypeEnum typeEnum = QuestionTypeEnum.getEnumByType(questionType);
			if (typeEnum == null)
				errorItemList.add("问题类型错误");

			String questionItem = row.get(index++);
			if (typeEnum != null && typeEnum != QuestionTypeEnum.TRUE_OR_FALSE
					&& typeEnum != QuestionTypeEnum.FILL_IN_THE_BLANK && StringTools.isEmpty(questionItem))
				errorItemList.add("问题选项不能为空");
			List<String> questionItemList = new ArrayList<>();
			if (!StringTools.isEmpty(questionItem)) {
				questionItemList = Arrays.stream(questionItem.split("\\n"))
						.map(String::trim)
						.filter(s -> !s.isEmpty())
						.map(opt -> opt.replaceFirst("^[A-Z]、", "")) // ← 关键清洗
						.toList();
			}

			String answerAnalysis = row.get(index++);
			if (StringTools.isEmpty(answerAnalysis))
				errorItemList.add("答案解析不可为空");

			String questionAnswer = row.get(index++);
			if (!StringTools.isEmpty(questionAnswer) && typeEnum != null) {
				if (typeEnum == QuestionTypeEnum.TRUE_OR_FALSE) {
					if (Constants.TRUE.equals(questionAnswer))
						questionAnswer = Constants.ONE;
					else if (Constants.FALSE.equals(questionAnswer))
						questionAnswer = Constants.ZERO;
					else
						errorItemList.add("判断题答案仅可为正确或错误");
				} else if (typeEnum == QuestionTypeEnum.SINGLE_CHOICE) {
					int singleAnswerIndex = Arrays.binarySearch(Constants.LETTER, questionAnswer);
					if (singleAnswerIndex >= 0 && singleAnswerIndex < questionItemList.size())
						questionAnswer = String.valueOf(singleAnswerIndex);
					else
						errorItemList.add("单选答案输入错误，请按模板重新填写");
				} else if (typeEnum == QuestionTypeEnum.MULTIPLE_CHOICE) {
					String cleanAnswer = questionAnswer.replace("\n", "").trim();
					if (StringTools.isEmpty(cleanAnswer))
						errorItemList.add("多选题答案不能为空");
					else {
						String[] answerArray = cleanAnswer.split("、");
						StringBuilder builder = new StringBuilder();
						boolean allValid = true;
						for (String ans : answerArray) {
							ans = ans.trim();
							int idx = Arrays.binarySearch(Constants.LETTER, ans);
							if (idx >= 0 && idx < questionItemList.size())
								builder.append(idx).append(",");
							else {
								allValid = false;
								break; // 任一无效即失败
							}
						}
						if (allValid && !builder.isEmpty())
							// 移除末尾逗号
							questionAnswer = builder.substring(0, builder.length() - 1);
						else
							errorItemList.add("多选答案输入错误，请按模板重新填写");
					}
				}
			} else
				errorItemList.add("答案输入错误，请按模板重新填写");

			String difficultyLevel = row.get(index++);
			int difficultyLevelInt = -1;
			if (VerifyUtil.verify(VerifyRegexEnum.POSITIVE_INTEGER, difficultyLevel)) {
				difficultyLevelInt = Integer.parseInt(difficultyLevel);
				if (difficultyLevelInt < 1 || difficultyLevelInt > 4)
					errorItemList.add("难度只能是数字1~4");
			} else
				errorItemList.add("难度只能是正整数1~4");

			String categoryName = row.get(index);
			Category category = categoryMap.get(categoryName);
			if (category == null)
				errorItemList.add("分类名称不存在");

			if (!errorItemList.isEmpty()) {
				ImportErrorItem errorItem = new ImportErrorItem();
				errorItem.setRowNumber(dataRowNum);
				errorItem.setErrorItemList(errorItemList);
				errorList.add(errorItem);
				continue;
			}

			categoryNameList.add(categoryName);

			Question question = new Question();
			question.setTitle(title);
			question.setQuestion(questionItself);
			question.setAnswer(questionAnswer);
			question.setAnswerAnalysis(answerAnalysis);
			question.setLevel(difficultyLevelInt);
			question.setCreateTime(new Date());
			question.setStatus(PostStatusEnum.NO_POST.getStatus());
			question.setCreaterId(sessionUserAdminDto.getUserId().toString());
			question.setReadCount(0);
			question.setCollectCount(0);
			question.setQuestionType(typeEnum.getType());

			List<Item4question> itemList = new ArrayList<>();
			int sortIndex = 0;
			for (String optionText : questionItemList) {
				Item4question item = new Item4question();
				item.setTitle(optionText);      // ← 纯文本，无 A、B、C
				item.setSort(++sortIndex);
				itemList.add(item);
			}
			question.setItemList(itemList);

			questionList.add(question);
		}
		if (!questionList.isEmpty())
			this.questionMapper.insertBatch(questionList);

		// 👇构建 question2category 关联数据
		List<Question2category> q2cList = new ArrayList<>();
		for (int i = 0; i < questionList.size(); i++) {
			Question question = questionList.get(i);
			String catName = categoryNameList.get(i);
			Category cat = categoryMap.get(catName); // 必然存在（已校验）

			Question2category q2c = new Question2category();
			q2c.setQuestionId(question.getQuestionId());      // ✅ 有值！
			q2c.setCategoryId(cat.getCategoryId());
			q2cList.add(q2c);
		}
		// 批量插入关联表
		if (!q2cList.isEmpty())
			this.question2categoryService.addBatch(q2cList);

		// 👇构建 item4question 关联数据
		List<Item4question> i4qList = new ArrayList<>();
		for (Question question : questionList) {
			List<Item4question> itemList = question.getItemList(); // 这里应已包含 title 和 sort
			for (Item4question item : itemList) {
				// 确保每个选项都关联到正确的 questionId
				item.setQuestionId(question.getQuestionId()); // MyBatis 插入后应已回填 ID
				i4qList.add(item);
			}
		}
		// 批量插入关联表
		if (!i4qList.isEmpty())
			this.item4questionMapper.insertBatch(i4qList);

		return errorList;
	}

	@Override
	public Question showQuestionNext(QuestionQuery questionQuery, Integer currentId, Integer nextType) {
		if (nextType == null)
			questionQuery.setQuestionId(currentId);
		else {
			questionQuery.setQuestionId(nextType);
			questionQuery.setCurrentId(currentId);
		}
		Question question = this.questionMapper.showQuestionNext(questionQuery);
		if (question == null && nextType == null)
			throw new BusinessException("抱歉，没有更多了");
		else if (question == null && nextType == -1)
			throw new BusinessException("已经在第一题");
		else if (question == null && nextType == 1)
			throw new BusinessException("已经在最后一题");
		Item4questionQuery itemQuery = new Item4questionQuery();
		itemQuery.setQuestionId(question.getQuestionId());
		itemQuery.setOrderBy("sort asc");
		List<Item4question> itemList = this.item4questionMapper.selectList(itemQuery);
		question.setItemList(itemList);
		return question;
	}

	/**
	 * 【新增】获取收藏列表中的下一个/上一个问题
	 */
	@Override
	public Question showNextCollectedQuestion(String userId, Integer currentId, Integer nextType) {
		if (userId == null || currentId == null || nextType == null)
			throw new BusinessException(ResponseCodeEnum.CODE_400);
		QuestionQuery query = new QuestionQuery();
		query.setUserId(userId);
		query.setCurrentId(currentId);
		query.setNextType(nextType);
		query.setStatus(1); // 只查已发布
		// 1. 查询主表信息
		Question question = this.questionMapper.selectNextCollectedQuestion(query);
		if (question == null) {
			if (nextType == 1)
				throw new BusinessException("已经是最后一个收藏了");
			else if (nextType == -1)
				throw new BusinessException("已经是第一个收藏了");
			else
				throw new BusinessException("没有更多了");
		}
		// 2. ⚠️ 必须补充查询选项列表 (Item4question)
		Item4questionQuery itemQuery = new Item4questionQuery();
		itemQuery.setQuestionId(question.getQuestionId());
		itemQuery.setOrderBy("sort asc");
		List<Item4question> itemList = this.item4questionMapper.selectList(itemQuery);
		question.setItemList(itemList);
		return question;
	}
}