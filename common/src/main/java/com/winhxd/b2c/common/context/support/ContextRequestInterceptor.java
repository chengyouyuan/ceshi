package com.winhxd.b2c.common.context.support;

import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class ContextRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        AdminUser adminUser = UserContext.getCurrentAdminUser();
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        StoreUser storeUser = UserContext.getCurrentStoreUser();
        if (adminUser != null) {
            requestTemplate.header(UserContext.HEADER_USER_ADMIN, ContextHelper.getHeaderJsonString(adminUser));
        }
        if (customerUser != null) {
            requestTemplate.header(UserContext.HEADER_USER_CUSTOMER, ContextHelper.getHeaderJsonString(customerUser));
        }
        if (storeUser != null) {
            requestTemplate.header(UserContext.HEADER_USER_STORE, ContextHelper.getHeaderJsonString(storeUser));
        }
    }
}
