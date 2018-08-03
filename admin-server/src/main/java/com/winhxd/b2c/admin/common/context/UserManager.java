package com.winhxd.b2c.admin.common.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winhxd.b2c.admin.module.system.constant.Constant;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lixiaodong
 */
@Component
public class UserManager implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
    private static Cache cache;

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static UserInfo getCurrentUser() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Cookie[] requestCookies = request.getCookies();
        if(null != requestCookies){
            for(Cookie cookie : requestCookies){
                if(cookie.getName().equals(Constant.TOKEN_NAME)){
                    String token = cookie.getValue();

                    String json = cache.get(CacheName.CACHE_KEY_USER_TOKEN + token);
                    if(StringUtils.isNotBlank(json)){
                        try {
                            UserInfo userInfo = new ObjectMapper().readValue(json,UserInfo.class);
                            return userInfo;
                        } catch (IOException e) {
                            logger.error("转换用户信息失败{}", json, e);
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        cache = applicationContext.getBean(Cache.class);
    }
}
