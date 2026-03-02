package com.javastudy.vocabease_admin.filter;

import com.javastudy.vocabease_common.entity.constants.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serial;

@Component
@Order(Constants.ORDER_CORS)
public class CorsFilter extends HttpFilter {
    @Serial
    private static final long serialVersionUID = -7244274804378366524L;

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {

        // 获取 Origin
        String origin = request.getHeader("Origin");
        if (origin != null) {
            // 允许指定的 origin（开发环境）
            if (origin.startsWith("http://localhost:") || origin.equals("http://127.0.0.1")) {
                response.setHeader("Access-Control-Allow-Origin", origin);
                // ⭐ 关键：允许携带 Cookie
                response.setHeader("Access-Control-Allow-Credentials", "true");
            }
        }

        // 允许的请求方法
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        // 允许的请求头
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With");

        // ⭐ 处理预检请求（OPTIONS）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return; // 预检请求不继续往下走
        }

        // 放行实际请求
        chain.doFilter(request, response);
    }
}
