package com.winhxd.b2c.common.context;

import brave.Span;
import brave.Tracer;
import com.winhxd.b2c.common.context.support.ContextHelper;
import com.winhxd.b2c.common.context.version.VersionContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;

/**
 * 用户数据上下文,用于获取当前后台管理员、门店用户、C端用户数据
 *
 * @author lixiaodong
 */
public class UserContext {

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

    public static void initContext(Tracer tracer) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String msVer = request.getHeader(VersionContext.HEADER_NAME);
        if (StringUtils.isNotBlank(msVer)) {
            VersionContext.setVersion(msVer);
        } else {
            VersionContext.clean();
        }

        Span span = tracer.currentSpan();

        Matcher matcher = ContextHelper.PATTERN_SERVICE_PATH.matcher(request.getServletPath());
        if (matcher.matches()) {
            span.tag(ContextHelper.TRACER_API_CODE, matcher.group(2));
        } else if ((matcher = ContextHelper.PATTERN_API_PATH.matcher(request.getServletPath())).matches()) {
            span.tag(ContextHelper.TRACER_API_CODE, matcher.group(3));
        }

        AdminUser adminUser = ContextHelper.getHeaderObject(request, ContextHelper.HEADER_USER_ADMIN, AdminUser.class);
        StoreUser storeUser = ContextHelper.getHeaderObject(request, ContextHelper.HEADER_USER_STORE, StoreUser.class);
        CustomerUser customerUser = ContextHelper.getHeaderObject(request, ContextHelper.HEADER_USER_CUSTOMER, CustomerUser.class);
        if (adminUser == null) {
            currentAdminUser.remove();
        } else {
            currentAdminUser.set(adminUser);
            if (StringUtils.isNotBlank(adminUser.getAccount())) {
                span.tag(ContextHelper.TRACER_API_USER, adminUser.getAccount());
            }
        }
        if (storeUser == null) {
            currentStoreUser.remove();
        } else {
            currentStoreUser.set(storeUser);
            if (storeUser.getStoreCustomerId() != null) {
                span.tag(ContextHelper.TRACER_API_STORE, storeUser.getStoreCustomerId().toString());
            }
        }
        if (customerUser == null) {
            currentCustomerUser.remove();
        } else {
            currentCustomerUser.set(customerUser);
            if (customerUser.getCustomerId() != null) {
                span.tag(ContextHelper.TRACER_API_CUSTOMER, customerUser.getCustomerId().toString());
            }
        }
    }

}
