package com.winhxd.b2c.common.domain.product.enums;

/**
 * 在门店上架商品内查找
 * @ClassName:  SearchSkuCodeEnum   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午5:27:51   
 *
 */
public enum SearchSkuCodeEnum {
   
    IN_SKU_CODE("IN", "在门店上架商品sku中查找"),
    NOT_IN_SKU_CODE("NOT", "不在门店上架商品sku中查找");
   

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
