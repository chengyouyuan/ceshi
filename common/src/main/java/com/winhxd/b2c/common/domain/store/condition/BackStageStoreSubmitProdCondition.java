package com.winhxd.b2c.common.domain.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.common.PagedCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后台提报商品入参
 * @ClassName: BackStageStoreSubmitProdCondition 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月14日 下午3:32:19
 */
@Data
@ApiModel("后台-门店商品管理入参")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackStageStoreSubmitProdCondition extends PagedCondition{
	@ApiModelProperty("id主键")
	private Long id;
	@ApiModelProperty("门店id")
	private Long storeId;
	@ApiModelProperty("门店名称")
	private String storeName;
	@ApiModelProperty("商品名称")
	private String prodName;
	@ApiModelProperty("商品code")
	private String prodCode;
	@ApiModelProperty("商品状态（0 待审核 ，1 审核通过 ，2 审核不通过，3 已添加）")
	private Short prodStatus;
	@ApiModelProperty("sku编码")
	private String skuCode;
	@ApiModelProperty("用户账号")
	private String storeMobile;
}
