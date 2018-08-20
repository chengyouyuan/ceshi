package com.winhxd.b2c.customer.api;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.model.CustomerUserInfo;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoSimpleVO;
import com.winhxd.b2c.common.domain.message.condition.SMSCondition;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerChangeMobileCondition;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerSendVerificationCodeCondition;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.common.util.GeneratePwd;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.common.util.MessageSendUtils;
import com.winhxd.b2c.customer.service.CustomerLoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author wufuyun
 * @date 2018年8月3日 上午9:44:11
 * @Description 微信小程序登录Controller
 * @version
 */
@Api(value = "CustomerLogin Controller", tags = "C-Login")
@RestController
@RequestMapping(value = "/api-customer/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiCustomerLoginController {
	private static final Logger logger = LoggerFactory.getLogger(ApiCustomerLoginController.class);

	@Autowired
	private CustomerLoginService customerLoginService;
	@Autowired
	private Cache cache;
	@Autowired
	MessageServiceClient messageServiceClient;
	@Autowired
	MessageSendUtils messageSendUtils;

	/**
	 * @author wufuyun
	 * @date 2018年8月3日 下午1:31:45
	 * @Description 通过账号验证码登录
	 * @param customerUserInfoCondition
	 * @return
	 */
	@ApiOperation(value = "微信小程序登录接口")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_202108, message = "验证码错误"),
			@ApiResponse(code = BusinessCode.CODE_202110, message = "该微信号已绑定过其它账号"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
			@ApiResponse(code = BusinessCode.CODE_202115, message = "网络请求超时") })

	@RequestMapping(value = "customer/security/2021/v1/weChatLogin", method = RequestMethod.POST)
	public ResponseResult<CustomerUserInfoSimpleVO> weChatLogin(
			@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
		logger.info("{} -微信小程序登录, 参数：storeUserInfoCondition={}", "", JsonUtil.toJSONString(customerUserInfoCondition));
		ResponseResult<CustomerUserInfoSimpleVO> result = new ResponseResult<>();
		ResponseResult<MiniOpenId> object = null;
		MiniOpenId mini = null;
		CustomerUserInfoSimpleVO vo;
		if (null == customerUserInfoCondition) {
			logger.info("{} - , 参数无效");
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		if (StringUtils.isBlank(customerUserInfoCondition.getCustomerMobile())) {
			logger.info("{} - , 参数无效");
			result = new ResponseResult<>(BusinessCode.CODE_1007);
			return result;
		}
		/**
		 * 根据手机号去取缓存verificationCode对比是否一致
		 */
		if (!customerUserInfoCondition.getVerificationCode().equals(cache
				.get(CacheName.CUSTOMER_USER_SEND_VERIFICATION_CODE + customerUserInfoCondition.getCustomerMobile()))) {
			logger.info("{} - ,验证码错误");
			throw new BusinessException(BusinessCode.CODE_202108);
		}
		CustomerUserInfo customerUserInfo = new CustomerUserInfo();
		CustomerUserInfo db = null;
		/**
		 * 拿code去换session_key
		 */
		object = messageServiceClient.getMiniOpenId(customerUserInfoCondition.getCode());
		if (object.getCode() != 0) {
			logger.info("{} - ,网络请求超时");
			throw new BusinessException(BusinessCode.CODE_202115);
		}
		mini = object.getData();
		customerUserInfo.setOpenid(mini.getOpenid());
		db = customerLoginService.getCustomerUserInfoByModel(customerUserInfo);
		if (null == db) {
			customerUserInfo.setSessionKey(mini.getSessionKey());
			customerUserInfo.setCreated(new Date());
			customerUserInfo.setCustomerMobile(customerUserInfoCondition.getCustomerMobile());
			customerUserInfo.setHeadImg(customerUserInfoCondition.getHeadImg());
			customerUserInfo.setNickName(customerUserInfoCondition.getNickName());
			customerUserInfo.setToken(GeneratePwd.getRandomUUID());
			customerLoginService.saveLoginInfo(customerUserInfo);
			vo = new CustomerUserInfoSimpleVO();
			vo.setCustomerMobile(customerUserInfoCondition.getCustomerMobile());
			vo.setToken(customerUserInfo.getToken());
			CustomerUser user = new CustomerUser();
			user.setCustomerId(customerUserInfo.getCustomerId());
			user.setOpenid(mini.getOpenid());
			cache.set(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(), JsonUtil.toJSONString(user));
			cache.expire(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(), 30 * 24 * 60 * 60);
			result.setData(vo);
		} else {
			if (!db.getCustomerMobile().equals(customerUserInfoCondition.getCustomerMobile())) {
				logger.info("{} - , 该微信号已被其他手机号绑定");
				throw new BusinessException(BusinessCode.CODE_202110);
			}
			customerUserInfo.setCustomerId(db.getCustomerId());
			customerUserInfo.setSessionKey(mini.getSessionKey());
			customerUserInfo.setToken(GeneratePwd.getRandomUUID());
			cache.del(CacheName.CUSTOMER_USER_INFO_TOKEN +db.getToken());
			customerLoginService.updateCustomerInfo(customerUserInfo);
			vo = new CustomerUserInfoSimpleVO();
			vo.setCustomerMobile(db.getCustomerMobile());
			vo.setToken(customerUserInfo.getToken());
			CustomerUser user = new CustomerUser();
			user.setOpenid(db.getOpenid());
			user.setCustomerId(db.getCustomerId());
			cache.set(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(), JsonUtil.toJSONString(user));
			cache.expire(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(), 30 * 24 * 60 * 60);
			result.setData(vo);
		}
		return result;
	}

	/**
	 * @author wufuyun
	 * @date 2018年8月3日 下午1:31:45
	 * @Description 通过账号发送验证码
	 * @param customerUserInfoCondition
	 * @return 验证码
	 */
	@ApiOperation(value = "发送验证码")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_202212, message = "验证码请求时长没有超过一分钟"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@RequestMapping(value = "customer/security/2022/v1/sendVerification", method = RequestMethod.POST)
	public ResponseResult<String> sendVerification(
			@RequestBody CustomerSendVerificationCodeCondition customerUserInfoCondition) {
		ResponseResult<String> result = new ResponseResult<>();
		if (null == customerUserInfoCondition) {
			logger.info("{} - 发送验证码, 参数：customerUserInfoCondition={}", "",
					JsonUtil.toJSONString(customerUserInfoCondition));
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		if (cache.exists(
				CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + customerUserInfoCondition.getCustomerMobile())) {
			logger.info("{} -发送验证码未超过一分钟");
			throw new BusinessException(BusinessCode.CODE_202212);
		}
		/**
		 * 随机生成6位数验证码
		 */
		String verificationCode = GeneratePwd.generatePwd6Mobile();
		cache.set(CacheName.CUSTOMER_USER_SEND_VERIFICATION_CODE + customerUserInfoCondition.getCustomerMobile(),
				verificationCode);
		cache.expire(CacheName.CUSTOMER_USER_SEND_VERIFICATION_CODE + customerUserInfoCondition.getCustomerMobile(),
				5 * 60);
		/**
		 * 60秒以后调用短信服务
		 */
		cache.set(CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + customerUserInfoCondition.getCustomerMobile(),
				verificationCode);
		cache.expire(CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + customerUserInfoCondition.getCustomerMobile(), 60);
		/**
		 * 发送模板内容
		 */
		String content = "【小程序】验证码：" + verificationCode + ",有效时间五分钟";
		SMSCondition sMSCondition = new SMSCondition();
		sMSCondition.setContent(content);
		sMSCondition.setMobile(customerUserInfoCondition.getCustomerMobile());
		messageSendUtils.sendSMS(sMSCondition);
		logger.info(customerUserInfoCondition.getCustomerMobile() + ":发送的内容为:" + content);
		return result;
	}

	/**
	 * @author wufuyun
	 * @date 2018年8月8日 下午8:56:52
	 * @Description 用户换绑手机号
	 * @param customerChangeMobileCondition
	 * @return
	 */
	@ApiOperation(value = "C端—用户换绑手机号")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
			@ApiResponse(code = BusinessCode.CODE_202308, message = "验证码错误"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@RequestMapping(value = "customer/2023/v1/customerChangeMobile", method = RequestMethod.POST)
	public ResponseResult<CustomerUserInfoSimpleVO> customerChangeMobile(
			@RequestBody CustomerChangeMobileCondition customerChangeMobileCondition) {
		logger.info("{} - 用户换绑手机号, 参数：customerUserInfoCondition={}", "",
				JsonUtil.toJSONString(customerChangeMobileCondition));
		ResponseResult<CustomerUserInfoSimpleVO> result = new ResponseResult<>();
		CustomerUserInfo customerUserInfo = new CustomerUserInfo();
		CustomerUserInfoSimpleVO vo = new CustomerUserInfoSimpleVO();
		if (null == customerChangeMobileCondition) {
			logger.info("{} - 用户换绑手机号, 参数：customerChangeMobileCondition={}", "",
					JsonUtil.toJSONString(customerChangeMobileCondition));
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		CustomerUserInfo info = new CustomerUserInfo();
		CustomerUser user = UserContext.getCurrentCustomerUser();
		if (null == user) {
			logger.info("{} - 未取到用户登录信息", "", JsonUtil.toJSONString(user));
			throw new BusinessException(BusinessCode.CODE_1002);
		}
		customerUserInfo.setOpenid(user.getOpenid());
		customerUserInfo = customerLoginService.getCustomerUserInfoByModel(customerUserInfo);
		if (null == customerUserInfo) {
			logger.info("{} - ");
			throw new BusinessException(BusinessCode.CODE_1002);
		}
		if (!customerChangeMobileCondition.getVerificationCode().equals(
				cache.get(CacheName.CUSTOMER_USER_SEND_VERIFICATION_CODE + customerChangeMobileCondition.getCustomerMobile()))) {
			logger.info("{} - 用户验证码错误");
			throw new BusinessException(BusinessCode.CODE_202308);
		}
		info.setCustomerMobile(customerChangeMobileCondition.getCustomerMobile());
		info.setCustomerId(user.getCustomerId());
		customerLoginService.updateCustomerInfo(info);
		vo.setCustomerMobile(customerChangeMobileCondition.getCustomerMobile());
		vo.setToken(customerUserInfo.getToken());
		result.setData(vo);
		return result;
	}
}
