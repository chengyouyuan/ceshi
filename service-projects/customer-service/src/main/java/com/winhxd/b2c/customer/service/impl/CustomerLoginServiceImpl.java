package com.winhxd.b2c.customer.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.system.login.model.CustomerUserInfo;
import com.winhxd.b2c.customer.dao.CustomerUserInfoMapper;
import com.winhxd.b2c.customer.service.CustomerLoginService;
/**
 * @author wufuyun
 * @date  2018年8月3日 上午10:03:32
 * @Description 微信登录业务实现
 * @version
 */
@Service
public class CustomerLoginServiceImpl implements CustomerLoginService{
    private static final Logger logger = LoggerFactory.getLogger(CustomerLoginServiceImpl.class);
    @Autowired
    private CustomerUserInfoMapper customerUserInfoMapper;
	@Override
	public int saveLoginInfo(CustomerUserInfo customerUserInfo) {
		customerUserInfo.setCreated(new Date());
		int cont = customerUserInfoMapper.insertSelective(customerUserInfo);
		return cont;
	}
	@Override
	public int updateCustomerInfo(CustomerUserInfo customerUserInfo) {
		customerUserInfo.setUpdated(new Date());
		int cont = customerUserInfoMapper.updateByPrimaryKeySelective(customerUserInfo);
		return cont;
	}
	@Override
	public CustomerUserInfo getCustomerUserInfoById(Long customerId) {
		return customerUserInfoMapper.selectByPrimaryKey(customerId);
	}

}
