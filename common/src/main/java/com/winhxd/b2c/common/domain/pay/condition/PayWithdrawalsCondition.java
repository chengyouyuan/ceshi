package com.winhxd.b2c.common.domain.pay.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/21 15 09
 * @Description 微信提现公共参数
 */
@Setter
@Getter
public class PayWithdrawalsCondition extends ApiCondition implements Serializable {
    @ApiModelProperty("提现申请ID")
    private Long withdrawalsId;

    @ApiModelProperty("操作人ID")
    private String operaterID;


}
