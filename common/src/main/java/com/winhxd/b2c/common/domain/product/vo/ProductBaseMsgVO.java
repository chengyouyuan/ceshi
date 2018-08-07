package com.winhxd.b2c.common.domain.product.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品信息VO
 * @ClassName:  ProductMsgVO   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午1:24:27   
 *
 */
@Data
public class ProductBaseMsgVO {
	
	@ApiModelProperty(value = "分类")
	private List<CategoryVO> categorys;
	
	@ApiModelProperty(value = "是否在门店上架商品内查找")
	private String searchSkuCode;


}
