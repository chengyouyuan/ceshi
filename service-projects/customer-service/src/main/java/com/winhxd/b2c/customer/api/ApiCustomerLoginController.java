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
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition;
import com.winhxd.b2c.common.domain.system.login.model.CustomerUserInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
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
	 * @date 2018年8月3日 上午10:04:51
	 * @Description 通过code获取openid和session_key
	 * @param CustomerUserInfoCondition
	 * @return 主键id
	 */
	@ApiOperation(value = "通过code获取openid和session_key")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@RequestMapping(value = "2021/v1/saveWeChatLogin", method = RequestMethod.POST)
	public ResponseResult<Long> saveWeChatLogin(@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
		ResponseResult<Long> result = new ResponseResult<>();
		ResponseResult<MiniOpenId> object = null;
		MiniOpenId mini = null;
		try {
			if (null == customerUserInfoCondition) {
				return new ResponseResult<>(BusinessCode.CODE_1007);
			}
			CustomerUserInfo customerUserInfo = new CustomerUserInfo();
			if (null != customerUserInfoCondition.getCustomerId()) {
				/**
				 * 拿code去换session_key
				 */
				object = messageServiceClient.getMiniOpenId(customerUserInfoCondition.getCode());
				if (object.getCode() == 0) {
					mini = object.getData();
					customerUserInfo.setSessionKey(mini.getSessionKey());
					customerUserInfo.setCustomerId(customerUserInfoCondition.getCustomerId());
					customerLoginService.updateCustomerInfo(customerUserInfo);
				}
			} else {
				BeanUtils.copyProperties(customerUserInfoCondition, customerUserInfo);
				object = messageServiceClient.getMiniOpenId(customerUserInfoCondition.getCode());
				if (object.getCode() == 0) {
					mini = object.getData();
					customerUserInfo.setOpenId(mini.getOpenId());
					customerUserInfo.setSessionKey(mini.getSessionKey());
					customerLoginService.saveLoginInfo(customerUserInfo);
					result.setData(customerUserInfo.getCustomerId());
				}
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
			@ApiResponse(code = BusinessCode.CODE_1008, message = "验证码错误"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })

	@RequestMapping(value = "2023/v1/weChatRegister", method = RequestMethod.POST)
	public ResponseResult<Long> weChatRegister(@RequestBody CustomerUserInfoCondition customerUserInfoCondition) {
		ResponseResult<Long> result = new ResponseResult<>();
		try {
			if (null == customerUserInfoCondition) {
				return new ResponseResult<>(BusinessCode.CODE_1007);
			}
			/**
			 * 根据手机号去取缓存verificationCode对比是否一致
			 */
			if (!customerUserInfoCondition.getVerificationCode()
					.equals(cache.get(customerUserInfoCondition.getCustomerMobile()))) {
				return new ResponseResult<>(BusinessCode.CODE_1008);
			}
			CustomerUserInfo customerUserInfo = null;
			;
			customerUserInfo = customerLoginService.getCustomerUserInfoById(customerUserInfoCondition.getCustomerId());
			if (null == customerUserInfo) {
				return new ResponseResult<>(BusinessCode.CODE_1004);
			}
			/**
			 * 数据库手机号为空则，绑定手机号
			 */
			if (StringUtils.isBlank(customerUserInfo.getCustomerMobile())) {
				customerUserInfo.setCustomerId(customerUserInfoCondition.getCustomerId());
				customerUserInfo.setCustomerMobile(customerUserInfoCondition.getCustomerMobile());
				customerLoginService.updateCustomerInfo(customerUserInfo);
			}
			result.setData(customerUserInfo.getCustomerId());
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
			 messageServiceClient.sendSMS(customerUserInfoCondition.getCustomerMobile(),content);
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
	 * @date 2018年8月6日 上午9:53:18
	 * @Description 小程序打礼包列表
	 * @param customerUserInfoCondition
	 * @return
	 */
	/*
	 * @ApiOperation(value = "查询大礼包列表")
	 * 
	 * @ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message =
	 * "成功"),
	 * 
	 * @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	 * 
	 * @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
	 * 
	 * @RequestMapping(value = "/api/weChatRegister/2022/v1/findBigGiftBagList",
	 * method = RequestMethod.POST) public ResponseResult<String>
	 * findBigGiftBagList(@RequestBody CustomerUserInfoCondition
	 * customerUserInfoCondition) { ResponseResult<String> result = new
	 * ResponseResult<>(); try { if(null == customerUserInfoCondition){ return
	 * new ResponseResult<>(BusinessCode.CODE_1007); } // TODO:掉寒宁服务 return
	 * result; } catch (BusinessException e) {
	 * logger.error("ApiCustomerLoginController -> findBigGiftBagList异常, 异常信息{}"
	 * + e.getMessage(), e.getErrorCode()); result = new
	 * ResponseResult<>(e.getErrorCode()); } catch (Exception e) {
	 * logger.error("ApiCustomerLoginController -> findBigGiftBagList异常, 异常信息{}"
	 * + e.getMessage(), e); result = new
	 * ResponseResult<>(BusinessCode.CODE_1001); } return result; }
	 */

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
	@RequestMapping(value = "2024/v1/customerEasy", method = RequestMethod.POST)
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
