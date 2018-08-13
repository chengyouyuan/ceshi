package test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderItemCondition;
import com.winhxd.b2c.common.domain.order.enums.PayTypeEnum;
import com.winhxd.b2c.common.mq.MQDestination;
import com.winhxd.b2c.common.mq.StringMessageSender;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.OrderServiceApplication;
import com.winhxd.b2c.order.service.OrderQueryService;
import com.winhxd.b2c.order.service.OrderService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
public class OrderServiceTest {

    
    @Autowired
    private OrderQueryService orderQueryService;
    
    @Autowired
    private OrderService orderService;

    @Autowired
    private StringMessageSender stringMessageSender;
    
    @Test
    public void testGetStoreOrderSalesSummary() {
        long storeId = 0L;
        //查询当天数据
        //获取当天最后一秒
        long lastSecond = Timestamp.valueOf(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 23, 59, 59)).getTime();
        //获取当天开始第一秒
        long startSecond = Timestamp.valueOf(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0)).getTime();
        Date startDateTime = new Date(startSecond);
        Date endDateTime = new Date(lastSecond);
        System.out.println(JsonUtil.toJSONString(orderQueryService.getStoreOrderSalesSummary(0, startDateTime, endDateTime)));
    }
    
    @Test
    public void testSubmitOrder() {
        List<OrderItemCondition> orderItemConditions = new ArrayList<>();
        OrderItemCondition itemCondition = new OrderItemCondition();
        itemCondition.setAmount(2);
        itemCondition.setSkuCode("ssskkkuuuccc");
        orderItemConditions.add(itemCondition);
        OrderCreateCondition createCondition = new OrderCreateCondition();
        createCondition.setCustomerId(0L);
        createCondition.setStoreId(0L);
        createCondition.setPayType(PayTypeEnum.WECHAT_SCAN_CODE_PAYMENT.getTypeCode());
        createCondition.setPickupDateTime(new Date());
        createCondition.setOrderItemConditions(orderItemConditions);
        
        System.out.println(orderService.submitOrder(createCondition));
    }
    
    @Test
    public void testOrderPayNotify() {
        orderService.orderPaySuccessNotify("C18080417612761795");
    }
    
    @Test
    public void testStringMessageSender() {
        stringMessageSender.send(MQDestination.ORDER_RECEIVE_TIMEOUT_DELAYED, "123", 10000);
        System.out.println("finished" + new Date());
    }
}
