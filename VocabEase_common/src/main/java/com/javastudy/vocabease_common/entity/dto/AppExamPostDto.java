package com.javastudy.vocabease_common.entity.dto;

import java.util.List;

public class AppExamPostDto {
    private Integer examId;
    private String remark;
    private List<AppQuestionSubmitDto> appQuestion4examList;

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

    public List<AppQuestionSubmitDto> getAppQuestion4examList() {
        return appQuestion4examList;
    }

    public void setAppQuestion4examList(List<AppQuestionSubmitDto> appQuestion4examList) {
        this.appQuestion4examList = appQuestion4examList;
    }
}
