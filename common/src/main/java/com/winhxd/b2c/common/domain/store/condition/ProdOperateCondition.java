package com.winhxd.b2c.common.domain.store.condition;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.condition.MobileCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * B端门店商品基本操作condition
 * 
 * @ClassName: ProdOperateCondition
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月3日 下午6:28:25
 */
@ApiModel("B端门店商品基本操作入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProdOperateCondition extends MobileCondition {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "门店编码", required = true)
	private Long storeId;

	@ApiModelProperty(value = "商品sku数组", required = true)
	private String[] skuCode;

	@ApiModelProperty("售卖价格")
	private BigDecimal sellMoney;

	@ApiModelProperty(value = "是否推荐 0不推荐 1推荐")
	private Byte recommend;

	@ApiModelProperty(value = "操作类型 0下架 1上架 2删除 3编辑", required = true)
	private Byte operateType;

}
