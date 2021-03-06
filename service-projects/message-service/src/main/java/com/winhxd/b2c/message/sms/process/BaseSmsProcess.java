package com.winhxd.b2c.message.sms.process;

import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author yaoshuai
 */
public abstract class BaseSmsProcess {

	/**
	 * @param smsSend
	 * @return 0为成功
	 */
	public abstract int sendMessage(MessageSmsHistory smsSend) throws IOException, URISyntaxException;
}
