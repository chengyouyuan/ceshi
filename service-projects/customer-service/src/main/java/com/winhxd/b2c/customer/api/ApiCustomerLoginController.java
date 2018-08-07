package com.winhxd.b2c.customer.api;

import java.util.Date;
import java.util.UUID;

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
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition;
import com.winhxd.b2c.common.domain.system.login.model.CustomerUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
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
	public ResponseResult<CustomerUserInfoVO> weChatRegister(
			@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
		ResponseResult<CustomerUserInfoVO> result = new ResponseResult<>();
		ResponseResult<MiniOpenId> object = null;
		MiniOpenId mini = null;
		CustomerUserInfoVO vo;
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
					vo = new CustomerUserInfoVO();
					vo.setCustomerId(customerUserInfo.getCustomerId());
					vo.setCustomerMobile(customerUserInfoCondition.getCustomerMobile());
					vo.setToken(customerUserInfo.getToken());
					cache.set(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(),
							JsonUtil.toJSONString(vo));
					cache.expire(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken(), 30 * 24 * 60 * 60);
					result.setData(vo);

				} else {
					if (!DB.getCustomerMobile().equals(customerUserInfoCondition.getCustomerMobile())) {
						return new ResponseResult<>(BusinessCode.CODE_1004);
					}
					customerUserInfo.setCustomerId(DB.getCustomerId());
					customerLoginService.updateCustomerInfo(customerUserInfo);
					vo = new CustomerUserInfoVO();
					vo.setCustomerId(DB.getCustomerId());
					vo.setCustomerMobile(DB.getCustomerMobile());
					vo.setToken(DB.getToken());
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
			/**
			 * 发送模板内容
			 */
			String content = "";
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
	 * @date 2018年8月6日 上午9:56:37
	 * @Description 用户领取礼包
	 * @param customerUserInfoCondition
	 * @return
	 */
	@ApiOperation(value = "领取礼包,返回列表")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@RequestMapping(value = "2023/v1/customerEasy", method = RequestMethod.POST)
	public ResponseResult<String> customerEasy(@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
		ResponseResult<String> result = new ResponseResult<>();
		try {
			if (null == customerUserInfoCondition) {
				return new ResponseResult<>(BusinessCode.CODE_1007);
			}
			// TODO:掉寒宁服务
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
