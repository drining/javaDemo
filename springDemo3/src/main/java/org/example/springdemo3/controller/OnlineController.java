package org.example.springdemo3.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.springdemo3.pojo.Result;
import org.example.springdemo3.utils.JwtUtils;
import org.example.springdemo3.utils.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户在线管理
 * - 退出登录
 * - 管理员踢下线
 * - 查看在线状态
 */
@Slf4j
@RestController
@Tag(name = "用户在线管理", description = "退出登录、踢下线、在线状态查询")
public class OnlineController {

    @Autowired
    private RedisService redisService;

    /**
     * 当前用户退出登录
     * 删除 Redis 中的 token → 下次请求需要重新登录
     */
    @PostMapping("/user/logout")
    @Operation(summary = "退出登录")
    public Result<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            return Result.error("token 无效");
        }

        String username = (String) claims.get("username");
        redisService.deleteToken(username);

        log.info("用户已退出登录: username={}", username);
        return Result.success("退出登录成功");
    }

    /**
     * 管理员踢用户下线
     * 删除指定用户的 Redis token → 该用户所有请求立即失效
     */
    @PostMapping("/admin/kick/{username}")
    @Operation(summary = "踢用户下线")
    public Result<String> kickUser(@PathVariable String username) {
        redisService.deleteToken(username);
        log.info("管理员踢用户下线: targetUsername={}", username);
        return Result.success("用户 " + username + " 已被踢下线");
    }

    /**
     * 查询用户是否在线
     */
    @GetMapping("/user/online/{username}")
    @Operation(summary = "查询在线状态")
    public Result<Boolean> isOnline(@PathVariable String username) {
        String token = redisService.getToken(username);
        boolean online = token != null;
        log.info("查询在线状态: username={}, online={}", username, online);
        return Result.success(online);
    }
}
