package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import com.winhxd.b2c.common.domain.common.inputmodel.DateInterval;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author wl
 * @Date 2018/8/6 09:56
 * @Description
 **/
@Data
public class CouponTemplateCondition extends PagedCondition implements Serializable {
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "优惠券标题")
    private String title;
    @ApiModelProperty(value = "优惠券说明")
    private String exolian;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "出资方规则ID")
    private Long investorId;
    @ApiModelProperty(value = "使用范围规则ID")
    private Long gradeId;
    @ApiModelProperty(value = "适用对象规则ID")
    private Long applyRuleId;
    @ApiModelProperty(value = "优惠券标签")
    private String couponLabel;
    @ApiModelProperty(value = "优惠券金额计算方式  1订单金额 2商品金额")
    private Short calType;
    @ApiModelProperty(value = "1为微信在线支付;2为微信扫码付款;")
    private Short payType;

    @ApiModelProperty(value = "优惠券类型")
    private Short applyRuleType;
    @ApiModelProperty(value = "是否有效 0有效1无效")
    private Short status;
    @ApiModelProperty(value = "优惠券模板编码(UUID")
    private String code;
    @ApiModelProperty(value = "出资方规则名称")
    private String investorName;
    @ApiModelProperty(value = "优惠券优惠方式名称")
    private String gradeName;
    @ApiModelProperty(value = "优惠券类型规则名称")
    private String applyRuleName;
    @ApiModelProperty(value = "创建人名称")
    private String createdByName;
    @ApiModelProperty(value = "创建人")
    private String createdBy;
    @ApiModelProperty(value = "角标")
    private String corner;
    @ApiModelProperty(value = "日期")
    private DateInterval dateInterval;
}
