package com.winhxd.b2c.common.context.support;

import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.util.JsonUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ContextRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        AdminUser adminUser = UserContext.getCurrentAdminUser();
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        StoreUser storeUser = UserContext.getCurrentStoreUser();
        if (adminUser != null) {
            requestTemplate.header(UserContext.HEADER_USER_ADMIN, getHeaderJsonString(adminUser));
        }
        if (customerUser != null) {
            requestTemplate.header(UserContext.HEADER_USER_CUSTOMER, getHeaderJsonString(customerUser));
        }
        if (storeUser != null) {
            requestTemplate.header(UserContext.HEADER_USER_STORE, getHeaderJsonString(storeUser));
        }
    }


    public static String getHeaderJsonString(Object obj) {
        String json = JsonUtil.toJSONString(obj);
        try {
            return URLEncoder.encode(json, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
