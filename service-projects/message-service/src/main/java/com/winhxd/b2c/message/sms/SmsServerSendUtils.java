package com.winhxd.b2c.message.sms;

import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.message.dao.MessageSmsHistoryMapper;
import com.winhxd.b2c.message.sms.enums.SmsReturnStatusEnum;
import com.winhxd.b2c.message.sms.enums.SmsSendStatusEnum;
import com.winhxd.b2c.message.sms.model.SmsReturn;
import com.winhxd.b2c.message.sms.process.YxtSmsProcess;
import com.winhxd.b2c.message.sms.security.SecurityCheckService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 发送短信入口
 */
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
	public void sendSms(String msg) {
		Map<String, Object> json = JsonUtil.parseJSONObject(msg);
		String telePhoneNo = null;
		String content = null;
		String type = null;
		String username = null;
		String grp = null;
		if (json.containsKey("telePhoneNo")) {
			telePhoneNo = json.get("telePhoneNo").toString();
		}
		if (json.containsKey("content")) {
			content = json.get("content").toString();
		}
		if (json.containsKey("type")) {
			type = json.get("type").toString();
		}
		if (json.containsKey("username")) {
			username = json.get("username").toString();
		}
		if (json.containsKey("grp")) {
			grp = json.get("grp").toString();
		}

		List<MessageSmsHistory> list = new ArrayList<>();
		MessageSmsHistory tSmsSendHistory = new MessageSmsHistory();
		if (StringUtils.isBlank(type)) {
			type = "1";
		}
		tSmsSendHistory.setSendType(Integer.parseInt(type));
		tSmsSendHistory.setSupplyId(type);
		tSmsSendHistory.setContent(content);
		tSmsSendHistory.setSendTime(new Date());
		tSmsSendHistory.setTelephone(telePhoneNo);
		tSmsSendHistory.setUserName(username);
		tSmsSendHistory.setGrp(grp);
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
			tSmsSendHistory.setSendStatus(SmsSendStatusEnum.SUCCESS.getCode());
		} else {
			tSmsSendHistory.setSendStatus(SmsSendStatusEnum.FAIL.getCode());
		}

		tSmsSendHistory.setErrorCode(String.valueOf(result));
		list.add(tSmsSendHistory);
		/**
		 * 保存到数据库中
		 */
		messageSmsHistoryMapper.insertBatch(list);
	}
}
