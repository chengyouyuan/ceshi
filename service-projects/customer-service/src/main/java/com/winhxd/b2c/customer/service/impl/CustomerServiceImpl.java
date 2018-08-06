package com.winhxd.b2c.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition1;
import com.winhxd.b2c.common.domain.system.login.model.CustomerUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO1;
import com.winhxd.b2c.customer.dao.CustomerUserInfoMapper;
import com.winhxd.b2c.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 小程序用户服务实现类
 * @author chengyy
 * @date 2018/8/6 9:23
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerUserInfoMapper customerUserInfoMapper;
    @Override
    public PagedList<CustomerUserInfoVO1> findCustomerPageInfo(CustomerUserInfoCondition1 condition) {
        PagedList<CustomerUserInfoVO1> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CustomerUserInfoVO1> customers = customerUserInfoMapper.selectCustomer(condition);
        PageInfo<CustomerUserInfoVO1> pageInfo = new PageInfo<>(customers);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }

    @Override
    public int modifyCustomerStatus(CustomerUserInfoCondition1 condition) {
        CustomerUserInfo record = new CustomerUserInfo();
        record.setCustomerId(condition.getCustomerId());
        record.setStatus(condition.getStatus());
        return customerUserInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<CustomerUserInfoVO1> findCustomerUserByIds(List<Long> ids) {
        if(ids == null || ids.size() == 0){
            return null;
        }
        return customerUserInfoMapper.selectCustomerUserByIds(ids);
    }
}
