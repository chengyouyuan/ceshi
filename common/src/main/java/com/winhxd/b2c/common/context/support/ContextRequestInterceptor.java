package com.winhxd.b2c.common.context.support;

import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.context.version.VersionContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

/**
 * 微服务间上下文传递
 *
 * @author lixiaodong
 */
public class ContextRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        AdminUser adminUser = UserContext.getCurrentAdminUser();
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        StoreUser storeUser = UserContext.getCurrentStoreUser();
        if (adminUser != null) {
            requestTemplate.header(ContextHelper.HEADER_USER_ADMIN, ContextHelper.getHeaderJsonString(adminUser));
        }
        if (customerUser != null) {
            requestTemplate.header(ContextHelper.HEADER_USER_CUSTOMER, ContextHelper.getHeaderJsonString(customerUser));
        }
        if (storeUser != null) {
            requestTemplate.header(ContextHelper.HEADER_USER_STORE, ContextHelper.getHeaderJsonString(storeUser));
        }

        String acceptLanguage = UserContext.getAcceptLanguage();
        if (StringUtils.isNotBlank(acceptLanguage)) {
            requestTemplate.header(HttpHeaders.ACCEPT_LANGUAGE, acceptLanguage);
        }

        String msVer = VersionContext.getVersion();
        if (StringUtils.isNotBlank(msVer)) {
            requestTemplate.header(VersionContext.HEADER_NAME, msVer);
        }
    }
}
