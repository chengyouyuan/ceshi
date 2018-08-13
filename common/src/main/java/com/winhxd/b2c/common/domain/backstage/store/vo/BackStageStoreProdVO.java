package com.winhxd.b2c.common.domain.backstage.store.vo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 门店商品管理后台返回的VO
 * @ClassName: BackStageStoreProdVO 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月13日 上午11:29:08
 */
public class BackStageStoreProdVO {
	@ApiModelProperty("id主键")
    private Long id;
    
    @ApiModelProperty("门店id")
    private Long storeId;
    
    @ApiModelProperty("商品规格")
    private String skuAttributeOption;
    
    @ApiModelProperty("售卖价格")
    private BigDecimal sellMoney;
    
    @ApiModelProperty("是否推荐 0不推荐 1推荐")
    private Short recommend;
    
    @ApiModelProperty("商品状态 0下架1上架2已删除")
    private Short prodStatus;
    
    @ApiModelProperty("商品sku")
    private String prodCode;
    
    @ApiModelProperty("商品名称")
    private String prodName;
    
    @ApiModelProperty("商品sku")
    private String skuCode;
    
    @ApiModelProperty("上架时间")
    private Date putawayTime;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("用户账号")
    private String storeMobile;

}
