package com.winhxd.b2c.admin.common.security.annotation;

import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;

import java.lang.annotation.*;

/**
 * 指定需要验证的权限
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPermission {
    PermissionEnum[] value() default {};
}
