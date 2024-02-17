package io.naddral.adessotest.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Aspect
@Component
@Order(1)
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object logMethodCall(ProceedingJoinPoint pjp) throws Throwable{
        String methodName = pjp.getSignature().getName();
        String className = pjp.getSignature().getDeclaringType().getSimpleName();
        logger.info("[ {}.{} ] START: {}", className, methodName, Instant.now());
        Object result = pjp.proceed();
        logger.info("[ {}.{} ] END: {}", className, methodName, Instant.now());
        return result;
    }

}
