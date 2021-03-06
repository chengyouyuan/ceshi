package com.winhxd.b2c.promotion.common;

import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;

import java.lang.reflect.Field;

/**
 * @author: sunwenwu
 * @Date: 2018/10/19 13：19
 * @Description:
 */
public class TestUtils {

    /**
     * 设置当前线程用户
     * @param customerId 用户ID
     * @throws Exception
     */
    public static void setCurrCustomer(Long customerId) throws Exception{
        ThreadLocal<CustomerUser> currentCustomerUser = new ThreadLocal<>();
        CustomerUser cu =  new CustomerUser();
        cu.setCustomerId(customerId);
        currentCustomerUser.set(cu);
        Class<UserContext> userContextClass = UserContext.class;
        Field f = userContextClass.getDeclaredField("currentCustomerUser");
        f.setAccessible(true);
        f.set(userContextClass,currentCustomerUser);
    }
}
