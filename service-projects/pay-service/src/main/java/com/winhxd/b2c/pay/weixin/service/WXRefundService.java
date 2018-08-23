package com.winhxd.b2c.pay.weixin.service;

import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundResponseDTO;
import com.winhxd.b2c.pay.weixin.model.PayRefund;

/**
 * 退款service
 * @author lizhonghua
 * @Description
 */
public interface WXRefundService {

    /**
     * 申请退款
     * @param payRefund
     * @return
     */
    PayRefundVO refundOrder(PayRefundCondition payRefund);

    /**
     * 根据商户流水号更新退款记录表
     * @param payRefundResponseDTO
     * @return
     * @throws Exception
     */
    PayRefund updatePayRefundByOutTradeNo(PayRefundResponseDTO payRefundResponseDTO) throws Exception;
}
