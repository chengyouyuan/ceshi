package com.winhxd.b2c.admin.common.context;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.constant.SysConstant;
import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.context.version.VersionContext;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lixiaodong
 */
public class UserManager {
    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
    private static ThreadLocal<UserInfo> currentUser = new InheritableThreadLocal<>();

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static UserInfo getCurrentUser() {
        return currentUser.get();
    }

    public static void initUser(HttpServletRequest request, Cache cache) {
        VersionContext.clean();
        String msVer = request.getHeader(VersionContext.HEADER_NAME);
        if (StringUtils.isNotBlank(msVer)) {
            VersionContext.setVersion(msVer);
        }
        Cookie[] requestCookies = request.getCookies();
        if (null != requestCookies) {
            for (Cookie cookie : requestCookies) {
                if (cookie.getName().equals(SysConstant.TOKEN_NAME)) {
                    String token = cookie.getValue();
                    if (StringUtils.isNotBlank(token)) {
                        String cacheKey = CacheName.CACHE_KEY_USER_TOKEN + token;
                        String json = cache.get(cacheKey);
                        if (StringUtils.isNotBlank(json)) {
                            UserInfo userInfo = JsonUtil.parseJSONObject(json, UserInfo.class);
                            cache.expire(cacheKey, 30 * 60);
                            currentUser.set(userInfo);

                            AdminUser adminUser = new AdminUser();
                            adminUser.setAccount(userInfo.getAccount());
                            adminUser.setId(userInfo.getId());
                            adminUser.setUsername(userInfo.getUsername());
                            UserContext.setCurrentAdminUser(adminUser);

                            if (StringUtils.isBlank(msVer) && StringUtils.isNotBlank(userInfo.getMsVer())) {
                                VersionContext.setVersion(userInfo.getMsVer());
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}
