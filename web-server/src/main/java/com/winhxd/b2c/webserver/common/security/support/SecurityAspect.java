package com.winhxd.b2c.webserver.common.security.support;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.RestResult;
import com.winhxd.b2c.common.i18n.MessageHelper;
import com.winhxd.b2c.webserver.common.security.Permission;
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
    @Autowired
    private MessageHelper messageHelper;

    @Around(value = "@annotation(com.winhxd.b2c.webserver.common.security.annotation.CheckPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
//        HashSet<Permission> hashSet = new HashSet<>(Arrays.asList(Permission.SYSTEM_MANAGEMENT, Permission.ORDER_MANAGEMENT, Permission.ORDER_MANAGEMENT_EDIT));
        HashSet<Permission> hashSet = new HashSet<>(Arrays.asList(Permission.SYSTEM_MANAGEMENT, Permission.ORDER_MANAGEMENT));
        boolean has = securityConfig.hasPermission(method, hashSet);
        if (has) {
            return joinPoint.proceed();
        } else {
            RestResult restResult = new RestResult();
            restResult.setCode(BusinessCode.CODE_401);
            restResult.setMessage(messageHelper.getMessage(String.valueOf(BusinessCode.CODE_401)));
            return restResult;
        }
    }
}
