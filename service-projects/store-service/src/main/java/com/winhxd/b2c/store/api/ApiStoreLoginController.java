package com.winhxd.b2c.store.api;

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
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserInfoCondition;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.store.service.StoreLoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author wufuyun
 * @date  2018年8月3日 下午3:06:09
 * @Description B端用户登录
 * @version
 */
@Api(value = "StoreLoginController Controller", tags = "B-Login")
@RestController
public class ApiStoreLoginController {
	private static final Logger logger = LoggerFactory.getLogger(ApiStoreLoginController.class);

	@Autowired
	private StoreLoginService storeLoginService;
	@Autowired
	private Cache cache;
	
	/**
	 * @author wufuyun
	 * @date  2018年8月4日 上午11:11:59
	 * @Description B端登录
	 * @param storeUserInfoCondition
	 * @return
	 */
	@ApiOperation(value = "B端登录验证")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1008, message = "验证码错误"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1005, message = "密码错误")})
	@RequestMapping(value = "/api/storeLogin/3021/v1/saveWeChatLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<Long> saveStoreLogin(@RequestBody StoreUserInfoCondition storeUserInfoCondition) {
		ResponseResult<Long> result = new ResponseResult<>();
		try {
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
	 * @date  2018年8月4日 上午11:10:34
	 * @Description 
	 * @param customerUserInfoCondition
	 * @return
	 */
	@ApiOperation(value = "通过账号发送验证码")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常") })
	@RequestMapping(value = "/api/storeLogin/2022/v1/sendVerification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<String> sendVerification(@RequestBody StoreUserInfoCondition storeUserInfoCondition) {
		ResponseResult<String> result = new ResponseResult<>();
		try {
			// TODO:调用massage 发送短信smsCode
			return result;
		} catch (BusinessException e) {
			logger.error("ApiStoreLoginController -> sendVerification异常, 异常信息{}" + e.getMessage(), e.getErrorCode());
			result = new ResponseResult<>(e.getErrorCode());
		} catch (Exception e) {
			logger.error("ApiStoreLoginController -> sendVerification异常, 异常信息{}" + e.getMessage(), e);
			result = new ResponseResult<>(BusinessCode.CODE_1001);
		}
		return result;
	}
}
