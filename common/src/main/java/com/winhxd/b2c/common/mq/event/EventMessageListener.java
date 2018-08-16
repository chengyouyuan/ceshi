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

//    String id() default "";
//
//    String containerFactory() default "";

//    String[] queues() default {};
//
//    Queue[] queuesToDeclare() default {};

//    boolean exclusive() default false;

//    String priority() default "";

//    String admin() default "";

//    QueueBinding[] bindings() default {};

//    String group() default "";

//    String returnExceptions() default "";

//    String errorHandler() default "";

    /**
     * n-m
     */
    String concurrency() default "";

//    String autoStartup() default "";
}