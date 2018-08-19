package com.winhxd.b2c.pay.weixin.base.wxpayapi;

import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderResponseDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundResponseDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants.SignType;

public interface WXPayApi {
	
    /**
     * 作用：统一下单
     * 场景：公共号支付、扫码支付、APP支付
     * @author mahongliang
     * @date  2018年8月18日 下午1:35:43
     * @Description 
     * @param payPreOrderDTO
     * @return
     */
    PayPreOrderResponseDTO unifiedOrder(PayPreOrderDTO payPreOrderDTO);
    
    /**
     * 微信统一签名方法
     * @author mahongliang
     * @date  2018年8月18日 下午6:26:47
     * @Description 
     * @param obj
     * @return
     */
    String generateSign(Object obj);
    
    /**
     * 微信统一签名方法
     * @author mahongliang
     * @date  2018年8月19日 下午12:24:05
     * @Description 
     * @param obj
     * @param signType
     * @return
     */
    String generateSign(Object obj, SignType signType);


    /**
     * 作用：申请退款<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    PayRefundResponseDTO refundOder(PayRefundDTO payRefundDTO);

}
