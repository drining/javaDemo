package org.example.springdemo3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.springdemo3.mapper.EmpMapper;
import org.example.springdemo3.pojo.Emp;
import org.example.springdemo3.pojo.LoginDTO;
import org.example.springdemo3.pojo.Result;
import org.example.springdemo3.utils.JwtUtils;
import org.example.springdemo3.utils.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "登录认证", description = "用户登录接口")
public class LoginController {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        log.info("用户登录: username={}", loginDTO.getUsername());

        // 1. 根据用户名查询员工
        Emp emp = empMapper.findByUsername(loginDTO.getUsername());
        if (emp == null) {
            return Result.error("用户名或密码错误");
        }

        // 2. 校验密码（明文比对，学习项目暂未使用加密）
        if (!emp.getPassword().equals(loginDTO.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        // 3. 生成 JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", emp.getId());
        claims.put("username", emp.getUsername());
        claims.put("name", emp.getName());
        String token = JwtUtils.generateToken(claims);

        // 4. 将 token 存入 Redis
        //    - 覆盖旧 token，实现「同一账号只能一处登录」
        //    - TTL 与 JWT 过期时间一致（12h）
        redisService.saveToken(emp.getUsername(), token);

        // 5. 返回 token 和用户信息
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", emp.getUsername());
        data.put("name", emp.getName());

        log.info("登录成功: username={}", emp.getUsername());
        return Result.success(data);
    }
}
