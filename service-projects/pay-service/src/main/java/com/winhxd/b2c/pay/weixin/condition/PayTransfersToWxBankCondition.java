package com.winhxd.b2c.pay.weixin.condition;

import com.winhxd.b2c.common.constant.WithDrawChannelCodeType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * PayTransfersToWxBankCondition
 *
 * @Author yindanqing
 * @Date 2018/8/14 10:43
 * @Description: 转账至微信余额
 */
@Setter
@Getter
public class PayTransfersToWxBankCondition implements java.io.Serializable {

    private static final long serialVersionUID = -7292089920160701229L;

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 提现流水号
     */
    private String partnerTradeNo;

    /**
     * 提现账户(银行卡)
     */
    private String account;

    /**
     * 收款人姓名
     */
    private String accountName;

    /**
     * 流向名称,银行代码
     */
    private WithDrawChannelCodeType channelCode;

    /**
     * 提现金额,单位元
     */
    private BigDecimal totalAmount;

    /**
     * 付款描述信息
     */
    private String desc;

}
