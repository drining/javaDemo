package org.example.springdemo3.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 * - 生成 token（登录成功后调用）
 * - 校验 token（拦截器中调用）
 */
public class JwtUtils {

    /** 密钥（至少 256 位，即 32 个字符）—— 生产环境应放到配置文件中 */
    private static final String SECRET = "JavaDemo3LoginSecretKey2026AtLeast32Chars!!";

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /** 过期时间：12 小时 */
    private static final long EXPIRATION_MS = 12 * 60 * 60 * 1000L;

    /**
     * 生成 JWT token
     * @param claims 要存入 token 的信息（如 userId, username）
     * @return token 字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析 token，获取 claims
     * @param token token 字符串
     * @return claims，解析失败返回 null
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            // token 过期、被篡改、格式错误等均返回 null
            return null;
        }
    }
}
