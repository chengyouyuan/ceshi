package com.winhxd.b2c.common.context;

import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 用户数据上下文,用于获取当前后台管理员、门店用户、C端用户数据
 *
 * @author lixiaodong
 */
public class UserContext {
    public static final String HEADER_USER_ADMIN = "b2c-user-admin";
    public static final String HEADER_USER_STORE = "b2c-user-store";
    public static final String HEADER_USER_CUSTOMER = "b2c-user-customer";

    private static ThreadLocal<AdminUser> currentAdminUser = new ThreadLocal<>();
    private static ThreadLocal<StoreUser> currentStoreUser = new ThreadLocal<>();
    private static ThreadLocal<CustomerUser> currentCustomerUser = new ThreadLocal<>();

    public static AdminUser getCurrentAdminUser() {
        return currentAdminUser.get();
    }

    public static StoreUser getCurrentStoreUser() {
        return currentStoreUser.get();
    }

    public static CustomerUser getCurrentCustomerUser() {
        return currentCustomerUser.get();
    }

    public static void initContext() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        AdminUser adminUser = getHeaderObject(request, HEADER_USER_ADMIN, AdminUser.class);
        StoreUser storeUser = getHeaderObject(request, HEADER_USER_STORE, StoreUser.class);
        CustomerUser customerUser = getHeaderObject(request, HEADER_USER_CUSTOMER, CustomerUser.class);
        if (adminUser == null) {
            currentAdminUser.remove();
        } else {
            currentAdminUser.set(adminUser);
        }
        if (storeUser == null) {
            currentStoreUser.remove();
        } else {
            currentStoreUser.set(storeUser);
        }
        if (customerUser == null) {
            currentCustomerUser.remove();
        } else {
            currentCustomerUser.set(customerUser);
        }
    }

    private static <T> T getHeaderObject(HttpServletRequest request, String headerName, Class<T> clazz) {
        String header = request.getHeader(headerName);
        if (StringUtils.isBlank(header)) {
            return null;
        }
        try {
            String json = URLDecoder.decode(header, "utf-8");
            return JsonUtil.parseJSONObject(json, clazz);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
