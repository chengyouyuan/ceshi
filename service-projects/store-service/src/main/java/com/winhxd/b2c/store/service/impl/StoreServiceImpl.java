package com.winhxd.b2c.store.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreInfoSimpleCondition;
import com.winhxd.b2c.common.domain.store.model.CustomerStoreRelation;
import com.winhxd.b2c.common.domain.store.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreVO;
import com.winhxd.b2c.common.domain.store.vo.StoreMessageAccountVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserInfoCondition;
import com.winhxd.b2c.common.domain.store.model.StoreStatusEnum;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
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

    @Autowired
    private MessageServiceClient messageServiceClient;

    @Override
    public int bindCustomer(Long customerId, Long storeUserId) {
        CustomerStoreRelation record = new CustomerStoreRelation();
        record.setCustomerId(customerId);
        List<CustomerStoreRelation> relations = customerStoreRelationMapper.selectByCondition(record);
        if (relations != null && relations.size() > 0) {
            //当前用户已经存在绑定关系
            record.setStoreUserId(storeUserId);
            List<CustomerStoreRelation> list = customerStoreRelationMapper.selectByCondition(record);
            if (list != null && list.size() > 0) {
                return -1;
            }
            return -2;
        }
        record.setStoreUserId(storeUserId);
        record.setBindingTime(new Date());
        record.setStatus(0);
        return customerStoreRelationMapper.insert(record);
    }

    @Override
    public StoreUserInfoVO findStoreUserInfo(Long storeUserId) {
        StoreUserInfo info = storeUserInfoMapper.selectByPrimaryKey(storeUserId);
        StoreUserInfoVO infoVO1 = new StoreUserInfoVO();
        if (info != null) {
            BeanUtils.copyProperties(info, infoVO1);
            return infoVO1;
        }
        return null;

    }

    @Override
    public PagedList<BackStageStoreVO> findStoreUserInfo(BackStageStoreInfoCondition storeCondition) {
        PagedList<BackStageStoreVO> pagedList = new PagedList<>();
        //去除code尾部0
        String regionCode = null;
        if (storeCondition.getRegionCode() != null) {
            regionCode = storeCondition.getRegionCode().replaceAll("0+$", "");
        }

        PageHelper.startPage(storeCondition.getPageNo(), storeCondition.getPageSize());
        StoreUserInfo storeUserInfo = new StoreUserInfo();
        storeUserInfo.setStoreRegionCode(regionCode);
        storeUserInfo.setStoreStatus(storeCondition.getStoreStatus());
        storeUserInfo.setStoreName(storeCondition.getStoreName());
        storeUserInfo.setStoreMobile(storeCondition.getStoreMobile());
        Page<StoreUserInfo> userInfoList = storeUserInfoMapper.selectStoreUserInfo(storeUserInfo);
        if (userInfoList.isEmpty()) {
            return pagedList;
        }

        //获取regioncode对应的区域名称
        List<String> regionCodeList = userInfoList.stream().map(storeUser -> storeUser.getStoreRegionCode()).collect(Collectors.toList());
        List<SysRegion> sysRegions = regionServiceClient.findRegionRangeList(regionCodeList).getData();

        List<BackStageStoreVO> storeVOS = new ArrayList<>();
        userInfoList.stream().forEach(storeUserInfo1 -> {
            BackStageStoreVO storeVO = new BackStageStoreVO();
            BeanUtils.copyProperties(storeUserInfo1, storeVO);
            changeStatusDesc(storeVO);
            if (!StringUtils.isEmpty(storeUserInfo1.getPayType())) {
                //获取地理区域名称
                for (SysRegion sysRegion : sysRegions) {
                    if (sysRegion.getRegionCode().equals(storeUserInfo1.getStoreRegionCode())) {
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
        pagedList.setTotalRows(userInfoList.getTotal());
        pagedList.setData(storeVOS);
        return pagedList;
    }

    private BackStageStoreVO changeStatusDesc(BackStageStoreVO stageStoreVO){
        if(null == stageStoreVO){
            return stageStoreVO;
        }
        if(StoreStatusEnum.VALID.getStatusCode() == stageStoreVO.getStoreStatus()){
            stageStoreVO.setStoreStatusDesc(StoreStatusEnum.VALID.getStatusDesc());
        }else if(StoreStatusEnum.INVALID.getStatusCode() == stageStoreVO.getStoreStatus()){
            stageStoreVO.setStoreStatusDesc(StoreStatusEnum.INVALID.getStatusDesc());
        } else if (StoreStatusEnum.UN_OPEN.getStatusCode() == stageStoreVO.getStoreStatus()) {
            stageStoreVO.setStoreStatusDesc(StoreStatusEnum.UN_OPEN.getStatusDesc());
        }
        //支付方式

        return stageStoreVO;
    }

    @Override
    public StoreUserInfoVO findStoreUserInfoByCustomerId(Long customerUserId) {
        StoreUserInfo storeUserInfo = storeUserInfoMapper.selectStoreUserInfoByCustomerId(customerUserId);
        if (storeUserInfo == null) {
            return null;
        }
        StoreUserInfoVO infoVo = new StoreUserInfoVO();
        BeanUtils.copyProperties(storeUserInfo, infoVo);
        return infoVo;
    }

    @Override
    public StoreUserInfo findByStoreCustomerId(Long storeCustomerId) {
        return storeUserInfoMapper.selectByStoreCustomerId(storeCustomerId);
    }

    @Override
    public List<StoreUserInfoVO> findStoreUserInfoList(Set<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return null;
        }
        List<StoreUserInfo> userInfos = storeUserInfoMapper.selectStoreUserByIds(ids);
        List<StoreUserInfoVO> list = new ArrayList<>();
        if (userInfos != null && userInfos.size() > 0) {
            for (StoreUserInfo info : userInfos) {
                StoreUserInfoVO infoVO = new StoreUserInfoVO();
                BeanUtils.copyProperties(info, infoVO);
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
        SysRegion sysRegion = regionServiceClient.getRegionByCode(storeUserInfo.getStoreRegionCode()).getData();
        if (sysRegion != null) {
            BeanUtils.copyProperties(sysRegion, backStageStoreVO);
        } else {
            logger.error("门店详细信息查询，未查询到该门店的行政区域！区域编码为：{}", storeUserInfo.getStoreRegionCode());
        }
        return backStageStoreVO;
    }

    @Override
    public void updateRegionCodeByCustomerId(StoreUserInfo storeUserInfo) {
        storeUserInfoMapper.updateRegionCodeByCustomerId(storeUserInfo);
    }

    @Override
    public PagedList<StoreUserInfoVO> findStorePageInfo(BackStageStoreInfoSimpleCondition condition) {
        PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        StoreUserInfo record = new StoreUserInfo();
        BeanUtils.copyProperties(condition, record);
        List<StoreUserInfoVO> list = storeUserInfoMapper.selectStoreByCondition(record);
        PageInfo<StoreUserInfoVO> pageInfo = new PageInfo<>(list);
        PagedList<StoreUserInfoVO> pagedList = new PagedList<>();
        pagedList.setTotalRows(pageInfo.getTotal());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setData(pageInfo.getList());
        return pagedList;
    }

    @Override
    public List<String> findByRegionCodes(List<String> regionCodeList) {
        regionCodeList = regionCodeList.stream().map(regionCode -> regionCode.replaceAll("0+$", "")).collect(Collectors.toList());
        return storeUserInfoMapper.selectByRegionCodes(regionCodeList);
    }

    @Override
    public int updateByPrimaryKeySelective(StoreUserInfo record) {
        return storeUserInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public StoreMessageAccountVO modifyStoreAndCreateAccount(StoreUserInfo storeUserInfo) {
        NeteaseAccountCondition neteaseAccountCondition = new NeteaseAccountCondition();
        neteaseAccountCondition.setCustomerId(storeUserInfo.getId());
        neteaseAccountCondition.setName(storeUserInfo.getShopkeeper());
        neteaseAccountCondition.setIcon(storeUserInfo.getShopOwnerImg());
        neteaseAccountCondition.setMobile(storeUserInfo.getStoreMobile());
        //创建云信账号
        NeteaseAccountVO neteaseAccountVO = messageServiceClient.createNeteaseAccount(neteaseAccountCondition).getData();
        StoreMessageAccountVO storeMessageAccountVO = new StoreMessageAccountVO();
        if (neteaseAccountVO != null && StringUtils.isNotBlank(neteaseAccountVO.getAccid()) && StringUtils.isNotBlank(neteaseAccountVO.getToken())) {
            //开店状态 有效
            storeUserInfo.setStoreStatus(StoreStatusEnum.VALID.getStatusCode());
            storeUserInfoMapper.updateByPrimaryKeySelective(storeUserInfo);
            storeMessageAccountVO.setNeteaseAccid(neteaseAccountVO.getAccid());
            storeMessageAccountVO.setNeteaseToken(neteaseAccountVO.getToken());
        } else {
            logger.error("惠小店开店创建云信账号失败 账号为：{}", neteaseAccountCondition.toString());
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        return storeMessageAccountVO;
    }

    @Override
    public boolean updateStoreCodeUrl(StoreUserInfoCondition condition) {
        StoreUserInfo record = new StoreUserInfo();
        record.setId(condition.getId());
        record.setMiniProgramCodeUrl(condition.getMiniProgramCodeUrl());
        int count = storeUserInfoMapper.updateByPrimaryKeySelective(record);
        return count == 1 ? true : false;
    }
}
