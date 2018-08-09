package com.winhxd.b2c.common.domain.store.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: ShopCarProdVO 
 * @Description: 购物车商品VO
 * @author: wuyuanbao
 * @date: 2018年8月4日 下午6:04:18
 */
@ApiModel("购物车商品VO")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ShopCartProdVO {
	@ApiModelProperty("商品sku")
	private String skuCode;
	
	@ApiModelProperty("商品图片")
	private String prodImage;
	
	@ApiModelProperty("商品名称")
	private String prodName;
	
	@ApiModelProperty("售卖价格")
	private BigDecimal sellMoney;

	@ApiModelProperty("商品状态 0下架1上架2已删除")
	private Byte prodStatus;

}
