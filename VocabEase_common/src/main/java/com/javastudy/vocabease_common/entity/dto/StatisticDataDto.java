package com.javastudy.vocabease_common.entity.dto;

import java.util.List;

public class StatisticDataDto {
    /**
     * 统计名
     */
    private String statisticName;
    /**
     * 统计数
     */
    private Integer count;
    /**
     *前一天统计数
     */
    private Integer preCount;
    /**
     *
     */
    private List<Integer> dataList;

    public String getStatisticName() {return statisticName;}

    public void setStatisticName(String statisticName) {this.statisticName = statisticName;}

    public Integer getCount() {return count;}

    public void setCount(Integer count) {this.count = count;}

    public Integer getPreCount() {return preCount;}

    public void setPreCount(Integer preCount) {this.preCount = preCount;}

    public List<Integer> getDataList() {return dataList;}

    public void setDataList(List<Integer> dataList) {this.dataList = dataList;}
}
