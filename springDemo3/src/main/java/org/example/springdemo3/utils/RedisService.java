package org.example.springdemo3.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis 工具服务
 * - 管理登录 token 的存储、校验和删除
 * - 用于实现"踢下线"功能
 */
@Slf4j
@Component
public class RedisService {

    /** token 在 Redis 中的 key 前缀 */
    private static final String TOKEN_KEY_PREFIX = "loginToken:";

    /** token 过期时间（小时），与 JWT 保持一致 */
    private static final long TOKEN_TTL_HOURS = 12;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 登录成功后保存 token 到 Redis
     * 如果该用户已登录，会覆盖旧 token → 旧设备被踢下线
     */
    public void saveToken(String username, String token) {
        String key = TOKEN_KEY_PREFIX + username;
        stringRedisTemplate.opsForValue().set(key, token, TOKEN_TTL_HOURS, TimeUnit.HOURS);
        log.info("Redis 保存 token: username={}, TTL={}h", username, TOKEN_TTL_HOURS);
    }

    /**
     * 获取 Redis 中存储的 token
     * @return token 字符串，不存在返回 null
     */
    public String getToken(String username) {
        return stringRedisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + username);
    }

    /**
     * 校验 token 是否有效
     * @return true 有效，false 无效（被踢下线或已过期）
     */
    public boolean isValidToken(String username, String token) {
        String storedToken = getToken(username);
        if (storedToken == null) {
            log.warn("Redis 中不存在 token，用户已被踢下线: username={}", username);
            return false;
        }
        if (!storedToken.equals(token)) {
            log.warn("Redis 中 token 不匹配，账号已在其他地方登录: username={}", username);
            return false;
        }
        // 刷新 token 的 TTL（每次请求延长有效期）
        stringRedisTemplate.expire(TOKEN_KEY_PREFIX + username, TOKEN_TTL_HOURS, TimeUnit.HOURS);
        return true;
    }

    /**
     * 删除 Redis 中的 token（踢下线 / 退出登录）
     */
    public void deleteToken(String username) {
        stringRedisTemplate.delete(TOKEN_KEY_PREFIX + username);
        log.info("Redis token 已删除，用户被踢下线: username={}", username);
    }
}
