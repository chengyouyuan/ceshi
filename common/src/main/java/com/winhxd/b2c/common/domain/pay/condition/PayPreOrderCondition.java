package com.winhxd.b2c.common.domain.pay.condition;

import java.io.Serializable;
import java.math.BigDecimal;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

/**
 * 预支付参数
 * @author mahongliang
 * @date  2018年8月15日 上午11:28:49
 * @Description 
 * @version
 */
public class PayPreOrderCondition extends ApiCondition implements Serializable {
	private static final long serialVersionUID = -6751906874587493059L;

    @ApiModelProperty("真实订单号")
    private String outOrderNo;

    @ApiModelProperty("订单总金额，单位为元")
    private BigDecimal totalAmount;

    @ApiModelProperty("买家用户标识,trade_type=JSAPI时（即公众号支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识（openid）")
    private String openid;

    @ApiModelProperty("商品描述")
    private String body;

    @ApiModelProperty("终端IP")
    private String spbillCreateIp;
    
    @ApiModelProperty("支付方式")
    private String payType;

    @ApiModelProperty("(非必填)设备号")
    private String deviceInfo;

    @ApiModelProperty("(非必填)商品ID")
    private String productId;

    @ApiModelProperty("(非必填)附加数据，可作为自定义参数使用")
    private String attach;

    @ApiModelProperty("(非必填)限制支付类型（微信参数），上传此参数no_credit--可限制用户不能使用信用卡支付")
    private String limitPay;

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getDeviceInfo() {
		deviceInfo = this.getMobileInfo().getImei();
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getLimitPay() {
		return limitPay;
	}

	public void setLimitPay(String limitPay) {
		this.limitPay = limitPay;
	}

}
