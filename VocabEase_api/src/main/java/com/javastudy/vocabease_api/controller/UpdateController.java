package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.config.AppConfig;
import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.enums.AppUpdateTypeEnum;
import com.javastudy.vocabease_common.entity.enums.RequestFrequencyEnum;
import com.javastudy.vocabease_common.entity.po.AppUpdate;
import com.javastudy.vocabease_common.entity.vo.AppUpdateVO;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.service.AppDeviceService;
import com.javastudy.vocabease_common.service.AppUpdateService;
import com.javastudy.vocabease_common.service.Item4questionService;
import com.javastudy.vocabease_common.service.QuestionService;
import com.javastudy.vocabease_common.utils.CopyUtil;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

@RestController("updateController")
@RequestMapping("/update")
public class UpdateController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateController.class);

    @Resource
    private AppConfig appConfig;
    @Resource
    private AppUpdateService appUpdateService;
    @Resource
    private QuestionService questionService;
    @Resource
    private Item4questionService item4questionService;
    @Resource
    private AppDeviceService appDeviceService;

    /**
     * 检测版本
     */
    @RequestMapping("/checkVersion")
    @GlobalInterceptor
    public ResponseVO<AppUpdateVO> checkVersion(@VerifyParam(required = true) String appVersion,
                                                   @VerifyParam(required = true) String deviceId) {
        if (StringTools.isEmpty(appVersion))
            return getSuccessResponseVO(null);
        AppUpdate appUpdate = this.appUpdateService.getLastAppUpdate(appVersion, deviceId);
        if (appUpdate == null)
            return getSuccessResponseVO(null);
        AppUpdateVO updateVO= CopyUtil.copy(appUpdate, AppUpdateVO.class);
        AppUpdateTypeEnum type = AppUpdateTypeEnum.getEnumByType(appUpdate.getUpdateType());
        File file = new File(this.appConfig.getProjectFolder() +
                Constants.APP_UPDATE_FOLDER + appUpdate.getId() + type.getSuffix());
        updateVO.setSize(file.length());
        updateVO.setUpdateList(Arrays.asList(appUpdate.getUpdateDescArray()));
        return getSuccessResponseVO(updateVO);
    }
    /**
     * 下载
     */
    @RequestMapping("/download")
    @GlobalInterceptor(frequencyType = RequestFrequencyEnum.DAY, requestFrequencyThreshold = 5)
    public void download(HttpServletResponse response, @VerifyParam(required = true) Integer id) {
        OutputStream os = null;
        FileInputStream fis = null;
        try {
            AppUpdate appUpdate = this.appUpdateService.getAppUpdateById(id);
            AppUpdateTypeEnum type = AppUpdateTypeEnum.getEnumByType(appUpdate.getUpdateType());
            String fileName = this.appConfig.getApplicationName() + "." + appUpdate.getVersion() + type.getSuffix();
            File file = new File(appConfig.getProjectFolder() + Constants.APP_UPDATE_FOLDER + appUpdate.getId() + type.getSuffix());
            if (!file.exists())
                return;
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/x-msdownload; charset=utf-8");
            response.setContentLengthLong(file.length());
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            os = response.getOutputStream();
            int len = 0;
            while ((len = fis.read(buffer)) != -1)
                os.write(buffer, 0, len);
            os.flush();
        } catch (Exception e) {
            logger.error("读取文件异常", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
        }
    }















}
