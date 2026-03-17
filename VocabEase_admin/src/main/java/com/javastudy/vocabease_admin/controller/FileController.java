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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 文件上传controller
 */
@Slf4j
@RequestMapping("/file")
@RestController("fileController")
public class FileController extends ABaseController{
    @Resource
    private AppConfig appConfig;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @RequestMapping("/uploadFile")
    @GlobalInterceptor
    public ResponseVO<String> uploadFile(MultipartFile file, Integer type) {
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
            logger.error("文件上传失败");
            throw new BusinessException("文件上传失败");
        }
        return getSuccessResponseVO(realFilePath);
    }

    // 1. 修改映射路径，使用 /** 捕获所有后续路径
    @RequestMapping("/getImage/**")
    @GlobalInterceptor
    public void getImage(HttpServletRequest request, HttpServletResponse response) {
        // 2. 手动从 URL 中提取路径
        // 获取请求 URI: /file/getImage/202603/5JTdsbXmX6bRNeKr9xPKPkzelJBiBA.jpeg
        String requestUri = request.getRequestURI();

        // 找到 "/getImage/" 的位置
        int index = requestUri.indexOf("/getImage/");
        if (index == -1) {
            response.setStatus(404);
            return;
        }

        // 截取 "/getImage/" 之后的部分: 202603/5JTdsbXmX6bRNeKr9xPKPkzelJBiBA.jpeg
        // 注意长度计算: "/getImage/" 长度为 10
        String relativePath = requestUri.substring(index + 10);

        // 可选：URL 解码，防止中文文件名乱码
        relativePath = java.net.URLDecoder.decode(relativePath, StandardCharsets.UTF_8);

        // 3. 拆分 folder 和 filename (根据最后一个斜杠)
        int lastSlashIndex = relativePath.lastIndexOf("/");
        String imageFolder = "";
        String imageName = relativePath;

        if (lastSlashIndex > 0) {
            imageFolder = relativePath.substring(0, lastSlashIndex);
            imageName = relativePath.substring(lastSlashIndex + 1);
        }

        // 4. 调用原有的读取逻辑
        readImage(response, imageFolder, imageName);
    }

    private void readImage(HttpServletResponse response, String imageFolder, String imageName) {
        // ⭐️ 添加调试日志
        logger.info("🖼️ 尝试加载图片 - 文件夹: {}, 文件名: {}", imageFolder, imageName);

        if (StringTools.isEmpty(imageFolder) || StringUtils.isBlank(imageName)) {
            logger.warn("⚠️ 文件夹或文件名为空");
            return;
        }

        String imageSuffix = StringTools.getFileSuffix(imageName);
        // ⭐️ 关键：拼接完整路径
        // 确保 appConfig.getProjectFolder() 结尾有 "/"
        String filePath = appConfig.getProjectFolder() + imageFolder + "/" + imageName;

        logger.info("📂 最终物理路径: {}", filePath); // ⭐️ 打印这个路径去硬盘比对！

        imageSuffix = imageSuffix.replace(".", "");
        String contentType = "image/" + imageSuffix;

        // 简单的安全判断，防止后缀为空导致 content-type 错误
        if ("image".equals(contentType)) {
            contentType = "application/octet-stream";
        }

        response.setContentType(contentType);
        // 修正 Header 写法，max-age 应该是 Cache-Control 的值，Content-Disposition 通常用于下载
        // 如果想让浏览器直接显示，不要设 Content-Disposition 为 attachment
        response.setHeader("Cache-Control", "max-age=2592000");

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
            logger.error("读取文件异常");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("IO异常");
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("IO异常");
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
            throw new BusinessException(ResponseCodeEnum.CODE_400);
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
            logger.error("模板下载异常");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("IO异常");
                }
            }
            if (is != null) {
                 try {
                     is.close();
                 } catch (IOException e) {
                     logger.error("IO异常");
                 }
            }
        }
    }
}