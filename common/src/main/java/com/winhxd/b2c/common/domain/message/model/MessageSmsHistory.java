package com.winhxd.b2c.common.domain.message.model;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName: MessageSmsHistory
 * @Description: 短信发送历史
 * @Author fanzhanzhan
 * @Date 2018-08-17 14:21
 **/
@Data
public class MessageSmsHistory {


	private Long id;

	/**
	 * 发送短信内容
	 */
	private String content;

	/**
	 * 发送时间
	 */
	private Date sendTime;

	/**
	 * 手机接受时间    发送报告返回
	 */
	private Date reviceTime;

	/**
	 * 发送短信号码
	 */
	private String telephone;

	/**
	 * 发送未成功的错误编码
	 */
	private String errorCode;

	/**
	 * 发送失败重试次数
	 */
	private long repeatTime;

	/**
	 * 发送是否成功 1为成功，0为失败   2:未发送
	 */
	private int sendStatus;

	/**
	 * 短信类型 参见SmsTypeEnum
	 */
	private String supplyId;

	/**
	 * 发送类型   1为同步发送，0为异步发送
	 */
	private int sendType;

	/**
	 * 租户ID
	 */
	private String grp;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 异步发送时间
	 */
	private Date scheduledDate;

	private long storeId;

	private String smsSendGroupid;
}
