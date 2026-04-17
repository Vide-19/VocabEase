package com.javastudy.vocabease_common.entity.dto;

public class AppQuestionSubmitDto {

    private Integer id;

    private Integer questionId;

    // 【重点修改】：
    // 定义为 Object，可以兼容  (数组) 和 "you" (字符串)
    private Object answer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }
}
