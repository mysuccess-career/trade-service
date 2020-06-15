package com.db.demo.demo.event.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Aspect
@Component
@ConditionalOnProperty(value = "trade.controller.log.enabled", havingValue = "true")
public class TradeControllerEventLogging {

    private static final Logger LOG = LoggerFactory.getLogger(TradeControllerEventLogging.class);
    private String apiExecution = "uri={}|http_method={}|status={}|response_time={}";

    @Around(value = "execution(* com.db.demo.demo.controller..*(..))")
    public Object restControllerExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getResponse();
        LOG.info(apiExecution,
                request.getRequestURI(),
                request.getMethod(),
                response.getStatus(),
                elapsedTime);
        return result;
    }

    @Pointcut("execution(* com.db.demo.demo.controller..*(..))")
    public void printParameters() {
    }

    @Before(value = "printParameters()", argNames = "joinPoint")
    public void beforeCall(JoinPoint joinPoint) {
        LOG.debug("Method Name :" + joinPoint.getSignature().toShortString() + "| Input => " + Arrays.asList(joinPoint.getArgs()));
    }


    @AfterReturning(
            value = "printParameters()",
            returning = "result"
    )
    public void afterReturningExecution(JoinPoint jp, Object result) {
        LOG.debug("After returning method: {} | class: {} | Output: {} "
                , jp.getSignature().getName(), jp.getTarget().getClass().getSimpleName(), result);
    }
}
