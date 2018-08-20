package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * PayTransfersQueryForWxChangeResponseDTO
 *
 * @Author yindanqing
 * @Date 2018/8/18 13:09
 * @Description: 查询微信转账至零钱详情返参
 */
@Setter
@Getter
public class PayTransfersQueryForWxChangeResponseDTO {

    /**
     * 返回状态码
     */
    private String returnCode;

    /**
     * 返回信息
     */
    private String returnMsg;

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
     * 商户号
     */
    private String mchId;

    /**
     * 调用企业付款API时，微信系统内部产生的单号
     */
    private String detailId;

    /**
     * 转账状态
     * SUCCESS:转账成功
     * FAILED:转账失败
     * PROCESSING:处理中
     */
    private String status;

    /**
     * 失败原因
     */
    private String reason;

    /**
     * 收款用户openid
     */
    private String openid;

    /**
     * 收款用户姓名
     */
    private String transferName;

    /**
     * 付款金额
     */
    private String paymentAmount;

    /**
     * 转账时间
     */
    private String transferTime;

    /**
     * 付款描述
     */
    private String desc;


}
