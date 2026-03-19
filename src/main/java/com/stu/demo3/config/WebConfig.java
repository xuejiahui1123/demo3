package com.stu.demo3.config;

import com.stu.demo3.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor()) // 挂载自定义拦截器
                .addPathPatterns("/api/**") // 拦截/api下所有请求（/**表示子路径全部匹配）
                .excludePathPatterns("/api/users/login"); // 仅放行登录接口，其余由拦截器内部判断
    }
}