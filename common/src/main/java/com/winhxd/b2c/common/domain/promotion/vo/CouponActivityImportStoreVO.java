package com.winhxd.b2c.common.domain.promotion.vo;

import com.winhxd.b2c.common.domain.promotion.util.BaseExcelDomain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author sjx
 * @date 2018/8/17
 */
@ApiModel("优惠券活动导入小店信息")
@Data
public class CouponActivityImportStoreVO extends BaseExcelDomain {

    @ApiModelProperty(value = "小店ID")
    @Excel(name = "惠小店ID", orderNum = "1")
    private String storeId;

    @ApiModelProperty(value = "小店名字")
    @Excel(name = "惠小店名称", orderNum = "2")
    private String storeName;
}
