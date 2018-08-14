package com.winhxd.b2c.common.domain.store.condition;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.common.ApiCondition;

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
public class ProdOperateCondition extends ApiCondition {
	
	@ApiModelProperty(value = "商品操作信息集合", required = true)
	private List<ProdOperateInfoCondition> products;

	@ApiModelProperty(value = "操作类型 0下架 1上架 2删除 3编辑", required = true)
	private Byte operateType;

}
