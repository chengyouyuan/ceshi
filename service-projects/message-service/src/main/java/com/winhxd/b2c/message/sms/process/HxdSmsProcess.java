package com.winhxd.b2c.message.sms.process;

import com.winhxd.b2c.common.context.support.ContextHelper;
import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.message.sms.common.SmsConstant;
import com.winhxd.b2c.message.sms.enums.SmsReturnStatusEnum;
import com.winhxd.b2c.message.sms.enums.SmsSupplierEnum;
import com.winhxd.b2c.message.sms.enums.SmsTypeEnum;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 创蓝发送短信
 * 
 * */
public class HxdSmsProcess extends SmsProcess {
	private static final Logger logger = LoggerFactory.getLogger(HxdSmsProcess.class);
    //private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	@Autowired
	private static HttpClientUtil httpClientUtil;

	@Override
	public int sendMessage(MessageSmsHistory smsSend) {
		SmsReturn smsReturn = HxdSmsProcess.sendWithReturn(smsSend.getTelephone(), smsSend.getContent(),smsSend.getSupplyId());

		return smsReturn.getStatus().getStatusCode();
	}

	/**
	 * 短信发送
	 * 
	 * @param mobile
	 *            发信发送的目的号码.多个号码之间用英文半角逗号( , )隔开
	 * @param content
	 *            短信的内容
	 * @return 发送成功返回true,失败返回false.
	 * */
	protected static boolean send(String mobile, String content,String supplyId) {
		boolean result = false;
		SmsReturn smsReturn = HxdSmsProcess.sendWithReturn(mobile, content,supplyId);
		if (smsReturn.getStatus() == SmsReturnStatusEnum.SUCCESS) {
			result = true;
		}

		return result;
	}

	/**
	 * 发送短信
	 * 
	 * @param mobile
	 *            手机号码，多个号码使用","分割
	 * @param content 短信内容
	 * @return 返回值定义参见HTTP协议文档
	 * @throws Exception
	 * */
	protected static SmsReturn sendWithReturn(String mobile, String content,String type) {
        SmsReturn smsReturn = null;
        SmsSupplierEnum smsSupplier = null;
        try {
            if (content != null && content.trim().length() > 0 ) {
                // 非验证码短信 使用营销账号
                if (content.indexOf(SmsConstant.FLAG_VERIFICATION) == -1 ) {
                    smsSupplier = SmsSupplierEnum.mixed_cl;
                    logger.info("类型：营销短信。手机号：" + mobile +",短信内容content： " + content);
                }
                else if (String.valueOf(SmsTypeEnum.VOICE.getType()).equals(type)) {
                    smsSupplier = SmsSupplierEnum.voice_cl;
                    logger.info("类型：语音短信。手机号：" + mobile +",短信内容content： " + content);
                }
                else {
                    smsSupplier = SmsSupplierEnum.verification_cl;
                    logger.info("类型：验证码短信短信。手机号：" + mobile +",短信内容content：" + content);
                }
                logger.info(smsSupplier.getAccount()+","+smsSupplier.getUrl());
                
                // 请求参数
				List<NameValuePair> nvps = new ArrayList<>();
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_ACCOUNT, smsSupplier.getAccount()));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_PSWD, smsSupplier.getPwd()));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_MOBILE, mobile));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_NEEDSTATUS, String.valueOf(SmsConstant.NEEDSTATUS)));
				nvps.add(new BasicNameValuePair(SmsConstant.KEY_CONTENT, content));

				CloseableHttpClient httpClient = httpClientUtil.getHttpClient();
				HttpPost httpPost = new HttpPost(smsSupplier.getUrl());
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, ContextHelper.UTF_8));
				HttpResponse response = httpClient.execute(httpPost);
				//String rs = HttpClientUtil.getSend(smsSupplier.getUrl(), SmsConstant.HTTPBATCH, pair);
				String rs = EntityUtils.toString(response.getEntity(), ContextHelper.UTF_8);
                smsReturn = analysisReturn(rs);
                smsReturn.setSmsSupplier(smsSupplier);
            }
            else {
                smsReturn = new SmsReturn();
                smsReturn.setStatus(SmsReturnStatusEnum.SMGTOOLONGERROR);
            }
        }
        catch (Exception e) {
            smsReturn = new SmsReturn();
            smsReturn.setSmsSupplier(smsSupplier);
            smsReturn.setStatus(SmsReturnStatusEnum.HTTPCLIENTERROR);
            logger.error("创蓝短信发送异常。", e);
        }
        logger.info("短信结果-------:"+ JsonUtil.toJSONString(smsReturn));
        return smsReturn;
    }

    /**
     * 解析短信发送结果
     * @param rs 短信发送返回结果
     * @return SmsReturn 短信发送返回对象
     * */
    public static SmsReturn analysisReturn(String rs) {
        SmsReturn smsReturn = new SmsReturn();
        String[] arrayRS ;
        String finishTime;// 短信发送完成时间
        logger.info(" 解析短信发送结果-------"+rs );
        if (StringUtils.isNotEmpty(rs) && rs.indexOf(SecurityConstant.SPLIT) > -1) {
            arrayRS = rs.split(SmsConstant.SPLIT_WRAP);

            int rsStatus = Integer.parseInt(arrayRS[0].split(SecurityConstant.SPLIT)[1]);
            for (SmsReturnStatusEnum status : SmsReturnStatusEnum.values()) {
                if (status.getStatusCode() == rsStatus) {
                    smsReturn.setStatus(status);
                    break;
                }
            }
            finishTime = SecurityCheckUtil.formatDateYMDHMS(arrayRS[0].split(SecurityConstant.SPLIT)[0]);
            smsReturn.setFinishTime(finishTime);

            if (rsStatus == SmsReturnStatusEnum.SUCCESS.getStatusCode()) {
                smsReturn.setMsgid(arrayRS[1]);
            }
        }
        else {
            smsReturn.setStatus(SmsReturnStatusEnum.SYSTEMERROR);
        }
        logger.info(" 解析短信结果-------"+ JsonUtil.toJSONString(smsReturn) );
        return smsReturn;
    }
}
