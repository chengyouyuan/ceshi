package com.winhxd.b2c.customer.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition1;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO1;

/**
 * @Description: 小程序用户服务接口类
 * @author chengyy
 * @date 2018/8/6 9:23
 */
public interface CustomerService {
    /**
     * @author chengyy
     * @date 2018/8/6 9:25
     * @Description 根据查询条件查询用户分页数据
     * @param condition 查询条件
     * @return 分页数据
     */
    PagedList<CustomerUserInfoVO1> findCustomerPageInfo(CustomerUserInfoCondition1 condition);
}
