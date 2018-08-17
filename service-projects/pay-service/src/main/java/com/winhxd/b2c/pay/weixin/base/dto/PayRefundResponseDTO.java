package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

/**
 * PayRefundResponseDTO
 *
 * @Author 李中华
 * @Date 2018/8/16 15:58
 * @Description:
 */
@Data
public class PayRefundResponseDTO extends ResponseBase{

    /**
     * 业务结果:SUCCESS/FAIL
     * SUCCESS退款申请接收成功，结果通过退款查询接口查询
     * FAIL 提交业务失败
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
     * 公众号ID
     */
    private String appid;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 微信生成的订单号
     */
    private String transactionId;

    /**
     * 商户订单号（流水号）
     */
    private String outTradeNo;

    /**
     * 商户退款单号（使用流水号）
     */
    private String outRefundNo;

    /**
     * 回调微信退款单号
     */
    private String refundId;

    /**
     * 回调退款金额：分/单位
     */
    private Integer refundFee;

    /**
     * 回调应结退款总金额：分/单位
     */
    private Integer settlementRefundFee;

    /**
     * 回调订单总金额：分/单位
     */
    private Integer totalFee;

    /**
     * 回调应结订单总金额：分/单位
     */
    private Integer settlementTotalFee;

    /**
     * 回调退款货币种类
     */
    private String feeType;

    /**
     * 回调现金支付金额：分/单位
     */
    private Integer cashFee;

    /**
     * 回调现金支付币种
     */
    private String cashFeeType;

    /**
     * 回调现金退款金额：分/单位
     */
    private Integer cashRefundFee;

    /**
     * 退款状态 0退款异常 1退款成功 2退款关闭
     */
    private String refundStatus;

    /**
     * 资金退款至用户帐号的时间 YYYY-MM-DD hh:mm:ss
     */
    private String successTime;

    /**
     * 退款入账账户   1)退回银行卡:{银行名称}{卡类型}{卡尾号}
     * 2)退回支付用户零钱:支付用户零钱
     * 3)退还商户:商户基本账户
     * 4)退回支付用户零钱通:支付用户零钱通
     */
    private String refundRecvAccout;

    /**
     * 退款资金来源
     */
    private String refundAccount;

    /**
     * 1:API接口
     * 2:VENDOR_PLATFORM商户平台
     */
    private String refundRequestSource;

    
}
