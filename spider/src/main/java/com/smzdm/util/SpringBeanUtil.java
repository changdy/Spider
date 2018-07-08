package com.smzdm.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUtil implements ApplicationContextAware {
    private static volatile ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        SpringBeanUtil.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(String beanName, Class<T> requireType) {
        return context.getBean(beanName, requireType);
    }

    public static <T> T getBean(Class<T> requireType) {
        return context.getBean(requireType);
    }
}