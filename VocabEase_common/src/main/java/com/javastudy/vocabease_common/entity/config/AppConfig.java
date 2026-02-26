package com.javastudy.vocabease_common.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${project.folder}")
    private String projectFolder;
    @Value("${admin.phone}")
    private String superAdminPhone;
    @Value("${spring.security.jwt.key}")
    private String jwtCommonSecret;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${spring.application.domain}")
    private String appDomain;

    public String getProjectFolder() {return projectFolder;}
    public String getSuperAdminPhone() {return superAdminPhone;}
    public String getJwtCommonSecret() {return jwtCommonSecret;}
    public String getApplicationName() {return applicationName;}
    public String getAppDomain() {return appDomain;}
}
