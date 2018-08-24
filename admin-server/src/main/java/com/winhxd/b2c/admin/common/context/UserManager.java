package com.winhxd.b2c.admin.common.context;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.constant.SysConstant;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lixiaodong
 */
@Component
public class UserManager implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    @Resource
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
        if (null != requestCookies) {
            for (Cookie cookie : requestCookies) {
                if (cookie.getName().equals(SysConstant.TOKEN_NAME)) {
                    String token = cookie.getValue();
                    String cacheKey = CacheName.CACHE_KEY_USER_TOKEN + token;

                    String json = cache.exists(cacheKey) ? cache.get(cacheKey) : null;
                    logger.info("根据token获取用户信息{} ---> {}", token, json);

                    if (StringUtils.isNotBlank(json)) {
                        UserInfo userInfo = JsonUtil.parseJSONObject(json, UserInfo.class);

                        // 重置会话过期时长
                        cache.setex(cacheKey, 30 * 60, json);
                        return userInfo;
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 删除某个用户的缓存
     *
     * @param userId
     */
    public static void delUserCache(Long userId) {
        String token = DigestUtils.md5DigestAsHex(userId.toString().getBytes());
        String cacheKey = CacheName.CACHE_KEY_USER_TOKEN + token;
        cache.del(cacheKey);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        cache = event.getApplicationContext().getBean(Cache.class);
    }
}
