package com.winhxd.b2c.common.domain.store.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * B端登入校验门店上架商品未设置价格商品相关信息返回VO
 * @ClassName: LoginCheckSellMoneyVO 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月6日 下午1:25:38
 */
@ApiModel("设置价格商品相关信息VO")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LoginCheckSellMoneyVO {
	
	@ApiModelProperty("门店编码")
	private Long storeId;
	
	@ApiModelProperty("是否有没有设置价格的上架商品，true表示有，false表示没有")
	private Boolean checkResult;
	
	@ApiModelProperty("没有设置价格的上架商品的数量")
	private Integer noSetPriceCount;
	
	@ApiModelProperty("没有设置价格的上架商品sku集合")
	private List<String> skuCodes;

}
