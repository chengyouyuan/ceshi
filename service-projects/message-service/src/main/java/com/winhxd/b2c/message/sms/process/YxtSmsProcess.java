package com.winhxd.b2c.message.sms.process;

import com.winhxd.b2c.common.context.support.ContextHelper;
import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.message.dao.MessageSmsHistoryMapper;
import com.winhxd.b2c.message.sms.common.SmsConstant;
import com.winhxd.b2c.message.sms.enums.*;
import com.winhxd.b2c.message.sms.model.SmsReturn;
import com.winhxd.b2c.message.sms.security.SecurityCheckUtil;
import com.winhxd.b2c.message.sms.security.SecurityConstant;
import com.winhxd.b2c.message.utils.HttpClientUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创蓝发送短信
 *
 * @author fanzhanzhan
 */
@Component
public class YxtSmsProcess {

	private final Logger logger = LoggerFactory.getLogger(YxtSmsProcess.class);
	private static AtomicInteger counter = new AtomicInteger(1);
	/**
	 * 轮询因子
	 */
	private static final int POLL_FACTOR = 2;
	/**
	 * 国际短信判断
	 */
	private static final String INTERNATIONAL_MESSAGE = "r=";

	/**
	 * 不需补发的短信状态
	 */
	protected static List<Integer> notRetrans = new ArrayList<>();

	@Autowired
	private HttpClientUtil httpClientUtil;

	@Autowired
	private MessageSmsHistoryMapper messageSmsHistoryMapper;

	@Autowired
	private YxSmsProcess yxSmsProcess;

	private YxtSmsProcess() {
		// 私有构造
	}

	static {
		/**
		 * 成功
		 */
		notRetrans.add(SmsReturnStatusEnum.SUCCESS.getStatusCode());
		/**
		 * 敏感短信
		 */
		notRetrans.add(SmsReturnStatusEnum.SENSITIVESMSERROR.getStatusCode());
		/**
		 *  手机号码错误
		 */
		notRetrans.add(SmsReturnStatusEnum.MOBLIEFORMATERROR.getStatusCode());
		notRetrans.add(SmsReturnStatusEnum.MOBLIEFORMATERROR_YX.getStatusCode());
		/**
		 * 安全检查不通过
		 */
		notRetrans.add(SmsReturnStatusEnum.SECURITYCHECKERROR.getStatusCode());
		/**
		 * 消息长度错误
		 */
		notRetrans.add(SmsReturnStatusEnum.SMGTOOLONGERROR.getStatusCode());
		/**
		 * 手机号码个数超限
		 */
		notRetrans.add(SmsReturnStatusEnum.OUTOFMOBLIEBOUNDSERROR.getStatusCode());
	}

	/**
	 * 营销短信 使用创蓝平台发送,漫道作为备用平台
	 * 验证码短信使用创蓝和阅信随机发送 互为备用
	 */
	public SmsReturn sendMessage(MessageSmsHistory smsSend) {
		SmsReturn smsReturn;
		int index = counter.incrementAndGet() % 2;
		if (index < 0) {
			index = -index;
		}

		if (smsSend.getSendType() != SmsTypeEnum.VOICE.getType() && smsSend.getSendType() != SmsTypeEnum.INTERNATIONAL.getType()) {
			smsSend.setContent(SignatureEnum.RETAIL.getRemark() + smsSend.getContent());
		}

		/**
		 * 营销短信使用创蓝平台，验证码短信在创蓝和阅信轮询
		 */
		if (index % POLL_FACTOR == 0 || !smsSend.getContent().contains(SmsConstant.FLAG_VERIFICATION) || smsSend.getSendType() == SmsTypeEnum.INTERNATIONAL.getType()) {
			smsReturn = clSend(smsSend);
		} else {
			smsReturn = yxSmsProcess.sendMessage(smsSend);
		}
		return smsReturn;
	}

	/**
	 * 创蓝平台发送短信验证码
	 */
	private SmsReturn clSend(MessageSmsHistory smsSend) {
		SmsReturn smsReturn;
		if (smsSend.getSendType() == SmsTypeEnum.INTERNATIONAL.getType()) {
			return sendInternationalMessage(smsSend);
		} else {
			smsReturn = sendWithReturn(smsSend.getTelephone(), smsSend.getContent(), smsSend.getSupplyId());
		}

		/**
		 * 如果创蓝发送失败 验证码短信使用阅信补发，营销短信使用漫道补发
		 */
		if (!notRetrans.contains(smsReturn.getStatus().getStatusCode())) {
			// 记录失败日志
			saveSend(smsReturn.getSmsSupplier().getAccount(), smsSend.getContent(), smsSend.getTelephone(), smsReturn.getStatus().getStatusCode());

			if (smsSend.getContent().contains(SmsConstant.FLAG_VERIFICATION)) {
				logger.info("创蓝短信发送失败,使用阅信平台发送：" + smsReturn.getStatus().getStatusCode());
				smsReturn = yxSmsProcess.sendMessage(smsSend);

				if (!YxtSmsProcess.notRetrans.contains(smsReturn.getStatus().getStatusCode())) {
					logger.info("阅信发送失败, 改用漫道发送");
					ManDaoRetailProcess mandao = new ManDaoRetailProcess();
					int rs = mandao.sendMessage(smsSend);
					smsReturn = analysisMandaoReturn(rs);
				}
			} else {
				logger.info("创蓝短信发送失败,使用漫道平台发送：" + smsReturn.getStatus().getStatusCode());
				ManDaoRetailProcess mandao = new ManDaoRetailProcess();
				int rs = mandao.sendMessage(smsSend);
				smsReturn = analysisMandaoReturn(rs);
			}
		}
		logger.info("创蓝短信发送====" + JsonUtil.toJSONString(smsReturn));
		return smsReturn;
	}

	/**
	 * 发送国际短信
	 */
	public SmsReturn sendInternationalMessage(MessageSmsHistory smsSend) {
		SmsReturn smsReturn;
		SmsSupplierEnum smsSupplier = SmsSupplierEnum.international_cl;
		try {
			List<NameValuePair> naps = new ArrayList<NameValuePair>();
			naps.add(new BasicNameValuePair("un", smsSupplier.getAccount()));
			naps.add(new BasicNameValuePair("pw", smsSupplier.getPwd()));
			naps.add(new BasicNameValuePair("da", smsSend.getTelephone()));
			naps.add(new BasicNameValuePair("dc", "15"));
			naps.add(new BasicNameValuePair("rf", "1"));
			naps.add(new BasicNameValuePair("tf", "3"));
			naps.add(new BasicNameValuePair("sm", smsSend.getContent()));

			CloseableHttpClient httpClient = httpClientUtil.getHttpClient();
			HttpPost httpPost = new HttpPost(smsSupplier.getUrl() + "mt");
			HttpResponse response = httpClient.execute(httpPost);
			String rs = EntityUtils.toString(response.getEntity(), ContextHelper.UTF_8);
			smsReturn = analysisInternationalReturn(rs);
			smsReturn.setSmsSupplier(smsSupplier);
		} catch (IOException e) {
			smsReturn = new SmsReturn();
			smsReturn.setSmsSupplier(smsSupplier);
			smsReturn.setStatus(SmsReturnStatusEnum.HTTPCLIENTERROR);
			logger.error("国际短信异常", e);
		}
		logger.info("创蓝国际短信发送====" + JsonUtil.toJSONString(smsReturn));
		return smsReturn;
	}


	/**
	 * 解析漫道短信返回结果
	 */
	public SmsReturn analysisMandaoReturn(int rs) {
		SmsReturn smsReturn = new SmsReturn();
		smsReturn.setSmsSupplier(SmsSupplierEnum.mandaoRetail);
		if (rs == SmsReturnStatusEnum.SUCCESS.getStatusCode()) {
			smsReturn.setStatus(SmsReturnStatusEnum.SUCCESS);
		} else {
			smsReturn.setStatus(SmsReturnStatusEnum.FAIL_MANDAO);
		}
		logger.info("漫道短信发送结果" + JsonUtil.toJSONString(smsReturn));
		return smsReturn;
	}

	/**
	 * 保存失败记录
	 */
	public void saveSend(String supplier, String content, String telephoneNO, int errorCode) {
		MessageSmsHistory tSMSSend = new MessageSmsHistory();
		tSMSSend.setSupplyId(supplier);
		tSMSSend.setContent(content);
		tSMSSend.setSendTime(new Date());
		tSMSSend.setTelephone(telephoneNO);
		tSMSSend.setErrorCode((short) errorCode);
		tSMSSend.setSendStatus((short) SmsSendStatusEnum.FAIL.getCode());
		messageSmsHistoryMapper.insert(tSMSSend);
	}

	/**
	 * 用创蓝发送短信 带返回 结果信息对象 的短信发送
	 *
	 * @param mobile  发信发送的目的号码.多个号码之间用半角逗号隔开
	 * @param content 短信的内容
	 * @param type    短信类型
	 * @return SMSReturn 短信发送返回信息对象
	 */
	protected SmsReturn sendWithReturn(String mobile, String content, String type) {
		SmsReturn smsReturn = null;
		SmsSupplierEnum smsSupplier = null;
		try {

			if (content != null && content.trim().length() > 0) {
				// 非验证码短信 使用营销账号
				if (content.indexOf(SmsConstant.SUFFIX_MARKETING) > -1) {
					smsSupplier = SmsSupplierEnum.marketing_cl;
					logger.info("类型：营销短信。手机号：" + mobile + ",短信内容content： " + content);
				} else if (content.indexOf(SmsConstant.FLAG_VERIFICATION) == -1) {
					smsSupplier = SmsSupplierEnum.mixed_cl;
					logger.info("类型：通知短信。手机号：" + mobile + ",短信内容content： " + content);
				} else if (String.valueOf(SmsTypeEnum.VOICE.getType()).equals(type)) {
					smsSupplier = SmsSupplierEnum.voice_cl;
					logger.info("类型：语音短信。手机号：" + mobile + ",短信内容content： " + content);
				} else {
					smsSupplier = SmsSupplierEnum.verification_cl;
					logger.info("类型：验证码短信短信。手机号：" + mobile + ",短信内容content：" + content);
				}
				logger.info(smsSupplier.getAccount() + "," + smsSupplier.getUrl() + SmsConstant.HTTPBATCH);

				CloseableHttpClient httpClient = httpClientUtil.getHttpClient();
				HttpPost httpPost = new HttpPost(new URI(smsSupplier.getUrl() + SmsConstant.HTTPBATCH));

				List<NameValuePair> nvps = new ArrayList<>();
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_ACCOUNT, smsSupplier.getAccount()));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_PSWD, smsSupplier.getPwd()));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_MOBILE, mobile));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_NEEDSTATUS, String.valueOf(SmsConstant.NEEDSTATUS)));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_CONTENT, content));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, ContextHelper.UTF_8));

				HttpResponse response = httpClient.execute(httpPost);
				String rs = EntityUtils.toString(response.getEntity(), ContextHelper.UTF_8);
				smsReturn = analysisReturn(rs);
				smsReturn.setSmsSupplier(smsSupplier);
			} else {
				smsReturn = new SmsReturn();
				smsReturn.setStatus(SmsReturnStatusEnum.SMGTOOLONGERROR);
			}
		} catch (Exception e) {
			smsReturn = new SmsReturn();
			smsReturn.setSmsSupplier(smsSupplier);
			smsReturn.setStatus(SmsReturnStatusEnum.HTTPCLIENTERROR);
			logger.error("创蓝短信发送异常。", e);
		}
		return smsReturn;
	}

	public SmsReturn analysisInternationalReturn(String rs) {
		SmsReturn smsReturn = new SmsReturn();
		String[] arrayRS;
		if (rs.indexOf(INTERNATIONAL_MESSAGE) > -1) {
			arrayRS = rs.split(INTERNATIONAL_MESSAGE);
			int rsStatus = Integer.parseInt(arrayRS[1]);
			for (InternationalStatusEnum status : InternationalStatusEnum.values()) {
				if (status.getStatusCode() == rsStatus) {
					smsReturn.setInternationalstatus(status);
					break;
				}
			}
			logger.info("国际短信发送失败：" + rs);
		} else {
			smsReturn.setInternationalstatus(InternationalStatusEnum.SUCCESS);
		}
		return smsReturn;
	}


	/**
	 * 解析短信发送结果
	 *
	 * @param rs 短信发送返回结果
	 * @return SmsReturn 短信发送返回对象
	 */
	public SmsReturn analysisReturn(String rs) {
		SmsReturn smsReturn = new SmsReturn();
		String[] arrayRS;
		String finishTime;// 短信发送完成时间

		if (StringUtils.isNotEmpty(rs) && rs.indexOf(SecurityConstant.SPLIT) > -1) {
			arrayRS = rs.split(SmsConstant.SPLIT_WRAP);

			int rsStatus = Integer.parseInt(arrayRS[0].split(SecurityConstant.SPLIT)[1]);
			for (SmsReturnStatusEnum status : SmsReturnStatusEnum.values()) {
				if (status.getStatusCode() == rsStatus) {
					smsReturn.setStatus(status);
					logger.warn("创蓝短信发送结果：" + status.getRemark());
					break;
				}
			}
			finishTime = SecurityCheckUtil.formatDateYMDHMS(arrayRS[0].split(SecurityConstant.SPLIT)[0]);
			smsReturn.setFinishTime(finishTime);

			if (rsStatus == SmsReturnStatusEnum.SUCCESS.getStatusCode()) {
				smsReturn.setMsgid(arrayRS[1]);
			}
		} else {
			smsReturn.setStatus(SmsReturnStatusEnum.SYSTEMERROR);
		}

		return smsReturn;
	}
}
