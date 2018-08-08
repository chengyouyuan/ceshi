package com.winhxd.b2c.store.api;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.vo.LoginCheckSellMoneyVO;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserInfoCondition;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.hxd.StoreHxdServiceClient;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.store.service.StoreLoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author wufuyun
 * @date 2018年8月3日 下午3:06:09
 * @Description B端用户登录
 * @version
 */
@Api(value = "StoreLoginController Controller", tags = "B-Login")
@RestController
@RequestMapping(value = "api/store/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiStoreLoginController {
	private static final Logger logger = LoggerFactory.getLogger(ApiStoreLoginController.class);
	/**
	 * 微信登录
	 */
	static final Integer LOGIN_LAG = 1;
	/**
	 * 验证码
	 */
	static final Integer LOGIN_PASSWORD_LAG_1 = 1;
	/**
	 * 密码登录
	 */
	static final Integer LOGIN_PASSWORD_LAG_2 = 2;
	@Autowired
	private StoreLoginService storeLoginService;
	@Autowired
	private Cache cache;
	@Autowired
	StoreServiceClient storeServiceClient;
	@Autowired
	MessageServiceClient messageServiceClient;
	@Autowired
	StoreHxdServiceClient storeHxdServiceClient;

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
			@ApiResponse(code = BusinessCode.CODE_1008, message = "验证码错误"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1005, message = "密码错误"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@RequestMapping(value = "1008/v1/saveWeChatLogin", method = RequestMethod.POST)
	public ResponseResult<StoreUserInfoVO> saveStoreLogin(@RequestBody StoreUserInfoCondition storeUserInfoCondition) {
		ResponseResult<StoreUserInfoVO> result = new ResponseResult<>();
		try {
			if (null == storeUserInfoCondition) {
				return new ResponseResult<>(BusinessCode.CODE_1007);
			}
			StoreUserInfo DB = null;
			StoreUserInfo storeUserInfo = new StoreUserInfo();
			StoreUser user = new StoreUser();

			/**
			 * 验证码登录 拿手机号去惠小店表里面查询是否存在
			 */
			storeUserInfo.setStoreMobile(storeUserInfoCondition.getStoreMobile());
			DB = storeLoginService.getstoreUserInfoByMobile(storeUserInfo);
			StoreUserInfoVO vo = new StoreUserInfoVO();
			// 验证码登录
			if (LOGIN_PASSWORD_LAG_1.equals(storeUserInfoCondition.getLoginPasswordFlag())) {
				if (!storeUserInfoCondition.getVerificationCode()
						.equals(cache.get(storeUserInfoCondition.getStoreMobile()))) {
					return new ResponseResult<>(BusinessCode.CODE_1008);
				}
				/**
				 * 微信登录
				 */
				if (LOGIN_LAG == storeUserInfoCondition.getLoginFlag()) {
					// TODO:根据openId查询惠下单用户表登录

				} else {
					if (DB == null) {
						return new ResponseResult<>(BusinessCode.CODE_1004);
					} else {
						vo.setBusinessId(DB.getId());
						vo.setStoreCustomerId(DB.getStoreCustomerId());
						vo.setToken(DB.getToken());
						BeanUtils.copyProperties(vo, user);
						cache.set(CacheName.STORE_USER_INFO_TOKEN + DB.getToken(), JsonUtil.toJSONString(user));
						cache.expire(CacheName.STORE_USER_INFO_TOKEN + DB.getToken(), 30 * 24 * 60 * 60);
						result.setData(vo);
					}
				}
			} else {

				if (LOGIN_LAG == storeUserInfoCondition.getLoginFlag()) {
					// TODO:根据openId查询惠下单用户表登录
				}
				/**
				 * 掉惠下单服务查询门店用户信息 用户名密码登录
				 */
				ResponseResult<Map<String, Object>> object = storeHxdServiceClient.getStoreUserInfo(
						storeUserInfoCondition.getStoreMobile(), storeUserInfoCondition.getStorePassword());
				Map<String, Object> map = object.getData();
				if (map.isEmpty()) {
					return new ResponseResult<>(BusinessCode.CODE_1004);
				} else {
					StoreUserInfo info = new StoreUserInfo();
					info.setStoreCustomerId(Long.parseLong(String.valueOf(map.get("storeCode"))));
					if (DB == null) {
						/**
						 * 如果是微信登录获取昵称头像
						 */
						if (LOGIN_LAG == storeUserInfoCondition.getLoginFlag()) {
							info.setOpenid(storeUserInfoCondition.getOpenid());
							info.setShopOwnerUrl(storeUserInfoCondition.getShopOwnerUrl());
						}
						info.setCreated(new Date());
						info.setStoreMobile(storeUserInfoCondition.getStoreMobile());
						info.setSource(storeUserInfoCondition.getSource());
						info.setToken(String.valueOf(UUID.randomUUID()));
						storeLoginService.saveStoreInfo(info);
						vo.setBusinessId(info.getId());
						vo.setStoreCustomerId(DB.getStoreCustomerId());
						vo.setToken(info.getToken());
						BeanUtils.copyProperties(vo, user);
						cache.set(CacheName.STORE_USER_INFO_TOKEN + info.getToken(), JsonUtil.toJSONString(user));
						cache.expire(CacheName.STORE_USER_INFO_TOKEN + info.getToken(), 30 * 24 * 60 * 60);
					}
				}
			}
			return result;
		} catch (BusinessException e) {
			logger.error("ApiStoreLoginController -> saveStoreLogin异常, 异常信息{}" + e.getMessage(), e.getErrorCode());
			result = new ResponseResult<>(e.getErrorCode());
		} catch (Exception e) {
			logger.error("ApiStoreLoginController -> saveStoreLogin异常, 异常信息{}" + e.getMessage(), e);
			result = new ResponseResult<>(BusinessCode.CODE_1001);
		}
		return result;
	}

	/**
	 * @author wufuyun
	 * @date 2018年8月4日 上午11:10:34
	 * @Description 通过账号发送验证码
	 * @param storeUserInfoCondition
	 * @return
	 */
	@ApiOperation(value = "通过账号发送验证码")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@RequestMapping(value = "1009/v1/sendVerification", method = RequestMethod.POST)
	public ResponseResult<String> sendVerification(@RequestBody StoreUserInfoCondition storeUserInfoCondition) {
		ResponseResult<String> result = new ResponseResult<>();
		try {
			if (null == storeUserInfoCondition) {
				return new ResponseResult<>(BusinessCode.CODE_1007);
			}
			StoreUserInfo storeUserInfo = new StoreUserInfo();
			/**
			 * 验证码登录 拿手机号去惠小店表里面查询是否存在
			 */
			storeUserInfo.setStoreMobile(storeUserInfoCondition.getStoreMobile());
			StoreUserInfo DB = storeLoginService.getstoreUserInfoByMobile(storeUserInfo);
			String content = "";
			/**
			 * 掉惠下单服务查询门店用户信息
			 */
			ResponseResult<Map<String, Object>> object = storeHxdServiceClient.getStoreUserInfo(
					storeUserInfoCondition.getStoreMobile(), storeUserInfoCondition.getStorePassword());
			Map<String, Object> map = object.getData();
			if (map.isEmpty()) {
				return new ResponseResult<>(BusinessCode.CODE_1004);
			} else {
				StoreUserInfo info = new StoreUserInfo();
				info.setStoreCustomerId(Long.parseLong(String.valueOf(map.get("storeCode"))));
				if (DB == null) {
					/**
					 * 如果是微信登录获取昵称头像
					 */
					if (LOGIN_LAG == storeUserInfoCondition.getLoginFlag()) {
						info.setOpenid(storeUserInfoCondition.getOpenid());
						info.setShopOwnerUrl(storeUserInfoCondition.getShopOwnerUrl());
					}
					info.setCreated(new Date());
					info.setStoreMobile(storeUserInfoCondition.getStoreMobile());
					info.setSource(storeUserInfoCondition.getSource());
					info.setToken(String.valueOf(UUID.randomUUID()));
					storeLoginService.saveStoreInfo(info);
				}
				messageServiceClient.sendSMS(storeUserInfoCondition.getStoreMobile(), content);
			}
			logger.info("手机号：" + storeUserInfoCondition.getStoreMobile() + "************发送内容：" + content);
		} catch (BusinessException e) {
			logger.error("ApiStoreLoginController -> sendVerification异常, 异常信息{}" + e.getMessage(), e.getErrorCode());
			result = new ResponseResult<>(e.getErrorCode());
		} catch (Exception e) {
			logger.error("ApiStoreLoginController -> sendVerification异常, 异常信息{}" + e.getMessage(), e);
			result = new ResponseResult<>(BusinessCode.CODE_1001);
		}
		return result;
	}

	/**
	 * @author wufuyun
	 * @date 2018年8月6日 上午10:05:09
	 * @Description 登录成功后查看惠小店产品有无未填写价格的商品提示
	 * @param storeUserInfoCondition
	 * @return
	 */
	@ApiOperation(value = "登录成功查看惠小店有误未填写商品价格")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@RequestMapping(value = "1010/v1/verificationProductPrice", method = RequestMethod.POST)
	public ResponseResult<LoginCheckSellMoneyVO> verificationProductPrice(
			@RequestBody StoreUserInfoCondition storeUserInfoCondition) {
		ResponseResult<LoginCheckSellMoneyVO> result = new ResponseResult<>();
		try {
			if (null == storeUserInfoCondition) {
				return new ResponseResult<>(BusinessCode.CODE_1007);
			}
			ResponseResult<LoginCheckSellMoneyVO> loginCheckSellMoneyVO = storeServiceClient
					.loginCheckSellMoney(storeUserInfoCondition.getId());
			return loginCheckSellMoneyVO;
		} catch (BusinessException e) {
			logger.error("ApiStoreLoginController -> verificationProductPrice异常, 异常信息{}" + e.getMessage(),
					e.getErrorCode());
			result = new ResponseResult<>(e.getErrorCode());
		} catch (Exception e) {
			logger.error("ApiStoreLoginController -> verificationProductPrice异常, 异常信息{}" + e.getMessage(), e);
			result = new ResponseResult<>(BusinessCode.CODE_1001);
		}
		return result;
	}

	/**
	 * @author wufuyun
	 * @date 2018年8月6日 上午10:12:38
	 * @Description 忘记密码
	 * @param storeUserInfoCondition
	 * @return
	 */
	@ApiOperation(value = "忘记密码,修改密码")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1005, message = "密码错误"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
			@ApiResponse(code = BusinessCode.CODE_1008, message = "验证码错误") })
	@RequestMapping(value = "1011/v1/modifyPassword", method = RequestMethod.POST)
	public ResponseResult<String> modifyPassword(@RequestBody StoreUserInfoCondition storeUserInfoCondition) {
		ResponseResult<String> result = new ResponseResult<>();
		StoreUserInfo storeUserInfo = new StoreUserInfo();
		try {
			if (null == storeUserInfoCondition) {
				return new ResponseResult<>(BusinessCode.CODE_1007);
			}
			String cacheVerificationCode = cache.get(storeUserInfoCondition.getStoreMobile());
			/**
			 * 验证App端传过的验证码是否和服务器一致
			 */
			logger.info("App 传过来的验证码：" + storeUserInfoCondition.getVerificationCode() + "************服务端的验证码："
					+ cacheVerificationCode);
			if (!cacheVerificationCode.equals(storeUserInfoCondition.getVerificationCode())) {
				result = new ResponseResult<>(BusinessCode.CODE_1008);
			}
			/**
			 * 设置密码和确认密码不一致
			 */
			if (!storeUserInfoCondition.getStorePassword().equals(storeUserInfoCondition.getConfirmPassword())) {
				result = new ResponseResult<>(BusinessCode.CODE_1005);
			}
			storeUserInfo.setId(storeUserInfoCondition.getId());
			storeUserInfo.setStorePassword(storeUserInfoCondition.getStorePassword());
			storeLoginService.modifyPassword(storeUserInfo);
			return result;
		} catch (BusinessException e) {
			logger.error("ApiStoreLoginController -> modifyPassword异常, 异常信息{}" + e.getMessage(), e.getErrorCode());
			result = new ResponseResult<>(e.getErrorCode());
		} catch (Exception e) {
			logger.error("ApiStoreLoginController -> modifyPassword异常, 异常信息{}" + e.getMessage(), e);
			result = new ResponseResult<>(BusinessCode.CODE_1001);
		}
		return result;
	}
}
