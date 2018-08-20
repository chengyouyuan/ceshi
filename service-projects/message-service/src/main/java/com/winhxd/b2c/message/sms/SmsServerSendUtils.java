package com.winhxd.b2c.message.sms;

import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;
import com.winhxd.b2c.message.dao.MessageSmsHistoryMapper;
import com.winhxd.b2c.message.sms.enums.SmsReturnStatusEnum;
import com.winhxd.b2c.message.sms.enums.SmsSendStatusEnum;
import com.winhxd.b2c.message.sms.model.SmsReturn;
import com.winhxd.b2c.message.sms.model.SmsSend;
import com.winhxd.b2c.message.sms.process.YxtSmsProcess;
import com.winhxd.b2c.message.sms.security.SecurityCheckService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: SmsServerSendUtils
 * @Description: 发送短信工具类
 * @Author fanzhanzhan
 * @Date 2018-08-20 10:43
 **/
@Component
public class SmsServerSendUtils {

	@Autowired
	private SecurityCheckService securityCheckService;

	@Autowired
	private MessageSmsHistoryMapper messageSmsHistoryMapper;

	@Autowired
	private YxtSmsProcess yxtSmsProcess;
	/**
	 * 发送短信
	 * @param msg  包含手机号、短信内容、类型、用户名、平台等信息
	 */
	public void sendSms(SmsSend msg) {
		String type = null;

		List<MessageSmsHistory> list = new ArrayList<>();
		MessageSmsHistory tSmsSendHistory = new MessageSmsHistory();
		if (StringUtils.isBlank(type)) {
			type = "1";
		}
		tSmsSendHistory.setSendType(Short.parseShort(type));
		tSmsSendHistory.setSupplyId(type);
		tSmsSendHistory.setContent(msg.getContent());
		tSmsSendHistory.setSendTime(new Date());
		tSmsSendHistory.setTelephone(msg.getTelePhoneNo());
		tSmsSendHistory.setUserName(msg.getUsername());
		tSmsSendHistory.setGrp(msg.getGrp());
		int result;
		boolean security = securityCheckService.securityCheck(tSmsSendHistory.getTelephone());
		if (security) {
			SmsReturn smsReturn = yxtSmsProcess.sendMessage(tSmsSendHistory);
			result = smsReturn.getStatus().getStatusCode();
			tSmsSendHistory.setSupplyId(smsReturn.getSmsSupplier().getAccount());
		} else {
			result = SmsReturnStatusEnum.SECURITYCHECKERROR.getStatusCode();
		}

		if (result == SmsReturnStatusEnum.SUCCESS.getStatusCode()) {
			tSmsSendHistory.setSendStatus((short)SmsSendStatusEnum.SUCCESS.getCode());
		} else {
			tSmsSendHistory.setSendStatus((short)SmsSendStatusEnum.FAIL.getCode());
		}

		tSmsSendHistory.setErrorCode((short)result);
		list.add(tSmsSendHistory);
		/**
		 * 保存到数据库中
		 */
		messageSmsHistoryMapper.insertBatch(list);
	}
}
