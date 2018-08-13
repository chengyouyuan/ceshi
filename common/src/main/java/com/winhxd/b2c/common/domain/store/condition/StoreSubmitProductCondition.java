package com.winhxd.b2c.common.domain.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提报商品condition
 * 
 * @ClassName: StoreSubmitProductCondition
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月3日 下午6:43:16
 */
@ApiModel("B端门店提报商品基本操作入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreSubmitProductCondition extends ApiCondition {
	 
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "门店编码", required = true)
	private Long storeId;

	@ApiModelProperty(value = "商品图片1")
	private String prodImage1;

	@ApiModelProperty(value = "商品图片2")
	private String prodImage2;

	@ApiModelProperty(value = "商品图片3")
	private String prodImage3;

	@ApiModelProperty(value = "商品信息（语音）")
	private String prodInfoVoice;

	@ApiModelProperty(value = "商品信息（文字）")
	private String prodInfoText;
	
	@ApiModelProperty(value = "页号")
	private Integer pageNo=1;
	
	@ApiModelProperty(value = "页大小")
	private Integer pageSize=10;

}
