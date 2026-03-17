package com.javastudy.vocabease_common.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 小程序用户表 数据库操作接口
 */
public interface AppAccountMapper<T, P> extends BaseMapper<T, P> {

    /**
     * 根据UserId更新
     */
    Integer updateByUserId(@Param("bean") T t, @Param("userId") String userId);


    /**
     * 根据UserId删除
     */
    Integer deleteByUserId(@Param("userId") String userId);


    /**
     * 根据UserId获取对象
     */
    T selectByUserId(@Param("userId") String userId);

    T selectByEmail(@Param("email") String email);

    T selectByOpenId(@Param("openId") String openId);
}
