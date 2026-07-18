package org.example.springdemo3.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.springdemo3.pojo.Result;
import org.example.springdemo3.utils.JwtUtils;
import org.example.springdemo3.utils.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 * 1. 解析 JWT，校验签名和过期时间
 * 2. 查询 Redis 中的 token，校验是否被踢下线
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private void writeUnauthorized(HttpServletResponse response, String msg) throws Exception {
        response.setStatus(401);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.unauthorized(msg)));
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. 从请求头获取 token
        String token = request.getHeader("Authorization");

        // 2. 解析 JWT —— 校验签名和过期时间
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            log.warn("JWT 无效或已过期: {}", token);
            writeUnauthorized(response, "未登录或 token 已过期");
            return false;
        }

        // 3. 从 claims 获取用户名
        String username = (String) claims.get("username");

        // 4. 校验 Redis 中的 token —— 检测是否被踢下线
        if (!redisService.isValidToken(username, token)) {
            String storedToken = redisService.getToken(username);
            String msg;
            if (storedToken == null) {
                msg = "token已过期";
            } else {
                msg = "账号已在其他地方登录";
            }
            log.warn("Redis token 校验失败: username={}, msg={}", username, msg);
            writeUnauthorized(response, msg);
            return false;
        }

        // 5. token 有效，放行
        log.info("token 校验通过: username={}", username);
        return true;
    }
}
