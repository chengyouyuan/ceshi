package com.winhxd.b2c.common.domain.product.vo;

import com.winhxd.b2c.common.domain.PagedList;

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
public class ProductSkuMsgVO extends ProductBaseMsgVO{
	
	@ApiModelProperty(value = "商品")
	private PagedList<ProductSkuVO> productSkus;
	
}
