/**
 *
 */
package com.winhxd.b2c.message.sms.process;

import com.winhxd.b2c.message.sms.common.SmsConstant;
import com.winhxd.b2c.message.sms.enums.SmsSupplierEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yaoshuai
 */
public class SmsAdapter {
	private static Log logger = LogFactory.getLog(SmsAdapter.class);
	private final static List<String> YDLIST = new ArrayList<String>();
	private final static List<String> LTLIST = new ArrayList<String>();
	private final static List<String> DXLIST = new ArrayList<String>();
	private static boolean isDetect = true;

	@Value("${longmessage.class}")
	private static String longmessageSuffix;

	@Value("${longmessage.class}")
	private static String replaceSuffix;

	static {
		YDLIST.add("134");
		YDLIST.add("135");
		YDLIST.add("136");
		YDLIST.add("137");
		YDLIST.add("138");
		YDLIST.add("139");
		YDLIST.add("147");
		YDLIST.add("150");
		YDLIST.add("151");
		YDLIST.add("152");
		YDLIST.add("157");
		YDLIST.add("158");
		YDLIST.add("159");
		YDLIST.add("182");
		YDLIST.add("183");
		YDLIST.add("187");
		YDLIST.add("188");

		LTLIST.add("130");
		LTLIST.add("131");
		LTLIST.add("132");
		LTLIST.add("145");
		LTLIST.add("155");
		LTLIST.add("156");
		LTLIST.add("185");
		LTLIST.add("186");

		DXLIST.add("133");
		DXLIST.add("153");
		DXLIST.add("180");
		DXLIST.add("189");
	}

	/**
	 * 根据参数创建短信平台实例
	 *
	 * @param codeType  供应商代码(参见SmsSupplierEnum)
	 * @param telephone 手机号
	 * @param content   短信内容
	 * @return SmsProcess 短信发送实例
	 */
	public static BaseSmsProcess adapter(String codeType, String telephone, String content) throws Exception {

		String replace;// 是否需要替代
		Class<?> clazz = null;
		BaseSmsProcess smsProcess = null;
		String className = null;

		try {
			if (isDetect && StringUtils.isNotEmpty(telephone) && telephone.startsWith(SmsConstant.INTERNATIONAL_PREFIX)) {
				codeType = SmsSupplierEnum.MANDAOI.getCode();
			}
			codeType = validateSupplier(codeType);

			if (content.length() >= SmsConstant.LONGMESSAGE_LENGTH) {

				className = longmessageSuffix;
			} else {

				String supplier = codeType;
				replace = replaceSuffix;//PropertiesUtils.getValue(supplier + SmsConstant.KEY_REPLACE);
				if (StringUtils.isNotEmpty(replace)) {
					supplier = replace;
				}

				if (supplier.indexOf(SmsConstant.SPLIT_UNDERLINE) > 0) {
					supplier = supplier.substring(0, supplier.indexOf(SmsConstant.SPLIT_UNDERLINE));
				}
				//className = PropertiesUtils.getValue(supplier.toLowerCase() + SmsConstant.KEY_CLASS);
			}

			clazz = Class.forName(className);
			smsProcess = (BaseSmsProcess) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e.getCause());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (smsProcess == null) {
			throw new Exception("no process was found.");
		}
		return smsProcess;
	}

	/**
	 * 验证供应商代码
	 *
	 * @param supplierCode 供应商代码
	 * @return 供应商代码(如果不在SmsSupplierEnum 则返回默认值)
	 */
	private static String validateSupplier(String supplierCode) {
		boolean isExist = false;

		for (SmsSupplierEnum supplier : SmsSupplierEnum.values()) {

			if (supplier.getCode().equals(supplierCode)) {
				isExist = true;
			}
		}

		if (!isExist || StringUtils.isEmpty(supplierCode)) {
			supplierCode = SmsSupplierEnum.DEFUALT.getCode();
		}

		return supplierCode;
	}

}
