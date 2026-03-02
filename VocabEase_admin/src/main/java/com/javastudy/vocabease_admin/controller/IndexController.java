package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.dto.StatisticDataDto;
import com.javastudy.vocabease_common.entity.dto.StatisticWeekDataDto;
import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.StatisticDataService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("indexController")
@RequestMapping("/index")
public class IndexController extends ABaseController {
    @Resource
    private StatisticDataService statisticDataService;

    /**
     * 统计数据
     */
    @RequestMapping("/getAllData")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.HOME)
    public ResponseVO<List<StatisticDataDto>> getAllData() {
        return getSuccessResponseVO(this.statisticDataService.getAllData());
    }
    /**
     * 统计周用户数据
     */
    @RequestMapping("/getWeekAllData")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.HOME)
    public ResponseVO<StatisticWeekDataDto> getWeekAllData() {
        return getSuccessResponseVO(this.statisticDataService.getWeekAllData());
    }
    /**
     * 统计内容数据
     */
    @RequestMapping("/getWeekContentData")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.HOME)
    public ResponseVO<StatisticWeekDataDto> getWeekContentData() {
        return getSuccessResponseVO(this.statisticDataService.getWeekContentData());
    }
}
