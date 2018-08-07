package com.winhxd.b2c.order.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wangbin
 * @date 2018/8/7 15:28
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderInfoConvertAnnotation {
    String type() default "1";
    boolean queryCustomerInfo() default false;

    boolean queryStoreInfo() default false;
}
