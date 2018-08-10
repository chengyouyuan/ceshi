package com.winhxd.b2c.store.service.impl;

import com.winhxd.b2c.common.domain.store.model.CustomerBrowseLog;
import com.winhxd.b2c.store.dao.CustomerBrowseLogMapper;
import com.winhxd.b2c.store.service.StoreBrowseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author liutong
 * @date 2018-08-07 18:42:07
 */
@Service("storeBrowseLogService")
public class StoreBrowseLogServiceImpl implements StoreBrowseLogService {

    @Autowired
    private CustomerBrowseLogMapper customerBrowseLogMapper;

    @Override
    public void saveBrowseLogLogin(CustomerBrowseLog customerBrowseLog) {
        customerBrowseLogMapper.insertSelective(customerBrowseLog);
    }

    @Override
    public CustomerBrowseLog getIdForLoginOut(Long storeId, Long customerId) {
        return customerBrowseLogMapper.selectIdForLoginOut(storeId, customerId);
    }

    @Override
    public void modifyByPrimaryKey(CustomerBrowseLog customerBrowseLog) {
        customerBrowseLogMapper.updateByPrimaryKey(customerBrowseLog);
    }

    @Override
    public int getBrowseNum(Long storeCustomerId, Date beginDate, Date endDate) {
        return customerBrowseLogMapper.getBrowseNum(storeCustomerId, beginDate, endDate);
    }
}
