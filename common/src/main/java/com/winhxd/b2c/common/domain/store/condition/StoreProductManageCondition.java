package com.winhxd.b2c.common.domain.store.condition;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @ClassName: StoreProductManageCondition 
 * @Description: 门店管理商品condition
 * @author: wuyuanbao
 * @date: 2018年8月4日 下午1:31:17
 */
@ApiModel("B端门店商品基本操作入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreProductManageCondition implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

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

	@ApiModelProperty("更新人")
	private Long updatedBy;

	@ApiModelProperty("更新人名称")
	private String updatedByName;
	
	@ApiModelProperty("更新时间")
	private Date updated;
	
	@ApiModelProperty("排序条件,0创建时间，1价格")
	private Integer orderBy;
	
	@ApiModelProperty("升序或者降序，如果默认升序 1 降序 0升序")
	private Byte descAsc;
	
	@ApiModelProperty("价格状态，0表示未设置价格，1表示已经设置价格")
	private Byte priceStatus;
	
	@ApiModelProperty(value = "页号")
	private Integer pageNo=1;
	
	@ApiModelProperty(value = "页大小")
	private Integer pageSize=10;
	

}
