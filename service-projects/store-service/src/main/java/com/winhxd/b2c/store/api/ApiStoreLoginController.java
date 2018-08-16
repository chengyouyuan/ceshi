package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import com.winhxd.b2c.common.domain.store.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoSimpleVO;
import com.winhxd.b2c.common.domain.system.login.condition.StoreSendVerificationCodeCondition;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserInfoCondition;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserSimpleInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.hxd.StoreHxdServiceClient;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.GeneratePwd;
import com.winhxd.b2c.common.util.JsonUtil;
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
	 * 惠小店状态 1有效
	 */
	static final Integer HXD_STATUS1 = 1;
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
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
			@ApiResponse(code = BusinessCode.CODE_1011, message = "微信快捷登录绑定账号无效") })
	@RequestMapping(value = "store/security/1008/v1/storeLogin", method = RequestMethod.POST)
	public ResponseResult<StoreUserInfoSimpleVO> storeLogin(
			@RequestBody StoreUserInfoCondition storeUserInfoCondition) {
		logger.info("{} - B端登录验证, 参数：storeUserInfoCondition={}", "", JsonUtil.toJSONString(storeUserInfoCondition));
		ResponseResult<StoreUserInfoSimpleVO> result = new ResponseResult<>();
		if (null == storeUserInfoCondition) {
			logger.info("{} - , 参数无效");
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		StoreUserInfo db = null;
		StoreUserInfo storeUserInfo = new StoreUserInfo();
		StoreUser user = new StoreUser();
		StoreUserInfoSimpleVO vo = new StoreUserInfoSimpleVO();
		StoreUserInfo open = new StoreUserInfo();
		/**
		 * 微信验证码登录
		 */
		if (LOGIN_LAG.equals(storeUserInfoCondition.getLoginFlag())
				&& LOGIN_PASSWORD_LAG_1.equals(storeUserInfoCondition.getLoginPasswordFlag())) {
			if (!storeUserInfoCondition.getVerificationCode().equals(
					cache.get(CacheName.STORE_USER_SEND_VERIFICATION_CODE + storeUserInfoCondition.getStoreMobile()))) {
				logger.info("{} - , 验证码错误");
				throw new BusinessException(BusinessCode.CODE_1008);
			}
			storeUserInfo.setOpenid(storeUserInfoCondition.getOpenid());
			db = storeLoginService.getStoreUserInfo(storeUserInfo);
			/**
			 * 查库
			 */
			if (db == null) {
				logger.info("{} - , 账号无效");
				result = new ResponseResult<>(BusinessCode.CODE_1004);
				return result;
			} else {
				
				/**
				 * 调用云信服务获取用户信息
				 */
				getNeteaseAcctInfo(db, vo);
				cache.del(CacheName.STORE_USER_INFO_TOKEN +db.getToken());
				storeUserInfo.setToken(GeneratePwd.getRandomUUID());
				storeUserInfo.setId(db.getId());
				storeLoginService.modifyStoreUserInfo(storeUserInfo);
				vo.setToken(storeUserInfo.getToken());
				vo.setCustomerId(db.getStoreCustomerId());
				storeUserInfo.setStoreCustomerId(db.getStoreCustomerId());
				getStoreUserInfoToken(storeUserInfo, user);
				result.setData(vo);
			}
		}
		/**
		 * 微信密码登录
		 */
		else if (LOGIN_LAG.equals(storeUserInfoCondition.getLoginFlag())
				&& LOGIN_PASSWORD_LAG_2.equals(storeUserInfoCondition.getLoginPasswordFlag())) {
			ResponseResult<StoreUserSimpleInfo> object = storeHxdServiceClient.getStoreUserInfo(
					storeUserInfoCondition.getStoreMobile(), storeUserInfoCondition.getStorePassword());
			StoreUserSimpleInfo map = object.getData();
			if (map == null) {
				logger.info("{} - , 账号无效");
				throw new BusinessException(BusinessCode.CODE_1004);
			} else {
				storeUserInfo.setStoreCustomerId(map.getStoreCustomerId());
				db = storeLoginService.getStoreUserInfo(storeUserInfo);
				if (db != null) {

					if (StringUtils.isBlank(db.getOpenid())) {
						storeUserInfo.setOpenid(storeUserInfoCondition.getOpenid());
					}
					/**
					 * 调用云信服务获取用户信息
					 */
					getNeteaseAcctInfo(db, vo);
					
					cache.del(CacheName.STORE_USER_INFO_TOKEN +db.getToken());
					/**
					 * 更新数据库
					 */
					storeUserInfo.setId(db.getId());
					storeUserInfo.setStoreMobile(map.getStoreMobile());
					storeUserInfo.setToken(GeneratePwd.getRandomUUID());
					storeLoginService.modifyStoreUserInfo(storeUserInfo);
					vo.setToken(storeUserInfo.getToken());
					vo.setCustomerId(db.getStoreCustomerId());
					storeUserInfo.setStoreCustomerId(db.getStoreCustomerId());
					getStoreUserInfoToken(storeUserInfo, user);
					result.setData(vo);
				} else {

					/**
					 * 查询OpenId是否 已经绑定其他手机号
					 */
					open.setOpenid(storeUserInfoCondition.getOpenid());
					db = storeLoginService.getStoreUserInfo(open);
					/**
					 * 如果可以查到。。证明该微信号绑定过其他账号
					 */
					if (null != db) {
						logger.info("{} - , 该微信号已绑定过账号");
						throw new BusinessException(BusinessCode.CODE_1010);
					}
					/*
					 * 插入数据库
					 */
					storeUserInfo.setOpenid(storeUserInfoCondition.getOpenid());
					storeUserInfo.setStoreCustomerId(map.getStoreCustomerId());
					storeUserInfo.setStoreRegionCode(map.getStoreRegionCode());
					logger.info("头像:" + storeUserInfoCondition.getShopOwnerImg());
					storeUserInfo.setShopOwnerImg(storeUserInfoCondition.getShopOwnerImg());
					storeUserInfo.setCreated(new Date());
					storeUserInfo.setStoreMobile(map.getStoreMobile());
					storeUserInfo.setSource(storeUserInfoCondition.getMobileInfo().getPlatform());
					storeUserInfo.setStoreStatus((short) 0);
					storeUserInfo.setToken(GeneratePwd.getRandomUUID());
					storeLoginService.saveStoreInfo(storeUserInfo);
					vo.setToken(storeUserInfo.getToken());
					vo.setCustomerId(map.getStoreCustomerId());
					getStoreUserInfoToken(storeUserInfo, user);
					result.setData(vo);
				}
			}

		}
		/**
		 * 微信快捷登录
		 */
		else if (LOGIN_LAG.equals(storeUserInfoCondition.getLoginFlag())
				&& LOGIN_PASSWORD_LAG_3.equals(storeUserInfoCondition.getLoginPasswordFlag())) {
			storeUserInfo.setOpenid(storeUserInfoCondition.getOpenid());
			db = storeLoginService.getStoreUserInfo(storeUserInfo);
			if (db == null) {
				logger.info("{} - , 账号无效");
				throw new BusinessException(BusinessCode.CODE_1004);
			}
			ResponseResult<StoreUserSimpleInfo> object = storeHxdServiceClient
					.getStoreUserInfoByCustomerId(db.getStoreCustomerId());
			StoreUserSimpleInfo map = object.getData();
			if (map == null) {
				logger.info("{} - , 微信快捷登录绑定账号无效");
				throw new BusinessException(BusinessCode.CODE_1011);
			}
			/**
			 * 调用云信服务获取用户信息
			 */
			getNeteaseAcctInfo(db, vo);
			cache.del(CacheName.STORE_USER_INFO_TOKEN +db.getToken());
			storeUserInfo.setId(db.getId());
			storeUserInfo.setStoreMobile(map.getStoreMobile());
			logger.info("头像:" + storeUserInfoCondition.getShopOwnerImg());
			storeUserInfo.setShopOwnerImg(storeUserInfoCondition.getShopOwnerImg());
			storeUserInfo.setToken(GeneratePwd.getRandomUUID());
			storeLoginService.modifyStoreUserInfo(storeUserInfo);
			vo.setToken(storeUserInfo.getToken());
			vo.setCustomerId(db.getStoreCustomerId());
			storeUserInfo.setStoreCustomerId(db.getStoreCustomerId());
			getStoreUserInfoToken(storeUserInfo, user);
			result.setData(vo);

		}
		/**
		 * 账号验证码登录
		 */
		else if (LOGIN_LAG_2.equals(storeUserInfoCondition.getLoginFlag())
				&& LOGIN_PASSWORD_LAG_1.equals(storeUserInfoCondition.getLoginPasswordFlag())) {
			if (!storeUserInfoCondition.getVerificationCode().equals(
					cache.get(CacheName.STORE_USER_SEND_VERIFICATION_CODE + storeUserInfoCondition.getStoreMobile()))) {
				logger.info("{} - , 验证码错误");
				throw new BusinessException(BusinessCode.CODE_1008);
			}
			storeUserInfo.setStoreMobile(storeUserInfoCondition.getStoreMobile());
			db = storeLoginService.getStoreUserInfo(storeUserInfo);
			/**
			 * 查库
			 */
			if (db == null) {
				logger.info("{} - , 账号无效");
				throw new BusinessException(BusinessCode.CODE_1004);
			} else {
				/**
				 * 调用云信服务获取用户信息
				 */
				getNeteaseAcctInfo(db, vo);
				cache.del(CacheName.STORE_USER_INFO_TOKEN +db.getToken());
				storeUserInfo.setToken(GeneratePwd.getRandomUUID());
				storeUserInfo.setId(db.getId());
				storeLoginService.modifyStoreUserInfo(storeUserInfo);
				vo.setToken(storeUserInfo.getToken());
				vo.setCustomerId(db.getStoreCustomerId());
				storeUserInfo.setStoreCustomerId(db.getStoreCustomerId());
				getStoreUserInfoToken(storeUserInfo, user);
				result.setData(vo);
			}
		}
		/**
		 * 账号密码登录
		 */
		else if (LOGIN_LAG_2.equals(storeUserInfoCondition.getLoginFlag())
				&& LOGIN_PASSWORD_LAG_2.equals(storeUserInfoCondition.getLoginPasswordFlag())) {

			ResponseResult<StoreUserSimpleInfo> object = storeHxdServiceClient.getStoreUserInfo(
					storeUserInfoCondition.getStoreMobile(), storeUserInfoCondition.getStorePassword());
			StoreUserSimpleInfo map = object.getData();
			if (map == null) {
				logger.info("{} - , 账号无效");
				throw new BusinessException(BusinessCode.CODE_1004);
			} else {
				storeUserInfo.setStoreCustomerId(map.getStoreCustomerId());
				db = storeLoginService.getStoreUserInfo(storeUserInfo);
				if (db != null) {
					/**
					 * 调用云信服务获取用户信息
					 */
					getNeteaseAcctInfo(db, vo);
					cache.del(CacheName.STORE_USER_INFO_TOKEN +db.getToken());
					/**
					 * 更新数据库
					 */
					storeUserInfo.setId(db.getId());
					storeUserInfo.setStoreMobile(map.getStoreMobile());
					storeUserInfo.setToken(GeneratePwd.getRandomUUID());
					storeLoginService.modifyStoreUserInfo(storeUserInfo);
					vo.setToken(storeUserInfo.getToken());
					vo.setCustomerId(db.getStoreCustomerId());
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
					storeUserInfo.setCreated(new Date());
					storeUserInfo.setStoreMobile(map.getStoreMobile());
					storeUserInfo.setSource(storeUserInfoCondition.getMobileInfo().getPlatform());
					storeUserInfo.setStoreStatus((short) 0);
					storeUserInfo.setToken(GeneratePwd.getRandomUUID());
					storeLoginService.saveStoreInfo(storeUserInfo);
					vo.setToken(storeUserInfo.getToken());
					vo.setCustomerId(map.getStoreCustomerId());
					getStoreUserInfoToken(storeUserInfo, user);
					result.setData(vo);
				}
			}

		}
		logger.info("{} - B端登录返参, 参数：StoreUserInfoSimpleVO={}", "", JsonUtil.toJSONString(vo));
		return result;
	}

	private void getNeteaseAcctInfo(StoreUserInfo db, StoreUserInfoSimpleVO vo) {
		ResponseResult<NeteaseAccountVO> netease;
		NeteaseAccountVO accountVO;
		NeteaseAccountCondition neteaseAccountCondition = new NeteaseAccountCondition();
		if(HXD_STATUS1.equals(db.getStoreStatus())){
			neteaseAccountCondition.setCustomerId(db.getId());
			netease =  messageServiceClient.getNeteaseAccountInfo(neteaseAccountCondition);
			if(netease.getCode() != 0){
				logger.info("{} - , 云信获取用户信息失败");
				throw new BusinessException(BusinessCode.CODE_1015);
			}
			accountVO =  netease.getData();
			vo.setNeteaseAccid(accountVO.getAccid());
			vo.setNeteaseToken(accountVO.getToken());
		}
	}

	private void getStoreUserInfoToken(StoreUserInfo db, StoreUser user) {
		user.setBusinessId(db.getId());
		user.setStoreCustomerId(db.getStoreCustomerId());
		cache.set(CacheName.STORE_USER_INFO_TOKEN + db.getToken(), JsonUtil.toJSONString(user));
		cache.expire(CacheName.STORE_USER_INFO_TOKEN + db.getToken(), 30 * 24 * 60 * 60);
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
			@ApiResponse(code = BusinessCode.CODE_1012, message = "验证码请求时长没有超过一分钟"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1010, message = "该微信号已绑定过账号") })
	@RequestMapping(value = "store/security/1009/v1/sendVerification", method = RequestMethod.POST)
	public ResponseResult<String> sendVerification(
			@RequestBody StoreSendVerificationCodeCondition storeSendVerificationCodeCondition) {
		logger.info("{} -账号发送验证码, 参数：storeUserInfoCondition={}", "",
				JsonUtil.toJSONString(storeSendVerificationCodeCondition));
		ResponseResult<String> result = new ResponseResult<>();
		if (null == storeSendVerificationCodeCondition) {
			logger.info("{} - , 参数无效");
			throw new BusinessException(BusinessCode.CODE_1004);
		}
		/**
		 * 掉惠下单服务查询门店用户信息
		 */
		ResponseResult<StoreUserSimpleInfo> object = storeHxdServiceClient
				.getStoreUserInfo(storeSendVerificationCodeCondition.getStoreMobile(), "");
		StoreUserSimpleInfo map = object.getData();
		if (map == null) {
			logger.info("{} - , 账号无效");
			throw new BusinessException(BusinessCode.CODE_1004);
		} else {
			/**
			 * 用惠下单StoreCustomerId查询惠小店数据库是否已经存在
			 */
			StoreUserInfo info = new StoreUserInfo();
			StoreUserInfo db = new StoreUserInfo();
			StoreUserInfo open = new StoreUserInfo();
			/**
			 * 如果是微信登录验证OpenId 是否绑定手机号是否与app传过来的一致
			 */
			if (LOGIN_LAG.equals(storeSendVerificationCodeCondition.getLoginFlag())) {
				info.setStoreCustomerId(map.getStoreCustomerId());
				db = storeLoginService.getStoreUserInfo(info);
				if (db != null) {
					if (StringUtils.isBlank(db.getOpenid())) {
						info.setOpenid(storeSendVerificationCodeCondition.getOpenid());
					}
					/**
					 * 更新数据库
					 */
					info.setId(db.getId());
					info.setStoreMobile(map.getStoreMobile());
					storeLoginService.modifyStoreUserInfo(info);
					result = sendVerificationCode(map.getStoreMobile());
				} else {
					/**
					 * 查询OpenId是否 已经绑定其他手机号
					 */
					if (StringUtils.isBlank(storeSendVerificationCodeCondition.getOpenid())) {
						logger.info("{} - ,openId为空");
						throw new BusinessException(BusinessCode.CODE_1007);
					}
					open.setOpenid(storeSendVerificationCodeCondition.getOpenid());
					db = storeLoginService.getStoreUserInfo(open);
					/**
					 * 如果可以查到。。证明该微信号绑定过其他账号
					 */
					if (null != db) {
						logger.info("{} - , 该微信号已绑定过账号");
						throw new BusinessException(BusinessCode.CODE_1010);
					}
					/*
					 * 插入数据库
					 */
					info.setOpenid(storeSendVerificationCodeCondition.getOpenid());
					info.setStoreCustomerId(map.getStoreCustomerId());
					info.setStoreRegionCode(map.getStoreRegionCode());
					info.setShopOwnerImg(storeSendVerificationCodeCondition.getShopOwnerImg());
					info.setCreated(new Date());
					info.setStoreMobile(map.getStoreMobile());
					logger.info("微信头像：" + storeSendVerificationCodeCondition.getShopOwnerImg());
					info.setSource(storeSendVerificationCodeCondition.getMobileInfo().getPlatform());
					info.setStoreStatus((short) 0);
					info.setToken(GeneratePwd.getRandomUUID());
					storeLoginService.saveStoreInfo(info);
					result = sendVerificationCode(map.getStoreMobile());
				}
			}
			/**
			 * 不是微信登录
			 */
			else {
				info.setStoreCustomerId(map.getStoreCustomerId());
				db = storeLoginService.getStoreUserInfo(info);
				/*
				 * 插入数据库
				 */
				if (db == null) {
					info.setCreated(new Date());
					info.setStoreMobile(map.getStoreMobile());
					info.setStoreCustomerId(map.getStoreCustomerId());
					info.setStoreRegionCode(map.getStoreRegionCode());
					info.setSource(storeSendVerificationCodeCondition.getMobileInfo().getPlatform());
					info.setStoreStatus((short) 0);
					info.setToken(GeneratePwd.getRandomUUID());
					storeLoginService.saveStoreInfo(info);
					result = sendVerificationCode(map.getStoreMobile());
				}
				/**
				 * 更新数据库
				 */
				else {
					info.setId(db.getId());
					info.setStoreMobile(map.getStoreMobile());
					storeLoginService.modifyStoreUserInfo(info);
					result = sendVerificationCode(map.getStoreMobile());
				}
			}

		}
		return result;
	}

	/**
	 * @author wufuyun
	 * @date 2018年8月8日 下午8:38:21
	 * @Description 发送验证码检验
	 * @param storeMobile
	 * @return
	 */
	private ResponseResult<String> sendVerificationCode(String storeMobile) {
		ResponseResult<String> result = new ResponseResult<>();
		String content = "";
		String verificationCode = "";
		if (cache.exists(CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + storeMobile)) {
			logger.info("{} - , 请求验证码时长为超过一分钟");
			throw new BusinessException(BusinessCode.CODE_1012);
		}
		/**
		 * 随机生成6位数验证码
		 */
		verificationCode = "888888";// GeneratePwd.generatePwd6Mobile();
		cache.set(CacheName.STORE_USER_SEND_VERIFICATION_CODE + storeMobile, verificationCode);
		cache.expire(CacheName.STORE_USER_SEND_VERIFICATION_CODE + storeMobile, 5 * 60);
		/**
		 * 60秒以后调用短信服务
		 */
		cache.set(CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + storeMobile, verificationCode);
		cache.expire(CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + storeMobile, 60);
		/**
		 * 发送模板内容
		 */
		content = "【惠小店】验证码：" + verificationCode + ",有效时间五分钟";
		// messageServiceClient.sendSMS(storeMobile, content);
		logger.info(storeMobile + ":发送的内容为:" + content);
		return result;
	}

	/**
	 * @author wufuyun
	 * @date 2018年8月6日 上午10:05:09
	 * @Description 登录成功后查看惠小店产品有无未填写价格的商品提示
	 * @param storeUserInfoCondition
	 * @return
	 */
	/*
	 * @ApiOperation(value = "登录成功查看惠小店有误未填写商品价格")
	 * 
	 * @ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message =
	 * "成功"),
	 * 
	 * @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	 * 
	 * @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	 * 
	 * @RequestMapping(value = "1010/v1/verificationProductPrice", method =
	 * RequestMethod.POST) public ResponseResult<LoginCheckSellMoneyVO>
	 * verificationProductPrice(
	 * 
	 * @RequestBody StoreUserInfoCondition storeUserInfoCondition) {
	 * ResponseResult<LoginCheckSellMoneyVO> result = new ResponseResult<>();
	 * try { if (null == storeUserInfoCondition) { return new
	 * ResponseResult<>(BusinessCode.CODE_1007); }
	 * ResponseResult<LoginCheckSellMoneyVO> loginCheckSellMoneyVO =
	 * storeServiceClient .loginCheckSellMoney(1L); return
	 * loginCheckSellMoneyVO; } catch (BusinessException e) { logger.
	 * error("ApiStoreLoginController -> verificationProductPrice异常, 异常信息{}" +
	 * e.getMessage(), e.getErrorCode()); result = new
	 * ResponseResult<>(e.getErrorCode()); } catch (Exception e) { logger.
	 * error("ApiStoreLoginController -> verificationProductPrice异常, 异常信息{}" +
	 * e.getMessage(), e); result = new
	 * ResponseResult<>(BusinessCode.CODE_1001); } return result; }
	 * 
	 *//**
		 * @author wufuyun
		 * @date 2018年8月6日 上午10:12:38
		 * @Description 忘记密码
		 * @param storeUserInfoCondition
		 * @return
		 */
	/*
	 * @ApiOperation(value = "忘记密码,修改密码")
	 * 
	 * @ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message =
	 * "成功"),
	 * 
	 * @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	 * 
	 * @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
	 * 
	 * @ApiResponse(code = BusinessCode.CODE_1005, message = "密码错误"),
	 * 
	 * @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
	 * 
	 * @ApiResponse(code = BusinessCode.CODE_1008, message = "验证码错误") })
	 * 
	 * @RequestMapping(value = "1011/v1/modifyPassword", method =
	 * RequestMethod.POST) public ResponseResult<String>
	 * modifyPassword(@RequestBody StoreUserInfoCondition
	 * storeUserInfoCondition) { ResponseResult<String> result = new
	 * ResponseResult<>(); StoreUserInfo storeUserInfo = new StoreUserInfo();
	 * try { if (null == storeUserInfoCondition) { return new
	 * ResponseResult<>(BusinessCode.CODE_1007); } String cacheVerificationCode
	 * = cache.get(storeUserInfoCondition.getStoreMobile());
	 *//**
		 * 验证App端传过的验证码是否和服务器一致
		 */
	/*
	 * logger.info("App 传过来的验证码：" + storeUserInfoCondition.getVerificationCode()
	 * + "************服务端的验证码：" + cacheVerificationCode); if
	 * (!cacheVerificationCode.equals(storeUserInfoCondition.getVerificationCode
	 * ())) { result = new ResponseResult<>(BusinessCode.CODE_1008); }
	 *//**
		 * 设置密码和确认密码不一致
		 *//*
		 * if (!storeUserInfoCondition.getStorePassword().equals(
		 * storeUserInfoCondition.getConfirmPassword())) { result = new
		 * ResponseResult<>(BusinessCode.CODE_1005); } storeUserInfo.setId(1L);
		 * storeUserInfo.setStorePassword(storeUserInfoCondition.
		 * getStorePassword());
		 * storeLoginService.modifyStoreUserInfo(storeUserInfo); return result;
		 * } catch (BusinessException e) {
		 * logger.error("ApiStoreLoginController -> modifyPassword异常, 异常信息{}" +
		 * e.getMessage(), e.getErrorCode()); result = new
		 * ResponseResult<>(e.getErrorCode()); } catch (Exception e) {
		 * logger.error("ApiStoreLoginController -> modifyPassword异常, 异常信息{}" +
		 * e.getMessage(), e); result = new
		 * ResponseResult<>(BusinessCode.CODE_1001); } return result; }
		 */
}
