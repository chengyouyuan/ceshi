package com.winhxd.b2c.local;

import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxChangeVO;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersForWxChangeDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersQueryForWxChangeDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayUtil;
import com.winhxd.b2c.pay.weixin.model.PayTransfers;
import com.winhxd.b2c.pay.weixin.util.BeanAndXmlUtil;

import java.math.BigDecimal;

/**
 * Main
 *
 * @Author yindanqing
 * @Date 2018/8/21 1:19
 * @Description:
 */
public class Main {

    public static void main(String[] args) throws Exception {
        testTransfersToChange();
    }

    private static void testTransfersToChange() throws Exception {
        //武富赟openID
        String openID = "ofTZZ5EVZ9WVpxbhVNPzvSVf8_KQ";
        PayTransfersForWxChangeDTO forWxChangeDTO = new PayTransfersForWxChangeDTO();
        forWxChangeDTO.setMchAppid(Constant.MCH_APP_ID);
        forWxChangeDTO.setMchid(Constant.MCH_ID);
        //forWxChangeDTO.setDeviceInfo("");
        forWxChangeDTO.setNonceStr(WXPayUtil.generateNonceStr());
        forWxChangeDTO.setPartnerTradeNo("T18082010527374800");
        //openID在这里不需要主动获取
        forWxChangeDTO.setOpenid(openID);
        forWxChangeDTO.setCheckName("FORCE_CHECK");
        forWxChangeDTO.setReUserName("武富赟");
        forWxChangeDTO.setAmount(30);
        forWxChangeDTO.setDesc("研发用户提现demo.");
        forWxChangeDTO.setSpbillCreateIp("106.38.97.194");
        //处理签名
        forWxChangeDTO.setSign(WXPayUtil.generateSignature(BeanAndXmlUtil.beanToSortedMap(forWxChangeDTO), Constant.KEY));
        System.out.println(Interface.transfersToChange(BeanAndXmlUtil.beanToSortedMap(forWxChangeDTO)));
    }

    /**
     * 查询转账到零钱
     * @throws Exception
     */
    private static void testQueryToChange() throws Exception {
        PayTransfersQueryForWxChangeDTO queryForWxChangeDTO = new PayTransfersQueryForWxChangeDTO();
        queryForWxChangeDTO.setMchId(Constant.MCH_ID);
        queryForWxChangeDTO.setAppid(Constant.APP_ID);
        queryForWxChangeDTO.setPartnerTradeNo("T18082010527374800");
        queryForWxChangeDTO.setNonceStr(WXPayUtil.generateNonceStr());
        //处理签名
        queryForWxChangeDTO.setSign(WXPayUtil.generateSignature(BeanAndXmlUtil.beanToSortedMap(queryForWxChangeDTO), Constant.KEY));
        //返参
        System.out.println(Interface.queryTransferToChange(BeanAndXmlUtil.beanToSortedMap(queryForWxChangeDTO)));
    }
}
