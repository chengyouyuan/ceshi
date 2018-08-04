package com.winhxd.b2c.common.domain.store.condition;

import java.util.Date;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;

import io.swagger.annotations.ApiModelProperty;

/**
 * 门店管理商品condition
 * @ClassName: StoreProductManageCondition 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月4日 下午1:31:17
 */
public class StoreProductManageCondition extends BaseCondition{
	
	@ApiModelProperty("门店id")
	private Long storeId;
	
	@ApiModelProperty("商品id")
	private String prodId;
	
	@ApiModelProperty("是否推荐 0不推荐 1推荐")
	private Byte recommend;
	
	@ApiModelProperty("商品状态 0下架1上架2已删除")
	private Byte prodStatus;
	
	@ApiModelProperty("商品spu")
	private String prodCode;
	
	@ApiModelProperty("商品sku")
	private String skuCode;
	
	@ApiModelProperty("更新人名称")
	private String updatedByName;
	
	@ApiModelProperty("更新时间")
	private Date updated;
	
	@ApiModelProperty("排序条件")
	private String orderBy;
	
	@ApiModelProperty("升序或者降序 0 降序 1升序")
	private Byte descAsc;

}
