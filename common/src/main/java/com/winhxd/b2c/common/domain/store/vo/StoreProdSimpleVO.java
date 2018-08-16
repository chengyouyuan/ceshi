package com.winhxd.b2c.common.domain.store.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 门店商品简单VO
 * @ClassName: StoreProdSimpleVO 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月8日 下午8:49:15
 */
@ApiModel("门店商品简单VO")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreProdSimpleVO {
	@ApiModelProperty("id主键")
	private Long id;
	
	@ApiModelProperty("门店id")
	private Long storeId;
	
	@ApiModelProperty("商品规格")
	private String skuAttributeOption;
	
	@ApiModelProperty("售卖价格，BigDecimal类型值为空，ios会解析为0，改为String")
	private String sellMoney;
	
	@ApiModelProperty("是否推荐 0不推荐 1推荐")
	private Byte recommend;
	
	@ApiModelProperty("商品状态 0下架1上架2已删除")
	private Byte prodStatus;
	
	@ApiModelProperty("商品code")
	private String prodCode;
	
	@ApiModelProperty("商品sku")
	private String skuCode;
	
	@ApiModelProperty("商品名称")
	private String skuName;
	
	@ApiModelProperty("商品图片")
	private String skuImage;
	
	@ApiModelProperty("上架时间")
	private Date putawayTime;

}
