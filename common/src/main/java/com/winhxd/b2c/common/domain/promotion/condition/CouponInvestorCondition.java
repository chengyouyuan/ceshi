package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/4 14:31
 * @Description
 **/
@Data
public class CouponInvestorCondition extends PagedCondition implements Serializable {
    @ApiModelProperty(value = "出资方规则id")
    private String id;
    @ApiModelProperty(value = "出资方规则编码")
    private String code;
    @ApiModelProperty(value = "出资方规则名称")
    private String name;
    @ApiModelProperty(value = "出资方名称")
    private String names;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "状态 0 无效 1 有效")
    private Short status;
    @ApiModelProperty(value = "出资方详情列表")
    private List details;
    @ApiModelProperty(value = "当前操作人id")
    private String userId;
    @ApiModelProperty(value = "当前操作人名称")
    private String userName;
}
