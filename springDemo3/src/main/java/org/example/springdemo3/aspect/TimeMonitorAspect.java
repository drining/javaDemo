package org.example.springdemo3.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 案例2：耗时监控切面
 *
 * 功能：监控所有 Service 方法的执行时间，超时记录 warn 日志
 * 知识点：@Around + 不同切入点组合，StopWatch 计时
 */
@Slf4j
@Aspect
@Component
public class TimeMonitorAspect {

    /**
     * 切入点：匹配 service 包及其子包下所有类的所有方法
     */
    @Pointcut("execution(* org.example.springdemo3.service..*.*(..))")
    public void servicePointcut() {
    }

    /**
     * 环绕通知：统计方法执行时间
     *
     * 如果方法执行超过 500ms，记录 warn 级别日志（性能告警）
     * 如果小于 500ms，记录 debug 级别日志
     */
    @Around("servicePointcut()")
    public Object monitorTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        System.out.println(">>> className = " + className);
        System.out.println(">>> methodName = " + methodName);
        System.out.println(">>> fullMethod = " + joinPoint.getSignature().getDeclaringTypeName() + "." + methodName);
        System.out.println(">>> args = " + java.util.Arrays.toString(joinPoint.getArgs()));
        System.out.println(">>> kind = " + joinPoint.getKind());

        long start = System.nanoTime();
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            // 在 finally 中计算耗时，确保无论是否异常都会记录
            long elapsedNanos = System.nanoTime() - start;
            long elapsedMs = elapsedNanos / 1_000_000;

            if (elapsedMs > 500) {
                log.warn("⚡【性能告警】{}.{}() 执行耗时: {}ms（超过 500ms 阈值）",
                        className, methodName, elapsedMs);
            } else {
                log.debug("⏱【耗时监控】{}.{}() 执行耗时: {}ms",
                        className, methodName, elapsedMs);
            }
        }
    }
}
