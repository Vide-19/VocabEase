package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.AppWrongWord;
import com.javastudy.vocabease_common.entity.po.Word;
import com.javastudy.vocabease_common.entity.query.AppWrongWordQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.mappers.AppWrongWordMapper;
import com.javastudy.vocabease_common.service.AppWrongWordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 用户错题本表 业务接口实现
 */
@Service("appWrongWordService")
public class AppWrongWordServiceImpl implements AppWrongWordService {

    @Resource
    private AppWrongWordMapper<AppWrongWord, AppWrongWordQuery> appWrongWordMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<AppWrongWord> findListByParam(AppWrongWordQuery param) {
        return this.appWrongWordMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AppWrongWordQuery param) {
        return this.appWrongWordMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<AppWrongWord> findListByPage(AppWrongWordQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<AppWrongWord> list = this.findListByParam(param);
        PaginationResultVO<AppWrongWord> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 根据Id删除
     */
    @Override
    public void deleteAppWrongWordById(Integer id) {
        this.appWrongWordMapper.deleteById(id);
    }

    /**
     * 新增错题
     */
    @Override
    public void addWrongWord(String userId, Integer wordId) {
        // 1. 检查是否已存在该错题
        AppWrongWord exist = this.appWrongWordMapper.selectByUserIdAndWordId(userId, wordId);
        Date currentDate = new Date();
        if (exist != null) {
            // 2. 如果存在，更新错误次数和时间
            exist.setWrongCount(exist.getWrongCount() + 1);
            exist.setLastWrongTime(currentDate);
            this.appWrongWordMapper.updateById(exist, exist.getId());
        } else {
            // 3. 如果不存在，插入新记录
            AppWrongWord wrongWord = new AppWrongWord();
            wrongWord.setUserId(userId);
            wrongWord.setWordId(wordId);
            wrongWord.setWrongCount(1);
            wrongWord.setLastWrongTime(currentDate);
            wrongWord.setLastReviewTime(currentDate);
            wrongWord.setNextReviewTime(currentDate);
            this.appWrongWordMapper.insert(wrongWord);
        }
    }

    @Override
    public Word getNextWrongWord(AppWrongWordQuery query) {
        // 1. 获取单词
        Word word = this.appWrongWordMapper.selectNextWrongWord(query);
        if (word != null) {
            // 2. 艾宾浩斯逻辑：更新错题表的复习时间
            // 注意：这里只是简单的更新 last_review_time，
            // 复杂的艾宾浩斯算法通常需要根据错误次数计算 next_review_time
            // 比如：错误次数 < 3, 间隔1天; 错误次数 >= 3, 间隔3天
            AppWrongWord wrongWord = this.appWrongWordMapper.selectByUserIdAndWordId(query.getUserId(), word.getWordId());
            if (wrongWord != null) {
                // 更新上次复习时间
                wrongWord.setLastReviewTime(new Date());
                // 简单的间隔算法示例
                int intervalDays = 1;
                if (wrongWord.getWrongCount() >= 3)
                    intervalDays = 3;
                 else if (wrongWord.getWrongCount() >= 5)
                    intervalDays = 7;
                // 计算下次复习时间
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH, intervalDays);
                wrongWord.setNextReviewTime(calendar.getTime());
                this.appWrongWordMapper.updateById(wrongWord, wrongWord.getId());
            }
        }
        return word;
    }

    @Override
    public void updateWrong(String userId, Integer wordId) {
        // 1. 查询当前错题记录
        AppWrongWord record = this.appWrongWordMapper.selectByUserIdAndWordId(userId, wordId);
        if (record == null)
            // 如果记录不存在，无需处理（可能已经被移出错题本）
            return;
        // 2. wrong_count 减 1，但不能小于 0
        int newWrongCount = Math.max(0, record.getWrongCount() - 1);
        if (newWrongCount == 0) {
            this.appWrongWordMapper.deleteById(record.getId());
            return;
        }
        record.setWrongCount(newWrongCount);
        // 3. 更新最后复习时间
        Date now = new Date();
        record.setLastReviewTime(now);
        // 4. 根据新的 wrong_count 重新计算下次复习时间 (艾宾浩斯逻辑)
        Date nextReviewTime = calculateNextReviewTime(newWrongCount, now);
        record.setNextReviewTime(nextReviewTime);
        this.appWrongWordMapper.updateById(record, record.getId());
    }

    /**
     * 根据错误次数计算下次复习间隔
     * 策略：错得越少，间隔越长
     */
    private Date calculateNextReviewTime(int wrongCount, Date baseTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(baseTime);
        // 简单的阶梯策略
        if (wrongCount == 0)
            // 已经掌握得很好了，下次复习间隔 7 天
            calendar.add(Calendar.DAY_OF_MONTH, 7);
        else if (wrongCount == 1)
            // 还有点生疏，间隔 3 天
            calendar.add(Calendar.DAY_OF_MONTH, 3);
        else if (wrongCount == 2)
            // 一般生疏，间隔 1 天
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        else
            // 依然很生疏 (>=3)，间隔 12 小时
            calendar.add(Calendar.HOUR_OF_DAY, 12);
        return calendar.getTime();
    }
}