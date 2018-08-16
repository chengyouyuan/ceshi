package com.winhxd.b2c.common.domain.order.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单支付数据获取 条件
 * @author wangbin
 * @date  2018年8月16日 上午11:28:49
 * @Description 
 * @version
 */
public class OrderPayInfoCondition extends ApiCondition {
	
	/**
     * 真实订单号
     */
    @ApiModelProperty(value="订单号", required=true)
    private String orderNo;
    
    /**
     * 终端IP
     */
    @ApiModelProperty(value="终端IP")
    private String spbillCreateIp = "127.0.0.1";
	
	/**
     * （非必填）设备号
     */
    @ApiModelProperty(value="设备号")
    private String deviceInfo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

}
