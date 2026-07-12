package org.example.springdemo3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
@SpringBootApplication
public class SpringDemo3Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemo3Application.class, args);
    }

    /**
     * 启动时自动执行 Redis 连接测试
     * 如果 Redis 连接失败，项目仍可正常启动
     */
    @Bean
    public CommandLineRunner redisDemo(StringRedisTemplate redisTemplate) {
        return args -> {
            log.info("========================================");
            log.info("  Redis 连接测试开始...");
            log.info("========================================");
            try {
                // 写入测试键
                redisTemplate.opsForValue().set("test:hello", "Redis connected successfully!");
                // 读取测试键
                String value = redisTemplate.opsForValue().get("test:hello");
                log.info("  ✅ Redis 连接成功！");
                log.info("  测试写入/读取: {}", value);
                // 清理测试键
                redisTemplate.delete("test:hello");
            } catch (Exception e) {
                log.warn("  ⚠️ Redis 连接失败，请检查 Redis 是否启动: {}", e.getMessage());
                log.warn("  应用仍可正常启动，但认证功能依赖 Redis。");
            }
            log.info("========================================");
        };
    }
}
