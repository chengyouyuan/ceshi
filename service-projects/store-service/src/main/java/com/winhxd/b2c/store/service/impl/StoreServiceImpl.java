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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

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
            //当前用户已经存在绑定关系
            record.setStoreUserId(storeUserId);
            List<CustomerStoreRelation> list = customerStoreRelationMapper.selectByCondition(record);
            if(list != null && list.size() > 0){
                return -1;
            }
            return -2;
        }
        record.setStoreUserId(storeUserId);
        record.setBindingTime(new Date());
        return customerStoreRelationMapper.insert(record);
    }

    @Override
    public StoreUserInfoVO findStoreUserInfo(Long storeUserId) {
        StoreUserInfo info =  storeUserInfoMapper.selectByPrimaryKey(storeUserId);
        StoreUserInfoVO infoVO1 = new StoreUserInfoVO();
        if(info != null) {
            BeanUtils.copyProperties(info, infoVO1);
            return infoVO1;
        }
        return null;

    }

    @Override
    public PagedList<BackStageStoreVO> findStoreUserInfo(BackStageStoreInfoCondition storeCondition) {
        PagedList<BackStageStoreVO> pagedList = new PagedList<>();
        //去除code尾部0
        String reginCode = null;
        if (storeCondition.getRegionCode() != null) {
            reginCode = storeCondition.getRegionCode().replaceAll("0+$", "");
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
    public StoreUserInfoVO findStoreUserInfoByCustomerId(Long customerUserId) {
        StoreUserInfo storeUserInfo = storeUserInfoMapper.selectStoreUserInfoByCustomerId(customerUserId);
        if(storeUserInfo == null){
            return null;
        }
        StoreUserInfoVO infoVo = new StoreUserInfoVO();
        BeanUtils.copyProperties(storeUserInfo,infoVo);
        return infoVo;
    }

    @Override
    public StoreUserInfo findByStoreCustomerId(Long storeCustomerId) {
        return storeUserInfoMapper.selectByStoreCustomerId(storeCustomerId);
    }

    @Override
    public List<StoreUserInfoVO> findStoreUserInfoList(Set<Long> ids) {
        if(ids == null || ids.size() == 0){
            return null;
        }
        List<StoreUserInfo> userInfos =  storeUserInfoMapper.selectStoreUserByIds(ids);
        List<StoreUserInfoVO> list = new ArrayList<>();
        if(userInfos != null && userInfos.size() > 0){
        for(StoreUserInfo info:userInfos){
            StoreUserInfoVO infoVO = new StoreUserInfoVO();
            BeanUtils.copyProperties(info,infoVO);
            list.add(infoVO);
            }
        }
        return list;

    }

    @Override
    public BackStageStoreVO findByIdForBackStage(Long id) {
        BackStageStoreVO backStageStoreVO = new BackStageStoreVO();
        StoreUserInfo storeUserInfo = storeUserInfoMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(storeUserInfo, backStageStoreVO);
        //查询省市县五级信息
        SysRegion sysRegion = regionServiceClient.getRegion(storeUserInfo.getStoreRegionCode()).getData();
        if(sysRegion != null) {
            BeanUtils.copyProperties(sysRegion, backStageStoreVO);
        } else {
            logger.error("门店详细信息查询，未查询到该门店的行政区域！区域编码为：{}", storeUserInfo.getStoreRegionCode());
        }
        return backStageStoreVO;
    }

    @Override
    public int updateByPrimaryKeySelective(StoreUserInfo record) {
        return storeUserInfoMapper.updateByPrimaryKeySelective(record);
    }
}
