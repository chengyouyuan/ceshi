package test;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.winhxd.b2c.common.domain.common.inputmodel.DateInterval;
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
    public void testListOrder4Management() throws ParseException {
        OrderInfoQuery4ManagementCondition condition = new OrderInfoQuery4ManagementCondition();
        condition.setCustomerId(0L);
        DateInterval dateInterval = new DateInterval();
        dateInterval.setStart(DateUtils.parseDate("2018-05-15 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        condition.setValuationType((short)2);
        condition.setDateInterval(dateInterval);
        System.out.println(JsonUtil
                .toJSONString(orderQueryService.listOrder4Management(condition)));
    }
}
