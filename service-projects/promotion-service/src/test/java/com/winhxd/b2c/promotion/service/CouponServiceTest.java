package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityStoreCustomer;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityTemplate;
import com.winhxd.b2c.common.feign.promotion.CouponActivityServiceClient;
import com.winhxd.b2c.common.util.DateUtil;
import com.winhxd.b2c.promotion.PromotionServiceApplication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum.PUSH_COUPON;

/**
 * @author: sunwenwu
 * @Date: 2018/10/19 09：35
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PromotionServiceApplication.class)
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
        CouponActivityAddCondition condition = getCouponActivityAddCondition(CouponActivityEnum.PUSH_COUPON,(short) 2,3,2,7);
        System.out.println(condition.toString());
        ResponseResult<Integer> result = couponActivityServiceClient.addCouponActivity(condition);
        System.out.println("pushCouponsTest--------------------result:"+result.getCode());
        Assert.assertEquals(BusinessCode.CODE_OK,result.getCode());
    }



    /**
     *
     * @param type PUSH_COUPON：推券   PULL_COUPON：领券
     * @param couponType 优惠券类型 1新用户注册 2老用户活动
     * @param diffDays 活动天数  默认开始时间为当前时间
     * @param storeOrCustomer 活动参与维度  1门店  2用户
     * @param effectDays 优惠券有效天数
     * @return
     */
    public CouponActivityAddCondition getCouponActivityAddCondition(CouponActivityEnum type, short couponType, Integer diffDays,Integer storeOrCustomer,Integer effectDays){
        List<CustomerUser> couponActivityCustomerList = new ArrayList<>();
        long [] customerIDs = {20L,21L};//用户ID集合【storeOrCustomer 为2生效】

        List<CouponActivityStoreCustomer> couponActivityStoreCustomerList = new ArrayList<>();
        long [] storeIDs = {52L,70L};//门店ID集合【storeOrCustomer 为1生效】

        List<CouponActivityTemplate> couponActivityTemplateList = new ArrayList<>();
        long [] templateIDs = {1L,3L};//优惠券模板ID集合


        CouponActivityAddCondition condition = new CouponActivityAddCondition();
        condition.setType(type.getCode());
        condition.setCreatedBy(1L);
        condition.setCreatedByName("admin");
        condition.setName(type.getDesc()+"_test");
        condition.setExolian(type.getDesc()+"_test");
        condition.setRemarks(type.getDesc()+"_test");
        condition.setActivityStatus((short)1);//活动状态1开启2停止
        condition.setCouponType(couponType);//优惠券类型 1新用户注册 2老用户活动
        condition.setActivityStart(new Date());
        condition.setActivityEnd(DateUtil.getEndDate(new Date(),diffDays));
        condition.setCouponActivityCustomerList(couponActivityCustomerList);
        condition.setCouponActivityCustomerList(couponActivityCustomerList);
        condition.setCouponActivityTemplateList(couponActivityTemplateList);
        //推券
        if(CouponActivityEnum.PUSH_COUPON.getCode() == type.getCode()){
            pushCoupon(diffDays, storeOrCustomer, effectDays, couponActivityCustomerList, customerIDs, couponActivityStoreCustomerList, storeIDs, couponActivityTemplateList, templateIDs, condition);
        } else {//领券
            condition.setCode("LQ_"+this.getTimeStr()+"_"+(int)((Math.random()*9+1)*10000));
            condition.setType((short)1);

        }

        return condition;
    }

    private void pushCoupon(Integer diffDays, Integer storeOrCustomer, Integer effectDays, List<CustomerUser> couponActivityCustomerList, long[] customerIDs, List<CouponActivityStoreCustomer> couponActivityStoreCustomerList, long[] storeIDs, List<CouponActivityTemplate> couponActivityTemplateList, long[] templateIDs, CouponActivityAddCondition condition) {
        condition.setCode("TQ_"+this.getTimeStr()+"_"+(int)((Math.random()*9+1)*10000));
        condition.setType((short)2);

        for (long sid:storeIDs){
            CouponActivityStoreCustomer casc = new CouponActivityStoreCustomer();
            casc.setStoreId(sid);
            couponActivityStoreCustomerList.add(casc);
        }
        for (long tid:templateIDs){
            CouponActivityTemplate cat = new CouponActivityTemplate();
            if(effectDays != null && effectDays>0){
                cat.setEffectiveDays(effectDays);
            }else{
                cat.setStartTime(new Date());
                cat.setEndTime(DateUtil.getEndDate(new Date(),diffDays));
            }
            cat.setCouponNum(6);//每种优惠券总数
            cat.setSendNum(2);//没人领取数量
            cat.setTemplateId(tid);
            if(PUSH_COUPON.getCode() == condition.getType() && condition.getCouponType() == CouponActivityEnum.OLD_USER.getCode() && storeOrCustomer==1){
                cat.setCouponActivityStoreCustomerList(couponActivityStoreCustomerList);
            }
            couponActivityTemplateList.add(cat);
        }

        //指定用户的情况
        if(PUSH_COUPON.getCode() == condition.getType() && condition.getCouponType() == CouponActivityEnum.OLD_USER.getCode() && storeOrCustomer==2){
            for (long cid:customerIDs){
                CustomerUser customerUser = new CustomerUser();
                customerUser.setCustomerId(cid);
                couponActivityCustomerList.add(customerUser);
            }
        }
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
