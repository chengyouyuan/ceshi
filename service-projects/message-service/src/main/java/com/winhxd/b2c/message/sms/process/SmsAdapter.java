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
	private final static List<String> ydList = new ArrayList<String>();
	private final static List<String> ltList = new ArrayList<String>();
	private final static List<String> dxList = new ArrayList<String>();
	private static boolean isDetect = true;

	@Value("${longmessage.class}")
	private static String longmessageSuffix;

	@Value("${longmessage.class}")
	private static String replaceSuffix;

	static {
		ydList.add("134");
		ydList.add("135");
		ydList.add("136");
		ydList.add("137");
		ydList.add("138");
		ydList.add("139");
		ydList.add("147");
		ydList.add("150");
		ydList.add("151");
		ydList.add("152");
		ydList.add("157");
		ydList.add("158");
		ydList.add("159");
		ydList.add("182");
		ydList.add("183");
		ydList.add("187");
		ydList.add("188");

		ltList.add("130");
		ltList.add("131");
		ltList.add("132");
		ltList.add("145");
		ltList.add("155");
		ltList.add("156");
		ltList.add("185");
		ltList.add("186");

		dxList.add("133");
		dxList.add("153");
		dxList.add("180");
		dxList.add("189");
	}

	/**
	 * 根据参数创建短信平台实例
	 *
	 * @param codeType  供应商代码(参见SmsSupplierEnum)
	 * @param telephone 手机号
	 * @param content   短信内容
	 * @return SmsProcess 短信发送实例
	 */
	public static SmsProcess adapter(String codeType, String telephone, String content) throws Exception {

		String replace;// 是否需要替代
		Class<?> clazz = null;
		SmsProcess smsProcess = null;
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
			smsProcess = (SmsProcess) clazz.newInstance();
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
