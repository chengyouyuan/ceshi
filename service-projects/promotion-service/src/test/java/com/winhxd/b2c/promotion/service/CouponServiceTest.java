package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.feign.promotion.CouponActivityServiceClient;
import com.winhxd.b2c.common.util.DateDealUtils;
import com.winhxd.b2c.promotion.PromotionServiceApplication;
import com.winhxd.b2c.promotion.common.TestUtils;
import io.swagger.models.auth.In;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: sunwenwu
 * @Date: 2018/10/19 09：35
 * @Description:
 */
@RunWith(SpringRunner.class)
//@SpringBootTest(classes = PromotionServiceApplication.class)
@SpringBootTest
public class CouponServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CouponActivityServiceClient couponActivityServiceClient;

    @Autowired
    private CouponPushService couponPushService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    /**
     * 校验用户是否有可领取的优惠券
     */
    @Test
    public void checkCouponsAvailableTest() throws Exception {
        long customerId = 20L;
        boolean availableCoupon = couponPushService.getAvailableCoupon(customerId);
        System.out.println("checkCouponsAvailableTest----------------result:"+availableCoupon);
    }


    @Test
    public void pushCouponsTest() {
        CouponActivityAddCondition condition = getCouponActivityAddCondition("TQ",(short) 2,3,2);
        ResponseResult<Integer> integerResponseResult = couponActivityServiceClient.addCouponActivity(condition);
        System.out.println("pushCouponsTest--------------------result:"+integerResponseResult.getData());

    }



    /**
     *
     * @param couponCode TQ：推券   LQ：领券
     * @param couponType 优惠券类型 1新用户注册 2老用户活动
     * @param diffDays 活动天数  默认开始时间为当前时间
     * @param storeOrCustomer 活动参与维度  1门店  2用户
     * @return
     */
    public CouponActivityAddCondition getCouponActivityAddCondition(String couponCode, short couponType, Integer diffDays,Integer storeOrCustomer){
        List<CustomerUser> couponActivityCustomerList = new ArrayList<>();
        long [] customerIDs = {20L,21L};//用户ID集合
        CouponActivityAddCondition condition = new CouponActivityAddCondition();
        condition.setCreatedBy(1L);
        condition.setCreatedByName("admin");
        condition.setExolian("test");
        condition.setRemarks("test");
        condition.setActivityStatus((short)1);//活动状态1开启2停止
        condition.setCouponType(couponType);//优惠券类型 1新用户注册 2老用户活动
        condition.setActivityStart(new Date());
        condition.setActivityEnd(DateDealUtils.getEndDate(new Date(),diffDays));
        condition.setCouponActivityCustomerList(couponActivityCustomerList);
        //推券
        if("TQ".equals(couponCode)){
            condition.setCode("TQ_"+this.getTimeStr()+"_"+(int)((Math.random()*9+1)*10000));
            condition.setType((short)2);


            //指定用户的情况
            if(CouponActivityEnum.PUSH_COUPON.getCode() == condition.getType() && condition.getCouponType() == CouponActivityEnum.OLD_USER.getCode() && storeOrCustomer==2){
                for (long cid:customerIDs){
                    CustomerUser customerUser = new CustomerUser();
                    customerUser.setCustomerId(cid);
                    couponActivityCustomerList.add(customerUser);
                }
            }
        } else {//领券
            condition.setCode("LQ_"+this.getTimeStr()+"_"+(int)((Math.random()*9+1)*10000));
            condition.setType((short)1);

        }

        return condition;
    }

    public String getTimeStr(){
        Calendar cal = Calendar.getInstance();
        String timw = cal.get(Calendar.YEAR)+""
                +(cal.get(Calendar.MONTH)+1)+""
                +cal.get(Calendar.DAY_OF_MONTH)+""
                +cal.get(Calendar.HOUR_OF_DAY)+""
                +cal.get(Calendar.MINUTE)+""
                +cal.get(Calendar.SECOND)+"";
        return timw;
    }

}
