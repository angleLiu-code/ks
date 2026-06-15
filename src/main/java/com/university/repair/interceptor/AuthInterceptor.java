package com.university.repair.interceptor;

import com.alibaba.fastjson.JSON;
import com.university.repair.util.JwtUtils;
import com.university.repair.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 身份验证拦截器
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 处理 OPTIONS 请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(JSON.toJSONString(
                    ResponseUtils.error("401", "缺少身份验证令牌")
            ));
            return false;
        }

        // 移除 "Bearer " 前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            // 验证 JWT token
            if (!JwtUtils.verify(token)) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(JSON.toJSONString(
                        ResponseUtils.error("401", "身份验证令牌无效或已过期")
                ));
                return false;
            }

            // 将用户信息存入请求属性中
            int userId = JwtUtils.getUserId(token);
            String userRole = JwtUtils.getUserRole(token);
            request.setAttribute("userId", userId);
            request.setAttribute("userRole", userRole);

            return true;
        } catch (Exception e) {
            log.error("Token验证失败: ", e);
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(JSON.toJSONString(
                    ResponseUtils.error("401", "身份验证失败")
            ));
            return false;
        }
    }
}
