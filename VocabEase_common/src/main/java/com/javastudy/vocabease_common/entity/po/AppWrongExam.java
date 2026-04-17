package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 我的错题本表
 */
public class AppWrongExam implements Serializable {


    @Serial
    private static final long serialVersionUID = -3386014358707836558L;
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 关联的试卷ID (AppExam表)
     */
    private Integer examId;

    /**
     * 关联的题目ID (Question表)
     */
    private Integer questionId;
    private String title;
    private Integer questionType;

    /**
     * 答错时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date wrongTime;

    /**
     * 用户当时的答案
     */
    private String userAnswer;

    /**
     * 是否已复习 (0:未复习, 1:已复习)
     */
    private Integer isReviewed;

    private Integer wrongCount;

    public Integer getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(Integer wrongCount) {
        this.wrongCount = wrongCount;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Integer getExamId() {
        return this.examId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionId() {
        return this.questionId;
    }

    public void setWrongTime(Date wrongTime) {
        this.wrongTime = wrongTime;
    }

    public Date getWrongTime() {
        return this.wrongTime;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getUserAnswer() {
        return this.userAnswer;
    }

    public void setIsReviewed(Integer isReviewed) {
        this.isReviewed = isReviewed;
    }

    public Integer getIsReviewed() {
        return this.isReviewed;
    }

    @Override
    public String toString() {
        return "主键ID:" + (id == null ? "空" : id) + "，用户ID:" + (userId == null ? "空" : userId) + "，关联的试卷ID (AppExam表):" + (examId == null ? "空" : examId) + "，关联的题目ID (Question表):" + (questionId == null ? "空" : questionId) + "，答错时间:" + (wrongTime == null ? "空" : DateUtil.format(wrongTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "，用户当时的答案:" + (userAnswer == null ? "空" : userAnswer) + "，是否已复习 (0:未复习, 1:已复习):" + (isReviewed == null ? "空" : isReviewed);
    }
}
