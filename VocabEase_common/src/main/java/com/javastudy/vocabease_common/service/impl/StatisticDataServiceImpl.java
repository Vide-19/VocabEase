package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.dto.StatisticDataDto;
import com.javastudy.vocabease_common.entity.dto.StatisticWeekDataDto;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.entity.enums.StatisticDataEnum;
import com.javastudy.vocabease_common.entity.po.*;
import com.javastudy.vocabease_common.entity.query.*;
import com.javastudy.vocabease_common.mappers.*;
import com.javastudy.vocabease_common.service.StatisticDataService;
import com.javastudy.vocabease_common.utils.DateUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("statisticDataService")
public class StatisticDataServiceImpl implements StatisticDataService {
    @Resource
    private AppAccountMapper<AppAccount, AppAccountQuery> appAccountMapper;
    @Resource
    private AppDeviceMapper<AppDevice, AppDeviceQuery> appDeviceMapper;
    @Resource
    private AppFeedbackMapper<AppFeedback, AppFeedbackQuery> appFeedbackMapper;
    @Resource
    private ArticleMapper<Article, ArticleQuery> articleMapper;
    @Resource
    private QuestionMapper<Question, QuestionQuery> questionMapper;
    @Resource
    private ShareMapper<Share, ShareQuery> shareMapper;
    @Resource
    private WordMapper<Word, WordQuery> wordMapper;
    @Resource
    private AppExamMapper<AppExam, AppExamQuery> appExamMapper;

    /**
     * 统计数据
     */
    @Override
    public List<StatisticDataDto> getAllData() {
        String yesterdayStart = DateUtil.format(DateUtil.getPreDateStart(1), DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern());
        String yesterdayEnd = DateUtil.format(DateUtil.getPreDateEnd(1), DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern());
        String todayStart = DateUtil.format(DateUtil.getPreDateStart(0), DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern());
        String todayEnd = DateUtil.format(new Date(), DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern());

        List<StatisticDataDto> dataList = new ArrayList<>();
        for (StatisticDataEnum e : StatisticDataEnum.values()) {
            StatisticDataDto data = new StatisticDataDto();
            data.setStatisticName(e.getDescription());

            if (e.equals(StatisticDataEnum.APP_READ)) {
                // 总活跃设备（用account代替device更合理）
                AppAccountQuery queryTotal = new AppAccountQuery();
                long total = appAccountMapper.selectCount(queryTotal);

                // 今日登录
                AppAccountQuery queryToday = new AppAccountQuery();
                queryToday.setLastLoginTimeStart(todayStart);
                queryToday.setLastLoginTimeEnd(todayEnd);
                long today = appAccountMapper.selectCount(queryToday);

                // 昨日登录
                AppAccountQuery queryYesterday = new AppAccountQuery();
                queryYesterday.setLastLoginTimeStart(yesterdayStart);
                queryYesterday.setLastLoginTimeEnd(yesterdayEnd);
                long yesterday = appAccountMapper.selectCount(queryYesterday);

                data.setTotalCount((int) total);
                data.setTodayCount((int) today);
                data.setYesterdayCount((int) yesterday);

            } else if (e.equals(StatisticDataEnum.REGISTER_USER)) {
                AppAccountQuery queryTotal = new AppAccountQuery();
                long total = appAccountMapper.selectCount(queryTotal);

                AppAccountQuery queryToday = new AppAccountQuery();
                queryToday.setCreateTimeStart(todayStart);
                queryToday.setCreateTimeEnd(todayEnd);
                long today = appAccountMapper.selectCount(queryToday);

                AppAccountQuery queryYesterday = new AppAccountQuery();
                queryYesterday.setCreateTimeStart(yesterdayStart);
                queryYesterday.setCreateTimeEnd(yesterdayEnd);
                long yesterday = appAccountMapper.selectCount(queryYesterday);

                data.setTotalCount((int) total);
                data.setTodayCount((int) today);
                data.setYesterdayCount((int) yesterday);

            } else if (e.equals(StatisticDataEnum.ARTICLE)) {
                ArticleQuery queryTotal = new ArticleQuery();
                long total = articleMapper.selectCount(queryTotal);

                ArticleQuery queryToday = new ArticleQuery();
                queryToday.setCreateTimeStart(todayStart);
                queryToday.setCreateTimeEnd(todayEnd);
                long today = articleMapper.selectCount(queryToday);

                ArticleQuery queryYesterday = new ArticleQuery();
                queryYesterday.setCreateTimeStart(yesterdayStart);
                queryYesterday.setCreateTimeEnd(yesterdayEnd);
                long yesterday = articleMapper.selectCount(queryYesterday);

                data.setTotalCount((int) total);
                data.setTodayCount((int) today);
                data.setYesterdayCount((int) yesterday);

            } else if (e.equals(StatisticDataEnum.WORD)) {
                WordQuery queryTotal = new WordQuery();
                long total = wordMapper.selectCount(queryTotal);

                WordQuery queryToday = new WordQuery();
                queryToday.setCreateTimeStart(todayStart);
                queryToday.setCreateTimeEnd(todayEnd);
                long today = wordMapper.selectCount(queryToday);

                WordQuery queryYesterday = new WordQuery();
                queryYesterday.setCreateTimeStart(yesterdayStart);
                queryYesterday.setCreateTimeEnd(yesterdayEnd);
                long yesterday = wordMapper.selectCount(queryYesterday);

                data.setTotalCount((int) total);
                data.setTodayCount((int) today);
                data.setYesterdayCount((int) yesterday);

            } else if (e.equals(StatisticDataEnum.QUESTION)) {
                QuestionQuery queryTotal = new QuestionQuery();
                long total = questionMapper.selectCount(queryTotal);

                QuestionQuery queryToday = new QuestionQuery();
                queryToday.setCreateTimeStart(todayStart);
                queryToday.setCreateTimeEnd(todayEnd);
                long today = questionMapper.selectCount(queryToday);

                QuestionQuery queryYesterday = new QuestionQuery();
                queryYesterday.setCreateTimeStart(yesterdayStart);
                queryYesterday.setCreateTimeEnd(yesterdayEnd);
                long yesterday = questionMapper.selectCount(queryYesterday);

                data.setTotalCount((int) total);
                data.setTodayCount((int) today);
                data.setYesterdayCount((int) yesterday);

            } else if (e.equals(StatisticDataEnum.SHARE)) {
                ShareQuery queryTotal = new ShareQuery();
                long total = shareMapper.selectCount(queryTotal);

                ShareQuery queryToday = new ShareQuery();
                queryToday.setCreateTimeStart(todayStart);
                queryToday.setCreateTimeEnd(todayEnd);
                long today = shareMapper.selectCount(queryToday);

                ShareQuery queryYesterday = new ShareQuery();
                queryYesterday.setCreateTimeStart(yesterdayStart);
                queryYesterday.setCreateTimeEnd(yesterdayEnd);
                long yesterday = shareMapper.selectCount(queryYesterday);

                data.setTotalCount((int) total);
                data.setTodayCount((int) today);
                data.setYesterdayCount((int) yesterday);

            } else if (e.equals(StatisticDataEnum.FEEDBACK)) {
                AppFeedbackQuery queryTotal = new AppFeedbackQuery();
                long total = appFeedbackMapper.selectCount(queryTotal);

                AppFeedbackQuery queryToday = new AppFeedbackQuery();
                queryToday.setCreateTimeStart(todayStart);
                queryToday.setCreateTimeEnd(todayEnd);
                long today = appFeedbackMapper.selectCount(queryToday);

                AppFeedbackQuery queryYesterday = new AppFeedbackQuery();
                queryYesterday.setCreateTimeStart(yesterdayStart);
                queryYesterday.setCreateTimeEnd(yesterdayEnd);
                long yesterday = appFeedbackMapper.selectCount(queryYesterday);

                data.setTotalCount((int) total);
                data.setTodayCount((int) today);
                data.setYesterdayCount((int) yesterday);

            } else if (e.equals(StatisticDataEnum.EXAM)) {
                AppExamQuery queryTotal = new AppExamQuery();
                long total = appExamMapper.selectCount(queryTotal);

                AppExamQuery queryToday = new AppExamQuery();
                queryToday.setCreateTimeStart(todayStart);
                queryToday.setCreateTimeEnd(todayEnd);
                long today = appExamMapper.selectCount(queryToday);

                AppExamQuery queryYesterday = new AppExamQuery();
                queryYesterday.setCreateTimeStart(yesterdayStart);
                queryYesterday.setCreateTimeEnd(yesterdayEnd);
                long yesterday = appExamMapper.selectCount(queryYesterday);

                data.setTotalCount((int) total);
                data.setTodayCount((int) today);
                data.setYesterdayCount((int) yesterday);
            }

            dataList.add(data);
        }
        return dataList;
    }
    /**
     * 统计周用户数据
     */
    @Override
    public StatisticWeekDataDto getWeekAllData() {
        List<String> days = getDays();
        StatisticWeekDataDto data = new StatisticWeekDataDto();
        data.setDataList(days);
        data.setItemDataList(new ArrayList<>());

        StatisticDataDto read = new StatisticDataDto();
        read.setDataList(new ArrayList<>());
        read.setStatisticName(StatisticDataEnum.APP_READ.getDescription());

        StatisticDataDto register = new StatisticDataDto();
        register.setDataList(new ArrayList<>());
        register.setStatisticName(StatisticDataEnum.REGISTER_USER.getDescription());

        data.getItemDataList().add(read);
        data.getItemDataList().add(register);

        for (String date : days) {
            // 活跃用户：用 用户表 + 最后登录时间（修复）
            AppAccountQuery appAccountQuery1 = new AppAccountQuery();
            appAccountQuery1.setLastLoginTimeStart(date + " 00:00:00");
            appAccountQuery1.setLastLoginTimeEnd(date + " 23:59:59");
            Integer readCount = appAccountMapper.selectCount(appAccountQuery1);
            read.getDataList().add(readCount);

            // 注册用户
            AppAccountQuery appAccountQuery2 = new AppAccountQuery();
            appAccountQuery2.setCreateTimeStart(date + " 00:00:00");
            appAccountQuery2.setCreateTimeEnd(date + " 23:59:59");
            Integer registerCount = appAccountMapper.selectCount(appAccountQuery2);
            register.getDataList().add(registerCount);
        }
        return data;
    }
    /**
     * 统计内容数据
     */
    @Override
    public StatisticWeekDataDto getWeekContentData() {
        List<String> days = getDays();
        StatisticWeekDataDto data = new StatisticWeekDataDto();
        data.setDataList(days);
        data.setItemDataList(new ArrayList<>());

        StatisticDataDto share = new StatisticDataDto();
        share.setDataList(new ArrayList<>());
        share.setStatisticName(StatisticDataEnum.SHARE.getDescription());

        StatisticDataDto exam = new StatisticDataDto();
        exam.setDataList(new ArrayList<>());
        exam.setStatisticName(StatisticDataEnum.EXAM.getDescription());

        StatisticDataDto feedback = new StatisticDataDto();
        feedback.setDataList(new ArrayList<>());
        feedback.setStatisticName(StatisticDataEnum.FEEDBACK.getDescription());

        data.getItemDataList().add(share);
        data.getItemDataList().add(exam);
        data.getItemDataList().add(feedback);

        for (String date : days) {
            String start = date + " 00:00:00";
            String end = date + " 23:59:59";

            ShareQuery shareQuery = new ShareQuery();
            shareQuery.setCreateTimeStart(start);
            shareQuery.setCreateTimeEnd(end);
            share.getDataList().add(shareMapper.selectCount(shareQuery));

            AppExamQuery appExamQuery = new AppExamQuery();
            appExamQuery.setCreateTimeStart(start);
            appExamQuery.setCreateTimeEnd(end);
            exam.getDataList().add(appExamMapper.selectCount(appExamQuery));

            AppFeedbackQuery appFeedbackQuery = new AppFeedbackQuery();
            appFeedbackQuery.setCreateTimeStart(start);
            appFeedbackQuery.setCreateTimeEnd(end);
            feedback.getDataList().add(appFeedbackMapper.selectCount(appFeedbackQuery));
        }
        return data;
    }

    private List<String> getDays() {
        Date dateStart = DateUtil.getPreDate(7);
        Date dateEnd = DateUtil.getPreDate(0);
        return DateUtil.getBetweenDate(dateStart, dateEnd);
    }
}
