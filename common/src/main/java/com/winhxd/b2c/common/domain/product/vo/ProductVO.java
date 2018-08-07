package com.winhxd.b2c.common.domain.product.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品列表VO
 * @ClassName:  ProductsVO   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午1:09:15   
 *
 */
@Data
public class ProductVO {

	@ApiModelProperty(value = "商品编码")
	private String productCode;
	
	@ApiModelProperty(value = "商品名称")
	private String productName;
	
	@ApiModelProperty(value = "商品sku")
	private List<ProductSkuVO> productSkus;

}
