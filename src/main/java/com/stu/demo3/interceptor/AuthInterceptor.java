package com.stu.demo3.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1：获取本次请求的HTTP动词（GET/POST/DELETE/PUT）和请求URI
        String method = request.getMethod(); // 获取HTTP方法，如GET、POST
        String uri = request.getRequestURI(); // 获取请求路径，如/api/users/1

        // 2：编写细粒度放行规则（URL+HTTP动词双重判断）
        // 规则A：POST请求 + 路径精确等于/api/users → 放行（用户注册）
        boolean isCreateUser = "POST".equalsIgnoreCase(method) && "/api/users".equals(uri);
        // 规则B：GET请求 + 路径以/api/users/开头 → 放行（根据ID查询用户，如/api/users/1、/api/users/100）
        boolean isGetUser = "GET".equalsIgnoreCase(method) && uri.startsWith("/api/users/");

        // 满足任一合法规则，直接放行，无需校验Token
        if (isCreateUser || isGetUser) {
            return true;
        }

        // 3：敏感请求（DELETE/PUT等）执行严格的Token校验
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            // 错误信息中携带敏感操作的方法和路径，方便排错
            String errorJson = "{\"code\": 401, \"msg\": \"非法操作:敏感动作[" + method + " " + uri + "]需登录验证\"}";
            PrintWriter writer = response.getWriter();
            writer.write(errorJson);
            writer.flush();
            writer.close();
            return false;
        }

        return true;
    }
}
