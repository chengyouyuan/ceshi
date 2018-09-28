package com.winhxd.b2c.common.feign.customer;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.condition.BackStageCustomerInfoCondition;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author chengyy
 * @Description: 用户服务接口
 * @date 2018/8/4 16:56
 */
@FeignClient(value = ServiceName.CUSTOMER_SERVICE, fallbackFactory = CustomerServiceClientFallBack.class)
public interface CustomerServiceClient {
    /**
     * @param condition 查询条件对象
     * @return 分页数据
     * @author chengyy
     * @date 2018/8/4 17:59
     * @Description 根据条件查询后台用户列表页数据，分页查询
     */
    @RequestMapping(value = "/customer/2005/v1/queryCustomerPageInfo", method = RequestMethod.POST)
    ResponseResult<PagedList<CustomerUserInfoVO>> queryCustomerPageInfo(@RequestBody BackStageCustomerInfoCondition condition);

    /**
     * @param condition 参数条件对象
     * @return null
     * @author chengyy
     * @date 2018/8/6 13:32
     * @Description 更新用户的状态
     */
    @RequestMapping(value = "/customer/2006/v1/updateStatus", method = RequestMethod.POST)
    ResponseResult<Void> updateStatus(@RequestBody BackStageCustomerInfoCondition condition);

    /**
     * @param ids 用户id
     * @return 用户信息
     * @author chengyy
     * @date 2018/8/6 15:27
     * @Description 批量查询用户信息
     */
    @RequestMapping(value = "/customer/2007/v1/findCustomerUserByIds", method = RequestMethod.POST)
    ResponseResult<List<CustomerUserInfoVO>> findCustomerUserByIds(@RequestBody List<Long> ids);

    /**
     * @param phones 用户手机号
     * @return 用户信息
     * @author sunwenwu
     * @date 2018/9/27
     * @Description 批量查询用户信息
     */
    @RequestMapping(value = "/customer/2010/v1/findCustomerUserByPhones", method = RequestMethod.POST)
    ResponseResult<List<CustomerUserInfoVO>> findCustomerUserByPhones(@RequestBody List<String> phones);

    /**
     * @param token
     * @return 用户信息
     * @author chengyy
     * @date 2018/8/10 14:55
     * @Description 根据用户token查询用户信息
     */
    @RequestMapping(value = "/customer/2008/v1/findCustomerByToken", method = RequestMethod.GET)
    ResponseResult<CustomerUserInfoVO> findCustomerByToken(@RequestParam("token") String token);

    /**
     * @param condition 查询条件
     * @return 分页数据
     * @author chengyy
     * @date 2018/8/21 14:38
     * @Description 查询有效的用户(有与门店绑定关系的用户)
     */
    @RequestMapping(value = "/customer/2009/v1/findAvabileCustomerPageInfo", method = RequestMethod.POST)
    ResponseResult<PagedList<CustomerUserInfoVO>> findAvabileCustomerPageInfo(@RequestBody BackStageCustomerInfoCondition condition);
}

@Component
class CustomerServiceClientFallBack implements CustomerServiceClient, FallbackFactory<CustomerServiceClient> {
    Logger logger = LoggerFactory.getLogger(CustomerServiceClientFallBack.class);
    Throwable throwable;

    @Override
    public CustomerServiceClient create(Throwable throwable) {
        this.throwable = throwable;
        return new CustomerServiceClientFallBack();
    }

    @Override
    public ResponseResult<PagedList<CustomerUserInfoVO>> queryCustomerPageInfo(BackStageCustomerInfoCondition condition) {
        logger.error("CustomerServiceClientFallBack -> queryCustomerPageInfo错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Void> updateStatus(BackStageCustomerInfoCondition condition) {
        logger.error("CustomerServiceClientFallBack -> updateStatus错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<List<CustomerUserInfoVO>> findCustomerUserByIds(List<Long> ids) {
        logger.error("CustomerServiceClientFallBack -> findCustomerUserByIds错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<List<CustomerUserInfoVO>> findCustomerUserByPhones(List<String> phones) {
        logger.error("CustomerServiceClientFallBack -> findCustomerUserByPhones错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<CustomerUserInfoVO> findCustomerByToken(String token) {
        logger.error("CustomerServiceClientFallBack -> findCustomerByToken错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<CustomerUserInfoVO>> findAvabileCustomerPageInfo(BackStageCustomerInfoCondition condition) {
        logger.error("CustomerServiceClientFallBack -> findAvabileCustomerPageInfo错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
