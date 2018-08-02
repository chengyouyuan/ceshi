package com.winhxd.b2c.common.domain.login.condition;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author wufuyun
 * @date  2018年8月2日 下午3:58:36
 * @Description 
 * @version
 */
@ApiModel("小程序用户请求参数")
@Data
public class CustomerUserInfoCondition implements Serializable {
	@ApiModelProperty(value = "用户主键")
    private Long customerId;
	@ApiModelProperty(value = "账号")
    private String customerMobile;
	@ApiModelProperty(value = "微信code")
    private String code;
	@ApiModelProperty(value = "纬度")
    private Double lat;
	@ApiModelProperty(value = "经度")
    private Double lon;
	@ApiModelProperty(value = "昵称")
    private String nickName;
	@ApiModelProperty(value = "头像")
    private String headurl;


    private static final long serialVersionUID = 1L;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile == null ? null : customerMobile.trim();
    }


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl == null ? null : headurl.trim();
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
}