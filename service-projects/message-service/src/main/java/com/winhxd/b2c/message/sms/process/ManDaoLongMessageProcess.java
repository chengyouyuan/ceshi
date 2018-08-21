package com.winhxd.b2c.message.sms.process;

import com.winhxd.b2c.common.context.support.ContextHelper;
import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;
import com.winhxd.b2c.message.sms.common.SmsConstant;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManDaoLongMessageProcess extends BaseSmsProcess {

	@Override
	public int sendMessage(MessageSmsHistory smsSend) {
		System.out.println("$$$$$$$$$$$$$$$$$1111");
//		String pathjpg = "e:\\111.gif";
		String title = null;
		try {
			title = new String("彩信".getBytes(), ContextHelper.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String txtbase64String = "";
		try {
			txtbase64String = Base64.getEncoder().encodeToString(smsSend.getContent().getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String content = "1_1.txt," + txtbase64String;    //"1_2.gif," + jpgbase64String + ";" +
		String stime = "";
		MdMmsSend mms = new MdMmsSend();
		String result = mms.mdMmsSend(title, smsSend.getTelephone(), content, stime);
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$" + result);
		if (StringUtils.isNumeric(result) && Long.parseLong(result) > 0) {
			return 0;
		}
		return -1;
	}

	public static void main(String args[]) {
		ManDaoLongMessageProcess tManDaoSmsProcess = new ManDaoLongMessageProcess();
		MessageSmsHistory smsSend = new MessageSmsHistory();
		smsSend.setContent("长沙疾控中心向新生儿家长推出“护苗日记”App，视频介绍小儿常见疾病知识和疫苗接种注意事项，并有常见问题专业解答。点击http://t.cn/8F14q7h 免费下载1。");
		smsSend.setTelephone("18610344057");
		System.out.println(tManDaoSmsProcess.sendMessage(smsSend));
		;
//		tManDaoSmsProcess.reviceMessage();
	}

	class MdMmsSend {

		/**
		 * 发送彩信的web service服务器地址
		 */
		private static final String SERVER_URL = "http://sdk3.entinfo.cn:8060/webservice.asmx";
		/**
		 * 序列号
		 */
		private String sn = "SDK-TML-010-00287";
		private String password = "336234";
		/**
		 * 序列号加密码经MD5加密后的32位大写字符串
		 */
		private String pwd = null;

		/**
		 * 构造方法
		 */
		public MdMmsSend() {
			this.pwd = getMD5(sn + password);
		}

		/**
		 * 序列号加密码32位MD5加密方法
		 *
		 * @param sourceStr 序列号加密码的字符串
		 * @return 加密后的字符串
		 */
		public String getMD5(String sourceStr) {
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

		/**
		 * 发彩信方法
		 *
		 * @param title   标题，GBK编码
		 * @param mobile  手机号，多个用英文逗号隔开
		 * @param content 内容，参照文档编辑内容格式
		 * @param stime   定时时间，例2013-04-03 12:23:32
		 * @return 成功返回长字符串，失败返回负数
		 */
		public String mdMmsSend(String title, String mobile, String content, String stime) {
			String result = null;
			String soapAction = "http://tempuri.org/mdMmsSend";
			String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
			xml += "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">";
			xml += "<soap12:Body>";
			xml += "<mdMmsSend xmlns=\"http://tempuri.org/\">";
			xml += "<sn>" + sn + "</sn>";
			xml += "<pwd>" + pwd + "</pwd>";
			xml += "<title>" + title + "</title>";
			xml += "<mobile>" + mobile + "</mobile>";
			xml += "<content>" + content + "</content>";
			xml += "<stime>" + stime + "</stime>";
			xml += "</mdMmsSend>";
			xml += "</soap12:Body>";
			xml += "</soap12:Envelope>";

			URL url;
			OutputStream out = null;
			InputStreamReader isr = null;
			BufferedReader in = null;
			HttpURLConnection httpconn = null;
			ByteArrayOutputStream bout = null;
			try {
				url = new URL(SERVER_URL);
				URLConnection connection = url.openConnection();
				httpconn = (HttpURLConnection) connection;
				bout = new ByteArrayOutputStream();
				bout.write(xml.getBytes("GBK"));
				byte[] b = bout.toByteArray();
				httpconn.setRequestProperty("Content-Length", String.valueOf(b.length));
				httpconn.setRequestProperty("Content-Type", "text/xml; charset=gb2312");
				httpconn.setRequestProperty("SOAPAction", soapAction);
				httpconn.setRequestMethod("POST");
				httpconn.setDoInput(true);
				httpconn.setDoOutput(true);

				out = httpconn.getOutputStream();
				out.write(b);
				out.flush();

				isr = new InputStreamReader(httpconn.getInputStream());
				in = new BufferedReader(isr);
				String inputLine;
				while (null != (inputLine = in.readLine())) {
					Pattern pattern = Pattern.compile(SmsConstant.SMS_MANDAO_MDMMSSEND_PATTERN);
					Matcher matcher = pattern.matcher(inputLine);
					while (matcher.find()) {
						result = matcher.group(1);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bout != null) {
					try {
						bout.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (isr != null) {
					try {
						isr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (httpconn != null) {
					httpconn.disconnect();
				}
			}
			return result;
		}
	}
}
