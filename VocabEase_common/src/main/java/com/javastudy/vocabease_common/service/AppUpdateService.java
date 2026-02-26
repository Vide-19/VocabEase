package com.javastudy.vocabease_common.service;

import com.javastudy.vocabease_common.entity.po.AppUpdate;
import com.javastudy.vocabease_common.entity.query.AppUpdateQuery;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 小程序发布表 业务接口
 */
public interface AppUpdateService {

	/**
	 * 根据条件查询列表
	 */
	List<AppUpdate> findListByParam(AppUpdateQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(AppUpdateQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<AppUpdate> findListByPage(AppUpdateQuery param);

	/**
	 * 新增
	 */
	Integer add(AppUpdate bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<AppUpdate> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<AppUpdate> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(AppUpdate bean,AppUpdateQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(AppUpdateQuery param);

	/**
	 * 根据Id查询对象
	 */
	AppUpdate getAppUpdateById(Integer id);


	/**
	 * 根据Id修改
	 */
	Integer updateAppUpdateById(AppUpdate bean,Integer id);


	/**
	 * 根据Id删除
	 */
	void deleteAppUpdateById(Integer id);

	void saveAppUpdate(AppUpdate appUpdate, MultipartFile file);

	void postAppUpdate(Integer id, Integer status, String grayscaleDevice);

	AppUpdate getLastAppUpdate(String appVersion, String deviceId);
}