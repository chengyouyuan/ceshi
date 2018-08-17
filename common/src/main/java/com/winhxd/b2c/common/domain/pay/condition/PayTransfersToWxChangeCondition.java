package com.winhxd.b2c.common.domain.pay.condition;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

import com.winhxd.b2c.common.domain.common.ApiCondition;

/**
 * PayTransfersToWxChangeCondition
 *
 * @Author yindanqing
 * @Date 2018/8/14 10:42
 * @Description: 转账至微信零钱
 */
@Setter
@Getter
public class PayTransfersToWxChangeCondition extends ApiCondition implements Serializable {

    private static final long serialVersionUID = 5046603829537736036L;

    /**
     * 商户账号appid
     */
    /*private String mchAppid;*/

    /**
     * 商户号
     */
/*    private String mchid;*/

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 提现流水号
     */
    private String partnerTradeNo;

    /**
     * 收款人唯一标识(微信openid)
     */
    private String accountId;

    /**
     * 收款人姓名
     */
    private String accountName;

    /**
     * 提现金额,单位元
     */
    private BigDecimal totalAmount;

    /**
     * 付款描述信息
     */
    private String desc;

    /**
     * Ip地址
     */
    private String spbillCreateIp;

}
