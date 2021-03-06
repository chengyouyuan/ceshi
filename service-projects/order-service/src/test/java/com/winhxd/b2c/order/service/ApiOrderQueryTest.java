package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.AllOrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCallbackCondition;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.OrderServiceApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author pangjianhua
 * @date 2018/8/13 17:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
public class ApiOrderQueryTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private OrderServiceClient serviceClient;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testQueryCustomerOrderList() throws Exception {

        AllOrderQueryByCustomerCondition condition = new AllOrderQueryByCustomerCondition();
        condition.setPageNo(1);
        condition.setPageSize(10);
        String requestJson = JsonUtil.toJSONString(condition);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api-order/order/4010/v1/orderListByCustomer").contentType(MediaType.APPLICATION_JSON).content(requestJson)
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();

        System.out.println(mvcResult.getResponse().getContentAsString());
        Assert.assertEquals("请求错误", 200, status);
    }

    @Test
    public void testRefundCallback() {
        OrderRefundCallbackCondition condition = new OrderRefundCallbackCondition();
        condition.setOrderNo("C18082009607365289");
        ResponseResult<Boolean> result = this.serviceClient.updateOrderRefundCallback(condition);
        System.out.println(result.getData());
    }
}
