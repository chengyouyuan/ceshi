package com.winhxd.b2c.message.sms;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.SMSCondition;
import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;
import com.winhxd.b2c.common.feign.message.SmsHxdServiceClient;
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

	//統一改成惠下单短信服务
	@Autowired
	private SmsHxdServiceClient smsHxdServiceClient;

	/**
	 * 调用惠下单服务发送短信
	 *
	 * @param smsCondition 包含手机号、短信内容、类型、用户名、平台等信息
	 */
	public void sendSmsByHxd(SMSCondition smsCondition) {
		String type = "1";
		smsCondition.setType(type);
		if (StringUtils.isBlank(smsCondition.getTelePhoneNo())) {
			smsCondition.setTelePhoneNo(smsCondition.getMobile());
		}
		List<MessageSmsHistory> list = new ArrayList<>();
		MessageSmsHistory tSmsSendHistory = new MessageSmsHistory();
		tSmsSendHistory.setSendType(Short.parseShort(type));
		tSmsSendHistory.setSupplyId(type);
		tSmsSendHistory.setContent(smsCondition.getContent());
		tSmsSendHistory.setSendTime(new Date());
		tSmsSendHistory.setTelephone(smsCondition.getTelePhoneNo());
		ResponseResult result = smsHxdServiceClient.sendSmsAsyncByCondition(smsCondition);
		if (result.getCode() == SmsReturnStatusEnum.SUCCESS.getStatusCode() || result.getCode() == 200) {
			tSmsSendHistory.setSendStatus((short) SmsSendStatusEnum.SUCCESS.getCode());
			tSmsSendHistory.setErrorCode((short) SmsSendStatusEnum.SUCCESS.getCode());
		} else {
			tSmsSendHistory.setSendStatus((short) SmsSendStatusEnum.FAIL.getCode());
			tSmsSendHistory.setErrorCode((short)result.getCode());
		}
		list.add(tSmsSendHistory);
		/**
		 * 保存到数据库中
		 */
		messageSmsHistoryMapper.insertBatch(list);
	}
	/**
	 * 发送短信
	 *
	 * @param msg 包含手机号、短信内容、类型、用户名、平台等信息
	 */
	public void sendSms(SmsSend msg) {
		String type = StringUtils.isBlank(msg.getType()) ? "1" : msg.getType();
		int result;
		boolean security;

		List<MessageSmsHistory> list = new ArrayList<>();
		MessageSmsHistory tSmsSendHistory = new MessageSmsHistory();
		tSmsSendHistory.setSendType(Short.parseShort(type));
		tSmsSendHistory.setSupplyId(type);
		tSmsSendHistory.setContent(msg.getContent());
		tSmsSendHistory.setSendTime(new Date());
		tSmsSendHistory.setTelephone(msg.getTelePhoneNo());
		tSmsSendHistory.setUserName(msg.getUsername());
		tSmsSendHistory.setGrp(msg.getGrp());
		security = securityCheckService.securityCheck(tSmsSendHistory.getTelephone());
		if (security) {
			SmsReturn smsReturn = yxtSmsProcess.sendMessage(tSmsSendHistory);
			result = smsReturn.getStatus().getStatusCode();
			tSmsSendHistory.setSupplyId(smsReturn.getSmsSupplier().getAccount());
		} else {
			result = SmsReturnStatusEnum.SECURITYCHECKERROR.getStatusCode();
		}

		if (result == SmsReturnStatusEnum.SUCCESS.getStatusCode()) {
			tSmsSendHistory.setSendStatus((short) SmsSendStatusEnum.SUCCESS.getCode());
		} else {
			tSmsSendHistory.setSendStatus((short) SmsSendStatusEnum.FAIL.getCode());
		}

		tSmsSendHistory.setErrorCode((short) result);
		list.add(tSmsSendHistory);
		/**
		 * 保存到数据库中
		 */
		messageSmsHistoryMapper.insertBatch(list);
	}
}
