package test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderItemCondition;
import com.winhxd.b2c.common.domain.order.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.order.util.OrderUtil;
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
    @Resource
    private Cache cache;
    
    @Test
    public void testGetStoreOrderSalesSummary() {
        //支付成功清空门店订单销量统计cache
//        System.out.println(cache.del(OrderUtil.getStoreOrderSalesSummaryKey(0L)));
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
    public void testListOrder4ManagementWithNoPage() {
        OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition = new OrderInfoQuery4ManagementCondition();
        infoQuery4ManagementCondition.setOrderNos(new String[]{"12312"});
        System.out.println(JsonUtil.toJSONString(orderQueryService.listOrder4ManagementWithNoPage(infoQuery4ManagementCondition)));
    }
    
    @Test
    public void testSubmitOrder() {
        List<OrderItemCondition> orderItemConditions = new ArrayList<>();
        OrderItemCondition itemCondition = new OrderItemCondition();
        itemCondition.setAmount(2);
        itemCondition.setSkuCode("12346496056002");
        itemCondition.setPrice(new BigDecimal("234"));
        OrderItemCondition itemCondition1 = new OrderItemCondition();
        itemCondition1.setAmount(2);
        itemCondition1.setSkuCode("12346496056001");
        itemCondition1.setPrice(new BigDecimal("20"));
        orderItemConditions.add(itemCondition);
        orderItemConditions.add(itemCondition1);
        OrderCreateCondition createCondition = new OrderCreateCondition();
        createCondition.setCustomerId(1L);
        createCondition.setStoreId(12L);
        createCondition.setPayType(PayTypeEnum.WECHAT_ONLINE_PAYMENT.getTypeCode());
        createCondition.setOrderItemConditions(orderItemConditions);
        
        System.out.println(orderService.submitOrder(createCondition));
    }
    
    @Test
    public void testOrderPayNotify() {
        orderService.orderPaySuccessNotify("C18080417612761795","asdfasdfasd");
    }
    
    @Test
    public void testStringMessageSender() {
        stringMessageSender.send(MQDestination.ORDER_RECEIVE_TIMEOUT_DELAYED, "123", 10000);
        System.out.println("finished" + new Date());
    }
}
