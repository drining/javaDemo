package org.example.springdemo3.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.springdemo3.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 * 检查请求头中的 token，解析成功则放行，否则返回 401
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. 从请求头获取 token
        String token = request.getHeader("Authorization");

        // 2. 校验 token
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            log.warn("token 无效或已过期: {}", token);
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":0,\"msg\":\"未登录或 token 已过期\",\"data\":null}");
            return false;
        }

        // 3. token 有效，放行
        log.info("token 校验通过: username={}", claims.get("username"));
        return true;
    }
}
