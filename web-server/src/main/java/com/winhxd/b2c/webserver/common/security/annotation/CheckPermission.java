package com.winhxd.b2c.webserver.common.security.annotation;

import com.winhxd.b2c.webserver.common.security.Permission;

import java.lang.annotation.*;

/**
 * 指定需要验证的权限
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPermission {
    Permission[] value() default {};
}
