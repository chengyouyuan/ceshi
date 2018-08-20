package com.winhxd.b2c.common.mq.event;

import java.lang.annotation.*;

/**
 * 方法必须包含两个参数 <br/>
 * 第一个为eventId: {@link java.lang.String} <br/>
 * 第二个为eventObject: 对应{@link EventType}构造函数传入的{@code Class<?>}
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