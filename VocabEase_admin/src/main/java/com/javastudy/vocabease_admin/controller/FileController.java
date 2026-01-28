package com.javastudy.vocabease_admin.controller;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.config.AppConfig;
import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.entity.enums.FileTypeEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.enums.TemplateEnum;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.utils.DateUtil;
import com.javastudy.vocabease_common.utils.ScaleFilterUtil;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Logger;

/**
 * 文件上传controller
 */
@Slf4j
@RequestMapping("/file")
@RestController("fileController")
public class FileController extends ABaseController{
    @Resource
    private AppConfig appConfig;

    private static final Logger logger = Logger.getLogger(FileController.class.getName());

    @RequestMapping("/uploadFile")
    @GlobalInterceptor
    public ResponseVO uploadFile(MultipartFile file, Integer type) {
        FileTypeEnum fileType = FileTypeEnum.getType(type);
        String mouth = DateUtil.format(new Date(), DateTimePatternEnum.YY_MM.getPattern());
        String folderName = appConfig.getProjectFolder() + mouth;
        File folder = new File(folderName);
        if (!folder.exists())
            folder.mkdirs();
        String fileSuffix = StringTools.getFileSuffix(file.getOriginalFilename());
        String realFileName = StringTools.getRandomString(Constants.LENGTH_30) + fileSuffix;
        String realFilePath = mouth + "/" + realFileName;
        File localFile = new File(appConfig.getProjectFolder() + realFilePath);
        try {
            file.transferTo(localFile);
            if (fileType != null)
                ScaleFilterUtil.createThumbnail(localFile, fileType.getMaxWidth(), fileType.getMaxWidth(), localFile);
        } catch (IOException e) {
            logger.warning("文件上传失败");
            throw new BusinessException("文件上传失败");
        }
        return getSuccessResponseVO(realFilePath);
    }

    @RequestMapping("/getImage/{imageFolder}/{imageName}")
    @GlobalInterceptor
    public void getImage(HttpServletResponse response, String imageFolder, @PathVariable("imageName") String imageName) {
        readImage(response, imageFolder, imageName);
    }

    private void readImage(HttpServletResponse response, String imageFolder, String imageName) {
        if (StringTools.isEmpty(imageFolder) || StringUtils.isBlank(imageName))
            return;
        String imageSuffix = StringTools.getFileSuffix(imageName);
        String filePath = appConfig.getProjectFolder() + imageFolder + "/" + imageName;
        imageSuffix = imageSuffix.replace(".", "");
        String contentType = "image/" + imageSuffix;
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "max-age=2592000");
        readFile(response, filePath);
    }

    protected void readFile(HttpServletResponse response, String filePath) {
        if (!StringTools.pathIsRight(filePath))
            return;
        OutputStream os = null;
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists())
                return;
            fis = new FileInputStream(file);
            byte[] byteData = new byte[1024];
            os = response.getOutputStream();
            int len = 0;
            while ((len = fis.read(byteData)) != -1)
                os.write(byteData, 0, len);
            os.flush();
        } catch (IOException e) {
            logger.warning("读取文件异常");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.warning("IO异常");
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.warning("IO异常");
                }
            }
        }
    }
    /**
     * 下载模板
     */
    @RequestMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response, HttpServletRequest request, Integer type) {
        TemplateEnum templateEnum = TemplateEnum.getEnumByType(type);
        if (templateEnum == null)
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        OutputStream os = null;
        InputStream is = null;
        try {
            String fileName = templateEnum.getName();
            response.setContentType("application/x-msdownload; charset=utf-8");
            if (request.getHeader("User-Agent").toLowerCase().indexOf("msie") > 0)
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            else
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\" ");
            ClassPathResource classPathResource = new ClassPathResource(templateEnum.getPath());
            is = classPathResource.getInputStream();
            byte[] byteData = new byte[1024];
            os = response.getOutputStream();
            int len = 0;
            while ((len = is.read(byteData)) != -1)
                os.write(byteData, 0, len);
            os.flush();
        } catch (Exception e){
            logger.warning("模板下载异常");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.warning("IO异常");
                }
            }
            if (is != null) {
                 try {
                     is.close();
                 } catch (IOException e) {
                     logger.warning("IO异常");
                 }
            }
        }
    }
























}
