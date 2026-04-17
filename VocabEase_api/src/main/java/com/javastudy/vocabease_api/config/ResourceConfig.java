package com.javastudy.vocabease_api.config;

import com.javastudy.vocabease_common.entity.config.AppConfig;
import com.javastudy.vocabease_common.entity.constants.Constants;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Resource
    private AppConfig appConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取物理路径，例如 D:\IdeaProject\VocabEase\avatar\
        String locationPath = appConfig.getProjectFolder() + Constants.AVATAR_FOLDER;

        // 统一替换为 Linux/URL 风格的正斜杠，并在前面加上 file:/
        // 最终变成: file:/D:/IdeaProject/VocabEase/avatar/
        locationPath = "file:/" + locationPath.replace("\\", "/");

        // 【关键】注册映射
        // 浏览器访问: http://localhost:9090/VocabEase/avatar/xxx.jpg
        // 映射到本地: D:\IdeaProject\VocabEase\avatar\xxx.jpg
        registry.addResourceHandler("/VocabEase/avatar/**")
                .addResourceLocations(locationPath);

        // 为了保险，也可以注册一个不带前缀的（可选）
        // registry.addResourceHandler("/avatar/**").addResourceLocations(locationPath);


        // ==========================================
        // 2. 👇 新增：轮播图/上传文件的映射
        // ==========================================

        // 1. 获取物理路径 D:\IdeaProject\VocabEase\
        String uploadPath = appConfig.getProjectFolder() + "202603/";

        // 2. 统一格式: file:/D:/IdeaProject/VocabEase/
        uploadPath = "file:/" + uploadPath.replace("\\", "/");

        System.out.println("🚀 正在配置上传文件映射...");
        System.out.println("   访问路径: /VocabEase/**");
        System.out.println("   物理路径: " + uploadPath);

        // 3. 注册映射
        // 访问路径: /202603/**  (或者 /** 如果你想映射根目录下所有文件)
        // 浏览器访问: http://localhost:9090/202603/xxx.jfif
        // 映射到本地: D:\IdeaProject\VocabEase\202603\xxx.jfif
        registry.addResourceHandler("/202603/**")
                .addResourceLocations(uploadPath);
    }
}