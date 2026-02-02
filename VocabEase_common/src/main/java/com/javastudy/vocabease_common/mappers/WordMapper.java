package com.javastudy.vocabease_common.mappers;

import com.javastudy.vocabease_common.entity.po.Word;
import com.javastudy.vocabease_common.entity.query.WordQuery;
import org.apache.ibatis.annotations.Param;

/**
 * 单词表 数据库操作接口
 */
public interface WordMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据WordId更新
	 */
	 Integer updateByWordId(@Param("bean") T t,@Param("wordId") Integer wordId);
	/**
	 * 根据WordId删除
	 */
	 Integer deleteByWordId(@Param("wordId") Integer wordId);
	/**
	 * 根据WordId获取对象
	 */
	 T selectByWordId(@Param("wordId") Integer wordId);
	/**
	 * 根据WordIds删除多个对象
	 */
	void deleteByWordIds(@Param("wordIds") String[] wordIds, @Param("status") Integer status, @Param("userId") Integer userId);

	Word showWordNext(@Param("query") WordQuery wordQuery);

}
