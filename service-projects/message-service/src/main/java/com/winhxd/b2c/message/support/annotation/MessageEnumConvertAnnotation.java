package com.winhxd.b2c.message.support.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: MessageEnumConvertAnnotation
 * @Description: TODO
 * @Author Jesse Fan
 * @Date 2018-08-14 19:07
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageEnumConvertAnnotation {
}
