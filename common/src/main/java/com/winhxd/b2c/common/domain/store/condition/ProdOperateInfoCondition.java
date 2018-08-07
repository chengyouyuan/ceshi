package com.winhxd.b2c.common.domain.store.condition;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品操作基本信息
 * @ClassName: StoreProdPutawayInfoCondition 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月7日 下午3:50:35
 */
@ApiModel("商品操作信息")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProdOperateInfoCondition {
	
	@ApiModelProperty(value = "商品sku", required = true)
	private String skuCode;

	@ApiModelProperty("售卖价格")
	private BigDecimal sellMoney;

	@ApiModelProperty(value = "是否推荐 0不推荐 1推荐")
	private Byte recommend; 

}
