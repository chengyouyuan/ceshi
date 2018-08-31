package com.winhxd.b2c.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.winhxd.b2c.common.domain.pay.condition.UpdateStoreBankRollCondition;
import com.winhxd.b2c.pay.service.PayService;
import com.winhxd.b2c.pay.service.VerifyService;
import com.winhxd.b2c.pay.weixin.base.config.PayConfig;

/**
 * TransfersTest
 *
 * @Author yindanqing
 * @Date 2018/8/18 14:47
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreBankrollTest {

    @Autowired
    private PayConfig payConfig;

    
    @Autowired
    private PayService payService;
    
    @Autowired
    private VerifyService verifyService;

    @Test
    public void changeStoreBandroll(){
    	UpdateStoreBankRollCondition condition=new UpdateStoreBankRollCondition();
    	condition.setStoreId(999L);
    	condition.setMoney(BigDecimal.valueOf(1));
    	condition.setType(3);
    	condition.setMoneyType((short)1);
    	condition.setOrderNo("C18082918971371312");
    	condition.setWithdrawalsNo("12345wer12");
    	payService.updateStoreBankroll(condition);
    }
    @Test
    public void verifyService(){
    	List<Long> ids=new ArrayList<>();
    	ids.add(597L);
    	verifyService.verifyByAccountingDetail(ids,"aa",11L,"aa");
    }
    @Test
    public void testValue(){
        System.out.println(payConfig.getAppID());
    }

}
