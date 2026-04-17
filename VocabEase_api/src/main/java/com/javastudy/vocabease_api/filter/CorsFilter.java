package com.javastudy.vocabease_api.filter;

import com.javastudy.vocabease_common.entity.constants.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
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
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*"); // 允许所有域名访问（仅开发环境）
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        chain.doFilter(req, res);
    }
}
