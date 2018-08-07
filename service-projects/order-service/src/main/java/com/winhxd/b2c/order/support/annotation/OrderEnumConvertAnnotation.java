package com.winhxd.b2c.order.support.annotation;

import java.lang.annotation.*;

/**
 * @author pangjianhua
 * @date 2018/8/7 15:28
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OrderEnumConvertAnnotation {
    String type() default "1";
}
