package com.winhxd.b2c.common.domain.store.condition;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.common.MobileCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * B端门店可上架商品列表condition
 * 
 * @ClassName: StoreProdCondition
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月3日 下午5:56:51
 */
@ApiModel("B端门店可上架商品列表入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AllowPutawayProdCondition extends MobileCondition {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "门店编码")
	private Long storeId;

	@ApiModelProperty(value = "待上架商品名称")
	private String prodName;

	@ApiModelProperty(value = "待上架商品sku")
	private String skuCode;
	
	@ApiModelProperty(value = "商品一级类目编码")
	private String categoryCode;
	
	@ApiModelProperty(value = "品牌编码")
	private List<String> brandCode;
	
	@ApiModelProperty(value = "商品类型默认0，0 惠下单商品，1 普通商品")
	private Byte prodType;
	
	@ApiModelProperty(value = "是否是首次请求默认true，true是 false否")
	private Boolean first=true;
	
	@ApiModelProperty(value = "页大小")
	private Integer pageSize;
	
	@ApiModelProperty(value = "页号")
	private Integer pageNo;

}
