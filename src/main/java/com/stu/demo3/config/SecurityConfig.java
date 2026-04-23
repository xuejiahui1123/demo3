package com.stu.demo3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // 开启CORS
                .csrf(AbstractHttpConfigurer::disable) // 关闭CSRF
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态
                )
                .authorizeHttpRequests(auth -> auth
                        // 放行：注册（POST /api/users）
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        // 放行：登录（POST /api/users/login）
                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                        // 其他所有接口必须登录认证
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable) // 关闭默认登录页
                .httpBasic(AbstractHttpConfigurer::disable); // 关闭httpBasic

        return http.build();
    }
}