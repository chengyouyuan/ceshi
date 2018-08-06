package com.winhxd.b2c.common.feign.customer;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition1;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO1;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description: 用户服务接口
 * @author chengyy
 * @date 2018/8/4 16:56
 */
@FeignClient(value = ServiceName.CUSTOMER_SERVICE,fallbackFactory = CustomerServiceClientFallBack.class)
public interface CustomerServiceClient {
     /**
      * @author chengyy
      * @date 2018/8/4 17:59
      * @Description 根据条件查询后台用户列表页数据，分页查询
      * @param  condition 查询条件对象
      * @return  分页数据
      */
     @RequestMapping(value="/customer/2005/v1/queryCustomerPageInfo",method = RequestMethod.POST)
     ResponseResult<PagedList<CustomerUserInfoVO1>> queryCustomerPageInfo(@RequestBody CustomerUserInfoCondition1 condition);

     /**
      * @author chengyy
      * @date 2018/8/6 13:32
      * @Description 更新用户的状态
      * @param condition 参数条件对象
      * @return   null
      */
     @RequestMapping(value = "/customer/2006/v1/updateStatus",method = RequestMethod.POST)
     ResponseResult<Void> updateStatus(@RequestBody CustomerUserInfoCondition1 condition);
}

class CustomerServiceClientFallBack implements CustomerServiceClient,FallbackFactory<CustomerServiceClient>{
    Logger logger = LoggerFactory.getLogger(CustomerServiceClientFallBack.class);
    Throwable throwable;
    @Override
    public CustomerServiceClient create(Throwable throwable) {
        this.throwable = throwable;
        return new CustomerServiceClientFallBack();
    }

    @Override
    public ResponseResult<PagedList<CustomerUserInfoVO1>> queryCustomerPageInfo(CustomerUserInfoCondition1 condition) {
        logger.error("CustomerServiceClientFallBack -> queryCustomerPageInfo错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Void> updateStatus(CustomerUserInfoCondition1 condition) {
        logger.error("CustomerServiceClientFallBack -> updateStatus错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
