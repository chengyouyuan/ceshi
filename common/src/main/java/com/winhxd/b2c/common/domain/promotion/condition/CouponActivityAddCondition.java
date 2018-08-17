package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.inputmodel.DateInterval;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityArea;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 新增、编辑用的condition
 * @author shijinxing
 * @date 2018/8/7
 */
@Data
@ApiModel(value = "用户请求参数",description = "新增、编辑请求参数")
public class CouponActivityAddCondition {

    @ApiModelProperty(value = "优惠券活动ID")
    private Long id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "活动编码")
    private String code;

    @ApiModelProperty(value = "活动说明")
    private String exolian;

    @ApiModelProperty(value = "活动备注")
    private String remarks;

    @ApiModelProperty(value = "1领券 2推券")
    private Short type;

    @ApiModelProperty(value = "优惠券详情")
    private List<CouponActivityTemplate> couponActivityTemplateList;

    @ApiModelProperty(value = "活动开始时间")
    private Date activityStart;

    @ApiModelProperty(value = "活动结束时间")
    private Date activityEnd;

    @ApiModelProperty(value = "优惠券类型 1新用户注册 2老用户活动")
    private Short couponType;

    @ApiModelProperty(value = "活动状态1开启2停止")
    private Short activityStatus;

    @ApiModelProperty(value = "创建时间")
    private DateInterval dateInterval;

    @ApiModelProperty(value = "创建人")
    private String createdByName;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "活动区域信息")
    private List<CouponActivityArea> couponActivityAreaList;
}
