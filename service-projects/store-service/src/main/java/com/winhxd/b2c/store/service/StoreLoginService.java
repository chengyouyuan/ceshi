package com.winhxd.b2c.store.service;

import com.winhxd.b2c.common.domain.store.model.StoreUserInfo;
import org.springframework.stereotype.Service;


@Service
public interface StoreLoginService {

	int modifyStoreUserInfo(StoreUserInfo storeUserInfo);

	StoreUserInfo getStoreUserInfo(StoreUserInfo storeUserInfo);

	int saveStoreInfo(StoreUserInfo info);

    int deleteStoreUserInfoById(Long storeUserInfoId);
}
