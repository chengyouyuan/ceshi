package com.winhxd.b2c.customer.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressVO;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.condition.BackStageCustomerInfoCondition;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.customer.service.CustomerAddressService;
import com.winhxd.b2c.customer.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chengyy
 * @Description: 小程序用户控制器
 * @date 2018/8/6 9:21
 */
@RestController
public class CustomerServiceController implements CustomerServiceClient {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerAddressService customerAddressService;
    private Logger logger = LoggerFactory.getLogger(CustomerServiceController.class);

    @Override
    public ResponseResult<PagedList<CustomerUserInfoVO>> queryCustomerPageInfo(@RequestBody BackStageCustomerInfoCondition condition) {
        ResponseResult<PagedList<CustomerUserInfoVO>> responseResult = new ResponseResult<PagedList<CustomerUserInfoVO>>();

        try {
            PagedList<CustomerUserInfoVO> page = customerService.findCustomerPageInfo(condition);
            responseResult.setData(page);
        } catch (Exception e) {
            logger.error("queryCustomerPageInfo查询用户失败：",e);
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<Boolean> updateStatus(@RequestBody BackStageCustomerInfoCondition condition) {
        ResponseResult<Boolean> responseResult = new ResponseResult<>();
        if (condition.getCustomerId() == null) {
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (condition.getStatus() == null) {
            throw new BusinessException(BusinessCode.CODE_200007);
        }
        int line = customerService.modifyCustomerStatus(condition);
        boolean flag = line == 1 ? true : false;

        responseResult.setData(flag);
        return responseResult;
    }

    @Override
    public ResponseResult<List<CustomerUserInfoVO>> findCustomerUserByIds(@RequestBody List<Long> ids) {
        ResponseResult<List<CustomerUserInfoVO>> responseResult = new ResponseResult<>();
        if (CollectionUtils.isEmpty(ids)) {
            return responseResult;
        }
        List<CustomerUserInfoVO> customers = customerService.findCustomerUserByIds(ids);
        responseResult.setData(customers);
        return responseResult;
    }

    @Override
    public ResponseResult<List<CustomerUserInfoVO>> findCustomerUserByPhones(@RequestBody List<String> phones) {
        ResponseResult<List<CustomerUserInfoVO>> responseResult = new ResponseResult<>();
        List<CustomerUserInfoVO> customers = customerService.findCustomerUserByPhones(phones);
        responseResult.setData(customers);
        return responseResult;
    }

    @Override
    public ResponseResult<CustomerUserInfoVO> findCustomerByToken(@RequestParam("token") String token) {
        ResponseResult<CustomerUserInfoVO> responseResult = new ResponseResult<>();
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(BusinessCode.CODE_1014);
        }
        CustomerUserInfoVO customerUserInfoVO = customerService.findCustomerByToken(token);
        responseResult.setData(customerUserInfoVO);
        return responseResult;
    }

    @Override
    public ResponseResult<PagedList<CustomerUserInfoVO>> findAvabileCustomerPageInfo(@RequestBody BackStageCustomerInfoCondition condition) {
        ResponseResult<PagedList<CustomerUserInfoVO>> responseResult = new ResponseResult<PagedList<CustomerUserInfoVO>>();
        try {
            PagedList<CustomerUserInfoVO> page = customerService.findAvabileCustomerPageInfo(condition);
            responseResult.setData(page);
        } catch (Exception e) {
            logger.error("findAvabileCustomerPageInfo查询用户失败：",e);
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<Boolean> updateDefaultCustomerAddress(@RequestParam("customerAddressId") Long customerAddressId) {
        ResponseResult<Boolean> result = new ResponseResult<>();
        int effc = customerAddressService.updateDefaultCustomerAddress(customerAddressId,UserContext.getCurrentCustomerUser());
        result.setData(effc > 0 ? true:false);
        return result;
    }

    @Override
    public ResponseResult<CustomerAddressVO> findDefaultCustomerAddress() {
        ResponseResult<CustomerAddressVO> result = new ResponseResult<CustomerAddressVO>();
        CustomerAddressVO address = customerAddressService.selectDefaultCustomerAddress(UserContext.getCurrentCustomerUser());
        result.setData(address);
        return result;
    }

    @Override
    public ResponseResult<CustomerAddressVO> getCustomerAddressById(@RequestParam("customerAddressId") Long customerAddressId) {
        ResponseResult<CustomerAddressVO> result = new ResponseResult<CustomerAddressVO>();
        CustomerAddressVO address = customerAddressService.selectCustomerAddressById(customerAddressId);
        result.setData(address);
        return result;
    }


}
