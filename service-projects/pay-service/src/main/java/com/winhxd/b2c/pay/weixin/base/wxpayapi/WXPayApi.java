package com.winhxd.b2c.pay.weixin.base.wxpayapi;

import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderResponseDTO;

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
    public PayPreOrderResponseDTO unifiedOrder(PayPreOrderDTO payPreOrderDTO);

}
