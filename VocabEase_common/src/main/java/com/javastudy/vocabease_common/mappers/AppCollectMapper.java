package com.javastudy.vocabease_common.mappers;

import com.javastudy.vocabease_common.entity.po.AppCollect;
import org.apache.ibatis.annotations.Param;

/**
 * 用户收藏表 数据库操作接口
 */
public interface AppCollectMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据CollectId更新
	 */
	 Integer updateByCollectId(@Param("bean") T t,@Param("collectId") Integer collectId);


	/**
	 * 根据CollectId删除
	 */
	 Integer deleteByCollectId(@Param("collectId") Integer collectId);


	/**
	 * 根据CollectId获取对象
	 */
	 T selectByCollectId(@Param("collectId") Integer collectId);

	 AppCollect selectByParam(@Param("query") P p);

	 T showCollectNext(@Param("query") P p);
}
