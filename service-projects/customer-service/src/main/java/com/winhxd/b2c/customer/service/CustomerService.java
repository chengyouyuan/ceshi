package com.winhxd.b2c.customer.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition1;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO;

import java.util.List;

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
    PagedList<CustomerUserInfoVO> findCustomerPageInfo(CustomerUserInfoCondition1 condition);

    /**
     * @author chengyy
     * @date 2018/8/6 13:54
     * @Description 跟新用户的状态，有效，无效
     * @param condition  参数对象
     * @return  sql语句执行之后影响的行数
     */
    int modifyCustomerStatus(CustomerUserInfoCondition1 condition);

    /**
     * @author chengyy
     * @date 2018/8/6 15:31
     * @Description 根据id批量查询用户信息
     * @param ids 用户id
     * @return  用户信息
     */
    List<CustomerUserInfoVO> findCustomerUserByIds(List<Long> ids);
}
