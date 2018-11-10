package com.winhxd.b2c.pay.service;

import com.winhxd.b2c.common.constant.TransfersChannelCodeTypeEnum;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxBankVO;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxChangeVO;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.pay.weixin.base.config.PayConfig;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersQueryForWxBankDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersQueryForWxBankResponseDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersQueryForWxChangeDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayUtil;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.impl.WXPayApiImpl;
import com.winhxd.b2c.pay.weixin.service.WXTransfersService;
import com.winhxd.b2c.pay.weixin.util.BeanAndXmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * TransfersTest
 *
 * @Author yindanqing
 * @Date 2018/8/18 14:47
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TransfersTest {

    @Autowired
    private PayConfig payConfig;

    @Autowired
    private WXTransfersService wxTransfersService;

    @Autowired
    private WXPayApi wxPayApi = new WXPayApiImpl();

    /**
     * 转账到微信零钱
     */
    @Test
    public void testTransfersToChange(){
        //这个openId要是能提现成功就代表配置对了。
        String openID = "oRG410ZVY0fekVjjyoFV-U1w-Qnc";
        PayTransfersToWxChangeCondition changeCondition = new PayTransfersToWxChangeCondition();
        changeCondition.setPartnerTradeNo("T18082519817485615");
        changeCondition.setAccountId(openID);
        changeCondition.setAccountName("大漠");
        changeCondition.setTotalAmount(new BigDecimal("0.15"));
        changeCondition.setDesc("研发用户提现demo.");
        changeCondition.setSpbillCreateIp("106.38.97.194");
        changeCondition.setOperaterID("8");
        PayTransfersToWxChangeVO toWxChangeVO = wxTransfersService.transfersToChange(changeCondition);
        System.out.println(JsonUtil.toJSONString(toWxChangeVO));
    }

    /**
     * 查询转账到微信零钱
     * @throws Exception
     */
    @Test
    public void testQueryToChange() throws Exception {
        PayTransfersQueryForWxChangeDTO queryForWxChangeDTO = new PayTransfersQueryForWxChangeDTO();
        queryForWxChangeDTO.setMchId(payConfig.getMchID());
        queryForWxChangeDTO.setAppid(payConfig.getAppID());
        queryForWxChangeDTO.setPartnerTradeNo("T18082010527374800");
        queryForWxChangeDTO.setNonceStr(WXPayUtil.generateNonceStr());
        //处理签名
        queryForWxChangeDTO.setSign(WXPayUtil.generateSignature(BeanAndXmlUtil.beanToSortedMap(queryForWxChangeDTO), payConfig.getKey()));
        //返参
        System.out.println(wxPayApi.queryTransferToChange(BeanAndXmlUtil.beanToSortedMap(queryForWxChangeDTO)));
    }

    /**
     * 转账到微信银行卡
     */
    @Test
    public void testTransfersToBank(){
        PayTransfersToWxBankCondition toWxBankCondition = new PayTransfersToWxBankCondition();
        toWxBankCondition.setPartnerTradeNo("T18081717643679377");
        toWxBankCondition.setAccount("6217000210004907167");
        toWxBankCondition.setAccountName("李中华");
        toWxBankCondition.setChannelCode(TransfersChannelCodeTypeEnum.CCB);
        toWxBankCondition.setTotalAmount(new BigDecimal("1.00"));
        toWxBankCondition.setDesc("研发用户提现demo.");
        toWxBankCondition.setOperaterID("8");
        PayTransfersToWxBankVO toWxBankVO = wxTransfersService.transfersToBank(toWxBankCondition);
        System.out.println(JsonUtil.toJSONString(toWxBankVO));
    }

    /**
     * 查询转账到微信银行卡
     * @throws Exception
     */
    @Test
    public void testQueryToBank() throws Exception {
        PayTransfersQueryForWxBankResponseDTO queryForWxBankResponseDTO = new PayTransfersQueryForWxBankResponseDTO();
        //请求查询接口参数
        PayTransfersQueryForWxBankDTO queryForWxBankDTO = new PayTransfersQueryForWxBankDTO();
        queryForWxBankDTO.setMchId(payConfig.getMchID());
        queryForWxBankDTO.setPartnerTradeNo("T18082815813873658");
        queryForWxBankDTO.setNonceStr(WXPayUtil.generateNonceStr());
        //处理签名
        queryForWxBankDTO.setSign(WXPayUtil.generateSignature(BeanAndXmlUtil.beanToSortedMap(queryForWxBankDTO), payConfig.getKey()));
        //返参
        String resultXml = wxPayApi.queryTransferToBank(BeanAndXmlUtil.beanToSortedMap(queryForWxBankDTO));
        if(StringUtils.isNotBlank(resultXml)){
            queryForWxBankResponseDTO = BeanAndXmlUtil.xml2Bean(resultXml, PayTransfersQueryForWxBankResponseDTO.class);
        }        //返参
        System.out.println(JsonUtil.toJSONString(queryForWxBankResponseDTO));
    }

    @Test
    public void testValue(){
        System.out.println(payConfig.getAppID());
    }

    @Test
    public void testValues(){
        System.out.println("1");
    }
}
