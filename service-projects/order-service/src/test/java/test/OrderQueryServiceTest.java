package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.OrderServiceApplication;
import com.winhxd.b2c.order.service.OrderQueryService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
public class OrderQueryServiceTest {

    
    @Autowired
    private OrderQueryService orderQueryService;
    
    @Test
    public void testListOrder4Management() {
        OrderInfoQuery4ManagementCondition condition = new OrderInfoQuery4ManagementCondition();
        condition.setCustomerId(0L);
        System.out.println(JsonUtil
                .toJSONString(orderQueryService.listOrder4Management(condition)));
    }
}
