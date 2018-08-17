package com.winhxd.b2c.message.sms.process;

import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;

/**
 * @author yaoshuai
 */
public abstract class SmsProcess {

	/**
	 * @param smsSend
	 * @return 0为成功
	 */
	public abstract int sendMessage(MessageSmsHistory smsSend);
}
