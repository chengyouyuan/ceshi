package com.winhxd.b2c.common.mq;

import java.lang.annotation.*;

/**
 * 仅支持一个参数为String的方法
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StringMessageListener {
    MQHandler value();

    /**
     * n-m
     */
    String concurrency() default "";
}