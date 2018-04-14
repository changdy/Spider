package com.smzdm.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by Changdy on 2017/11/29.
 */
@Slf4j
@Aspect
@Component
public class HttpAspect {

    @Pointcut("execution(public * com.smzdm.controller.*.*(..))")
    public void log() {
    }

    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturning(Object object) {
        log.info("response={}", JSON.toJSON(object));
    }
}
