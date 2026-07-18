package org.example.springdemo3.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 案例1：操作日志切面
 *
 * 功能：记录所有 Controller 方法的入参、执行耗时、返回值
 * 知识点：@Aspect、@Component、@Around、@Pointcut、execution() 表达式
 */
@Slf4j
@Aspect         // 1. 声明这是一个切面类
@Component      // 2. 交给 Spring 容器管理
public class LogAspect {

    /**
     * 3. 定义切入点：匹配 controller 包下所有类的所有方法
     *
     * execution() 表达式语法：
     *   execution(修饰符 返回类型 包.类.方法(参数))
     *
     * 这里写的是：
     *   execution(* org.example.springdemo3.controller..*.*(..))
     *     ├── *                   → 任意返回类型
     *     ├── 包路径..            → 该包及其子包
     *     ├── *                   → 任意类名
     *     ├── .*(..)              → 任意方法，任意参数
     */
    @Pointcut("execution(* org.example.springdemo3.controller..*.*(..))")
    public void controllerPointcut() {
        // 切入点方法，方法名作为切入点标识，用于复用
    }

    /**
     * 4. 环绕通知：Around
     *
     * @Around = @Before + @AfterReturning/@AfterThrowing
     * 在方法执行前后注入增强逻辑
     *
     * ProceedingJoinPoint 可以：
     *   - getSignature()：获取方法签名（类名、方法名）
     *   - getArgs()：获取方法入参
     *   - proceed()：执行目标方法（必须调用，否则目标方法不会执行）
     */
    @Around("controllerPointcut()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取目标方法信息
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 记录请求开始（@Before 阶段）
        log.info("=== 【操作日志】{}.{}() 开始 ===", className, methodName);
        log.info("请求参数: {}", Arrays.toString(args));

        long startTime = System.currentTimeMillis();

        try {
            // 调用目标方法（核心）
            Object result = joinPoint.proceed();

            // 记录请求结束（@AfterReturning 阶段）
            long elapsed = System.currentTimeMillis() - startTime;
            log.info("执行耗时: {}ms", elapsed);
            // log.info("返回结果: {}", result);
            log.info("=== 【操作日志】{}.{}() 结束 ===", className, methodName);

            return result;
        } catch (Throwable e) {
            // 异常处理（@AfterThrowing 阶段）
            long elapsed = System.currentTimeMillis() - startTime;
            log.error("=== 【操作日志】{}.{}() 异常 ===", className, methodName);
            log.error("异常信息: {}, 耗时: {}ms", e.getMessage(), elapsed);
            throw e; // 继续往外抛，交给全局异常处理器
        }
    }
}
