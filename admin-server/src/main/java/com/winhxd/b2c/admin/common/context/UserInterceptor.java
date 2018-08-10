package com.winhxd.b2c.admin.common.context;

import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.context.support.ContextHelper;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
//        AdminUser adminUser = new AdminUser();
//        adminUser.setAccount("userAccount");
//        adminUser.setId(250L);
//        adminUser.setUsername("管理员");
//        requestTemplate.header(UserContext.HEADER_USER_ADMIN, ContextHelper.getHeaderJsonString(adminUser));

        UserInfo currentUser = UserManager.getCurrentUser();
        if (currentUser != null) {
            AdminUser adminUser = new AdminUser();
            adminUser.setAccount(currentUser.getAccount());
            adminUser.setId(currentUser.getId());
            adminUser.setUsername(currentUser.getUsername());
            requestTemplate.header(UserContext.HEADER_USER_ADMIN, ContextHelper.getHeaderJsonString(adminUser));
        }
    }
}
