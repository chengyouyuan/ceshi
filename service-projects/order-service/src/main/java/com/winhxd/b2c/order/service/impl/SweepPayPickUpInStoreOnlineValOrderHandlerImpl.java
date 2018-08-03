package com.winhxd.b2c.order.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.order.service.OrderHandler;

/**
 * 扫码支付线上计价自提订单处理接口
 * @author wangbin
 * @date  2018年8月3日 上午9:36:33
 * @version 
 */
@Service("SweepPayPickUpInStoreOnlineValOrderHandler")
public class SweepPayPickUpInStoreOnlineValOrderHandlerImpl  extends SweepPayPickUpInStoreOrderHandlerImpl implements OrderHandler {

    private static final Logger logger = LoggerFactory.getLogger(SweepPayPickUpInStoreOnlineValOrderHandlerImpl.class);
    
}
