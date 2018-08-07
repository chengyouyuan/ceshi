package com.winhxd.b2c.common.domain.product.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询商品信息 condition
 * @ClassName:  ProductCondition   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午1:06:11   
 *
 */
@Data
public class ProductConditionByPage extends ProductCondition{

	@ApiModelProperty(value = "每页显示条数" ,required = true)
    private Integer pageSize;
    
	@ApiModelProperty(value = "页号" ,required = true)
    private Integer pageNo;

}
