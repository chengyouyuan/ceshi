package com.winhxd.b2c.common.domain.promotion.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/13 11:09
 * @Description  显示门店下优惠券的领取数量和使用数量
 **/
@Data
public class CouponInStoreGetedAndUsedVO {
    @ApiModelProperty(value = "模板id")
    private Long templeteId;
    @ApiModelProperty(value = "门店id")
    private Long storeId;
    @ApiModelProperty(value = "活动id")
    private Long couponActivityId;
    @ApiModelProperty(value = "领取数量")
    private Integer totalCount ;
    @ApiModelProperty(value = "使用数量")
    private Integer usedCount;
    @ApiModelProperty(value = "满减优惠类型(1-金额/2-折扣)")
    private Short reduceType;
    @ApiModelProperty(value = "满减金额")
    private Float reduceAmt;
    @ApiModelProperty(value = "成本")
    private Float cost ;
    @ApiModelProperty(value = "优惠金额")
    private Float discountedAmt ;
    @ApiModelProperty(value = "满减优惠折扣")
    private Float discounted;
    @ApiModelProperty(value = "优惠最大限额")
    private Float maxAmt;
    @ApiModelProperty(value = "有效期开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date startTime;
    @ApiModelProperty(value = "有效期结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date endTime;
    @ApiModelProperty(value = "适用对象类型")
    private Short applyRuleType;
    @ApiModelProperty(value = "适用对象类型")
    private Long applyId;
    @ApiModelProperty(value = "适用品牌信息")
    private List<BrandVO> brands;
    @ApiModelProperty(value = "适用商品信息")
    private List<ProductSkuVO> products;
    @ApiModelProperty(value = "是否过期  0已过期  1未过期")
    private Short expire;

    @ApiModelProperty(value = "logo(品牌或品牌商)地址")
    private String logoUrl;
    @ApiModelProperty(value = "已推给用户数量")
    private Integer pushCount;


}
