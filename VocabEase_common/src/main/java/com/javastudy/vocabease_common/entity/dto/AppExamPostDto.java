package com.javastudy.vocabease_common.entity.dto;

import com.javastudy.vocabease_common.entity.po.AppQuestion4exam;

import java.util.List;

public class AppExamPostDto {
    private Integer examId;
    private String remark;
    private List<AppQuestion4exam> appQuestion4examList;

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<AppQuestion4exam> getAppQuestion4examList() {
        return appQuestion4examList;
    }

    public void setAppQuestion4examList(List<AppQuestion4exam> appQuestion4examList) {
        this.appQuestion4examList = appQuestion4examList;
    }
}
