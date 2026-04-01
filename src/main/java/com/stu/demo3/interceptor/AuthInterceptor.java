package com.stu.demo3.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // ====================== 实验要求：只有 注册 + 登录 放行 ======================
        // 放行1：注册 POST /api/users
        boolean register = "POST".equalsIgnoreCase(method) && "/api/users".equals(uri);
        // 放行2：登录 POST /api/users/login
        boolean login = "POST".equalsIgnoreCase(method) && "/api/users/login".equals(uri);

        if (register || login) {
            return true; // 放行
        }

        // ====================== 其他所有接口必须校验 Token ======================
        String token = request.getHeader("Authorization");

        // 没有Token 或 为空 → 拦截返回401
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            response.setContentType("application/json;charset=UTF-8");
            String errorJson = "{\"code\": 401, \"msg\": \"未登录，请先登录获取Token\"}";
            PrintWriter writer = response.getWriter();
            writer.write(errorJson);
            writer.flush();
            writer.close();
            return false;
        }

        // Token 存在 → 放行
        return true;
    }
}