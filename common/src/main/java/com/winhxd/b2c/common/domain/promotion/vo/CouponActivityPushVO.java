package com.winhxd.b2c.common.domain.promotion.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityArea;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityTemplate;
import com.winhxd.b2c.common.domain.promotion.model.CouponPushCustomer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: retail2c
 * @description: 优惠券推券活动VO
 * @author: ChenYanqi
 * @create: 2018-12-12 14:20
 **/
@ApiModel("优惠券推券活动VO")
@Data
public class CouponActivityPushVO {

    @ApiModelProperty(value = "优惠券活动ID")
    private Long id;

    @ApiModelProperty(value = "活动编码")
    @Excel(name = "推券活动编码", width = 40)
    private String code;

    @ApiModelProperty(value = "活动名称")
    @Excel(name = "推券活动名称", width = 30)
    private String name;

    @ApiModelProperty(value = "活动状态名称1开启2停止3停止并撤销")
    @Excel(name = "活动状态", width = 30)
    private String activityStatusName;

    @ApiModelProperty(value = "引用数量")
    @Excel(name = "引用数量", width = 30)
    private Integer citeNum;

    @ApiModelProperty(value = "己领取数量")
    @Excel(name = "已领取数量", width = 30)
    private Integer getNum;

    @ApiModelProperty(value = "己使用数量")
    @Excel(name = "已使用数量", width = 30)
    private Integer useNum;

    @ApiModelProperty(value = "己撤销优惠券数量")
    @Excel(name = "己撤销优惠券数量", width = 30)
    private Integer revocationNum;

    @ApiModelProperty(value = "活动开始时间")
    @Excel(name = "活动开始时间", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date activityStart;

    @ApiModelProperty(value = "活动结束时间")
    @Excel(name = "活动结束时间", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date activityEnd;

    @ApiModelProperty(value = "优惠券有效期开始时间")
    @Excel(name = "优惠券有效期开始时间", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date couponStartTime;

    @ApiModelProperty(value = "优惠券有效期结束时间")
    @Excel(name = "优惠券有效期结束时间", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date couponEndTime;

    @ApiModelProperty(value = "创建人")
    @Excel(name = "创建人", width = 30)
    private String createdByName;

    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date created;

    @ApiModelProperty(value = "修改人")
    @Excel(name = "修改人", width = 30)
    private String updatedByName;

    @ApiModelProperty(value = "修改时间")
    @Excel(name = "修改时间", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updated;

    @ApiModelProperty(value = "配置可领券小店数量")
    private Integer storeNum;

    @ApiModelProperty(value = "配置可领券小店数量")
    private String couponNumTypeName;

    @ApiModelProperty(value = "优惠券数量的限制数量")
    private Integer couponNum;

    @ApiModelProperty(value = "活动说明")
    private String exolian;

    @ApiModelProperty(value = "活动备注")
    private String remarks;

    @ApiModelProperty(value = "1领券2推券")
    private Short type;

    @ApiModelProperty(value = "优惠券类型 1新用户注册 2老用户活动")
    private Short couponType;

    @ApiModelProperty(value = "活动状态1开启2停止3停止并撤销")
    private Short activityStatus;

    @ApiModelProperty(value = "优惠券数量的限制方式")
    private Short couponNumType;

    @ApiModelProperty(value = "优惠券详情")
    private List<CouponActivityTemplate> couponActivityTemplateList;

    @ApiModelProperty(value = "区域信息")
    private List<CouponActivityArea> couponActivityAreaList;

    @ApiModelProperty(value = "推券活动指定的C端用户")
    private List<CouponPushCustomer> couponPushCustomerList;

    @ApiModelProperty(value = "推券活动门店下覆盖的用户总数")
    private Long storeCustomerNum;

}
