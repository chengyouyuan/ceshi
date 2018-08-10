package com.winhxd.b2c.common.mq;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StringMessageListener {
    MessageQueueHandler value();

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
     *
     * @return
     */
    String concurrency() default "";

//    String autoStartup() default "";
}