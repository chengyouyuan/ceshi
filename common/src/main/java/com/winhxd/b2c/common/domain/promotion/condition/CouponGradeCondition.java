package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author wl
 * @Date 2018/8/8 16:43
 * @Description
 **/
@Data
public class CouponGradeCondition extends PagedCondition implements Serializable {
    @ApiModelProperty(value = "坎级规则id")
    private Long id;
    @ApiModelProperty(value = "坎级规则编码")
    private String code;
    @ApiModelProperty(value = "坎级规则名称")
    private String name;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "状态")
    private Short status;
    @ApiModelProperty(value = "类型 (1-满减/2-满赠/3-按件减阶梯/4-按件减翻倍/5-按件增阶梯/6-按件增翻倍)")
    private Short type;
    @ApiModelProperty(value = "满减金额")
    private BigDecimal reducedAmt;
    @ApiModelProperty(value = "满减优惠类型(1-金额/2-折扣)")
    private Short reducedType;
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountedAmt;
    @ApiModelProperty(value = "满减优惠折扣")
    private BigDecimal discounted;
    @ApiModelProperty(value = "优惠最大限额")
    private BigDecimal discountedMaxAmt;
    @ApiModelProperty(value = "满赠金额")
    private BigDecimal fullGiveAmt;
    @ApiModelProperty(value = "加价金额")
    private BigDecimal increaseAmt;
    @ApiModelProperty(value = "成本")
    private BigDecimal cost;
    @ApiModelProperty(value = "按件减(阶梯/翻倍)件数")
    private Integer count;
    @ApiModelProperty(value = "按件增(翻倍)次数上限")
    private Integer times;
    @ApiModelProperty(value = "当前操作人id")
    private String userId;
    @ApiModelProperty(value = "当前操作人名称")
    private String userName;
}
