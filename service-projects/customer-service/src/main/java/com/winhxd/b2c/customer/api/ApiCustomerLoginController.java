package com.winhxd.b2c.customer.api;

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
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition;
import com.winhxd.b2c.common.domain.system.login.model.CustomerUserInfo;
import com.winhxd.b2c.common.exception.BusinessException;
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
public class ApiCustomerLoginController {
	private static final Logger logger = LoggerFactory.getLogger(ApiCustomerLoginController.class);

	@Autowired
	private CustomerLoginService customerLoginService;
	@Autowired
	private Cache cache;

	/**
	 * @author wufuyun
	 * @date 2018年8月3日 上午10:04:51
	 * @Description 通过code获取openid和session_key
	 * @param CustomerUserInfoCondition
	 * @return 主键id
	 */
	@ApiOperation(value = "通过code获取openid和session_key")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常") })
	@RequestMapping(value = "/api/weChatLogin/2021/v1/saveWeChatLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<Long> saveWeChatLogin(@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
		ResponseResult<Long> result = new ResponseResult<>();
		try {
			CustomerUserInfo customerUserInfo = new CustomerUserInfo();
			if (null != customerUserInfoCondition.getCustomerId()) {
				// TODO:调用金彪服务拿code去换session_key更新到数据库
				customerUserInfo.setSessionKey("");
				customerLoginService.updateCustomerInfo(customerUserInfo);
			} else {
				BeanUtils.copyProperties(customerUserInfoCondition, customerUserInfo);
				// TODO: 通过code调用微信接口返回openid和session_key保存到数据库
				customerUserInfo.setOpenId("");
				customerUserInfo.setSessionKey("");
				customerLoginService.saveLoginInfo(customerUserInfo);
				result.setData(customerUserInfo.getCustomerId());
			}
			return result;
		} catch (BusinessException e) {
			logger.error("ApiCustomerLoginController -> saveWeChatLogin异常, 异常信息{}" + e.getMessage(), e.getErrorCode());
			result = new ResponseResult<>(e.getErrorCode());
		} catch (Exception e) {
			logger.error("ApiCustomerLoginController -> saveWeChatLogin异常, 异常信息{}" + e.getMessage(), e);
			result = new ResponseResult<>(BusinessCode.CODE_1001);
		}
		return result;
	}

	/**
	 * @author wufuyun
	 * @date 2018年8月3日 下午1:31:45
	 * @Description 通过账号验证码登录
	 * @param customerUserInfoCondition
	 * @return
	 */
	@ApiOperation(value = "通过账号验证码登录")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1008, message = "验证码错误") })

	@RequestMapping(value = "/api/weChatRegister/2023/v1/weChatRegister", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<Long> weChatRegister(@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
		ResponseResult<Long> result = new ResponseResult<>();
		try {
			/**
			 * 根据手机号去code对比是否一致
			 */
			if (!customerUserInfoCondition.getVerificationCode()
					.equals(cache.get(customerUserInfoCondition.getCustomerMobile()))) {
				return new ResponseResult<>(BusinessCode.CODE_1008);
			}
			CustomerUserInfo customerUserInfo = new CustomerUserInfo();
			customerUserInfo = customerLoginService.getCustomerUserInfoById(customerUserInfoCondition.getCustomerId());
			/**
			 * 数据库手机号为空则，绑定手机号
			 */
			if (StringUtils.isBlank(customerUserInfo.getCustomerMobile())) {
				customerUserInfo.setCustomerId(customerUserInfoCondition.getCustomerId());
				customerUserInfo.setCustomerMobile(customerUserInfoCondition.getCustomerMobile());
				customerLoginService.updateCustomerInfo(customerUserInfo);
			}
			// result.setData(customerUserInfo.getCustomerId());
			return result;
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
	@ApiOperation(value = "通过账号发送验证码")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常") })
	@RequestMapping(value = "/api/weChatRegister/2022/v1/sendVerification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<String> sendVerification(@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
		ResponseResult<String> result = new ResponseResult<>();
		try {
			// TODO:调用massage 发送短信smsCode
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
}
