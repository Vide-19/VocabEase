package com.javastudy.vocabease_common.entity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${project.folder}")
    private String projectFolder;
    @Value("${admin.phone}")
    private String superAdminPhone;

    public String getProjectFolder() {return projectFolder;}

    public void setProjectFolder(String projectFolder) {this.projectFolder = projectFolder;}

    public String getSuperAdminPhone() {return superAdminPhone;}

    public void setSuperAdminPhone(String superAdminPhone) {this.superAdminPhone = superAdminPhone;}
}
