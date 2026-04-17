package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.dto.ImportErrorItem;
import com.javastudy.vocabease_common.entity.dto.SessionUserAdminDto;
import com.javastudy.vocabease_common.entity.enums.*;
import com.javastudy.vocabease_common.entity.po.Category;
import com.javastudy.vocabease_common.entity.po.Word;
import com.javastudy.vocabease_common.entity.po.Word2category;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.query.WordQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.WordMapper;
import com.javastudy.vocabease_common.service.CategoryService;
import com.javastudy.vocabease_common.service.Word2categoryService;
import com.javastudy.vocabease_common.service.WordService;
import com.javastudy.vocabease_common.utils.ExcelUtil;
import com.javastudy.vocabease_common.utils.StringTools;
import com.javastudy.vocabease_common.utils.VerifyUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 单词表 业务接口实现
 */
@Service("wordService")
public class WordServiceImpl implements WordService {

    @Resource
    private WordMapper<Word, WordQuery> wordMapper;
    @Resource
    private CategoryService categoryService;
    @Resource
    private Word2categoryService word2categoryService;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Word> findListByParam(WordQuery param) {
        return this.wordMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(WordQuery param) {
        return this.wordMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<Word> findListByPage(WordQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<Word> list = this.findListByParam(param);
        return new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Word bean) {
        return this.wordMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Word> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.wordMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Word> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.wordMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(Word bean, WordQuery param) {
        StringTools.checkParam(param);
        return this.wordMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(WordQuery param) {
        StringTools.checkParam(param);
        return this.wordMapper.deleteByParam(param);
    }

    /**
     * 根据WordId获取对象
     */
    @Override
    public Word getWordByWordId(Integer wordId) {
        return this.wordMapper.selectByWordId(wordId);
    }

    /**
     * 根据WordId修改
     */
    @Override
    public Integer updateWordByWordId(Word bean, Integer wordId) {
        return this.wordMapper.updateByWordId(bean, wordId);
    }

    /**
     * 根据WordId删除
     */
    @Override
    public Integer deleteWordByWordId(Integer wordId) {
        return this.wordMapper.deleteByWordId(wordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWord(Word word, Boolean isAdmin) {
        Category categoryDB = this.categoryService.getCategoryByCategoryId(word.getCategoryId());
        if (categoryDB == null)
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        Date currentDate = new Date();
        if (word.getWordId() == null) {
            word.setCreateTime(currentDate);
            word.setUpdateTime(currentDate);
            word.setPostType(0);
            this.wordMapper.insert(word);
        }//更新
        else {
            Word oldWord = this.wordMapper.selectByWordId(word.getWordId());
            if (!isAdmin && !oldWord.getCreatorId().equals(word.getCreatorId()))
                throw new BusinessException("无法修改当前单词");
            word.setCreatorId(null);
            word.setUpdateTime(currentDate);
            this.wordMapper.updateByWordId(word, word.getWordId());
            this.word2categoryService.deleteWord2categoryByWordIds(new String[]{word.getWordId().toString()});
        }
        //新增与修改对应分类，以为两元素都是主键，就不修改了，先删除再新增
        Word2category word2category = new Word2category();
        word2category.setWordId(word.getWordId());
        word2category.setCategoryId(word.getCategoryId());
        this.word2categoryService.add(word2category);
    }

    /**
     * 根据多个WordId删除单词
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWordByWordIds(String wordIds, Integer userId) {
        String[] wordIdArray = wordIds.split(",");
        // 1. 查询单词详情
        WordQuery wordQuery = new WordQuery();
        wordQuery.setWordIds(wordIdArray);
        List<Word> wordList = this.wordMapper.selectList(wordQuery);
        // 2. 检查是否有已发布的单词
        List<String> publishedWords = new ArrayList<>();
        for (Word word : wordList)
            if (word.getStatus() != null &&
                    word.getStatus().equals(PostStatusEnum.IS_POST.getStatus()))
                // 假设 Word 有 getWord() 方法返回单词文本
                publishedWords.add(word.getWord());
        if (!publishedWords.isEmpty())
            throw new BusinessException("以下单词已发布，禁止删除：" + String.join(", ", publishedWords));
        // 3. 权限校验
        if (userId != null) {
            List<Word> notOwnList = wordList.stream()
                    .filter(a -> !a.getCreatorId().equals(String.valueOf(userId))).toList();
            if (!notOwnList.isEmpty())
                throw new BusinessException("非管理员或作者无法删除当前单词");
        }
        // 4. 执行删除
        this.wordMapper.deleteByWordIds(wordIdArray, PostStatusEnum.NO_POST.getStatus(), userId);
        this.word2categoryService.deleteWord2categoryByWordIds(wordIdArray);
    }

    @Override
    public Integer getCategoryIdByWordId(Integer wordId) {
        return 0;
    }

    @Override
    public void updateWordStatus(String wordIds, Integer status) {
        WordQuery wordQuery = new WordQuery();
        wordQuery.setWordIds(wordIds.split(","));
        Word word = new Word();
        word.setStatus(status);
        this.updateByParam(word, wordQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ImportErrorItem> importWord(SessionUserAdminDto sessionUserAdminDto, MultipartFile file) {
        List<Category> categoryList = this.categoryService.getCategoryListByType(CategoryTypeEnum.WORD.getType());
        Map<String, Category> categoryMap = categoryList.stream().collect(Collectors.toMap(Category::getCategoryName, Function.identity(), (data1, data2) -> data2));
        List<List<String>> dataList = ExcelUtil.readExcel(file, Constants.EXCEL_TITLE_WORD, 1);
        //错误列
        List<ImportErrorItem> errorList = new ArrayList<>();
        //数据列
        List<Word> wordList = new ArrayList<>();
        int dataRowNum = 2;
        List<String> categoryNameList = new ArrayList<>(); // ✅ 新增：保存每行对应的分类名
        for (List<String> row : dataList) {
            if (errorList.size() > Constants.LENGTH_50)
                throw new BusinessException("错误数据过多，超" + Constants.LENGTH_50 + "行，请重新按照模板上传数据");
            dataRowNum++;
            List<String> errorItemList = new ArrayList<>();
            int index = 0;
            // String[]{"单词", "音标", "词性", "释义", "例句", "难度", "分类"} 👇👇👇👇
            String wordItself = row.get(index++);
            if (StringTools.isEmpty(wordItself) || wordItself.length() > Constants.LENGTH_100)
                errorItemList.add("单词不能为空，同时长度不能超过" + Constants.LENGTH_100);

            String phonetic = row.get(index++);
            String partOfSpeech = row.get(index++);

            String definition = row.get(index++);
            if (StringTools.isEmpty(definition))
                errorItemList.add("释义不能为空");

            String exampleSentence = row.get(index++);

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

            if (!errorItemList.isEmpty() || !errorList.isEmpty()) {
                ImportErrorItem errorItem = new ImportErrorItem();
                errorItem.setRowNumber(dataRowNum);
                errorItem.setErrorItemList(errorItemList);
                errorList.add(errorItem);
                continue;
            }

            categoryNameList.add(categoryName);
            Date currentDate = new Date();
            Word word = new Word();
            word.setWord(wordItself);
            word.setPhonetic(phonetic);
            word.setPartOfSpeech(partOfSpeech);
            word.setDefinition(definition);
            word.setExampleSentence(exampleSentence);
            word.setLevel(difficultyLevelInt);
            word.setStatus(PostStatusEnum.NO_POST.getStatus());
            word.setCreatorId(String.valueOf(sessionUserAdminDto.getUserId()));
            word.setCreateTime(currentDate);
            word.setUpdateTime(currentDate);
            wordList.add(word);
        }
        if (!wordList.isEmpty())
            this.wordMapper.insertBatch(wordList);

        // 构建 word2category 关联数据
        List<Word2category> w2cList = new ArrayList<>();
        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            String catName = categoryNameList.get(i);
            Category cat = categoryMap.get(catName); // 必然存在（已校验）

            Word2category w2c = new Word2category();
            w2c.setWordId(word.getWordId());      // ✅ 有值！
            w2c.setCategoryId(cat.getCategoryId());
            w2cList.add(w2c);
        }
        // 批量插入关联表
        if (!w2cList.isEmpty())
            this.word2categoryService.addBatch(w2cList);
        return errorList;
    }

    @Override
    public Word showWordNext(WordQuery wordQuery, Integer currentId, Integer nextType) {
        wordQuery.setCurrentId(currentId);
        wordQuery.setNextType(nextType);
        Word word = this.wordMapper.showWordNext(wordQuery);
        if (word == null && nextType == null)
            throw new BusinessException("抱歉，没有更多了");
        else if (word == null && nextType == -1)
            throw new BusinessException("已经在第一页");
        else if (word == null && nextType == 1)
            throw new BusinessException("已经在最后一页");
        return word;
    }

    /**
     * 获取收藏列表中的下一个/上一个单词
     */
    @Override
    public Word showNextCollectedWord(String userId, Integer currentId, Integer nextType) {
        if (userId == null || currentId == null || nextType == null)
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        WordQuery query = new WordQuery();
        query.setUserId(userId);
        query.setCurrentId(currentId);    // 当前单词ID
        query.setNextType(nextType);      // 方向
        // 确保只查有效的单词
        query.setStatus(1);
        Word word = this.wordMapper.selectNextCollectedWord(query);
        if (word == null) {
            if (nextType == 1)
                throw new BusinessException("已经是最后一个收藏了");
            else if (nextType == -1)
                throw new BusinessException("已经是第一个收藏了");
        }
        return word;
    }

    @Override
    public List<Word> getStudyList(Integer difficulty, Integer lastWordId, Integer limit) {
        return this.wordMapper.selectStudyList(difficulty, lastWordId, limit);
    }

    @Override
    public Word getFirst(WordQuery wordQuery) {
        return this.wordMapper.selectFirst(wordQuery);
    }
}