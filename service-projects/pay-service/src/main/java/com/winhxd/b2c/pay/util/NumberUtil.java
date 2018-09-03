package com.winhxd.b2c.pay.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {

	private static boolean isMatch(String regex, String orginal) {
		if (orginal == null || "".equals(orginal.trim())) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}

	/**
	 * @author liuhanning
	 * @date 2017年10月16日 下午4:14:39
	 * @Description 正整数
	 * @param orginal
	 * @return
	 */
	public static boolean isPositiveInteger(String orginal) {
		return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
	}

	/**
	 * @author liuhanning
	 * @date 2017年10月16日 下午4:14:39
	 * @Description 负整数
	 * @param orginal
	 * @return
	 */
	public static boolean isNegativeInteger(String orginal) {
		return isMatch("^-[1-9]\\d*", orginal);
	}

	/**
	 * @author liuhanning
	 * @date 2017年10月16日 下午4:14:39
	 * @Description 整数
	 * @param orginal
	 * @return
	 */
	public static boolean isWholeNumber(String orginal) {
		return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
	}

	/**
	 * @author liuhanning
	 * @date 2017年10月16日 下午4:14:39
	 * @Description 正的数字 包含小数
	 * @param orginal
	 * @return
	 */
	public static boolean isPositiveDecimal(String orginal) {
		return isMatch("\\+{0,1}[0]\\.[0-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
	}

	/**
	 * @author liuhanning
	 * @date 2017年10月16日 下午4:14:39
	 * @Description 负的数字 包含小数
	 * @param orginal
	 * @return
	 */
	public static boolean isNegativeDecimal(String orginal) {
		return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", orginal);
	}

	/**
	 * @author liuhanning
	 * @date 2017年10月16日 下午4:14:39
	 * @Description 数字 包含小数
	 * @param orginal
	 * @return
	 */
	public static boolean isDecimal(String orginal) {
		return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
	}

	/**
	 * @author liuhanning
	 * @date 2017年10月16日 下午4:14:39
	 * @Description 数字
	 * @param orginal
	 * @return
	 */
	public static boolean isRealNumber(String orginal) {
		return isWholeNumber(orginal) || isDecimal(orginal);
	}
}
