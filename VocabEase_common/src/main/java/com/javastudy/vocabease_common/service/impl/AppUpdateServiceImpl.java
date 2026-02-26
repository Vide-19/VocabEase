package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.config.AppConfig;
import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.enums.AppUpdateStatusEnum;
import com.javastudy.vocabease_common.entity.enums.AppUpdateTypeEnum;
import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.po.AppUpdate;
import com.javastudy.vocabease_common.entity.query.AppUpdateQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.mappers.AppUpdateMapper;
import com.javastudy.vocabease_common.service.AppUpdateService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * 小程序发布表 业务接口实现
 */
@Service("appUpdateService")
public class AppUpdateServiceImpl implements AppUpdateService {

    @Resource
    private AppUpdateMapper<AppUpdate, AppUpdateQuery> appUpdateMapper;
    @Resource
    private AppConfig appConfig;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<AppUpdate> findListByParam(AppUpdateQuery param) {
        return this.appUpdateMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AppUpdateQuery param) {
        return this.appUpdateMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<AppUpdate> findListByPage(AppUpdateQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<AppUpdate> list = this.findListByParam(param);
        PaginationResultVO<AppUpdate> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(AppUpdate bean) {
        return this.appUpdateMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<AppUpdate> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appUpdateMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<AppUpdate> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appUpdateMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(AppUpdate bean, AppUpdateQuery param) {
        StringTools.checkParam(param);
        return this.appUpdateMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(AppUpdateQuery param) {
        StringTools.checkParam(param);
        return this.appUpdateMapper.deleteByParam(param);
    }

    /**
     * 根据Id获取对象
     */
    @Override
    public AppUpdate getAppUpdateById(Integer id) {
        return this.appUpdateMapper.selectById(id);
    }

    /**
     * 根据Id修改
     */
    @Override
    public Integer updateAppUpdateById(AppUpdate bean, Integer id) {
        return this.appUpdateMapper.updateById(bean, id);
    }

    /**
     * 根据Id删除
     */
    @Override
    public void deleteAppUpdateById(Integer id) {
        this.appUpdateMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAppUpdate(AppUpdate appUpdate, MultipartFile file) {
        AppUpdateQuery query = new AppUpdateQuery();
        query.setOrderBy("id desc");
        query.setSimplePage(new SimplePage(0, 1));
        List<AppUpdate> list = this.findListByParam(query);
        if (!list.isEmpty()) {
            AppUpdate latest = list.get(0);
            long versionDB = Long.parseLong(latest.getVersion().replace(".", ""));
            long versionNew = Long.parseLong(appUpdate.getVersion().replace(".", ""));
            //新增
            if (appUpdate.getId() == null && versionNew <= versionDB)
                throw new BusinessException("版本新增错误");
            //修改
            if (appUpdate.getId() != null && versionNew <= versionDB)
                throw new BusinessException("版本修改错误");
        }
        if (appUpdate.getId() == null) {
            appUpdate.setCreateTime(new Date());
            appUpdate.setStatus(AppUpdateStatusEnum.INIT.getStatus());
            this.appUpdateMapper.insert(appUpdate);
        } else {
            appUpdate.setStatus(null);
            appUpdate.setGrayscaleDevice(null);
            this.appUpdateMapper.updateById(appUpdate, appUpdate.getId());
        }
        if (file != null) {
            File folder = new File(appConfig.getProjectFolder() + Constants.APP_UPDATE_FOLDER);
            if (!folder.exists())
                folder.mkdirs();
            AppUpdateTypeEnum updateType = AppUpdateTypeEnum.getEnumByType(appUpdate.getUpdateType());
            try {
                file.transferTo(new File(folder.getAbsolutePath() + "/" + appUpdate.getId() + updateType.getSuffix()));
            } catch (IOException e) {
                throw new BusinessException("更新失败");
            }
        }
    }

    @Override
    public void postAppUpdate(Integer id, Integer status, String grayscaleDevice) {
        AppUpdateStatusEnum statusEnum = AppUpdateStatusEnum.getEnum(status);
        if (statusEnum == null)
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        if (statusEnum == AppUpdateStatusEnum.GRAYSCALE && grayscaleDevice.isEmpty()) 
			throw new BusinessException(ResponseCodeEnum.CODE_400);
        if (statusEnum != AppUpdateStatusEnum.GRAYSCALE)
            grayscaleDevice = "";
        AppUpdate appUpdate = new AppUpdate();
        appUpdate.setStatus(status);
        appUpdate.setGrayscaleDevice(grayscaleDevice);
        this.appUpdateMapper.updateById(appUpdate, id);
    }

    @Override
    public AppUpdate getLastAppUpdate(String appVersion, String deviceId) {
        return this.appUpdateMapper.selectLastUpdate(appVersion, deviceId);
    }


}