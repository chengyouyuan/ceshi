package com.winhxd.b2c.store.service.impl;

import com.winhxd.b2c.common.domain.store.model.CustomerBrowseLog;
import com.winhxd.b2c.common.domain.store.model.StoreUserInfo;
import com.winhxd.b2c.store.dao.CustomerBrowseLogMapper;
import com.winhxd.b2c.store.dao.StoreUserInfoMapper;
import com.winhxd.b2c.store.service.StoreBrowseLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author liutong
 * @date 2018-08-07 18:42:07
 */
@Service("storeBrowseLogService")
public class StoreBrowseLogServiceImpl implements StoreBrowseLogService {

    private static final Logger logger = LoggerFactory.getLogger(StoreBrowseLogService.class);

    @Autowired
    private CustomerBrowseLogMapper customerBrowseLogMapper;

    @Autowired
    private StoreUserInfoMapper storeUserInfoMapper;

    @Override
    public void saveBrowseLogLogin(Long storeId, Long customerId) {
        StoreUserInfo storeUserInfo = storeUserInfoMapper.selectByPrimaryKey(storeId);
        if (storeUserInfo == null) {
            logger.warn("记录用户浏览门店记录，用户访问的门店不存在 storeId:{},customerId:{}", storeId, customerId);
            return;
        }
        CustomerBrowseLog customerBrowseLog = new CustomerBrowseLog();
        customerBrowseLog.setStoreCustomerId(storeUserInfo.getStoreCustomerId());
        customerBrowseLog.setCustomerId(customerId);
        customerBrowseLog.setLoginDatetime(new Date());
        customerBrowseLogMapper.insertSelective(customerBrowseLog);
    }

    @Override
    public void saveBrowseLogLogout(Long storeId, Long customerId) {
        StoreUserInfo storeUserInfo = storeUserInfoMapper.selectByPrimaryKey(storeId);
        if (storeUserInfo == null) {
            logger.warn("记录用户浏览门店记录，用户访问的门店不存在 storeId:{},customerId:{}", storeId, customerId);
            return;
        }
        CustomerBrowseLog customerBrowseLog = customerBrowseLogMapper.selectForLoginOut(storeUserInfo.getStoreCustomerId(), customerId);
        customerBrowseLog.setLogoutDatetime(new Date());
        long times = (System.currentTimeMillis() - customerBrowseLog.getLoginDatetime().getTime());
        customerBrowseLog.setStayTimeMillis(times);
        customerBrowseLogMapper.updateByPrimaryKey(customerBrowseLog);
    }

    @Override
    public int getBrowseNum(Long storeCustomerId, Date beginDate, Date endDate) {
        return customerBrowseLogMapper.getBrowseNum(storeCustomerId, beginDate, endDate);
    }
}
