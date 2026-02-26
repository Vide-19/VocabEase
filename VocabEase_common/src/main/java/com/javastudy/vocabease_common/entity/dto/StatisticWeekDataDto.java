package com.javastudy.vocabease_common.entity.dto;

import java.util.List;

public class StatisticWeekDataDto {
    private List<String> dataList;
    private List<StatisticDataDto> itemDataList;

    public List<String> getDataList() {return dataList;}

    public void setDataList(List<String> dataList) {this.dataList = dataList;}

    public List<StatisticDataDto> getItemDataList() {return itemDataList;}

    public void setItemDataList(List<StatisticDataDto> itemDataList) {this.itemDataList = itemDataList;}
}
