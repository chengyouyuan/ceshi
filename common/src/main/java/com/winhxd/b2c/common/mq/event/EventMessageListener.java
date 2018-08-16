package com.winhxd.b2c.common.mq.event;

import java.lang.annotation.*;

/**
 * 方法必须包含两个参数 <br/>
 * 第一个为eventId: {@link java.lang.String} <br/>
 * 第二个为eventObject: 对应{@link EventType}声明的class
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventMessageListener {
    EventTypeHandler value();

    /**
     * n-m
     */
    String concurrency() default "";
}