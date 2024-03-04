package org.mindswap.springtheknife.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution( * org.mindswap.springtheknife.service.user.UserService.createUser*.*(..))")
    public void logBeforeServiceToCreate(JoinPoint joinPoint) {
        logger.info("Before " + joinPoint.getSignature().getName() + " method call");
    }

    @AfterReturning(pointcut = "execution(* org.mindswap.springtheknife.service.*.*(..))", returning = "result")
    public void logAfterService(JoinPoint joinPoint, Object result) {
        logger.info("After " + joinPoint.getSignature().getName() + " method call");
        logger.info("Response " + result);
    }

    @AfterThrowing(pointcut = "execution(* org.mindswap.springtheknife.service.*.*(..))", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception){
        logger.error("Exception in " + joinPoint.getSignature().getName() + " method call");
        logger.error("Response: " + exception);
    }

    @Around("execution(* org.mindswap.springtheknife.service.user.UserService.getUser(..))")
        public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
            long startTime = System.currentTimeMillis();
            logger.info("Before " + joinPoint.getSignature().getName() + " method call");
            Object result = joinPoint.proceed();
            logger.info("After " + joinPoint.getSignature(). getName() + " method call");
            long endTime = System.currentTimeMillis();
            logger.info("Execution time of " + joinPoint.getSignature().getName() + "method call " + (endTime - startTime) + " milliseconds");
            return result;
        }
    }


