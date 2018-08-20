package com.winhxd.b2c.message.sms.model;

/**
 * @ClassName: SmsSend
 * @Description: 发送短信实体
 * @Author fanzhanzhan
 * @Date 2018-08-20 10:43
 **/
public class SmsSend {
	/**
	 * 发送短信的手机号
	 */
	private String telePhoneNo;
	/**
	 * 短信内容
	 */
	private String content;
	/**
	 * 短信类型
	 */
	private String type;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 平台
	 */
	private String grp;

	public String getTelePhoneNo() {
		return telePhoneNo;
	}

	public void setTelePhoneNo(String telePhoneNo) {
		this.telePhoneNo = telePhoneNo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGrp() {
		return grp;
	}

	public void setGrp(String grp) {
		this.grp = grp;
	}
}