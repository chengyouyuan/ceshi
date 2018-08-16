package com.winhxd.b2c.pay.weixin.service;

import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundDTO;

public interface WXRefundService {

    /**
     * 申请退款
     * @param payRefund
     * @return
     */
    PayRefundDTO refundOrder(PayRefundCondition payRefund);


    PayRefundDTO refundQuery(PayRefundCondition payRefund);
}
