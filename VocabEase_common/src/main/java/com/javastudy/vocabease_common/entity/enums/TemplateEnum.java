package com.javastudy.vocabease_common.entity.enums;

public enum TemplateEnum {
    ARTICLE(0, "/template/template_article.xlsx", "文章上传模板.xlsx"),
    QUESTION(1, "/template/template_question.xlsx", "试题上传模板.xlsx");

    private Integer type;
    private String path;
    private String name;

    TemplateEnum(Integer type, String path, String name) {
        this.type = type;
        this.path = path;
        this.name = name;
    }

    public Integer getType() {return type;}
    public String getPath() {return path;}
    public String getName() {return name;}

    public static TemplateEnum getEnumByType(Integer type) {
        for (TemplateEnum templateEnum : TemplateEnum.values())
            if (templateEnum.getType().equals(type))
                return templateEnum;
        return null;
    }
}
