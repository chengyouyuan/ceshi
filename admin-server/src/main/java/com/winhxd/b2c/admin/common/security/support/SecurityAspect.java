package com.winhxd.b2c.admin.common.security.support;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.i18n.MessageHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @author lixiaodong
 */
@Component
@Aspect
public class SecurityAspect {
    @Autowired
    private SecurityConfig securityConfig;

    @Around(value = "@annotation(com.winhxd.b2c.admin.common.security.annotation.CheckPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
//        HashSet<PermissionEnum> hashSet = new HashSet<>(Arrays.asList(PermissionEnum.SYSTEM_MANAGEMENT, PermissionEnum.ORDER_MANAGEMENT, PermissionEnum.ORDER_MANAGEMENT_EDIT));
        HashSet<PermissionEnum> hashSet = new HashSet<>(Arrays.asList(PermissionEnum.SYSTEM_MANAGEMENT, PermissionEnum.ORDER_MANAGEMENT));
        boolean has = securityConfig.hasPermission(method, hashSet);
        if (has) {
            return joinPoint.proceed();
        } else {
            ResponseResult responseResult = new ResponseResult();
            responseResult.setCode(BusinessCode.CODE_1002);
            return responseResult;
        }
    }
}
