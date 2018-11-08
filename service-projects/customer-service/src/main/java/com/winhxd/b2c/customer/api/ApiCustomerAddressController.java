package com.winhxd.b2c.customer.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressSelectCondition;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressVO;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.customer.service.CustomerAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private CustomerServiceClient customerServiceClient;


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
			@ApiResponse(code = BusinessCode.CODE_503704, message = "C端用户收货地址不存在"),
			@ApiResponse(code = BusinessCode.CODE_611109, message = "手机号格式不正确")})
	@RequestMapping(value = "customerAddress/2025/v1/customerUpdateAddress", method = RequestMethod.POST)
	public ResponseResult<Boolean> customerUpdateAddress(@RequestBody CustomerAddressCondition customerAddressCondition) {
		logger.info("{} - 更新用户收货地址, 参数：customerUpdateAddress={}", "", customerAddressCondition);

		return customerServiceClient.customerUpdateAddress(customerAddressCondition);
	}


	@ApiOperation(value = "C端—删除用户收货地址")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
	@RequestMapping(value = "customerAddress/2026/v1/customerDeleteAddress", method = RequestMethod.POST)
	public ResponseResult<Boolean> customerDeleteAddress(@RequestBody CustomerAddressSelectCondition customerAddressSelectCondition) {
		logger.info("{} - 删除用户收货地址, 参数：customerAddressSelectCondition={}", "", customerAddressSelectCondition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        int effc = customerAddressService.deleteByPrimaryKey(customerAddressSelectCondition);
        result.setData(effc > 0 ? true:false);
        return result;
	}


    @ApiOperation(value = "C端—查询当前用户所有收货地址")
    @ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
    @RequestMapping(value = "customerAddress/2027/v1/selectAllCustomerAddress", method = RequestMethod.POST)
    public ResponseResult<List<CustomerAddressVO>> selectAllCustomerAddress(@RequestBody CustomerAddressCondition customerAddressCondition) {
        logger.info("{} - 删除用户收货地址, 参数：CustomerAddressCondition={}", "", customerAddressCondition);
        ResponseResult<List<CustomerAddressVO>> result = new ResponseResult<>();
        List<CustomerAddressVO> customerAddressList= customerAddressService.getCustomerAddressByUserId(UserContext.getCurrentCustomerUser().getCustomerId());
        result.setData(customerAddressList);
        return result;
    }

    @ApiOperation(value = "C端—查询用户收货地址ById")
    @ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
    @RequestMapping(value = "customerAddress/2028/v1/selectOneCustomerAddress", method = RequestMethod.POST)
    public ResponseResult<CustomerAddressVO> selectOneCustomerAddress(@RequestBody CustomerAddressSelectCondition customerAddressSelectCondition) {
        logger.info("{} - 通过主键查询用户收货地址, 参数：customerAddressSelectCondition={}", "", customerAddressSelectCondition);
        ResponseResult<CustomerAddressVO> result = new ResponseResult<>();
        CustomerAddressVO customerAddress= customerAddressService.selectByPrimaryKey(customerAddressSelectCondition);
        result.setData(customerAddress);
        return result;
    }


}
