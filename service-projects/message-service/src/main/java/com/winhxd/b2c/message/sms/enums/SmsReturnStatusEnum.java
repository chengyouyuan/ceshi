package com.winhxd.b2c.message.sms.enums;

/**
 * 短信发送返回状态枚举
 * */
public enum SmsReturnStatusEnum {
	/**
	 * 短信发送返回状态枚举
	 */
	SUCCESS(0,"短信发送成功"),
	SECURITYCHECKERROR(98,"安全检查不通过"),
	HTTPCLIENTERROR(99,"HttpClient请求错误"),
	USERNOTFOUNDERROR(101,"无此用户"),
	PASSWORDERROR(102,"密码错误"),
	SUBMITOVERSPEEDERROR(103,"提交过快(提交速度超过流速限制)"),
	SYSTEMERROR(104,"系统繁忙(因平台侧原因，暂时无法处理提交的短信)"),
	SENSITIVESMSERROR(105,"敏感短信(短信内容包含敏感词)"),
	SMGTOOLONGERROR(106,"消息长度错误(>536或<=0)"),
	MOBLIEFORMATERROR(107,"包含错误的手机号码"),
	OUTOFMOBLIEBOUNDSERROR(108,"手机号码个数超限(群发>50000或<=0;单发>200或<=0)"),
	NOTENOUGHAMOUNTERROR(109,"余额不足(该用户可用短信数已使用完)"),
	NOTINSENDTIMEERROR(110,"不在发送时间内"),
	OUTOFMONTHLYERROR(111,"超出该账户当月发送额度限制"),
	NOPRODUCTERROR(112,"无此产品,用户没有订购该产品"),
	EXTNOFORMATERROR(113,"extno格式错(非数字或者长度不对)"),
	NOTAPPROVEDERROR(115,"自动审核驳回"),
	SIGNIDENTIFYERROR(116,"签名不合法，未带签名(用户必须带签名的前提下)"),
	IPIDENTIFYERROR(117,"IP地址认证错,请求调用的IP地址不是系统登记的IP地址"),
	NOPERMISSIONERROR(118,"用户没有相应的发送权限"),
	USEROVERDUEERROR(119,"用户已过期"),
	NOTINWHITELISTERROR(120,"测试内容不是白名单"),
	PARAMETERERROR(1,"阅信平台参数异常"),
	MOBLIEFORMATERROR_YX(2,"包含错误的手机号码"),
	EXTENDPARAMETERERROR(3,"阅信平台扩展参数异常"),
	SENDTIMEERROR(4,"发送时间参数异常"),
	CONTENTERROR(5,"短信内容解析异常"),
	IPIDENTIFYERROR_YX(10,"IP认证失败"),
	USERERROR_YX(11,"帐号密码认证失败"),
	FAIL_MANDAO(-1,"漫道短信发送失败");
	private int statusCode;// 短信发送状态代码
	
	private String remark;// 描述
	
	SmsReturnStatusEnum(int statusCode, String remark) {
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
