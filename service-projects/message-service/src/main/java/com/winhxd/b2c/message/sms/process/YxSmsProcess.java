package com.winhxd.b2c.message.sms.process;

import com.winhxd.b2c.common.context.support.ContextHelper;
import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.message.sms.common.SmsConstant;
import com.winhxd.b2c.message.sms.enums.SmsReturnStatusEnum;
import com.winhxd.b2c.message.sms.enums.SmsSupplierEnum;
import com.winhxd.b2c.message.sms.enums.SmsTypeEnum;
import com.winhxd.b2c.message.sms.model.SmsReturn;
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

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 阅信平台发送短信
 * @author fanzhanzhan
 */
@Component
public class YxSmsProcess {
	private static final Logger LOGGER = LoggerFactory.getLogger(YxSmsProcess.class);
	//private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	@Autowired
	private HttpClientUtil httpClientUtil;

	@Autowired
	private YxtSmsProcess yxtSmsProcess;

	private YxSmsProcess() {
		//构造私有
	}

	public SmsReturn sendMessage(MessageSmsHistory smsSend) {
		SmsReturn smsReturn = sendWithReturn(smsSend.getTelephone(), smsSend.getContent(), smsSend.getSupplyId());
		// 如果阅信发送失败 使用创蓝平台发送
		if (!YxtSmsProcess.notRetrans.contains(smsReturn.getStatus().getStatusCode())) {
			yxtSmsProcess.saveSend(smsReturn.getSmsSupplier().getAccount(), smsSend.getContent(), smsSend.getTelephone(), smsReturn.getStatus().getStatusCode());
			LOGGER.info("阅信短信发送失败,使用创蓝平台发送：" + smsReturn.getStatus().getStatusCode());
			smsReturn = yxtSmsProcess.sendWithReturn(smsSend.getTelephone(), smsSend.getContent(), smsSend.getSupplyId());
			LOGGER.info("创蓝短信发送结果：" + JsonUtil.toJSONString(smsReturn));
		}
		else {
			LOGGER.info("阅信短信发送结果：" + JsonUtil.toJSONString(smsReturn));
		}
		return smsReturn;
	}

	/**
	 * 用阅信发送短信 带返回 结果信息对象 的短信发送
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
			String mttime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String sentContent = content;
			int contentLength = 4;
			if (content != null && content.trim().length() > contentLength
					&& content.indexOf(SmsConstant.FLAG_VERIFICATION) > -1) {
				if (String.valueOf(SmsTypeEnum.VOICE.getType()).equals(type)) {
					smsSupplier = SmsSupplierEnum.voice_yx;
					// 阅信语音验证码 内容只能是全数字 取最后六位
					// sentContent = content.substring(0,content.length());
					//2018.06.21 wangzekun 修改  阅信语音验证码只能是全数字 以前的截取有问题 现在修改为过滤掉汉字 只保留数字
					String regEx = "[^0-9]";
					Pattern p = Pattern.compile(regEx);
					Matcher m = p.matcher(content);
					sentContent = m.replaceAll("").trim();
					LOGGER.info("类型：语音短信。手机号：" + mobile + ",短信内容content:" + sentContent);
				} else {
					smsSupplier = SmsSupplierEnum.verification_yx;
					LOGGER.info("类型：验证码短信短信。手机号：" + mobile + ",短信内容content：" + content);
				}
				LOGGER.info(smsSupplier.getAccount() + "," + smsSupplier.getUrl());

				List<NameValuePair> nvps = new ArrayList<>();
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_NAME_YX, smsSupplier.getAccount()));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_PSWD_YX, md5Digest(smsSupplier.getPwd() + mttime)));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_CONTENT_YX, URLEncoder.encode(sentContent, ContextHelper.UTF_8)));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_PHONE_YX, mobile));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_MTTIME_YX, mttime));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_SUBID_YX, ""));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_RPTTYPE_YX, SmsConstant.RPTTYPE_JSON));

				CloseableHttpClient httpClient = httpClientUtil.getHttpClient();
				HttpPost httpPost = new HttpPost(smsSupplier.getUrl());
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
			smsReturn.setStatus(SmsReturnStatusEnum.HTTPCLIENTERROR);
			smsReturn.setSmsSupplier(smsSupplier);
			LOGGER.error("阅信短信发送结果异常。", e);
		}
		LOGGER.info("阅信短信发送结果====" + JsonUtil.toJSONString(smsReturn));
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
		if (StringUtils.isNotBlank(rs)) {
			Map<String, Object> obj = JsonUtil.parseJSONObject(rs);
			String reqCode = obj.get("ReqCode").toString();
			String reqId = obj.get("ReqId").toString();
			for (SmsReturnStatusEnum status : SmsReturnStatusEnum.values()) {
				if (status.getStatusCode() == Integer.parseInt(reqCode)) {
					smsReturn.setStatus(status);
					LOGGER.warn("阅信短信发送结果：" + status.getRemark());
					break;
				}
			}
			smsReturn.setMsgid(reqId);
		}
		return smsReturn;
	}

	private String md5Digest(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			char[] charArray = str.toCharArray();
			byte[] byteArray = new byte[charArray.length];
			for (int i = 0; i < charArray.length; i++) {
				byteArray[i] = (byte) charArray[i];
			}
			byte[] md5Bytes = md5.digest(byteArray);
			StringBuilder hexValue = new StringBuilder();
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
		} catch (Exception e) {
			LOGGER.error("加密异常", e);
			return "";
		}
	}
}
