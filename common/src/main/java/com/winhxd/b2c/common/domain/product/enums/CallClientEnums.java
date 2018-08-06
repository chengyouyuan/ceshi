package com.winhxd.b2c.common.domain.product.enums;

/**
 * 调用端
 * @ClassName:  CallClient   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午5:27:51   
 *
 */
public enum CallClientEnums {
   
    CALL_B("B", "B端用户调用"),
    CALL_C("C", "C端用户调用");
   

    private String code;
    private String desc;

    CallClientEnums(String code, String desc) {
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
