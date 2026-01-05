package com.javastudy.vocabease_admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.javastudy.vocabease_common.mappers")
@ComponentScan(basePackages = {
        "com.javastudy.vocabease_admin",
        "com.javastudy.vocabease_common"  // ← 显式包含 common 模块
})
public class VocabEaseAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(VocabEaseAdminApplication.class, args);
    }
}
