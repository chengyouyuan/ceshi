package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 上传产品图片VO
 * @ClassName: ProductImageVO 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月11日 下午1:21:59
 */
@ApiModel("上传产品图片VO")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProductImageVO {
	@ApiModelProperty(value = "图片名称", required = true)
	private String imageName;
	@ApiModelProperty(value = "图片路径", required = true)
	private String imageUrl;
}
