package com.winhxd.b2c.customer.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.condition.BackStageCustomerInfoCondition;

import java.util.List;

/**
 * @author chengyy
 * @Description: 小程序用户服务接口类
 * @date 2018/8/6 9:23
 */
public interface CustomerService {
    /**
     * @param condition 查询条件
     * @return 分页数据
     * @author chengyy
     * @date 2018/8/6 9:25
     * @Description 根据查询条件查询用户分页数据
     */
    PagedList<CustomerUserInfoVO> findCustomerPageInfo(BackStageCustomerInfoCondition condition);

    /**
     * @param condition 参数对象
     * @return sql语句执行之后影响的行数
     * @author chengyy
     * @date 2018/8/6 13:54
     * @Description 跟新用户的状态，有效，无效
     */
    int modifyCustomerStatus(BackStageCustomerInfoCondition condition);

    /**
     * @param ids 用户id
     * @return 用户信息
     * @author chengyy
     * @date 2018/8/6 15:31
     * @Description 根据id批量查询用户信息
     */
    List<CustomerUserInfoVO> findCustomerUserByIds(List<Long> ids);

    /**
     * @param token
     * @return 用户信息
     * @author chengyy
     * @date 2018/8/10 14:47
     * @Description 根据token查询用户信息
     */
    CustomerUserInfoVO findCustomerByToken(String token);

    /**
     * @param condition 查询条件
     * @return 分页数据
     * @author chengyy
     * @date 2018/8/6 9:25
     * @Description 根据查询条件查询有绑定关系用户分页数据
     */
    PagedList<CustomerUserInfoVO> findAvabileCustomerPageInfo(BackStageCustomerInfoCondition condition);

    /**
     * @param phones 用户id
     * @return 用户信息
     * @author sunwenwu
     * @date 2018/9/27
     * @Description 根据手机号批量查询用户信息
     */
    List<CustomerUserInfoVO> findCustomerUserByPhones(List<String> phones);
}
