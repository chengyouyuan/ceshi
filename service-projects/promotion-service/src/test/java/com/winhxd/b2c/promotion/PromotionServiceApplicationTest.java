package com.winhxd.b2c.promotion;

import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.promotion.vo.CouponPushVO;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.promotion.common.TestUtils;
import com.winhxd.b2c.promotion.service.CouponPushService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PromotionServiceApplicationTest {

    @Autowired
    private CouponPushService couponPushService;



    @Test
    public void contextLoads() {
    }

    /**
     * 指定推送优惠券测试
     */
    @Test
    public void getSpecifiedPushCoupon() throws Exception {
        TestUtils.setCurrCustomer(25L);
        List<CouponPushVO> result = couponPushService.getSpecifiedPushCoupon(UserContext.getCurrentCustomerUser());
        System.err.println("================>" + JsonUtil.toJSONString(result));
    }
}
