package com.winhxd.b2c.admin.common.security.annotation;


import com.winhxd.b2c.common.domain.system.security.enums.MenuEnum;

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
    MenuEnum[] value();
}
