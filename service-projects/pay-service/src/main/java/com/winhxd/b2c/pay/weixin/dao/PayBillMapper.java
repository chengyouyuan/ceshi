package com.winhxd.b2c.pay.weixin.dao;

import java.util.List;

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
    
    /**
     * 查询订单支付状态
     * @author mahongliang
     * @date  2018年8月17日 下午5:15:30
     * @Description 
     * @param outOrderNo
     * @return
     */
    List<Integer> selectPayBillStatusByOutOrderNo(String outOrderNo);
    
    /**
     * 支付账单通用查询
     * @author mahongliang
     * @date  2018年8月17日 下午4:28:42
     * @Description 
     * @param bill
     * @return
     */
    //List<PayBill> selectByModel(PayBill bill);
}