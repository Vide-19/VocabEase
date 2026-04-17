package com.javastudy.vocabease_common.mappers;

import com.javastudy.vocabease_common.entity.po.Word;
import com.javastudy.vocabease_common.entity.query.AppWrongWordQuery;
import org.apache.ibatis.annotations.Param;

/**
 * 用户错题本表 数据库操作接口
 */
public interface AppWrongWordMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据Id更新
	 */
	 Integer updateById(@Param("bean") T t,@Param("id") Integer id);

	/**
	 * 根据Id删除
	 */
	 void deleteById(@Param("id") Integer id);

	/**
	 * 根据UserIdAndWordId获取对象
	 */
	 T selectByUserIdAndWordId(@Param("userId") String userId,@Param("wordId") Integer wordId);

	/**
	 * 获取下一个复习单词
	 */
	Word selectNextWrongWord(@Param("query") AppWrongWordQuery query);
}
