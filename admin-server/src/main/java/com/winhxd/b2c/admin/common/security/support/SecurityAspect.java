package com.winhxd.b2c.admin.common.security.support;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
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
        UserInfo userInfo = UserManager.getCurrentUser();
        if(null == userInfo){
            // 登录凭证无效
            return new ResponseResult(BusinessCode.CODE_1002);
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 用户权限列表
        // userInfo.getPermissions()返回数据例：["ORDER_MANAGEMENT","SYSTEM_MANAGEMENT"]
        HashSet<PermissionEnum> hashSet = new HashSet<>(userInfo.getPermissions().size()+1);
        hashSet.add(PermissionEnum.AUTHENTICATED);
        for(String permission : userInfo.getPermissions()){
            hashSet.add(PermissionEnum.valueOf(permission));
        }

        boolean has = securityConfig.hasPermission(method, hashSet);
        if (has) {
            return joinPoint.proceed();
        } else {
            // 权限不足
            return new ResponseResult(BusinessCode.CODE_1003);
        }
    }
}
