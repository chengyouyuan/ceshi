package com.winhxd.b2c.store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.store.dao.StoreUserInfoMapper;
import com.winhxd.b2c.store.service.StoreLoginService;
/**
 * @author wufuyun
 * @date  2018年8月6日 上午10:41:39
 * @Description 门店服务层
 * @version
 */
@Service
public class StoreLoginServiceImpl implements StoreLoginService{
    @Autowired
    private StoreUserInfoMapper storeUserInfoMapper;
	@Override
	public int modifyStoreUserInfo(StoreUserInfo storeUserInfo) {
		return storeUserInfoMapper.updateByPrimaryKeySelective(storeUserInfo);
	}
	@Override
	public int saveStoreInfo(StoreUserInfo info) {
		return storeUserInfoMapper.insertSelective(info);
	}
	@Override
	public StoreUserInfo getstoreUserInfo(StoreUserInfo storeUserInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
