package com.javastudy.vocabease_common.service;


import com.javastudy.vocabease_common.entity.dto.StatisticDataDto;
import com.javastudy.vocabease_common.entity.dto.StatisticWeekDataDto;

import java.util.List;

public interface StatisticDataService {
    List<StatisticDataDto> getAllData();

    StatisticWeekDataDto getWeekAllData();

    StatisticWeekDataDto getWeekContentData();
}
