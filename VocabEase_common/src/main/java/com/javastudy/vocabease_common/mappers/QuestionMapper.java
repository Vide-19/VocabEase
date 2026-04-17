package com.javastudy.vocabease_common.mappers;

import com.javastudy.vocabease_common.entity.po.Question;
import com.javastudy.vocabease_common.entity.query.QuestionQuery;
import org.apache.ibatis.annotations.Param;

/**
 * 问题表 数据库操作接口
 */
public interface QuestionMapper<T, P> extends BaseMapper<T, P> {

    /**
     * 根据QuestionId更新
     */
    Integer updateByQuestionId(@Param("bean") T t, @Param("questionId") Integer questionId);

    /**
     * 根据QuestionId删除
     */
    Integer deleteByQuestionId(@Param("questionId") Integer questionId);

    /**
     * 根据QuestionId获取对象
     */
    Question selectByQuestionId(@Param("questionId") Integer questionId);

    Question showQuestionNext(@Param("query") QuestionQuery questionQuery);

    Question selectNextCollectedQuestion(@Param("query") QuestionQuery questionQuery);


}
