package com.winhxd.b2c.store.service.impl;

import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.domain.PagedList;

import com.winhxd.b2c.common.domain.backstage.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.backstage.store.enums.BackStageStorPaymentWayeEnum;
import com.winhxd.b2c.common.domain.backstage.store.vo.BackStageStoreVO;
import com.winhxd.b2c.common.domain.store.model.CustomerStoreRelation;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.feign.system.RegionServiceClient;
import com.winhxd.b2c.store.dao.CustomerStoreRelationMapper;
import com.winhxd.b2c.store.dao.StoreUserInfoMapper;
import com.winhxd.b2c.store.service.StoreService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chengyy
 * @Description: 门店服务的实现类
 * @date 2018/8/3 10:51
 */
@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    private CustomerStoreRelationMapper customerStoreRelationMapper;
    @Autowired
    private StoreUserInfoMapper storeUserInfoMapper;
    @Autowired
    private RegionServiceClient regionServiceClient;

    @Override
    public int bindCustomer(Long customerId, Long storeUserId) {
        CustomerStoreRelation record = new CustomerStoreRelation();
        record.setCustomerId(customerId);
        List<CustomerStoreRelation> relations = customerStoreRelationMapper.selectByCondition(record);
        if (relations != null && relations.size() > 0) {
            //当前门店已经存在绑定关系不能再进行绑定
            return 0;
        }
        record.setStoreUserId(storeUserId);
        record.setBindingTime(new Date());
        return customerStoreRelationMapper.insert(record);
    }

    @Override
    public StoreUserInfoVO findStoreUserInfo(Long storeUserId) {
        StoreUserInfo info =  storeUserInfoMapper.selectByPrimaryKey(storeUserId);
        StoreUserInfoVO infoVO1 = new StoreUserInfoVO();
        BeanUtils.copyProperties(info,infoVO1);
        return infoVO1;

    }

    @Override
    public PagedList<BackStageStoreVO> findStoreUserInfo(BackStageStoreInfoCondition storeCondition) {
        PagedList<BackStageStoreVO> pagedList = new PagedList<>();
        //去除code尾部0
        String reginCode = null;
        if (storeCondition.getReginCode() != null) {
            reginCode = storeCondition.getReginCode().replaceAll("0+$", "");
        }

        PageHelper.startPage(storeCondition.getPageNo(), storeCondition.getPageSize());
        StoreUserInfo storeUserInfo = new StoreUserInfo();
        storeUserInfo.setStoreRegionCode(reginCode);
        storeUserInfo.setStoreStatus(storeCondition.getStoreStatus());
        storeUserInfo.setStoreName(storeCondition.getStoreName());
        storeUserInfo.setStoreMobile(storeCondition.getStoreMobile());
        List<StoreUserInfo> userInfoList = storeUserInfoMapper.findStoreUserInfo(storeUserInfo);
        if (userInfoList.isEmpty()){
            return pagedList;
        }

        //获取regincode对应的区域名称
        List<String> reginCodeList = userInfoList.stream().map(storeUser -> storeUser.getStoreRegionCode()).collect(Collectors.toList());
        List<SysRegion> sysRegions = regionServiceClient.getRegionsByRange(reginCodeList).getData();

        List<BackStageStoreVO> storeVOS = new ArrayList<>();
        Set<String> codes = new HashSet<>();
        userInfoList.stream().forEach(storeUserInfo1 -> {
            BackStageStoreVO storeVO = new BackStageStoreVO();
            BeanUtils.copyProperties(storeUserInfo1,storeVO);
            codes.add(storeUserInfo1.getStoreRegionCode());
            if (!StringUtils.isEmpty(storeUserInfo1.getPaymentWay())){
                //获取支付类型
                String[] codeArr = storeUserInfo1.getPaymentWay().split(",");
                String paymentWayStr = Arrays.asList(codeArr).stream().map(s -> BackStageStorPaymentWayeEnum.codeOf(s).getStatusDes())
                        .collect(Collectors.joining(","));
                storeVO.setPaymentWay(paymentWayStr);

                //获取地理区域名称
                for (SysRegion sysRegion : sysRegions) {
                    if (sysRegion.getRegionCode().equals(storeUserInfo1.getStoreRegionCode())){
                        storeVO.setCity(sysRegion.getCity());
                        storeVO.setCounty(sysRegion.getCounty());
                        storeVO.setProvince(sysRegion.getProvince());
                        storeVO.setTown(sysRegion.getTown());
                        storeVO.setVillage(sysRegion.getVillage());
                        break;
                    }
                }
            }
            storeVOS.add(storeVO);
        });
        pagedList.setTotalRows(userInfoList.size());
        pagedList.setData(storeVOS);
        return pagedList;
    }

    @Override
    public StoreUserInfo findStoreUserInfoByCustomerId(Long customerUserId) {
        return storeUserInfoMapper.selectStoreUserInfoByCustomerId(customerUserId);
    }

    @Override
    public StoreUserInfo selectByStoreId(Long storeId) {
        return storeUserInfoMapper.selectByStoreId(storeId);
    }

    @Override
    public List<StoreUserInfoVO> findStoreUserInfoList(Set<Long> ids) {
        if(ids == null || ids.size() == 0){
            return null;
        }
        List<StoreUserInfoVO> userInfos =  storeUserInfoMapper.selectStoreUserByIds(ids);
        return userInfos;

    }

    @Override
    public int updateByPrimaryKeySelective(StoreUserInfo record) {
        return storeUserInfoMapper.updateByPrimaryKeySelective(record);
    }
}
