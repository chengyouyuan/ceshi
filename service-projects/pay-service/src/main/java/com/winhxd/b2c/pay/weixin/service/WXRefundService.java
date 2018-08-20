package com.winhxd.b2c.pay.weixin.service;

import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundResponseDTO;
import com.winhxd.b2c.pay.weixin.model.PayRefund;

public interface WXRefundService {

    /**
     * 申请退款
     * @param payRefund
     * @return
     */
    PayRefundVO refundOrder(PayRefundCondition payRefund);


    PayRefundDTO refundQuery(PayRefundCondition payRefund);

    PayRefund updatePayRefundByOutTradeNo(PayRefundResponseDTO payRefundResponseDTO) throws Exception;
}
