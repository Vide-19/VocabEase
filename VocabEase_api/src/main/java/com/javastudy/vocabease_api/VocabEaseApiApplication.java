package com.javastudy.vocabease_api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.javastudy.vocabease_common.mappers")
@ComponentScan(basePackages = {
        "com.javastudy.vocabease_api",
        "com.javastudy.vocabease_common"  // ← 显式包含 common 模块
})
public class VocabEaseApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(VocabEaseApiApplication.class, args);
    }
}
