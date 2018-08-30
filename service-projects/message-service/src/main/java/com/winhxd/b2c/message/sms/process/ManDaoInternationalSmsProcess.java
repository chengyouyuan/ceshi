package com.winhxd.b2c.message.sms.process;

import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;
import com.winhxd.b2c.message.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ManDaoInternationalSmsProcess extends BaseSmsProcess {

	@Autowired
	private HttpClientUtil httpClientUtil;

	@Override
	public int sendMessage(MessageSmsHistory smsSend) throws IOException, URISyntaxException {
		Client client = new Client();
		return client.sendMessage(smsSend.getTelephone(), smsSend.getContent());
	}

	class Client {

		private String serviceURL = "http://sdk2.entinfo.cn:8060/gjWebService.asmx/mdSmsSend_g";

		private String sn = "SDK-TML-010-00287";// 序列号
		private String password = "336234";
		private String pwd = "";// 密码

		/*
		 * 构造函数
		 */
		public Client() throws UnsupportedEncodingException {
			this.pwd = this.getMD5(sn + password);
		}

		/*
		 * 方法名称：getMD5 功 能：字符串MD5加密 参 数：待转换字符串 返 回 值：加密之后字符串
		 */
		public String getMD5(String sourceStr) throws UnsupportedEncodingException {
			String resultStr = "";
			try {
				byte[] temp = sourceStr.getBytes();
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(temp);
				// resultStr = new String(md5.digest());
				byte[] b = md5.digest();
				for (int i = 0; i < b.length; i++) {
					char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
					char[] ob = new char[2];
					ob[0] = digit[(b[i] >>> 4) & 0X0F];
					ob[1] = digit[b[i] & 0X0F];
					resultStr += new String(ob);
				}
				return resultStr;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			}
		}

		public int sendMessage(String telephone, String smsContent) throws IOException, URISyntaxException {
			int resReturn = -1;
			serviceURL = serviceURL + "?sn=" + sn + "&pwd=" + pwd + "&mobile=" + telephone + "&content=" + smsContent + "&ext=1&stime=&rrid=";
			String result = httpClientUtil.doGet(serviceURL);
			if (result == null) {
				resReturn = 0;
			}
			return resReturn;
		}

	}

}
