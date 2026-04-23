package com.stu.demo3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 只保留跨域配置，必须加！
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")        // 所有接口
                .allowedOrigins("*")      // 允许所有域名
                .allowedMethods("*")      // 允许所有请求方式（GET,POST,PUT,DELETE）
                .allowedHeaders("*")      // 允许所有请求头
                .allowCredentials(false)
                .maxAge(3600);
    }
}