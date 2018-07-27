package com.winhxd.b2c.webserver.common.security.annotation;

import com.winhxd.b2c.webserver.common.security.Menu;

import java.lang.annotation.*;

/**
 * 在Controller方法上指定菜单
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MenuAssign {
    /**
     * 管理菜单
     *
     * @return
     */
    Menu[] value();
}
