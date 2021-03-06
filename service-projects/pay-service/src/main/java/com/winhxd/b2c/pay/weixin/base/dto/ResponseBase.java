package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

/**
 * 响应数据成功时，默认返回请求时的公共参数
 * @author mahongliang
 * @date  2018年8月18日 下午3:22:12
 * @Description 
 * @version
 */
@Data
public class ResponseBase extends RequestBase {
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";
	
	/**
	 * 错误码
	 */
    private String returnCode;
    /**
     * 错误描述
     */
    private String returnMsg;

}
