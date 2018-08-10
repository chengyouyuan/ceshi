package com.winhxd.b2c.store.service;

import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;

@Service
public interface StoreLoginService {

	int modifyStoreUserInfo(StoreUserInfo storeUserInfo);

	StoreUserInfo getStoreUserInfo(StoreUserInfo storeUserInfo);

	int saveStoreInfo(StoreUserInfo info);

}
