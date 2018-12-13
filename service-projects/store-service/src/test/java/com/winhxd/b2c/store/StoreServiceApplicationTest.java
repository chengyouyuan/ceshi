package com.winhxd.b2c.store;

import com.winhxd.b2c.common.domain.system.login.condition.CustomerBindingStatusCondition;
import com.winhxd.b2c.store.service.StoreService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreServiceApplicationTest {

    @Autowired
    private StoreService storeService;

    @Test
    public void contextLoads() {
        new Thread(() -> System.out.print("hello"));
    }


    @Test
    public void testChangeBind() {
        List<CustomerBindingStatusCondition> conditions = new ArrayList<>();

        CustomerBindingStatusCondition condition = new CustomerBindingStatusCondition();
        condition.setCustomerId(52L);
        condition.setStoreId(93L);
        conditions.add(condition);

        CustomerBindingStatusCondition condition2 = new CustomerBindingStatusCondition();
        condition2.setCustomerId(19L);
        condition2.setStoreId(93L);
        conditions.add(condition2);

        storeService.changeBind(conditions);

    }
}
