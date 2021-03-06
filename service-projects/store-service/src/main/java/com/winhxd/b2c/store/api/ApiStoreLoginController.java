package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.AppConstant;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.constant.SendSMSTemplate;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.condition.SMSCondition;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import com.winhxd.b2c.common.domain.store.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoSimpleVO;
import com.winhxd.b2c.common.domain.system.login.condition.StoreSendVerificationCodeCondition;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserInfoCondition;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserLogoutCodeCondition;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserSimpleInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.hxd.StoreHxdServiceClient;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.common.util.GeneratePwd;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.common.util.MessageSendUtils;
import com.winhxd.b2c.store.service.StoreLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author wufuyun
 * @date 2018年8月3日 下午3:06:09
 * @Description B端用户登录
 * @version
 */
@Api(value = "StoreLoginController Controller", tags = "B-Login")
@RestController
@RequestMapping(value = "api-store/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiStoreLoginController {
	private static final Logger logger = LoggerFactory.getLogger(ApiStoreLoginController.class);

	/**
	 * 惠小店状态 0开店、1有效、2无效
	 */
	static final Short HXD_STATUS2 = 2;
	/**
	 * 微信登录
	 */
	static final Integer LOGIN_LAG = 1;
	/**
	 * 账号登录
	 */
	static final Integer LOGIN_LAG_2 = 2;
	/**
	 * 验证码
	 */
	static final Integer LOGIN_PASSWORD_LAG_1 = 1;
	/**
	 * 密码登录
	 */
	static final Integer LOGIN_PASSWORD_LAG_2 = 2;
	/**
	 * 快捷登录
	 */
	static final Integer LOGIN_PASSWORD_LAG_3 = 3;
	@Autowired
	private StoreLoginService storeLoginService;
	@Autowired
	private Cache cache;

	@Autowired
	MessageSendUtils messageSendUtils;
	@Autowired
	StoreHxdServiceClient storeHxdServiceClient;
	@Autowired
	MessageServiceClient messageServiceClient;

	/**
	 * @author wufuyun
	 * @date 2018年8月4日 上午11:11:59
	 * @Description B端登录
	 * @param storeUserInfoCondition
	 * @return
	 */
	@ApiOperation(value = "B端登录验证")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_100808, message = "验证码错误"),
			@ApiResponse(code = BusinessCode.CODE_100821, message = "您的账号或者密码错误"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
			@ApiResponse(code = BusinessCode.CODE_100810, message = "该微信号已绑定过账号"),
			@ApiResponse(code = BusinessCode.CODE_100811, message = "此手机号已被其他微信绑定，不能再次绑定"),
			@ApiResponse(code = BusinessCode.CODE_100819, message = "您还没有绑定惠下单账号"),
			@ApiResponse(code = BusinessCode.CODE_100822, message = "您还不是惠下单用户快去注册吧"),
			@ApiResponse(code = BusinessCode.CODE_100809, message = "您的账号存在异常行为，已被锁定，如有疑问请联系客服4006870066。") })
	@RequestMapping(value = "store/security/1008/v1/storeLogin", method = RequestMethod.POST)
	public ResponseResult<StoreUserInfoSimpleVO> storeLogin(
			@RequestBody StoreUserInfoCondition storeUserInfoCondition) {
		logger.info("{} - B端登录验证, 参数：storeUserInfoCondition={}", "", JsonUtil.toJSONString(storeUserInfoCondition));
		ResponseResult<StoreUserInfoSimpleVO> result = new ResponseResult<>();
		if (null == storeUserInfoCondition) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		StoreUserInfo db = null;
		StoreUserInfo storeUserInfo = new StoreUserInfo();
		StoreUser user = new StoreUser();
		StoreUserInfoSimpleVO vo = new StoreUserInfoSimpleVO();
		StoreUserInfo open = new StoreUserInfo();
		/**
		 * 微信绑定账号
		 */
		if (LOGIN_LAG.equals(storeUserInfoCondition.getLoginFlag())
				&& LOGIN_PASSWORD_LAG_1.equals(storeUserInfoCondition.getLoginPasswordFlag())) {
			if (!storeUserInfoCondition.getVerificationCode().equals(
					cache.get(CacheName.STORE_USER_SEND_VERIFICATION_CODE + storeUserInfoCondition.getStoreMobile()))) {
				throw new BusinessException(BusinessCode.CODE_100808);
			}
			if (StringUtils.isBlank(storeUserInfoCondition.getOpenid())) {
				throw new BusinessException(BusinessCode.CODE_1007);
			}
			open.setOpenid(storeUserInfoCondition.getOpenid());
			db = storeLoginService.getStoreUserInfo(open);
			if (db != null) {
				result = new ResponseResult<>(BusinessCode.CODE_100810);
				return result;
			}
			open.setOpenid(null);
			open.setStoreMobile(storeUserInfoCondition.getStoreMobile());
			db = storeLoginService.getStoreUserInfo(open);
			if(db != null){
				result = new ResponseResult<>(BusinessCode.CODE_100811);
				return result;
			}
			storeUserInfo.setStoreMobile(storeUserInfoCondition.getStoreMobile());

			ResponseResult<StoreUserSimpleInfo> object = storeHxdServiceClient
					.getStoreUserInfo(storeUserInfoCondition.getStoreMobile(), "");
			StoreUserSimpleInfo map = object.getData();
			if (map == null) {
				throw new BusinessException(BusinessCode.CODE_100822);
			}

			storeUserInfo.setStoreCustomerId(map.getStoreCustomerId());
			storeUserInfo.setStoreRegionCode(map.getStoreRegionCode());
			storeUserInfo.setStorePicImg(map.getStorePicImg());
			storeUserInfo.setCreated(new Date());
			storeUserInfo.setStoreMobile(map.getStoreMobile());
			storeUserInfo.setSource(storeUserInfoCondition.getMobileInfo().getPlatform());
			storeUserInfo.setStoreStatus((short) 0);
			storeUserInfo.setShopOwnerImg(storeUserInfoCondition.getShopOwnerImg());
			storeUserInfo.setToken(GeneratePwd.getRandomUUID());
			storeUserInfo.setOpenid(storeUserInfoCondition.getOpenid());
			storeUserInfo.setWechatName(storeUserInfoCondition.getWechatName());
			storeUserInfo.setAppLoginStatus((short) 0);
			storeLoginService.saveStoreInfo(storeUserInfo);

			db = storeLoginService.getStoreUserInfo(storeUserInfo);
			/**
			 * 查库
			 */
			if (db == null) {
				logger.info("{} - , 请求超时");
				result = new ResponseResult<>(BusinessCode.CODE_100815);
				return result;
			} else {
				if (HXD_STATUS2.equals(db.getStoreStatus())) {
					logger.info("{} - , 您的账号存在异常行为，已被锁定，如有疑问请联系客服4006870066。");
					throw new BusinessException(BusinessCode.CODE_100809);
				}
				/**
				 * 调用云信服务获取用户信息
				 */
				getNeteaseAcctInfo(db, vo);
				cache.del(CacheName.STORE_USER_INFO_TOKEN + db.getToken());
				storeUserInfo.setToken(GeneratePwd.getRandomUUID());
				storeUserInfo.setOpenid(storeUserInfoCondition.getOpenid());
				storeUserInfo.setId(db.getId());
				storeUserInfo.setAppLoginStatus((short) 0);
				storeLoginService.modifyStoreUserInfo(storeUserInfo);
				vo.setToken(storeUserInfo.getToken());
				vo.setCustomerId(db.getStoreCustomerId());
				vo.setStoreMobile(storeUserInfoCondition.getStoreMobile());
				storeUserInfo.setStoreCustomerId(db.getStoreCustomerId());
				getStoreUserInfoToken(storeUserInfo, user);
				result.setData(vo);
			}
		}
		/**
		 * 微信快捷登录
		 */
		else if (LOGIN_LAG.equals(storeUserInfoCondition.getLoginFlag())
				&& LOGIN_PASSWORD_LAG_3.equals(storeUserInfoCondition.getLoginPasswordFlag())) {
			storeUserInfo.setOpenid(storeUserInfoCondition.getOpenid());
			storeUserInfo.setWechatName(storeUserInfoCondition.getWechatName());
				db = storeLoginService.getStoreUserInfo(storeUserInfo);
				if (db == null) {
					logger.info("{} - , 您还没有绑定惠下单账号");
					throw new BusinessException(BusinessCode.CODE_100819);
			}
			if (HXD_STATUS2.equals(db.getStoreStatus())) {
				logger.info("{} - , 您的账号存在异常行为，已被锁定，如有疑问请联系客服4006870066。");
				throw new BusinessException(BusinessCode.CODE_100809);
			}
			ResponseResult<StoreUserSimpleInfo> object = storeHxdServiceClient
					.getStoreUserInfoByCustomerId(db.getStoreCustomerId());
			StoreUserSimpleInfo map = object.getData();
			if (map == null) {
				logger.info("{} - , 您还不是惠下单用户快去注册吧");
				throw new BusinessException(BusinessCode.CODE_100822);
			}
			/**
			 * 调用云信服务获取用户信息
			 */
			getNeteaseAcctInfo(db, vo);
			cache.del(CacheName.STORE_USER_INFO_TOKEN + db.getToken());
			storeUserInfo.setId(db.getId());
			storeUserInfo.setStoreMobile(map.getStoreMobile());
			storeUserInfo.setStoreRegionCode(map.getStoreRegionCode());
			storeUserInfo.setStorePicImg(map.getStorePicImg());
			logger.info("头像:" + storeUserInfoCondition.getShopOwnerImg());
			storeUserInfo.setShopOwnerImg(storeUserInfoCondition.getShopOwnerImg());
			storeUserInfo.setAppLoginStatus((short) 0);
			storeUserInfo.setToken(GeneratePwd.getRandomUUID());
			/**
			 * 每次登陆都更新店铺信息
			 */
			updateStoreInfo(storeUserInfo,db.getStoreCustomerId());
			storeLoginService.modifyStoreUserInfo(storeUserInfo);
			vo.setToken(storeUserInfo.getToken());
			vo.setCustomerId(db.getStoreCustomerId());
			vo.setStoreMobile(map.getStoreMobile());
			storeUserInfo.setStoreCustomerId(db.getStoreCustomerId());
			getStoreUserInfoToken(storeUserInfo, user);
			result.setData(vo);

		}
		/**
		 * 账号验证码登录(暂时不用)
		 */
		else if (LOGIN_LAG_2.equals(storeUserInfoCondition.getLoginFlag())
				&& LOGIN_PASSWORD_LAG_1.equals(storeUserInfoCondition.getLoginPasswordFlag())) {
			if (!storeUserInfoCondition.getVerificationCode().equals(
					cache.get(CacheName.STORE_USER_SEND_VERIFICATION_CODE + storeUserInfoCondition.getStoreMobile()))) {
				logger.info("{} - , 验证码错误");
				throw new BusinessException(BusinessCode.CODE_100808);
			}
			storeUserInfo.setStoreMobile(storeUserInfoCondition.getStoreMobile());
			db = storeLoginService.getStoreUserInfo(storeUserInfo);
			/**
			 * 查库
			 */
			if (db == null) {
				logger.info("{} - , 您还不是惠下单用户快去注册吧");
				throw new BusinessException(BusinessCode.CODE_100822);
			} else {
				if (HXD_STATUS2.equals(db.getStoreStatus())) {
					logger.info("{} - , 您的账号存在异常行为，已被锁定，如有疑问请联系客服4006870066。");
					throw new BusinessException(BusinessCode.CODE_100809);
				}
				/**
				 * 调用云信服务获取用户信息
				 */
				getNeteaseAcctInfo(db, vo);
				cache.del(CacheName.STORE_USER_INFO_TOKEN + db.getToken());
				storeUserInfo.setToken(GeneratePwd.getRandomUUID());
				storeUserInfo.setId(db.getId());
				storeUserInfo.setAppLoginStatus((short) 0);
				storeLoginService.modifyStoreUserInfo(storeUserInfo);
				vo.setToken(storeUserInfo.getToken());
				vo.setCustomerId(db.getStoreCustomerId());
				vo.setStoreMobile(storeUserInfoCondition.getStoreMobile());
				storeUserInfo.setStoreCustomerId(db.getStoreCustomerId());
				getStoreUserInfoToken(storeUserInfo, user);
				result.setData(vo);
			}
		}
		/**
		 * 账号密码登录(暂时不用)
		 */
		else if (LOGIN_LAG_2.equals(storeUserInfoCondition.getLoginFlag())
				&& LOGIN_PASSWORD_LAG_2.equals(storeUserInfoCondition.getLoginPasswordFlag())) {
			if (StringUtils.isBlank(storeUserInfoCondition.getStorePassword())
					|| StringUtils.isBlank(storeUserInfoCondition.getStoreMobile())) {
				logger.info("{} - , 您的账号或者密码错误");
				throw new BusinessException(BusinessCode.CODE_100821);
			}
			ResponseResult<StoreUserSimpleInfo> object = storeHxdServiceClient.getStoreUserInfo(
					storeUserInfoCondition.getStoreMobile(), storeUserInfoCondition.getStorePassword());
			StoreUserSimpleInfo map = object.getData();
			if (map == null) {
				logger.info("{} - , 您的账号或者密码错误");
				throw new BusinessException(BusinessCode.CODE_100821);
			} else {
				storeUserInfo.setStoreCustomerId(map.getStoreCustomerId());
				db = storeLoginService.getStoreUserInfo(storeUserInfo);
				if (db != null) {
					if (HXD_STATUS2.equals(db.getStoreStatus())) {
						logger.info("{} - , 您的账号存在异常行为，已被锁定，如有疑问请联系客服4006870066。");
						throw new BusinessException(BusinessCode.CODE_100809);
					}
					/**
					 * 调用云信服务获取用户信息
					 */
					getNeteaseAcctInfo(db, vo);
					cache.del(CacheName.STORE_USER_INFO_TOKEN + db.getToken());
					/**
					 * 更新数据库
					 */
					storeUserInfo.setId(db.getId());
					storeUserInfo.setStoreMobile(map.getStoreMobile());
					storeUserInfo.setToken(GeneratePwd.getRandomUUID());
					storeUserInfo.setAppLoginStatus((short) 0);
					storeUserInfo.setStoreRegionCode(map.getStoreRegionCode());
					storeUserInfo.setStorePicImg(map.getStorePicImg());
					storeLoginService.modifyStoreUserInfo(storeUserInfo);
					vo.setToken(storeUserInfo.getToken());
					vo.setCustomerId(db.getStoreCustomerId());
					vo.setStoreMobile(storeUserInfoCondition.getStoreMobile());
					storeUserInfo.setStoreCustomerId(db.getStoreCustomerId());
					getStoreUserInfoToken(storeUserInfo, user);
					result.setData(vo);
				} else {
					/*
					 * 插入数据库
					 */
					storeUserInfo.setOpenid(storeUserInfoCondition.getOpenid());
					storeUserInfo.setStoreCustomerId(map.getStoreCustomerId());
					storeUserInfo.setStoreRegionCode(map.getStoreRegionCode());
					storeUserInfo.setStorePicImg(map.getStorePicImg());
					storeUserInfo.setCreated(new Date());
					storeUserInfo.setStoreMobile(map.getStoreMobile());
					storeUserInfo.setSource(storeUserInfoCondition.getMobileInfo().getPlatform());
					storeUserInfo.setStoreStatus((short) 0);
					storeUserInfo.setToken(GeneratePwd.getRandomUUID());
					storeUserInfo.setAppLoginStatus((short) 0);
					storeLoginService.saveStoreInfo(storeUserInfo);
					vo.setToken(storeUserInfo.getToken());
					vo.setCustomerId(map.getStoreCustomerId());
					vo.setStoreMobile(storeUserInfoCondition.getStoreMobile());
					getStoreUserInfoToken(storeUserInfo, user);
					result.setData(vo);
				}
			}
		} else {
			logger.info("{} - , 参数无效");
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		logger.info("{} - B端登录返参, 参数：StoreUserInfoSimpleVO={}", "", JsonUtil.toJSONString(vo));
		return result;
	}

	private void getNeteaseAcctInfo(StoreUserInfo db, StoreUserInfoSimpleVO vo) {
		ResponseResult<NeteaseAccountVO> netease;
		NeteaseAccountVO accountVO;
		NeteaseAccountCondition neteaseAccountCondition = new NeteaseAccountCondition();
		neteaseAccountCondition.setCustomerId(db.getId());
		netease = messageServiceClient.getNeteaseAccountInfo(neteaseAccountCondition);
		if (netease.getCode() != 0) {
			logger.info("{} - , 云信获取用户信息失败");
			storeLoginService.deleteStoreUserInfoById(db.getId());
			throw new BusinessException(BusinessCode.CODE_100815);
		}
		accountVO = netease.getData();
		vo.setNeteaseAccid(accountVO.getAccid());
		vo.setNeteaseToken(accountVO.getToken());
	}

	private void getStoreUserInfoToken(StoreUserInfo db, StoreUser user) {
		user.setBusinessId(db.getId());
		user.setStoreCustomerId(db.getStoreCustomerId());
		user.setOpenId(db.getOpenid());
		cache.set(CacheName.STORE_USER_INFO_TOKEN + db.getToken(), JsonUtil.toJSONString(user));
		cache.expire(CacheName.STORE_USER_INFO_TOKEN + db.getToken(), AppConstant.LOGIN_APP_TOKEN_EXPIRE_SECOND);
	}

	private void updateStoreInfo(StoreUserInfo storeUserInfo,Long storeCustomerId){
		ResponseResult<Map<String, Object>> result = storeHxdServiceClient.getStoreBaseInfo(storeCustomerId.toString());
		Map<String, Object> map = result.getData();
		//先更新门店信息，再返回给前端展示
		storeUserInfo.setId(storeUserInfo.getId());
		storeUserInfo.setStoreName(Objects.toString(map.get("storeName"), ""));
		storeUserInfo.setStoreAddress(Objects.toString(map.get("storeAddress"), ""));
		storeUserInfo.setStoreRegionCode(Objects.toString(map.get("storeRegionCode"), ""));
		storeUserInfo.setLon(Double.parseDouble(Objects.toString(map.get("longitude"), "0")));
		storeUserInfo.setLat(Double.parseDouble(Objects.toString(map.get("latitude"), "0")));
	}
	/**
	 * @author wufuyun
	 * @date 2018年8月4日 上午11:10:34
	 * @Description 通过账号发送验证码
	 * @param storeSendVerificationCodeCondition
	 * @return
	 */
	@ApiOperation(value = "通过账号发送验证码")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
			@ApiResponse(code = BusinessCode.CODE_100912, message = "验证码请求时长没有超过一分钟"),
			@ApiResponse(code = BusinessCode.CODE_100922, message = "您还不是惠下单用户快去注册吧")})
	@RequestMapping(value = "store/security/1009/v1/sendVerification", method = RequestMethod.POST)
	public ResponseResult<String> sendVerification(
			@RequestBody StoreSendVerificationCodeCondition storeSendVerificationCodeCondition) {
		logger.info("{} -账号发送验证码, 参数：storeUserInfoCondition={}", "",
				JsonUtil.toJSONString(storeSendVerificationCodeCondition));
		if (null == storeSendVerificationCodeCondition) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		ResponseResult<String> result = new ResponseResult<>();
		/**
		 * 调惠下单服务查询门店用户信息
		 */
		ResponseResult<StoreUserSimpleInfo> object = storeHxdServiceClient
				.getStoreUserInfo(storeSendVerificationCodeCondition.getStoreMobile(), "");
		StoreUserSimpleInfo map = object.getDataWithException();
		if (map == null) {
			logger.info("{} - , 您还不是惠下单用户快去注册吧");
			throw new BusinessException(BusinessCode.CODE_100922);
		} else {
			result = sendVerificationCode(map.getStoreMobile());
			return result;
		}
	}

	/**
	 * @author wufuyun
	 * @date 2018年8月8日 下午8:38:21
	 * @Description 发送验证码检验
	 * @param storeMobile
	 * @return
	 */
	private ResponseResult<String> sendVerificationCode(String storeMobile) {
		if (cache.exists(CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + storeMobile)) {
			logger.info("{} - , 请求验证码时长没有超过一分钟");
			throw new BusinessException(BusinessCode.CODE_100912);
		}
		String verificationCode = GeneratePwd.generatePwd6Mobile();
		cache.set(CacheName.STORE_USER_SEND_VERIFICATION_CODE + storeMobile, verificationCode);
		cache.expire(CacheName.STORE_USER_SEND_VERIFICATION_CODE + storeMobile, AppConstant.SEND_SMS_EXPIRE_SECOND);
		/**
		 * 60秒以后调用短信服务
		 */
		cache.set(CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + storeMobile, verificationCode);
		cache.expire(CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + storeMobile,
				AppConstant.REQUEST_SEND_SMS_EXPIRE_SECOND);
		ResponseResult<String> result = new ResponseResult<>();
		/**
		 * 发送模板内容
		 */
		String content = String.format(SendSMSTemplate.SMSCONTENT, verificationCode);
		SMSCondition sMSCondition = new SMSCondition();
		sMSCondition.setContent(content);
		sMSCondition.setMobile(storeMobile);
		messageSendUtils.sendSms(sMSCondition);
		logger.info(storeMobile + ":发送的内容为:" + content);
		return result;
	}

	/**
	 * @author wufuyun
	 * @date 2018年8月24日 下午1:29:59
	 * @Description 惠小店app退出登录
	 * @param storeUserLogoutCodeCondition
	 * @return
	 */
	@ApiOperation(value = "惠小店app退出登录")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@RequestMapping(value = "store/1010/v1/storeUserLogout", method = RequestMethod.POST)
	public ResponseResult<Void> storeUserLogout(
			@RequestBody StoreUserLogoutCodeCondition storeUserLogoutCodeCondition) {
		logger.info("{} - 惠小店app退出登录, 参数：customerUserInfoCondition={}", "",
				JsonUtil.toJSONString(storeUserLogoutCodeCondition));
		if (null == storeUserLogoutCodeCondition) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		ResponseResult<Void> result = new ResponseResult<>();
		StoreUserInfo info = new StoreUserInfo();
		StoreUser user = UserContext.getCurrentStoreUser();
		info.setId(user.getBusinessId());
		info.setAppLoginStatus((short) 1);
		storeLoginService.modifyStoreUserInfo(info);
		return result;
	}

}
