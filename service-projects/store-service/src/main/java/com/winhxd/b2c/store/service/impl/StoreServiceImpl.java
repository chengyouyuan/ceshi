package com.winhxd.b2c.store.service.impl;

import com.winhxd.b2c.common.domain.store.model.CustomerStoreRelation;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.store.dao.CustomerStoreRelationMapper;
import com.winhxd.b2c.store.dao.StoreUserInfoMapper;
import com.winhxd.b2c.store.service.StoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description: 门店服务的实现类
 * @author chengyy
 * @date 2018/8/3 10:51
 */
@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    private CustomerStoreRelationMapper customerStoreRelationMapper;
    @Autowired
    private StoreUserInfoMapper storeUserInfoMapper;

    @Override
    public int bindCustomer(Long customerId, Long storeUserId) {
        CustomerStoreRelation record = new CustomerStoreRelation();
        record.setCustomerId(customerId);
        List<CustomerStoreRelation> relations = customerStoreRelationMapper.selectByCondition(record);
        if(relations != null && relations.size() >0){
            //当前门店已经存在绑定关系不能再进行绑定
            return 0;
        }
        record.setStoreUserId(storeUserId);
        record.setBindingTime(new Date());
        return customerStoreRelationMapper.insert(record);
    }

    @Override
    public StoreUserInfoVO findStoreUserInfo(Long storeUserId) {
        StoreUserInfo userInfo = storeUserInfoMapper.selectByPrimaryKey(storeUserId);
        if(userInfo == null){
            return null;
        }
        StoreUserInfoVO userInfoVO = new StoreUserInfoVO();
        BeanUtils.copyProperties(userInfo,userInfoVO);
        return userInfoVO;
    }
}
