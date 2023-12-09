package com.equator.linker.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * TODO 不能处理同名方法？
 *
 * @Author: Equator
 * @Date: 2021/11/23 0:39
 **/
@Service
public class BeanManager implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T> T getBean(final Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public static <T> T getBean(final String beanName) {
        @SuppressWarnings("unchecked") final T bean = (T) context.getBean(beanName);
        return bean;
    }

    public static <T> Map<String, T> getBeans(final Class<T> requiredType) {
        return context.getBeansOfType(requiredType);
    }

    public static Map<String, Object> getBeansWithAnnotation(final Class<? extends Annotation> annotationType) {
        return context.getBeansWithAnnotation(annotationType);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        context = applicationContext;
    }

}
