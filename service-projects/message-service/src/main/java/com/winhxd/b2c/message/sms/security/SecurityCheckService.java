package com.winhxd.b2c.message.sms.security;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 短信发送安全检查
 */
@Service
public class SecurityCheckService {

	private final Logger logger = LoggerFactory.getLogger(SecurityCheckService.class);

	@Resource
	private Cache cache;

	/**
	 * 短信发送安全检查
	 *
	 * @param mobile 手机号码,多个手机号用英文半角逗号( , )隔开。
	 * @return 检查通过返回true，否则返回false
	 */
	public boolean securityCheck(String mobile) {
		boolean rs = false;
		logger.info("短信发送安全检查 ,mobile=" + mobile);
		String[] arrayMoile;// 手机号码数组
		try {
			// 校验手机号安全检查
			if (StringUtils.isNotEmpty(mobile)) {
				arrayMoile = mobile.split(SecurityConstant.SPLIT);
				for (int i = 0; i < arrayMoile.length; i++) {

					if (!mobileCheck(arrayMoile[i])) {
						rs = false;
						break;
					}
					rs = true;
				}
			}

			// ip地址短信发送安全检查
			if (rs) {
				updateTimes(mobile);
				rs = true;
			} else {
				rs = false;
			}
		} catch (Exception e) {
			rs = false;
			logger.info("短信发送安全检查异常 ", e);
		}
		logger.info("短信发送安全检查 结果 = " + rs);
		return rs;
	}

	/**
	 * 每日发送短信次数以及时间间隔检查
	 *
	 * @param key          手机号码/ip地址 本日发送短信情况在redis中的key
	 * @param maxTimes     次数上限
	 * @param timeInterval 时间间隔
	 * @return 检查通过返回true，否则返回false
	 */
	private boolean timesCheck(String key, int maxTimes, long timeInterval) {
		/**
		 * 检查结果
		 */
		boolean checkRs = true;
		/**
		 * 本日已发送次数
		 */
		int nTimes;
		/**
		 * 上次发送时间  本次发送时间
		 */
		long lastTime, thisTime;
		/**
		 * 上次访问日期   本次访问日期   手机用户当日发送短信情况 结构为 ：次数,上次发送时,日期 格式的字符串。
		 */
		String lastDate, thisDate, info;
		Date today;
		/**
		 * 日志输出内容
		 */
		StringBuilder sbufLog = new StringBuilder();

		sbufLog.append("开始每日发送短信次数检查,key = ").append(key).append(",maxTimes = ").append(maxTimes).append(",timeInterval = ").append(timeInterval);
		logger.info(sbufLog.toString());
		try {
			info = cache.get(key);
			today = new Date();
			thisTime = today.getTime();
			thisDate = SecurityCheckUtil.formatDateYMD(today);

			if (info != null && info.length() > 0) {
				nTimes = Integer.parseInt(info.split(SecurityConstant.SPLIT)[0]);
				lastTime = Long.parseLong(info.split(SecurityConstant.SPLIT)[1]);
				lastDate = info.split(SecurityConstant.SPLIT)[2];

				// 判断当日发送次数以及时间间隔
				if (thisDate.equals(lastDate) && nTimes < maxTimes && (thisTime - lastTime) > timeInterval) {
					checkRs = true;
				} else if (!thisDate.equals(lastDate)) {
					// 日期不一致则为本日首次访问
					checkRs = true;
				} else {
					// 否则为超出限制
					checkRs = false;
					sbufLog.delete(0, sbufLog.length());
					sbufLog.append("安全检查检查结果 info= ").append(info);
				}
			}
		} catch (Exception e) {
			checkRs = false;
			logger.error("次数检查异常", e);
		}
		return checkRs;
	}

	/**
	 * 手机号码短信发送次数及时间间隔检查
	 * 白名单
	 *
	 * @param mobile 手机号码
	 * @return 检查通过返回true，否则返回false
	 */
	private boolean mobileCheck(String mobile) {
		boolean checkRs = false;
		logger.info("开始手机号码短信发送次数及时间间隔检查,mobile = " + mobile);

		int maxTimes;// 次数上限
		Long timeInterval;// 时间间隔(毫秒)
		String redisKey;// 手机号码当日发送短信信息redis key

		if (SecurityCheckUtil.validateMobile(mobile)) {
			redisKey = CacheName.MESSAGE_SMS_SEND_VERIFICATION_CODE + mobile;
			maxTimes = SecurityConstant.getMobileMaxTimes();
			timeInterval = SecurityConstant.getMobileTimeInterval();
			if (timesCheck(redisKey, maxTimes, timeInterval)) {
				checkRs = true;
			}
		} else {
			logger.info("老框架手机号码格式错误,checkRs = " + checkRs);
		}

		logger.info("手机号码短信发送次数及时间间隔检查结果 = " + checkRs);
		return checkRs;
	}

	/**
	 * 更新每日发送短信情况
	 *
	 * @param mobile 手机号码 多个用,(英文半角逗号)隔开
	 */
	public void updateTimes(String mobile) {

		Date today = new Date();
		long thisTime = today.getTime();// 本次发送成功时间
		String thisDate = SecurityCheckUtil.formatDateYMD(today);

		if (StringUtils.isNotEmpty(mobile)) {
			updateMobileTimes(mobile, thisTime, thisDate);
		}
	}

	/**
	 * 更新手机号码每日发送短信次数
	 *
	 * @param mobile   手机号码 多个用,(英文半角逗号)隔开
	 * @param thisTime 短信发送成功时间
	 * @param thisDate 当前日期
	 */
	private void updateMobileTimes(String mobile, long thisTime, String thisDate) {
		try {
			String mobileKey;// 手机号码发送短信情况key
			String mobileInfo;// 手机号码发送短信情况
			int mobileTimes = SecurityConstant.FIRSR_TIMES;// 已发送次数
			String lastDate;

			String[] arrayMobile = mobile.split(SecurityConstant.SPLIT);// 手机号码数组

			for (int i = 0; i < arrayMobile.length; i++) {

				mobileKey = CacheName.MESSAGE_SMS_SEND_VERIFICATION_CODE + arrayMobile[i];
				mobileInfo = cache.get(mobileKey);// 获取手机发送短信情况

				if (StringUtils.isNotEmpty(mobileInfo)) {

					mobileTimes = Integer.parseInt(mobileInfo.split(SecurityConstant.SPLIT)[0]);// 发送次数
					lastDate = mobileInfo.split(SecurityConstant.SPLIT)[2];

					if (thisDate.equals(lastDate)) {
						mobileTimes++;
					} else {
						mobileTimes = SecurityConstant.FIRSR_TIMES;
					}
				} else {
					mobileTimes = SecurityConstant.FIRSR_TIMES;
				}
				mobileInfo = mobileTimes + SecurityConstant.SPLIT + String.valueOf(thisTime) + SecurityConstant.SPLIT + thisDate;
				/**
				 * 缓存12小时
				 */
				cache.set(mobileKey, mobileInfo, Long.toString(12 * 60 * 60 * 1000));
			}
		} catch (Exception e) {
			logger.info("更新手机号码发送次数异常 :", e);
		}
	}
}
