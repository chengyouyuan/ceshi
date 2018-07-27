package com.winhxd.b2c.webserver.common.context;

import com.winhxd.b2c.common.domain.system.vo.UserInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lixiaodong
 */
public class UserManager {
    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static UserInfo getCurrentUser() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return null;
    }
}
