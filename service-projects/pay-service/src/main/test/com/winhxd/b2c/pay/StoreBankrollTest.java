package com.winhxd.b2c.pay;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.winhxd.b2c.common.constant.TransfersChannelCodeTypeEnum;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.condition.UpdateStoreBankRollCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxBankVO;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxChangeVO;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.pay.service.PayService;
import com.winhxd.b2c.pay.weixin.base.config.PayConfig;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersQueryForWxBankDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersQueryForWxBankResponseDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersQueryForWxChangeDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayUtil;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.impl.WXPayApiImpl;
import com.winhxd.b2c.pay.weixin.service.WXTransfersService;
import com.winhxd.b2c.pay.weixin.util.BeanAndXmlUtil;

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
    

    @Test
    public void changeStoreBandroll(){
    	UpdateStoreBankRollCondition condition=new UpdateStoreBankRollCondition();
    	condition.setStoreId(999L);
    	condition.setMoney(BigDecimal.valueOf(2.56));
    	condition.setType(1);
    	condition.setMoneyType((short)1);
    	condition.setOrderNo("123qwe11");
    	condition.setWithdrawalsNo("12345wer12");
    	payService.updateStoreBankroll(condition);
    }
    @Test
    public void testValue(){
        System.out.println(payConfig.getAppID());
    }

}
