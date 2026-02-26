package com.javastudy.vocabease_common.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * 小程序设备表 数据库操作接口
 */
public interface AppDeviceMapper<T,P> extends BaseMapper<T,P> {

	/**
	 * 根据DeviceId更新
	 */
	 Integer updateByDeviceId(@Param("bean") T t,@Param("deviceId") String deviceId);


	/**
	 * 根据DeviceId删除
	 */
	 Integer deleteByDeviceId(@Param("deviceId") String deviceId);


	/**
	 * 根据DeviceId获取对象
	 */
	 T selectByDeviceId(@Param("deviceId") String deviceId);


}
