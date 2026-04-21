package com.javastudy.vocabease_common.service.impl;

import com.javastudy.vocabease_common.entity.enums.PageSize;
import com.javastudy.vocabease_common.entity.po.AppCarousel;
import com.javastudy.vocabease_common.entity.query.AppCarouselQuery;
import com.javastudy.vocabease_common.entity.query.SimplePage;
import com.javastudy.vocabease_common.entity.vo.PaginationResultVO;
import com.javastudy.vocabease_common.mappers.AppCarouselMapper;
import com.javastudy.vocabease_common.service.AppCarouselService;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * 小程序轮播图表 业务接口实现
 */
@Service("appCarouselService")
public class AppCarouselServiceImpl implements AppCarouselService {

    @Resource
    private AppCarouselMapper<AppCarousel, AppCarouselQuery> appCarouselMapper;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<AppCarousel> findListByParam(AppCarouselQuery param) {
        return this.appCarouselMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(AppCarouselQuery param) {
        return this.appCarouselMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<AppCarousel> findListByPage(AppCarouselQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<AppCarousel> list = this.findListByParam(param);
        PaginationResultVO<AppCarousel> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(AppCarousel bean) {
        return this.appCarouselMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<AppCarousel> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appCarouselMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<AppCarousel> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.appCarouselMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(AppCarousel bean, AppCarouselQuery param) {
        StringTools.checkParam(param);
        return this.appCarouselMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(AppCarouselQuery param) {
        StringTools.checkParam(param);
        return this.appCarouselMapper.deleteByParam(param);
    }

    /**
     * 根据CarouselId获取对象
     */
    @Override
    public AppCarousel getAppCarouselByCarouselId(Integer carouselId) {
        return this.appCarouselMapper.selectByCarouselId(carouselId);
    }

    /**
     * 根据CarouselId修改
     */
    @Override
    public Integer updateAppCarouselByCarouselId(AppCarousel bean, Integer carouselId) {
        return this.appCarouselMapper.updateByCarouselId(bean, carouselId);
    }

    /**
     * 新增/修改轮播图
     */
    @Override
    public void saveAppCarousel(@RequestBody AppCarousel appCarousel) {
        //新增
        if (appCarousel.getCarouselId() == null) {
            AppCarouselQuery query = new AppCarouselQuery();
            // 查询当前轮播图表下的最大 sort 值
            Integer maxSort = this.appCarouselMapper.selectMaxSortByType();   //👇改取最大，非计数
            appCarousel.setSort(maxSort == null ? 1 : maxSort + 1);
            this.appCarouselMapper.insert(appCarousel);
        }
        //修改
        else
            this.appCarouselMapper.updateByCarouselId(appCarousel, appCarousel.getCarouselId());
    }

    /**
     * 删除轮播图
     */
    @Override
    public void deleteAppCarouselByCarouselId(Integer carouselId) {
        this.appCarouselMapper.deleteByCarouselId(carouselId);
    }

    /**
     * 修改排序
     */
    @Override
    public void updateSort(String carouselIds) {
        String[] carouselIdArray = carouselIds.split(",");
        int index = 1;
        for (String carouselId : carouselIdArray) {
            Integer id = Integer.parseInt(carouselId);
            AppCarousel appCarousel = new AppCarousel();
            appCarousel.setSort(index);
            this.appCarouselMapper.updateByCarouselId(appCarousel, id);
            index++;
        }
    }
}