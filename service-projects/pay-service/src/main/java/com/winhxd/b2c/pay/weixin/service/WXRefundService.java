package com.winhxd.b2c.pay.weixin.service;

import com.winhxd.b2c.pay.weixin.condition.PayRefundCondition;
import com.winhxd.b2c.pay.weixin.dto.PayRefundDTO;

public interface WXRefundService {

    /**
     * 申请退款
     * @param payRefund
     * @return
     */
    PayRefundDTO refundOrder(PayRefundCondition payRefund);


    PayRefundDTO refundQuery(PayRefundCondition payRefund);
}
