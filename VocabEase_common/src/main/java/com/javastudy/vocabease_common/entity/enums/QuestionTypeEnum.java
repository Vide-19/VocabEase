package com.javastudy.vocabease_common.entity.enums;

public enum QuestionTypeEnum {
    TRUE_OR_FALSE(0, "判断题"),
    SINGLE_CHOICE(1, "单选题"),
    MULTIPLE_CHOICE(2, "多选题"),
    FILL_IN_THE_BLANK(3, "填空题");

    private Integer type;
    private String description;

    QuestionTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {return type;}
    public String getDescription() {return description;}

    public static QuestionTypeEnum getTypeByDescription(String description) {
        for (QuestionTypeEnum type : QuestionTypeEnum.values())
            if (description.equals(type.getDescription()))
                return type;
        return null;
    }
}
