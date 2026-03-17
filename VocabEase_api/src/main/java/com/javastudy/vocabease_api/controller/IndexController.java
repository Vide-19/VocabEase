package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.enums.PostStatusEnum;
import com.javastudy.vocabease_common.entity.enums.RequestFrequencyEnum;
import com.javastudy.vocabease_common.entity.po.*;
import com.javastudy.vocabease_common.entity.query.AppCarouselQuery;
import com.javastudy.vocabease_common.entity.query.Item4questionQuery;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexController extends ABaseController {

    @Resource
    private CategoryService categoryService;
    @Resource
    private AppCarouselService appCarouselService;
    @Resource
    private QuestionService questionService;
    @Resource
    private Item4questionService item4questionService;
    @Resource
    private AppDeviceService appDeviceService;

    /**
     * 加载分类信息
     */
    @RequestMapping("/loadCategory")
    @GlobalInterceptor
    public ResponseVO<List<Category>> loadCategory(@VerifyParam(required = true) Integer type) {
        return getSuccessResponseVO(this.categoryService.getCategoryListByType(type));
    }
    /**
     * 加载统计信息
     */
    @RequestMapping("/loadCarousel")
    @GlobalInterceptor
    public ResponseVO<List<AppCarousel>> loadCarousel() {
        AppCarouselQuery query = new AppCarouselQuery();
        query.setOrderBy("sort asc");
        List<AppCarousel> carouselList = this.appCarouselService.findListByParam(query);
        return getSuccessResponseVO(carouselList);
    }
    /**
     * 通过id找到问题与选项
     */
    @RequestMapping("/loadQuestionById")
    @GlobalInterceptor
    public ResponseVO<Question> loadQuestionById(@VerifyParam(required = true) Integer id) {
        Question question = this.questionService.getQuestionByQuestionId(id);
        if (question == null || PostStatusEnum.NO_POST.getStatus().equals(question.getStatus()))
            throw new BusinessException("找不到对应问题");
        Item4questionQuery query = new Item4questionQuery();
        query.setQuestionId(id);
        query.setOrderBy("sort asc");
        List<Item4question> item4questionList = this.item4questionService.findListByParam(query);
        question.setItemList(item4questionList);
        return getSuccessResponseVO(question);
    }
    /**
     * 通过id找到问题与选项
     */
    @RequestMapping("/report")
    @GlobalInterceptor(frequencyType = RequestFrequencyEnum.DAY, requestFrequencyThreshold = 5)//通过spring aop实现接口限流
    public ResponseVO<Question> report(HttpServletRequest request,
                                       @VerifyParam(required = true, max = 32) String deviceId,
                                       @VerifyParam(required = true, max = 32) String deviceBrand) {
        AppDevice appDevice = new AppDevice();
        appDevice.setDeviceId(deviceId);
        appDevice.setDeviceBrand(deviceBrand);
        appDevice.setLastLoginIp(getIpAddress(request));
        this.appDeviceService.reportData(appDevice);
        return getSuccessResponseVO(null);
    }
}