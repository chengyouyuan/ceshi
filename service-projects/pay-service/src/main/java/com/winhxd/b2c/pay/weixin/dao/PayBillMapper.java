package com.winhxd.b2c.pay.weixin.dao;

import com.winhxd.b2c.pay.weixin.model.PayBill;

public interface PayBillMapper {

    int insertSelective(PayBill record);

    int updateByPrimaryKeySelective(PayBill record);
    
    /**
     * 已支付的订单流水数量
     * @author mahongliang
     * @date  2018年8月16日 下午9:01:38
     * @Description 
     * @param outOrderNo
     * @return
     */
    Long selectPaidByOutOrderNo(String outOrderNo);
}