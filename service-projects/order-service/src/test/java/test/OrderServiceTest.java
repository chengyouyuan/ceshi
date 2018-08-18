package test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.OrderNotifyMsg;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderConfirmCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderItemCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderPickupCondition;
import com.winhxd.b2c.common.domain.order.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.util.OrderUtil;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.common.mq.MQDestination;
import com.winhxd.b2c.common.mq.StringMessageSender;
import com.winhxd.b2c.common.mq.event.EventMessageSender;
import com.winhxd.b2c.common.mq.event.EventType;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.OrderServiceApplication;
import com.winhxd.b2c.order.dao.OrderInfoMapper;
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
    @Autowired
    private MessageServiceClient messageServiceClient;
    
    @Autowired
    private EventMessageSender eventMessageSender;
    

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);
    
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
    public void testOrderPayNotify() throws InterruptedException {
        orderService.orderPaySuccessNotify("C18081809917533139","123123123123");
        Thread.sleep(1000000L);
    }
    
    @Test
    public void testOrderConfirm4Store() throws InterruptedException {
        OrderConfirmCondition orderConfirmCondition = new OrderConfirmCondition();
        orderConfirmCondition.setOrderNo("C18081809917533139");
        orderConfirmCondition.setStoreId(12L);
        orderConfirmCondition.setOrderTotal(new BigDecimal("0.1"));
        orderService.orderConfirm4Store(orderConfirmCondition);
        Thread.sleep(1000000L);
    }
    
    @Test
    public void testOrderPickup4Store() throws InterruptedException {
        OrderPickupCondition orderPickupCondition = new OrderPickupCondition();
        orderPickupCondition.setOrderNo("C18081809917533139");
        orderPickupCondition.setStoreId(12L);
        orderPickupCondition.setPickupCode("6937");
        orderService.orderPickup4Store(orderPickupCondition);
        Thread.sleep(1000000L);
    }
    
    
    
    @Test
    public void testStringMessageSender() {
        stringMessageSender.send(MQDestination.ORDER_RECEIVE_TIMEOUT_DELAYED, "123", 10000);
        System.out.println("finished" + new Date());
    }
    
    @Test
    public void testEventMessageSender() {
        OrderInfo orderInfo = orderInfoMapper.selectByOrderNo("C18081621219186610");
        eventMessageSender.send(EventType.EVENT_CUSTOMER_ORDER_PAY_SUCCESS, orderInfo.getOrderNo(), orderInfo);
        System.out.println("finished" + new Date());
    }
    
    @Test
    public void orderNeedPickupSendMsg2Store(){
        String last4MobileNums = "3479";
        Long storeId = 12L;
        String storeMsg = MessageFormat.format(OrderNotifyMsg.WAIT_PICKUP_ORDER_NOTIFY_MSG_4_STORE, last4MobileNums);
        try {
            // 发送云信
            String createdBy= "";
            int expiration = 0;
            int msgType = 0;
            short pageType = 1;
            int audioType = 0;
            String treeCode = "treeCode";
            NeteaseMsgCondition neteaseMsgCondition = OrderUtil.genNeteaseMsgCondition(storeId, storeMsg, createdBy, expiration, msgType,
                    pageType, audioType, treeCode);
            if (messageServiceClient.sendNeteaseMsg(neteaseMsgCondition).getCode() != BusinessCode.CODE_OK) {
                throw new BusinessException(BusinessCode.CODE_1001);
            }
        } catch (Exception e) {
            logger.error("订单待提货给门店:storeId={},发送消息:{},失败", storeId, storeMsg);
            logger.error("订单待提货给门店发送消息失败：", e);
            throw e;
        }
    }
    
    @Test
    public void newOrderSendMsg2Store() throws InterruptedException{
        Thread a = new Thread(new Runnable() {
            
            @Override
            public void run() {
                OrderUtil.newOrderSendMsg2Store(messageServiceClient, 12L);
                
            }
        });
        a.setDaemon(false);
        a.start();
        a.join();
        Thread.sleep(1000000L);
    }
}
