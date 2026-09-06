package com.example.lantu_web_java.config;

import com.example.lantu_web_java.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // CORS 预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            sendUnauthorized(response, "未登录，请先登录");
            return false;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        if (!JwtUtil.validateToken(token)) {
            sendUnauthorized(response, "登录已过期，请重新登录");
            return false;
        }

        Long userId = JwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);
        return true;
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String json = String.format(
                "{\"code\":401,\"message\":\"%s\",\"data\":null}",
                message
        );
        response.getWriter().write(json);
    }
}
