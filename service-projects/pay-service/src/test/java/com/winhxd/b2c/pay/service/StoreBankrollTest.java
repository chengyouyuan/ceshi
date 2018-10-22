package com.winhxd.b2c.pay.service;

import com.winhxd.b2c.common.domain.pay.condition.UpdateStoreBankRollCondition;
import com.winhxd.b2c.common.domain.pay.model.PayOrderPayment;
import com.winhxd.b2c.pay.dao.PayOrderPaymentMapper;
import com.winhxd.b2c.pay.weixin.base.config.PayConfig;
import com.winhxd.b2c.pay.weixin.dao.PayBillMapper;
import com.winhxd.b2c.pay.weixin.model.PayBill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    
    @Autowired
    private PayBillMapper payBillMapper;
    
    @Autowired
    private PayOrderPaymentMapper payOrderPaymentMapper;

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
    public void updatePayment(){
    	//测试时需要修改selectByOutOrderNo 的sql  去掉参数
    	List<PayBill> list=payBillMapper.selectByOutOrderNo("");
    	for (PayBill condition : list) {
    		PayOrderPayment payOrderPayment=new PayOrderPayment();
    		payOrderPayment.setOrderTransactionNo(condition.getOutTradeNo());
    		payOrderPayment.setCallbackDate(new Date());
    		payOrderPayment.setUpdated(new Date());
    		payOrderPayment.setTimeEnd(condition.getTimeEnd());
    		payOrderPayment.setCallbackStatus(condition.getStatus());
    		payOrderPayment.setCallbackErrorCode(condition.getErrorCode());
    		payOrderPayment.setCallbackErrorReason(condition.getErrorMessage());
    		payOrderPayment.setTransactionId(condition.getTransactionId());
    		payOrderPayment.setCallbackMoney(condition.getCallbackTotalAmount());
    		payOrderPayment.setAppid(condition.getAppid());
    		payOrderPayment.setAttach(condition.getAttach());
    		payOrderPayment.setBody(condition.getBody());
    		payOrderPayment.setDetail(condition.getDetail());
    		payOrderPayment.setDeviceInfo(condition.getDeviceInfo());
    		payOrderPayment.setMchId(condition.getMchId());
    		payOrderPayment.setNonceStr(condition.getNonceStr());
    		payOrderPayment.setTimeStart(condition.getTimeStart());
    		payOrderPayment.setTimeExpire(condition.getTimeExpire());
    		int insertResult=payOrderPaymentMapper.updateByOrderTransactionNoSelective(payOrderPayment);
		}
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


	@Test
	public void testEmail() {
		System.out.print("^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$");
	}
}
