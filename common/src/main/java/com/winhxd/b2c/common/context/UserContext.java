package com.winhxd.b2c.common.context;

import com.winhxd.b2c.common.context.support.ContextHelper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 获取当前后台管理员用户信息
     */
    public static AdminUser getCurrentAdminUser() {
        return currentAdminUser.get();
    }

    /**
     * 获取当前门店用户信息
     */
    public static StoreUser getCurrentStoreUser() {
        return currentStoreUser.get();
    }

    /**
     * 获取当前C端用户信息
     */
    public static CustomerUser getCurrentCustomerUser() {
        return currentCustomerUser.get();
    }

    public static void initContext() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        AdminUser adminUser = ContextHelper.getHeaderObject(request, HEADER_USER_ADMIN, AdminUser.class);
        StoreUser storeUser = ContextHelper.getHeaderObject(request, HEADER_USER_STORE, StoreUser.class);
        CustomerUser customerUser = ContextHelper.getHeaderObject(request, HEADER_USER_CUSTOMER, CustomerUser.class);
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

}
