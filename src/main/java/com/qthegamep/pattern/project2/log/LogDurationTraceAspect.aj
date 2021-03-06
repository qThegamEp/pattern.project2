package com.qthegamep.pattern.project2.log;

import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.*;

public aspect LogDurationTraceAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogDurationTraceAspect.class);

    pointcut all(): execution(* *(..)) && if(LOG.isTraceEnabled());

    Object around(): all() {
        Signature signature = thisJoinPoint.getSignature();
        Object[] args = thisJoinPoint.getArgs();
        LocalDateTime startDateTime = LocalDateTime.now();
        Object result = proceed();
        LocalDateTime endDateTime = LocalDateTime.now();
        long duration = MILLIS.between(startDateTime, endDateTime);
        LOG.trace("{} : {} : {} : {} ms", signature, args, result, duration);
        return result;
    }
}
