package com.winhxd.b2c.common.domain.product.enums;

/**
 * sku集合内/外查找
 * @ClassName:  SearchSkuCodeEnum   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午5:27:51   
 *
 */
public enum SearchSkuCodeEnum {
   
    IN_SKU_CODE("IN", "在sku集合范围内查找"),
    NOT_IN_SKU_CODE("NOT", "在sku集合范围外查找");
   

    private String code;
    private String desc;

    SearchSkuCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

 
}
