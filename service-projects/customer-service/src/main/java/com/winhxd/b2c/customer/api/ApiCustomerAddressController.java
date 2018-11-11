package com.winhxd.b2c.customer.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressLabelCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressSelectCondition;
import com.winhxd.b2c.common.domain.customer.enums.CustomerAddressEnum;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressLabelVO;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.common.util.SecurityCheckUtil;
import com.winhxd.b2c.customer.service.CustomerAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sunwenwu
 * @date 2018年11月8日14:21:37
 * @Description C端用户地址管理 Controller
 * @version
 */
@Api(value = "CustomerAddress Controller", tags = "C-Address")
@RestController
@RequestMapping(value = "/api-customer/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
	@RequestMapping(value = "address/2024/v1/saveCustomerAddress", method = RequestMethod.POST)
	public ResponseResult<Boolean> saveCustomerAddress(@RequestBody CustomerAddressCondition customerAddressCondition) {
		logger.info("{} - 保存用户收货地址, 参数：customerAddressCondition={}", "", customerAddressCondition);
		ResponseResult<Boolean> result = new ResponseResult<>();
		//参数校验
		addOrUpdateVerifyParam(customerAddressCondition,CustomerAddressEnum.INSERT);
		int effc = customerAddressService.saveCustomerAddress(customerAddressCondition, UserContext.getCurrentCustomerUser());
		result.setData(effc > 0 ? true:false);
		return result;
	}


	@ApiOperation(value = "C端—更新用户收货地址")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
			@ApiResponse(code = BusinessCode.CODE_503704, message = "C端用户收货地址不存在"),
			@ApiResponse(code = BusinessCode.CODE_611109, message = "手机号格式不正确")})
	@RequestMapping(value = "address/2025/v1/updateCustomerAddress", method = RequestMethod.POST)
	public ResponseResult<Boolean> updateCustomerAddress(@RequestBody CustomerAddressCondition customerAddressCondition) {
		logger.info("{} - 更新用户收货地址, 参数：customerUpdateAddress={}", "", customerAddressCondition);
        ResponseResult<Boolean> result = new ResponseResult<>();
		//参数校验
		addOrUpdateVerifyParam(customerAddressCondition,CustomerAddressEnum.UPDATE);
        int effc = customerAddressService.updateCustomerAddress(customerAddressCondition, UserContext.getCurrentCustomerUser());
        result.setData(effc > 0 ? true:false);

        return result;
	}


	@ApiOperation(value = "C端—删除用户收货地址")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
	@RequestMapping(value = "address/2026/v1/deleteCustomerAddress", method = RequestMethod.POST)
	public ResponseResult<Boolean> deleteCustomerAddress(@RequestBody CustomerAddressSelectCondition customerAddressSelectCondition) {
		logger.info("删除用户收货地址, 参数：customerAddressSelectCondition={}", "", customerAddressSelectCondition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        int effc = customerAddressService.deleteCustomerAddress(customerAddressSelectCondition);
        result.setData(effc > 0 ? true:false);
        return result;
	}


    @ApiOperation(value = "C端—查询当前用户所有收货地址")
    @ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
    @RequestMapping(value = "address/2027/v1/selectCustomerAddressByUserId", method = RequestMethod.POST)
    public ResponseResult<List<CustomerAddressVO>> selectCustomerAddressByUserId(@RequestBody CustomerAddressCondition customerAddressCondition) {
        logger.info("C端—查询当前用户所有收货地址, 参数：CustomerAddressCondition={}", "", customerAddressCondition);
        ResponseResult<List<CustomerAddressVO>> result = new ResponseResult<>();
        List<CustomerAddressVO> customerAddressList= customerAddressService.selectCustomerAddressByUserId(UserContext.getCurrentCustomerUser().getCustomerId());
        result.setData(customerAddressList);
        return result;
    }

    @ApiOperation(value = "C端—查询用户收货地址ById")
    @ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
    @RequestMapping(value = "address/2028/v1/selectCustomerAddressById", method = RequestMethod.POST)
    public ResponseResult<CustomerAddressVO> selectCustomerAddressById(@RequestBody CustomerAddressSelectCondition ondition) {
        logger.info("通过主键查询用户收货地址, 参数：customerAddressSelectCondition={}", "", ondition);
        ResponseResult<CustomerAddressVO> result = new ResponseResult<>();
        CustomerAddressVO customerAddress= customerAddressService.selectCustomerAddressById(ondition.getId());
        result.setData(customerAddress);
        return result;
    }

    @ApiOperation(value = "C端—查询当前用户默认收货地址")
    @ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
    @RequestMapping(value = "address/2032/v1/selectDefaultCustomerAddress", method = RequestMethod.POST)
    public ResponseResult<CustomerAddressVO> selectDefaultCustomerAddress(@RequestBody ApiCondition condition) {
        logger.info("查询当前用户默认收货地址, 参数：ApiCondition={}", "", condition);
        ResponseResult<CustomerAddressVO> result = new ResponseResult<>();
        CustomerAddressVO address = customerAddressService.selectDefaultCustomerAddress(UserContext.getCurrentCustomerUser());
		result.setData(address);
		return result;
    }

	/**
	 * @param
	 * @return
	 * @author chenyanqi
	 * @Description C端用户地址标签的查询
	 */
	@ApiOperation(value = "C端用户地址标签的查询")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
			@ApiResponse(code = BusinessCode.CODE_202308, message = "验证码错误"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
	@RequestMapping(value = "customer/2029/v1/selectAddressLabelByUserId", method = RequestMethod.POST)
	public ResponseResult<List<CustomerAddressLabelVO>> selectAddressLabelByUserId(ApiCondition condition) {
        logger.info("customer/2029/v1/getAddressLabelByUserId -- 查询C端用户地址标签开始");
		CustomerUser currentCustomerUser = UserContext.getCurrentCustomerUser();
		long customerId = currentCustomerUser.getCustomerId();
		List<CustomerAddressLabelVO> list = customerAddressService.selectAddressLabelByUserId(customerId);
		ResponseResult<List<CustomerAddressLabelVO>> result = new ResponseResult<>();
		result.setData(list);
		return result;
	}

	/**
	 * @param
	 * @return
	 * @author chenyanqi
	 * @Description 新增用户地址标签
	 */
	@ApiOperation(value = "新增用户地址标签")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
			@ApiResponse(code = BusinessCode.CODE_202308, message = "验证码错误"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
	@RequestMapping(value = "customer/2030/v1/addCustomerAddressLabel", method = RequestMethod.POST)
	public ResponseResult<Boolean> addCustomerAddressLabel(@RequestBody CustomerAddressLabelCondition customerAddressLabelCondition) {
        logger.info("customer/2030/v1/addCustomerAddressLabel -- 新增用户地址标签开始，入参：customerAddressLabelCondition--{}", JsonUtil.toJSONString(customerAddressLabelCondition));
		String labelName = customerAddressLabelCondition.getLabelName();
		if (customerAddressLabelCondition == null) {
            logger.error("接口2030新增用户地址标签参数为空");
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		if (StringUtils.isEmpty(labelName)) {
            logger.error("接口2030新增用户地址标签，标签名字为空");
			throw new BusinessException(BusinessCode.CODE_503705);
		}
		CustomerUser currentCustomerUser = UserContext.getCurrentCustomerUser();
		Long customerId = currentCustomerUser.getCustomerId();
		customerAddressLabelCondition.setCustomerId(customerId);
		//校验是否有和以前重复保存的标签
		List<CustomerAddressLabelVO> labelNames = customerAddressService.selectAddressLabelByUserId(customerId);
		labelNames.forEach(
				label -> {
					if (labelName.equals(label.getLabelName())) {
                        logger.info("2030接口保存标签时，有和以前重复的标签 ，标签名--{}", label);
						throw new BusinessException(BusinessCode.CODE_503706);
					}
				});
		int i = customerAddressService.saveCustomerAddressLabel(customerAddressLabelCondition);
		ResponseResult<Boolean> result = new ResponseResult<>();
		result.setData(i > 0 ? true : false);
		return result;
	}

	/**
	 * @param
	 * @return
	 * @author chenyanqi
	 * @Description 删除用户地址标签
	 */
	@ApiOperation(value = "删除用户地址标签")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
			@ApiResponse(code = BusinessCode.CODE_202308, message = "验证码错误"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")})
	@RequestMapping(value = "customer/2031/v1/deleteCustomerAddressLabel", method = RequestMethod.POST)
	public ResponseResult<Boolean> deleteCustomerAddressLabel(@RequestBody CustomerAddressLabelCondition customerAddressLabelCondition) {
        logger.info("customer/2031/v1/deleteCustomerAddressLabel -- 删除用户地址标签开始，入参：customerAddressLabelCondition--{}", JsonUtil.toJSONString(customerAddressLabelCondition));
		if (customerAddressLabelCondition == null) {
            logger.error("接口2031删除用户地址标签参数为空");
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		if (StringUtils.isEmpty(customerAddressLabelCondition.getLabelName())) {
			logger.error("接口2031删除用户的地址标签，标签名字为空");
			throw new BusinessException(BusinessCode.CODE_503705);
		}
		CustomerUser currentCustomerUser = UserContext.getCurrentCustomerUser();
		customerAddressLabelCondition.setCustomerId(currentCustomerUser.getCustomerId());
		int i = customerAddressService.deleteCustomerAddressLabel(customerAddressLabelCondition);
		ResponseResult<Boolean> result = new ResponseResult<>();
		result.setData(i > 0 ? true : false);
		return result;
	}

	//参数校验
	public void addOrUpdateVerifyParam (CustomerAddressCondition condition,CustomerAddressEnum opt) {
		//判断必填参数
		if (null == condition) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		if (CustomerAddressEnum.INSERT.equals(opt)) {
			if (null == condition.getContacterDetailAddress() ||
					null == condition.getContacterMobile() ||
					null == condition.getContacterName() ||
                    null == condition.getContacterProvince() ||
                    null == condition.getContacterCity() ||
                    null == condition.getContacterCounty() ||
                    null == condition.getContacterProvinceCode() ||
                    null == condition.getContacterCityCode() ||
                    null == condition.getContacterCountyCode()) {
				throw new BusinessException(BusinessCode.CODE_1007);
			}
			if (!SecurityCheckUtil.validateMobile(condition.getContacterMobile())) {
				throw new BusinessException(BusinessCode.CODE_611109);
			}
		} else if (CustomerAddressEnum.UPDATE.equals(opt)) {
			if (null == condition.getId()) {
				throw new BusinessException(BusinessCode.CODE_1007);
			}
			if (null != condition.getContacterMobile() && !SecurityCheckUtil.validateMobile(condition.getContacterMobile())) {
				throw new BusinessException(BusinessCode.CODE_611109);
			}
		}
	}

}
