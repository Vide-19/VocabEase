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

    /**
     * 统计数据
     */
    @Override
    public List<StatisticDataDto> getAllData() {
        String preDate = DateUtil.format(DateUtil.getPreDate(1), DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern());
        List<StatisticDataDto> dataList = new ArrayList<>();
        for (StatisticDataEnum e : StatisticDataEnum.values()) {
            StatisticDataDto data = new StatisticDataDto();
            data.setStatisticName(e.getDescription());
            if (e.equals(StatisticDataEnum.APP_READ)) {
                AppDeviceQuery appDeviceQuery = new AppDeviceQuery();
                data.setCount(appDeviceMapper.selectCount(appDeviceQuery));
                appDeviceQuery.setCreateTimeStart(preDate);
                appDeviceQuery.setCreateTimeEnd(preDate);
                data.setPreCount(appDeviceMapper.selectCount(appDeviceQuery));
            } else if (e.equals(StatisticDataEnum.REGISTER_USER)) {
                AppAccountQuery appAccountQuery = new AppAccountQuery();
                data.setCount(appAccountMapper.selectCount(appAccountQuery));
                appAccountQuery.setCreateTimeStart(preDate);
                appAccountQuery.setCreateTimeEnd(preDate);
                data.setPreCount(appAccountMapper.selectCount(appAccountQuery));
            } else if (e.equals(StatisticDataEnum.ARTICLE)) {
                ArticleQuery articleQuery = new ArticleQuery();
                data.setCount(articleMapper.selectCount(articleQuery));
                articleQuery.setCreateTimeStart(preDate);
                articleQuery.setCreateTimeEnd(preDate);
                data.setPreCount(articleMapper.selectCount(articleQuery));
            } else if (e.equals(StatisticDataEnum.WORD)) {
                WordQuery wordQuery = new WordQuery();
                data.setCount(wordMapper.selectCount(wordQuery));
                wordQuery.setCreateTimeStart(preDate);
                wordQuery.setCreateTimeEnd(preDate);
                data.setPreCount(wordMapper.selectCount(wordQuery));
            } else if (e.equals(StatisticDataEnum.QUESTION)) {
                QuestionQuery questionQuery = new QuestionQuery();
                data.setCount(questionMapper.selectCount(questionQuery));
                questionQuery.setCreateTimeStart(preDate);
                questionQuery.setCreateTimeEnd(preDate);
                data.setPreCount(questionMapper.selectCount(questionQuery));
            } else if (e.equals(StatisticDataEnum.SHARE)) {
                ShareQuery shareQuery = new ShareQuery();
                data.setCount(shareMapper.selectCount(shareQuery));
                shareQuery.setCreateTimeStart(preDate);
                shareQuery.setCreateTimeEnd(preDate);
                data.setPreCount(shareMapper.selectCount(shareQuery));
            } else if (e.equals(StatisticDataEnum.FEEDBACK)) {
                AppFeedbackQuery appFeedbackQuery = new AppFeedbackQuery();
                data.setCount(appFeedbackMapper.selectCount(appFeedbackQuery));
                appFeedbackQuery.setCreateTimeStart(preDate);
                appFeedbackQuery.setCreateTimeEnd(preDate);
                data.setPreCount(appFeedbackMapper.selectCount(appFeedbackQuery));
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
            AppDeviceQuery appDeviceQuery = new AppDeviceQuery();
            appDeviceQuery.setCreateTimeStart(date);
            appDeviceQuery.setCreateTimeEnd(date);
            Integer readCount = appDeviceMapper.selectCount(appDeviceQuery);
            read.getDataList().add(readCount);

            AppAccountQuery appAccountQuery = new AppAccountQuery();
            appAccountQuery.setCreateTimeStart(date);
            appAccountQuery.setCreateTimeEnd(date);
            Integer registerCount = appAccountMapper.selectCount(appAccountQuery);
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

        StatisticDataDto article = new StatisticDataDto();
        article.setDataList(new ArrayList<>());
        article.setStatisticName(StatisticDataEnum.ARTICLE.getDescription());

        StatisticDataDto word = new StatisticDataDto();
        word.setDataList(new ArrayList<>());
        word.setStatisticName(StatisticDataEnum.WORD.getDescription());

        StatisticDataDto question = new StatisticDataDto();
        question.setDataList(new ArrayList<>());
        question.setStatisticName(StatisticDataEnum.QUESTION.getDescription());

        StatisticDataDto feedback = new StatisticDataDto();
        feedback.setDataList(new ArrayList<>());
        feedback.setStatisticName(StatisticDataEnum.FEEDBACK.getDescription());

        data.getItemDataList().add(article);
        data.getItemDataList().add(word);
        data.getItemDataList().add(question);
        data.getItemDataList().add(feedback);

        for (String date : days) {
            ArticleQuery articleQuery = new ArticleQuery();
            articleQuery.setCreateTimeStart(date);
            articleQuery.setCreateTimeEnd(date);
            article.getDataList().add(articleMapper.selectCount(articleQuery));

            WordQuery wordQuery = new WordQuery();
            wordQuery.setCreateTimeStart(date);
            wordQuery.setCreateTimeEnd(date);
            word.getDataList().add(wordMapper.selectCount(wordQuery));

            QuestionQuery questionQuery = new QuestionQuery();
            questionQuery.setCreateTimeStart(date);
            questionQuery.setCreateTimeEnd(date);
            question.getDataList().add(questionMapper.selectCount(questionQuery));

            AppFeedbackQuery appFeedbackQuery = new AppFeedbackQuery();
            appFeedbackQuery.setpFeedbackId(0);
            appFeedbackQuery.setCreateTimeStart(date);
            appFeedbackQuery.setCreateTimeEnd(date);
            feedback.getDataList().add(appFeedbackMapper.selectCount(appFeedbackQuery));
        }
        return data;
    }

    private List<String> getDays() {
        Date dateStart = DateUtil.getPreDate(7);
        Date dateEnd = DateUtil.getPreDate(1);
        return DateUtil.getBetweenDate(dateStart, dateEnd);
    }
}
