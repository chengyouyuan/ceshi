package com.winhxd.b2c.customer.service;


import com.winhxd.b2c.common.domain.customer.model.CustomerUserInfo;

public interface CustomerLoginService {

	int saveLoginInfo(CustomerUserInfo customerUserInfo);

	int updateCustomerInfo(CustomerUserInfo customerUserInfo);

	CustomerUserInfo getCustomerUserInfoByModel(CustomerUserInfo customerUserInfo);

}
