package com.winhxd.b2c.customer.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition1;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO1;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    private Logger logger = LoggerFactory.getLogger(CustomerServiceController.class);

    @Override
    public ResponseResult<PagedList<CustomerUserInfoVO1>> queryCustomerPageInfo(@RequestBody  CustomerUserInfoCondition1 condition) {
        ResponseResult<PagedList<CustomerUserInfoVO1>> responseResult = new ResponseResult<PagedList<CustomerUserInfoVO1>>();
        try {
            PagedList<CustomerUserInfoVO1> page = customerService.findCustomerPageInfo(condition);
            responseResult.setData(page);
        } catch (Exception e) {
            logger.error("CustomerServiceController ->queryCustomerPageInfo报错，错误信息为{}", e);
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<Void> updateStatus(@RequestBody  CustomerUserInfoCondition1 condition) {
        ResponseResult<Void> responseResult = new ResponseResult<>();
        if (condition.getCustomerId() == null) {
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (condition.getStatus() == null) {
            throw new BusinessException(BusinessCode.CODE_200007);
        }
        int line = customerService.modifyCustomerStatus(condition);
        responseResult.setCode(line == 1 ? BusinessCode.CODE_OK : BusinessCode.CODE_200008);
        return responseResult;
    }

    @Override
    public ResponseResult<List<CustomerUserInfoVO1>> findCustomerUserByIds(@RequestBody  List<Long> ids) {
        ResponseResult<List<CustomerUserInfoVO1>> responseResult = new ResponseResult<>();
        List<CustomerUserInfoVO1> customers = customerService.findCustomerUserByIds(ids);
        responseResult.setData(customers);
        return responseResult;
    }
}