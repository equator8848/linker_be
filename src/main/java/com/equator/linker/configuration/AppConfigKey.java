package com.equator.linker.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author libinkai
 * @date 2020/11/1 2:07 下午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppConfigKey {
    String value();

    String defaultValue() default "";
}
