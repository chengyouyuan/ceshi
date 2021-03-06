package com.winhxd.b2c.common.domain.store.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 门店商品管理后台返回的VO
 * @ClassName: BackStageStoreProdVO 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月13日 上午11:29:08
 */
@Data
@ApiModel("后台-门店商品管理返参")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackStageStoreProdVO implements Serializable{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("id主键")
    private Long id;
    
    @ApiModelProperty("门店id")
    private Long storeId;

    @ApiModelProperty("商品状态（字符串） 0下架1上架2已删除")
    @Excel(name = "状态", width = 30)
    private String prodStatusStr;

    @ApiModelProperty("商品sku")
    @Excel(name = "sku编码", width = 30)
    private String skuCode;

    @ApiModelProperty("商品名称")
    @Excel(name = "商品名称", width = 30)
    private String prodName;

    @ApiModelProperty("商品图片")
    @Excel(name = "商品图片", width = 60)
    private String skuImage;

    @ApiModelProperty("商品规格")
    @Excel(name = "商品规格", width = 30)
    private String skuAttributeOption;

    @ApiModelProperty("用户账号")
    @Excel(name = "门店账户", width = 30)
    private String storeMobile;

    @ApiModelProperty("门店名称")
    @Excel(name = "门店名称", width = 30)
    private String storeName;

    @ApiModelProperty("售卖价格")
    @Excel(name = "商品售价", width = 30)
    private BigDecimal sellMoney;
    
    @ApiModelProperty("是否推荐 0不推荐 1推荐")
    private Short recommend;
    
    @ApiModelProperty("商品状态 0下架1上架2已删除")
    private Short prodStatus;
    
    @ApiModelProperty("商品sku")
    private String prodCode;
    
    @ApiModelProperty("上架时间")
    private Date putawayTime;


}
