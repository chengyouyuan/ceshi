package com.winhxd.b2c.customer.api;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
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
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerChangeMobileCondition;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition;
import com.winhxd.b2c.common.domain.system.login.model.CustomerUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoSimpleVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.common.util.GeneratePwd;
import com.winhxd.b2c.common.util.JsonUtil;
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
@RequestMapping(value = "/api/weChatLogin/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiCustomerLoginController {
	private static final Logger logger = LoggerFactory.getLogger(ApiCustomerLoginController.class);

	@Autowired
	private CustomerLoginService customerLoginService;
	@Autowired
	private Cache cache;
	@Autowired
	MessageServiceClient messageServiceClient;

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
			@ApiResponse(code = BusinessCode.CODE_1008, message = "验证码错误"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })

	@RequestMapping(value = "2021/v1/saveWeChatLogin", method = RequestMethod.POST)
	public ResponseResult<CustomerUserInfoSimpleVO> weChatRegister(
			@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
		ResponseResult<CustomerUserInfoSimpleVO> result = new ResponseResult<>();
		ResponseResult<MiniOpenId> object = null;
		MiniOpenId mini = null;
		CustomerUserInfoSimpleVO vo;
		try {
			if (null == customerUserInfoCondition) {
				return new ResponseResult<>(BusinessCode.CODE_1007);
			}
			if (StringUtils.isBlank(customerUserInfoCondition.getCustomerMobile())) {
				return new ResponseResult<>(BusinessCode.CODE_1007);
			}
			/**
			 * 根据手机号去取缓存verificationCode对比是否一致
			 */
			if (!customerUserInfoCondition.getVerificationCode()
					.equals(cache.get(customerUserInfoCondition.getCustomerMobile()))) {
				return new ResponseResult<>(BusinessCode.CODE_1008);
			}
			CustomerUserInfo customerUserInfo = new CustomerUserInfo();
			CustomerUserInfo DB = null;
			/**
			 * 拿code去换session_key
			 */
			object = messageServiceClient.getMiniOpenId(customerUserInfoCondition.getCode());
			if (object.getCode() == 0) {
				mini = object.getData();
				customerUserInfo.setOpenId(mini.getOpenId());
				customerUserInfo.setSessionKey(mini.getSessionKey());
				DB = customerLoginService.getCustomerUserInfoByModel(customerUserInfo);
				if (null == DB) {
					customerUserInfo.setCreated(new Date());
					customerUserInfo.setToken(String.valueOf(UUID.randomUUID()));
					customerLoginService.saveLoginInfo(customerUserInfo);
					vo = new CustomerUserInfoSimpleVO();
					vo.setCustomerId(customerUserInfo.getCustomerId());
					vo.setCustomerMobile(customerUserInfoCondition.getCustomerMobile());
					vo.setToken(customerUserInfo.getToken());
					CustomerUser user = new CustomerUser();
					user.setOpenId(mini.getOpenId());
					BeanUtils.copyProperties(vo, user);
					cache.set(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(),
							JsonUtil.toJSONString(user));
					cache.expire(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(), 30 * 24 * 60 * 60);
					result.setData(vo);

				} else {
					if (!DB.getCustomerMobile().equals(customerUserInfoCondition.getCustomerMobile())) {
						return new ResponseResult<>(BusinessCode.CODE_1004);
					}
					customerUserInfo.setCustomerId(DB.getCustomerId());
					customerLoginService.updateCustomerInfo(customerUserInfo);
					vo = new CustomerUserInfoSimpleVO();
					vo.setCustomerId(DB.getCustomerId());
					vo.setCustomerMobile(DB.getCustomerMobile());
					vo.setToken(DB.getToken());
					if (!cache.exists(CacheName.CUSTOMER_USER_INFO_TOKEN + DB.getToken())) {
						CustomerUser user = new CustomerUser();
						user.setOpenId(DB.getOpenId());
						BeanUtils.copyProperties(vo, user);
						cache.set(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(),
								JsonUtil.toJSONString(user));
						cache.expire(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(),
								30 * 24 * 60 * 60);
					}
					result.setData(vo);
				}
			} else {
				return new ResponseResult<>(BusinessCode.CODE_1001);
			}
		} catch (BusinessException e) {
			logger.error("ApiCustomerLoginController -> weChatRegister异常, 异常信息{}" + e.getMessage(), e.getErrorCode());
			result = new ResponseResult<>(e.getErrorCode());
		} catch (Exception e) {
			logger.error("ApiCustomerLoginController -> weChatRegister异常, 异常信息{}" + e.getMessage(), e);
			result = new ResponseResult<>(BusinessCode.CODE_1001);
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
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@RequestMapping(value = "2022/v1/sendVerification", method = RequestMethod.POST)
	public ResponseResult<String> sendVerification(@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
		ResponseResult<String> result = new ResponseResult<>();
		try {
			if (null == customerUserInfoCondition) {
				return new ResponseResult<>(BusinessCode.CODE_1007);
			}
			if (cache.exists(
					CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + customerUserInfoCondition.getCustomerMobile())) {
				return new ResponseResult<>(BusinessCode.CODE_1012);
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
			cache.expire(CacheName.SEND_VERIFICATION_CODE_REQUEST_TIME + customerUserInfoCondition.getCustomerMobile(),
					60);
			/**
			 * 发送模板内容
			 */
			String content = "【小程序】验证码：" + verificationCode + ",有效时间五分钟";
			messageServiceClient.sendSMS(customerUserInfoCondition.getCustomerMobile(), content);
			return result;
		} catch (BusinessException e) {
			logger.error("ApiCustomerLoginController -> sendVerification异常, 异常信息{}" + e.getMessage(), e.getErrorCode());
			result = new ResponseResult<>(e.getErrorCode());
		} catch (Exception e) {
			logger.error("ApiCustomerLoginController -> sendVerification异常, 异常信息{}" + e.getMessage(), e);
			result = new ResponseResult<>(BusinessCode.CODE_1001);
		}
		return result;
	}

	/**
	 * @author wufuyun
	 * @date  2018年8月8日 下午8:56:52
	 * @Description 用户换绑手机号
	 * @param customerUserInfoCondition
	 * @return
	 */
	@ApiOperation(value = "C端—用户换绑手机号")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@RequestMapping(value = "2023/v1/customerChangeMobile", method = RequestMethod.POST)
	public ResponseResult<String> customerChangeMobile(@RequestBody CustomerChangeMobileCondition customerChangeMobileCondition) {
		ResponseResult<String> result = new ResponseResult<>();
		try {
			if (null == customerChangeMobileCondition) {
				return new ResponseResult<>(BusinessCode.CODE_1007);
			}
			CustomerUserInfo info = new CustomerUserInfo();
			CustomerUser user = UserContext.getCurrentCustomerUser();
			if(null == user.getCustomerId()){
				return new ResponseResult<>(BusinessCode.CODE_1002);
			}
			info.setCustomerMobile(customerChangeMobileCondition.getCustomerMobile());
			info.setCustomerId(user.getCustomerId());
			customerLoginService.updateCustomerInfo(info);
			return result;
		} catch (BusinessException e) {
			logger.error("ApiCustomerLoginController -> customerChangeMobile异常, 异常信息{}" + e.getMessage(), e.getErrorCode());
			result = new ResponseResult<>(e.getErrorCode());
		} catch (Exception e) {
			logger.error("ApiCustomerLoginController -> customerChangeMobile异常, 异常信息{}" + e.getMessage(), e);
			result = new ResponseResult<>(BusinessCode.CODE_1001);
		}
		return result;
	}
}
