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

        System.out.println("🚀 正在配置静态资源映射...");
        System.out.println("   访问路径: /VocabEase/avatar/**");
        System.out.println("   物理路径: " + locationPath);

        // 【关键】注册映射
        // 浏览器访问: http://localhost:9090/VocabEase/avatar/xxx.jpg
        // 映射到本地: D:\IdeaProject\VocabEase\avatar\xxx.jpg
        registry.addResourceHandler("/VocabEase/avatar/**")
                .addResourceLocations(locationPath);

        // 为了保险，也可以注册一个不带前缀的（可选）
        // registry.addResourceHandler("/avatar/**").addResourceLocations(locationPath);
    }
}