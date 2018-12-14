package com.winhxd.b2c.customer.Service;

import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.customer.model.CustomerUserInfo;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition;
import com.winhxd.b2c.customer.common.TestUtils;
import com.winhxd.b2c.customer.service.CustomerLoginService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private CustomerLoginService customerLoginService;

    /**
     * 添加数盟id 测试
     */
    @Test
    public void addDigitalUnionId() throws Exception {

        // 设置参数
        CustomerUserInfoCondition condition = new CustomerUserInfoCondition();
        condition.setDigitalUnionId("D2M2hUTVhrYk4xcVNodkNOYjZRaW8xK1plTU05aWxqemsX8D");

        // 获取当前用户
        CustomerUserInfo customerUserInfo = new CustomerUserInfo();
        TestUtils.setCurrCustomer(52L);
        CustomerUser currentCustomerUser = UserContext.getCurrentCustomerUser();

        // 添加数盟id
        customerUserInfo.setCustomerId(currentCustomerUser.getCustomerId());
        customerUserInfo.setDigitalUnionId(condition.getDigitalUnionId());
        customerLoginService.updateCustomerInfo(customerUserInfo);
    }
}
