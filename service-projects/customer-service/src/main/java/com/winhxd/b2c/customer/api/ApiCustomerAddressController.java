package com.winhxd.b2c.customer.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressCondition;
import com.winhxd.b2c.customer.service.CustomerAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sunwenwu
 * @date 2018年11月8日14:21:37
 * @Description C端用户地址管理 Controller
 * @version
 */
@Api(value = "CustomerAddress Controller", tags = "C-Address")
@RestController
@RequestMapping(value = "/api-customerAddress/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiCustomerAddressController {
	private static final Logger logger = LoggerFactory.getLogger(ApiCustomerAddressController.class);

	@Autowired
	private CustomerAddressService customerAddressService;


	/**
	 * @author sunwenwu
	 * @date 2018年11月8日14:34:22
	 * @Description 保存用户收货地址
	 * @param customerAddressCondition
	 * @return
	 */
	@ApiOperation(value = "C端—保存用户收货地址")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
			@ApiResponse(code = BusinessCode.CODE_611109, message = "手机号格式不正确")})
	@RequestMapping(value = "customerAddress/2024/v1/customerSaveAddress", method = RequestMethod.POST)
	public ResponseResult<Boolean> customerSaveAddress(@RequestBody CustomerAddressCondition customerAddressCondition) {
		logger.info("{} - 保存用户收货地址, 参数：customerAddressCondition={}", "", customerAddressCondition);

		ResponseResult<Boolean> result = new ResponseResult<>();

		int effc = customerAddressService.insert(customerAddressCondition, UserContext.getCurrentCustomerUser());

		result.setData(effc > 0 ? true:false);
		return result;
	}


	@ApiOperation(value = "C端—更新用户收货地址")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
			@ApiResponse(code = BusinessCode.CODE_611109, message = "手机号格式不正确")})
	@RequestMapping(value = "customerAddress/2025/v1/customerUpdateAddress", method = RequestMethod.POST)
	public ResponseResult<Boolean> customerUpdateAddress(@RequestBody CustomerAddressCondition customerAddressCondition) {
		logger.info("{} - 更新用户收货地址, 参数：customerAddressCondition={}", "", customerAddressCondition);

		ResponseResult<Boolean> result = new ResponseResult<>();

		int effc = customerAddressService.updateByPrimaryKey(customerAddressCondition, UserContext.getCurrentCustomerUser());

		result.setData(effc > 0 ? true:false);
		return result;
	}
}
