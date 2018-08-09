package com.winhxd.b2c.customer.dao;

import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition1;
import com.winhxd.b2c.common.domain.system.login.model.CustomerUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerUserInfoMapper {
    int deleteByPrimaryKey(Long customerId);

    int insert(CustomerUserInfo record);

    int insertSelective(CustomerUserInfo record);

    CustomerUserInfo selectByPrimaryKey(Long customerId);

    int updateByPrimaryKeySelective(CustomerUserInfo record);

    int updateByPrimaryKey(CustomerUserInfo record);
    
    CustomerUserInfo selectCustomerModel(CustomerUserInfo customerUserInfo);

    /**
     * @param condition 查询条件
     * @return 分页数据
     * @author chengyy
     * @date 2018/8/6 9:33
     * @Description 根据条件查询用户分页数据
     */
    List<CustomerUserInfoVO> selectCustomer(CustomerUserInfoCondition1 condition);

    /**
     * @param ids 用户ids
     * @return 用户信息
     * @author chengyy
     * @date 2018/8/6 15:39
     * @Description 根据用户id批量查询用户信息
     */
    List<CustomerUserInfoVO> selectCustomerUserByIds(@Param("ids") List<Long> ids);
    
    /**
     * @author wufuyun
     * @date  2018年8月8日 下午3:59:11
     * @Description 查询C端用户信息
     * @param customerUserInfo
     * @return
     */
	CustomerUserInfo selectByCustomerUserInfoByModel(CustomerUserInfo customerUserInfo);
}