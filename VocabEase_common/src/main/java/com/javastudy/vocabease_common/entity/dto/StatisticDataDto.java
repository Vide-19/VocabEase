package com.javastudy.vocabease_common.entity.dto;

import java.util.List;

public class StatisticDataDto {
    /**
     * 统计名
     */
    private String statisticName;
    /**
     * 总统计数
     */
    private Integer totalCount;
    /**
     * 今天统计数
     */
    private Integer todayCount;
    /**
     * 前一天统计数
     */
    private Integer yesterdayCount;
    /**
     *
     */
    private List<Integer> dataList;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(Integer todayCount) {
        this.todayCount = todayCount;
    }

    public String getStatisticName() {
        return statisticName;
    }

    public void setStatisticName(String statisticName) {
        this.statisticName = statisticName;
    }

    public Integer getYesterdayCount() {
        return yesterdayCount;
    }

    public void setYesterdayCount(Integer yesterdayCount) {
        this.yesterdayCount = yesterdayCount;
    }

    public List<Integer> getDataList() {
        return dataList;
    }

    public void setDataList(List<Integer> dataList) {
        this.dataList = dataList;
    }
}
