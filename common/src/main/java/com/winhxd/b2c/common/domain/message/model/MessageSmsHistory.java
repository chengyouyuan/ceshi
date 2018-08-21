package com.winhxd.b2c.common.domain.message.model;

import lombok.Data;

import java.util.Date;

/**
 * 短信消息表
 *
 * @author fanzhanzhan
 * @date 2018/8/18 14:50
 */
@Data
public class MessageSmsHistory {
	private Long id;

	private String content;

	private Date sendTime;

	private Date reviceTime;

	private String telephone;

	private Short errorCode;

	private Short sendStatus;

	private String supplyId;

	private Short sendType;

	private String grp;

	private String userName;

	private Date scheduledDate;

	private Long storeId;

	private String smsSendGroupid;

	private String repeatTime;
}