package com.winhxd.b2c.message.sms.enums;

/**
 * 国际短信发送返回状态枚举
 * */
public enum InternationalStatusEnum {
	/**
	 * 国际短信返回状态
	 */
	SUCCESS(0,"短信发送成功"),
	SECURITYCHECKERROR(98,"安全检查不通过"),
	HTTPCLIENTERROR(99,"HttpClient请求错误"),
	ADDRESS_ERROR(100,"接口地址错"),
	USERNOTFOUNDERROR(101,"账号不存在"),
	PASSWORDERROR(102,"密码错误"),
	SUBMITOVERSPEEDERROR(103,"提交过快(提交速度超过流速限制)"),
	SYSTEMERROR(104,"系统繁忙(因平台侧原因，暂时无法处理提交的短信)"),
	SENSITIVESMSERROR(105,"敏感短信(短信内容包含敏感词)"),
	NULLSMG_ERROR(106,"消息为空"),
	SMGTOOLONGERROR(107,"消息长度错误(>536或<=0)"),
	MOBLIEFORMATERROR(108,"包含错误的手机号码"),
	OUTOFMOBLIEBOUNDSERROR(109,"手机号码个数超限(群发>50000或<=0;单发>200或<=0)"),
	NOTENOUGHAMOUNTERROR(110,"余额不足(该用户可用短信数已使用完)"),
	NOTINSENDTIMEERROR(111,"不在发送时间内"),
	NOPRODUCTERROR(112,"无此产品,用户没有订购该产品"),
	NOTAPPROVEDERROR(115,"发送权限错误"),
	SIGNIDENTIFYERROR(113,"签名不合法，未带签名(用户必须带签名的前提下)"),
	IPIDENTIFYERROR(114,"IP地址认证错,请求调用的IP地址不是系统登记的IP地址"),
	USEROVERDUEERROR(116,"用户已过期"),
	TEMPLATE_VARIABLE_ERROR(117,"模板变量格式错误"),
	TEMPLATE_ID_ERROR(118,"模板ID不能为空"),
	TEMPLATE_CONTENT_ERROR(119,"模板ID不能为空"),
	GATEWAY_ERROR(120,"网关错误"),
	NO_SMG_ERROR(121,"该国家尚未支持国际验证码"),
	TEMPLATE_ERROR(122,"模板错误，请去提交模板"),
	NULL_SMG_ERROR(123,"短信内容不能为空"),
	LACK_PARAM_ERROR(124,"缺少参数");
	private int statusCode;// 短信发送状态代码
	
	private String remark;// 描述
	
	private InternationalStatusEnum( int statusCode,String remark) {
        this.statusCode = statusCode;
        this.remark = remark;
    }

	/**
	 * 获取短信发送状态代码
	 * */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * 获取短信发送状态说明
	 * */
	public String getRemark() {
		return remark;
	}
}
