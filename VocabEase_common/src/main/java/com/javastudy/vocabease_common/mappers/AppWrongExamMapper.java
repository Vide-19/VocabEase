package com.javastudy.vocabease_common.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 我的错题本表 数据库操作接口
 */
public interface AppWrongExamMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据Id更新
	 */
	 Integer updateById(@Param("bean") T t,@Param("id") Integer id);


	/**
	 * 根据Id删除
	 */
	 Integer deleteById(@Param("id") String id);


	/**
	 * 根据Id获取对象
	 */
	 T selectById(@Param("id") Integer id);

	 void updateBatch(@Param("list") List<T> list);
}
