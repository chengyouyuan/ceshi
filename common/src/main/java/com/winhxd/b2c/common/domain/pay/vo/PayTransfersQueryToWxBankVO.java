package com.winhxd.b2c.common.domain.pay.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * PayTransfersQueryToWxBankVO
 *
 * @Author yindanqing
 * @Date 2018/8/22 16:02
 * @Description: 查询wx转账至银行卡返参
 */
@Setter
@Getter
public class PayTransfersQueryToWxBankVO {

    /**
     * 业务结果
     */
    private String resultCode;

    /**
     * 错误代码
     */
    private String errCode;

    /**
     * 错误代码描述
     */
    private String errCodeDes;

    /**
     * 商户订单号
     */
    private String partnerTradeNo;

    /**
     * 微信企业付款单号
     */
    private String paymentNo;

    /**
     * 银行卡号
     */
    private String bankNoMd5;

    /**
     * 用户真实姓名
     */
    private String trueNameMd5;

    /**
     * 代付金额
     */
    private BigDecimal amount;

    /**
     * 代付单状态
     * PROCESSING（处理中，如有明确失败，则返回额外失败原因；否则没有错误原因）
     * SUCCESS:转账成功
     * FAILED（付款失败,需要替换付款单号重新发起付款）
     * BANK_FAIL（银行退票，订单状态由付款成功流转至退票,退票时付款金额和手续费会自动退还）
     */
    private String status;

    /**
     * 手续费金额
     */
    private BigDecimal cmmsAmt;

    /**
     * 商户下单时间
     * 微信侧订单创建时间
     */
    private String createTime;

    /**
     * 成功付款时间
     * 微信侧付款成功时间（但无法保证银行不会退票）
     */
    private String paySuccTime;

    /**
     * 失败原因
     */
    private String reason;

}
