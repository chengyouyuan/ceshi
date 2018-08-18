package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.winhxd.b2c.common.domain.common.model.BaseImageFile;
import io.swagger.annotations.ApiModel;
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
public class ProductImageVO extends BaseImageFile {

}
