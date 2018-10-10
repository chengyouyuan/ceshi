package com.winhxd.b2c.common.domain.promotion.vo;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityArea;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityTemplate;
import com.winhxd.b2c.common.domain.promotion.model.CouponPushCustomer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *
 * @author shijinxing
 * @date 2018/8/6
 */
@ApiModel("优惠券活动")
@Data
public class CouponActivityVO {

    @ApiModelProperty(value = "优惠券活动ID")
    private Long id;

    @ApiModelProperty(value = "活动编码")
    private String code;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "活动说明")
    private String exolian;

    @ApiModelProperty(value = "活动备注")
    private String remarks;

    @ApiModelProperty(value = "1领券2推券")
    private Short type;

    @ApiModelProperty(value = "优惠券类型 1新用户注册 2老用户活动")
    private Short couponType;

    @ApiModelProperty(value = "活动状态1开启2停止")
    private Short activityStatus;

    @ApiModelProperty(value = "活动状态名称1开启2停止3停止并撤销")
    private String activityStatusName;

    @ApiModelProperty(value = "引用数量")
    private Integer citeNum;

    @ApiModelProperty(value = "配置可领券小店数量")
    private Integer storeNum;

    @ApiModelProperty(value = "优惠券数量的限制方式")
    private Short couponNumType;

    @ApiModelProperty(value = "优惠券数量的限制方式名称")
    private String couponNumTypeName;

    @ApiModelProperty(value = "优惠券数量的限制数量")
    private Integer couponNum;

    @ApiModelProperty(value = "己领取数量")
    private Integer getNum;

    @ApiModelProperty(value = "己使用数量")
    private Integer useNum;

    @ApiModelProperty(value = "己撤销优惠券数量")
    private Integer revocationNum;

    @ApiModelProperty(value = "活动开始时间")
    private Date activityStart;

    @ApiModelProperty(value = "活动结束时间")
    private Date activityEnd;

    @ApiModelProperty(value = "优惠券有效期开始时间")
    private Date couponStartTime;

    @ApiModelProperty(value = "优惠券有效期结束时间")
    private Date couponEndTime;

    @ApiModelProperty(value = "创建人")
    private String createdByName;

    @ApiModelProperty(value = "创建时间")
    private Date created;

    @ApiModelProperty(value = "修改人")
    private String updatedByName;

    @ApiModelProperty(value = "修改时间")
    private Date updated;

    @ApiModelProperty(value = "优惠券详情")
    private List<CouponActivityTemplate> couponActivityTemplateList;

    @ApiModelProperty(value = "区域信息")
    private List<CouponActivityArea> couponActivityAreaList;

    @ApiModelProperty(value = "推券活动指定的C端用户")
    private List<CouponPushCustomer> couponPushCustomerList;

    @ApiModelProperty(value = "推券活动门店下覆盖的用户总数")
    private Long storeCustomerNum;

}
