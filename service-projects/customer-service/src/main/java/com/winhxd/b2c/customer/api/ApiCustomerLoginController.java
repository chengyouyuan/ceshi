package com.winhxd.b2c.customer.api;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.AppConstant;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.constant.SendSMSTemplate;
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
	 * 小程序用户状态 0无效
	 */
	static final Integer HXD_STATUS0 = 0;
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
			@ApiResponse(code = BusinessCode.CODE_202115, message = "网络请求超时"),
			@ApiResponse(code = BusinessCode.CODE_202109, message = "您的账号存在异常行为，已被锁定，如有疑问请联系客服4006870066。") })

	@RequestMapping(value = "customer/security/2021/v1/weChatLogin", method = RequestMethod.POST)
	public ResponseResult<CustomerUserInfoSimpleVO> weChatLogin(@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
        String logTitle = "=/api-customer/customer/security/2021/v1/weChatLogin-C端小程序登录接口=";
        logger.info("{} -微信小程序登录, 参数：storeUserInfoCondition={}", logTitle, JsonUtil.toJSONString(customerUserInfoCondition));
		if (null == customerUserInfoCondition) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		if (StringUtils.isBlank(customerUserInfoCondition.getCustomerMobile())) {
			return new ResponseResult<>(BusinessCode.CODE_1007);
		}
		ResponseResult<CustomerUserInfoSimpleVO> result = new ResponseResult<>();
		ResponseResult<MiniOpenId> object = null;
		MiniOpenId mini = null;
		CustomerUserInfoSimpleVO vo = null;
		/**
		 * 根据手机号去取缓存verificationCode对比是否一致
		 */
		if (!customerUserInfoCondition.getVerificationCode().equals(cache
				.get(CacheName.CUSTOMER_USER_SEND_VERIFICATION_CODE + customerUserInfoCondition.getCustomerMobile()))) {
            logger.info("{} - ,验证码错误", logTitle);
			throw new BusinessException(BusinessCode.CODE_202108);
		}
		CustomerUserInfo customerUserInfo = new CustomerUserInfo();
		CustomerUserInfo db = null;
		/**
		 * 拿code去换session_key
		 */
		object = messageServiceClient.getMiniOpenId(customerUserInfoCondition.getCode());
		if (object.getCode() != 0) {
            logger.info("{} - ,网络请求超时", logTitle);
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
			// 为获取数盟id，添加返回参数openId
			vo.setOpenid(mini.getOpenid());

			CustomerUser user = new CustomerUser();
			user.setCustomerId(customerUserInfo.getCustomerId());
			user.setOpenid(mini.getOpenid());
			cache.set(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(), JsonUtil.toJSONString(user));
			cache.expire(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(),
					AppConstant.LOGIN_APP_TOKEN_EXPIRE_SECOND);
			result.setData(vo);
		} else {
			if(HXD_STATUS0.equals(db.getStatus())){
                logger.info("{} -,您的账号-{}-存在异常行为，已被锁定，如有疑问请联系客服4006870066。", logTitle, db.getCustomerMobile());
				throw new BusinessException(BusinessCode.CODE_202109);
			}
			if (!db.getCustomerMobile().equals(customerUserInfoCondition.getCustomerMobile())) {
                logger.info("{} -,该微信号已被其他手机号绑定,该微信号的openid 为-{}-", logTitle, db.getOpenid());
				throw new BusinessException(BusinessCode.CODE_202110);
			}
			cache.del(CacheName.CUSTOMER_USER_INFO_TOKEN + db.getToken());
			customerUserInfo.setCustomerId(db.getCustomerId());
			customerUserInfo.setHeadImg(customerUserInfoCondition.getHeadImg());
			customerUserInfo.setNickName(customerUserInfoCondition.getNickName());
			customerUserInfo.setSessionKey(mini.getSessionKey());
			customerUserInfo.setUpdated(new Date());
			customerUserInfo.setToken(GeneratePwd.getRandomUUID());
			customerLoginService.updateCustomerInfo(customerUserInfo);
			vo = new CustomerUserInfoSimpleVO();
			vo.setCustomerMobile(db.getCustomerMobile());
			vo.setToken(customerUserInfo.getToken());
			// 为获取数盟id，添加返回参数openId
			vo.setOpenid(mini.getOpenid());

			CustomerUser user = new CustomerUser();
			user.setOpenid(db.getOpenid());
			user.setCustomerId(db.getCustomerId());

			cache.set(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(), JsonUtil.toJSONString(user));
			cache.expire(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(),
					AppConstant.LOGIN_APP_TOKEN_EXPIRE_SECOND);
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
        String logTitle = "=/api-customer/customer/security/2022/v1/sendVerification-C端通过账号发送验证码接口=";
        logger.info("{} - 发送验证码, 参数：customerUserInfoCondition={}", logTitle, JsonUtil.toJSONString(customerUserInfoCondition));
		if (null == customerUserInfoCondition) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		ResponseResult<String> result = new ResponseResult<>();
		String content = "";
		if (cache.exists(
				CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + customerUserInfoCondition.getCustomerMobile())) {
            logger.info("{} - 发送验证码未超过一分钟", logTitle);
			throw new BusinessException(BusinessCode.CODE_202212);
		}
		/**
		 * 随机生成6位数验证码
		 */
		String verificationCode = GeneratePwd.generatePwd6Mobile();
		cache.set(CacheName.CUSTOMER_USER_SEND_VERIFICATION_CODE + customerUserInfoCondition.getCustomerMobile(),
				verificationCode);
		cache.expire(CacheName.CUSTOMER_USER_SEND_VERIFICATION_CODE + customerUserInfoCondition.getCustomerMobile(),
				AppConstant.SEND_SMS_EXPIRE_SECOND);
		/**
		 * 60秒以后调用短信服务
		 */
		cache.set(CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + customerUserInfoCondition.getCustomerMobile(),
				verificationCode);
		cache.expire(CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + customerUserInfoCondition.getCustomerMobile(),
				AppConstant.REQUEST_SEND_SMS_EXPIRE_SECOND);
		/**
		 * 发送模板内容
		 */
		content = String.format(SendSMSTemplate.SMSCONTENT, verificationCode);
		SMSCondition sMSCondition = new SMSCondition();
		sMSCondition.setContent(content);
		sMSCondition.setMobile(customerUserInfoCondition.getCustomerMobile());
		messageSendUtils.sendSms(sMSCondition);
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
		if (null == customerChangeMobileCondition) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		ResponseResult<CustomerUserInfoSimpleVO> result = new ResponseResult<>();
		CustomerUserInfo customerUserInfo = new CustomerUserInfo();
		CustomerUserInfoSimpleVO vo = new CustomerUserInfoSimpleVO();
		CustomerUserInfo info = new CustomerUserInfo();
		CustomerUser user = UserContext.getCurrentCustomerUser();
		customerUserInfo.setOpenid(user.getOpenid());
		customerUserInfo = customerLoginService.getCustomerUserInfoByModel(customerUserInfo);
		if (null == customerUserInfo) {
			throw new BusinessException(BusinessCode.CODE_1002);
		}
		if (!customerChangeMobileCondition.getVerificationCode().equals(cache.get(
				CacheName.CUSTOMER_USER_SEND_VERIFICATION_CODE + customerChangeMobileCondition.getCustomerMobile()))) {
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


	/**
	 * 添加数盟id
	 *
	 * @param customerUserInfoCondition
	 * @return
	 */
	@ApiOperation(value = "微信小程序添加数盟id")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
	@RequestMapping(value = "customer/2035/v1/addDigitalUnionId", method = RequestMethod.POST)
	public ResponseResult<Boolean> addDigitalUnionId(@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
		String logTitle = "=/api-customer/customer/2035/v1/addDigitalUnionId-C端小程序添加数盟id接口=";
		logger.info("{} -微信小程序添加数盟id, 参数：customerUserInfoCondition={}", logTitle, JsonUtil.toJSONString(customerUserInfoCondition));
		if (StringUtils.isBlank(customerUserInfoCondition.getDigitalUnionId())) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		CustomerUser currentCustomerUser = UserContext.getCurrentCustomerUser();

		CustomerUserInfo customerUserInfo = new CustomerUserInfo();
		customerUserInfo.setCustomerId(currentCustomerUser.getCustomerId());
		customerUserInfo.setDigitalUnionId(customerUserInfoCondition.getDigitalUnionId());
		int num = customerLoginService.updateCustomerInfo(customerUserInfo);
		boolean flag = num == 1 ? true : false;

		ResponseResult<Boolean> result = new ResponseResult<>();
		result.setData(flag);

		return result;
	}
}
