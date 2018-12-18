package com.winhxd.b2c.store.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.customer.enums.CustomerBindStoreStatusEnum;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreInfoSimpleCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreCustomerRegionCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreListByKeywordsCondition;
import com.winhxd.b2c.common.domain.store.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.store.enums.PickupTypeEnum;
import com.winhxd.b2c.common.domain.store.enums.StoreBindingStatus;
import com.winhxd.b2c.common.domain.store.model.StoreCustomerRelation;
import com.winhxd.b2c.common.domain.store.model.StoreCustomerRelationLog;
import com.winhxd.b2c.common.domain.store.model.StoreStatusEnum;
import com.winhxd.b2c.common.domain.store.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreVO;
import com.winhxd.b2c.common.domain.store.vo.StoreMessageAccountVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerBindingStatusCondition;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserInfoCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.common.feign.system.RegionServiceClient;
import com.winhxd.b2c.store.dao.StoreCustomerRelationLogMapper;
import com.winhxd.b2c.store.dao.StoreCustomerRelationMapper;
import com.winhxd.b2c.store.dao.StoreUserInfoMapper;
import com.winhxd.b2c.store.service.StoreService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private StoreCustomerRelationMapper storeCustomerRelationMapper;
    @Autowired
    private StoreUserInfoMapper storeUserInfoMapper;
    @Autowired
    private RegionServiceClient regionServiceClient;

    @Autowired
    private MessageServiceClient messageServiceClient;

    @Autowired
    private CustomerServiceClient customerServiceClient;

    @Autowired
    private Cache cache;

    private static final String CHECK_STORE_UNBIND_KEY = "check:store:unbind:lock:";
    private static final String CHECK_STORE_CHANGEBIND_KEY = "check:store:changebind:lock:";

    // 顾客解绑
    private static final Short UNBIND = 2;
    // 顾客换绑
    private static final Short CHANGE_BIND = 3;

    @Autowired
    private StoreCustomerRelationLogMapper storeCustomerRelationLogMapper;

    @Override
    public StoreBindingStatus bindCustomer(Long customerId, Long storeUserId) {
        StoreCustomerRelation record = new StoreCustomerRelation();
        record.setCustomerId(customerId);
        List<StoreCustomerRelation> relations = storeCustomerRelationMapper.selectByCondition(record);
        if (null != relations && relations.size() > 0) {
            //当前用户已经存在绑定关系
            for (StoreCustomerRelation relation : relations) {
                if(relation.getStoreUserId() != null && storeUserId !=null && relation.getStoreUserId().longValue() == storeUserId.longValue()){
                    //已绑定当前门店
                    return StoreBindingStatus.AdreadyBinding;
                }
            }
            //绑定了其他门店
            return StoreBindingStatus.DifferenceBinding;
        }
        record.setStoreUserId(storeUserId);
        record.setBindingTime(new Date());
        record.setStatus(1);
        //todo,需要判断storeUserId是否有效,以免产生脏数据；
        StoreUserInfo store = storeUserInfoMapper.selectByPrimaryKey(storeUserId);
        if (null == store) {
            throw new BusinessException(BusinessCode.CODE_102902);
        }
        storeCustomerRelationMapper.insert(record);

        return StoreBindingStatus.NewBinding;
    }

    @Override
    public StoreUserInfoVO findStoreUserInfo(Long storeUserId) {
        StoreUserInfo info = storeUserInfoMapper.selectByPrimaryKey(storeUserId);
        StoreUserInfoVO infoVO1 = null;
        if (info != null) {
            infoVO1 = new StoreUserInfoVO();
            BeanUtils.copyProperties(info, infoVO1);
            if(StringUtils.isBlank(infoVO1.getStoreShortName())){
                infoVO1.setStoreShortName(infoVO1.getStoreName());
            }
        }
        return infoVO1;
    }

    @Override
    public PagedList<BackStageStoreVO> findStoreUserInfo(BackStageStoreInfoCondition storeCondition) {
        PagedList<BackStageStoreVO> pagedList = new PagedList<>();
        //去除code尾部0
        String regionCode = null;
        if (storeCondition.getRegionCode() != null) {
            regionCode = storeCondition.getRegionCode().replaceAll("0+$", "");
        }

        if (!storeCondition.getIsQueryAll()) {
            PageHelper.startPage(storeCondition.getPageNo(), storeCondition.getPageSize());
        }
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
        List<SysRegion> sysRegions = regionServiceClient.findRegionRangeList(regionCodeList).getDataWithException();

        List<BackStageStoreVO> storeVOS = new ArrayList<>();
        userInfoList.stream().forEach(storeUserInfo1 -> {
            BackStageStoreVO storeVO = new BackStageStoreVO();
            BeanUtils.copyProperties(storeUserInfo1, storeVO);
            changeStatusDesc(storeVO);
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
            if (!StringUtils.isEmpty(storeUserInfo1.getPayType())) {
                String typeDesc = Arrays.asList(storeUserInfo1.getPayType().split(",")).stream()
                        .map(s -> PayTypeEnum.getPayTypeEnumByTypeCode(new Short(s)).getTypeDesc()).collect(Collectors.joining(","));
                storeVO.setPayTypeDesc(typeDesc);
            }
            storeVOS.add(storeVO);
        });
        pagedList.setTotalRows(userInfoList.getTotal());
        pagedList.setPageNo(storeCondition.getPageNo());
        pagedList.setData(storeVOS);
        return pagedList;
    }

    /**
     * 处理字典类型字段回显
     * @param stageStoreVO
     * @return
     */
    private BackStageStoreVO changeStatusDesc(BackStageStoreVO stageStoreVO){
        if(null == stageStoreVO){
            return null;
        }
        // 状态
        if (stageStoreVO.getStoreStatus() != null) {
            if (StoreStatusEnum.VALID.getStatusCode() == stageStoreVO.getStoreStatus()) {
                stageStoreVO.setStoreStatusDesc(StoreStatusEnum.VALID.getStatusDesc());
            } else if (StoreStatusEnum.INVALID.getStatusCode() == stageStoreVO.getStoreStatus()) {
                stageStoreVO.setStoreStatusDesc(StoreStatusEnum.INVALID.getStatusDesc());
            } else if (StoreStatusEnum.UN_OPEN.getStatusCode() == stageStoreVO.getStoreStatus()) {
                stageStoreVO.setStoreStatusDesc(StoreStatusEnum.UN_OPEN.getStatusDesc());
            }
        }
        return stageStoreVO;
    }

    @Override
    public StoreUserInfoVO findStoreUserInfoByCustomerId(Long customerUserId) {
        StoreUserInfo storeUserInfo = storeUserInfoMapper.selectStoreUserInfoByCustomerId(customerUserId);
        StoreUserInfoVO infoVo = null;
        if (null != storeUserInfo) {
            infoVo = new StoreUserInfoVO();
            BeanUtils.copyProperties(storeUserInfo, infoVo);
        }
        return infoVo;
    }

    @Override
    public StoreUserInfo findByStoreCustomerId(Long storeCustomerId) {
        return storeUserInfoMapper.selectByStoreCustomerId(storeCustomerId);
    }

    @Override
    public List<StoreUserInfoVO> findStoreUserInfoList(Set<Long> ids) {
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
        SysRegion sysRegion = regionServiceClient.getRegionByCode(storeUserInfo.getStoreRegionCode()).getDataWithException();
        if (sysRegion != null) {
            BeanUtils.copyProperties(sysRegion, backStageStoreVO);
        } else {
            logger.error("门店详细信息查询，未查询到该门店的行政区域！区域编码为：{}", storeUserInfo.getStoreRegionCode());
        }
        this.changeDescForDetail(backStageStoreVO);
        return backStageStoreVO;
    }

    /**
     * 后台管理查看详细和编辑时 处理字典类型字段回显
     *
     * @param stageStoreVO
     * @return
     */
    private void changeDescForDetail(BackStageStoreVO stageStoreVO){
        if(null == stageStoreVO){
            return;
        }
        //取货方式
        if (StringUtils.isNotBlank(stageStoreVO.getPickupType())) {
            String typeDesc = Arrays.stream(stageStoreVO.getPickupType().split(","))
                    .map(s -> PickupTypeEnum.getPickupTypeDescByCode(new Short(s))).collect(Collectors.joining(","));
            stageStoreVO.setPickupTypeDesc(typeDesc);
        }
        //支付方式
        if (StringUtils.isNotBlank(stageStoreVO.getPayType())) {
            String typeDesc = Arrays.stream(stageStoreVO.getPayType().split(","))
                    .map(s -> PayTypeEnum.getPayTypeEnumDescByTypeCode(new Short(s))).collect(Collectors.joining(","));
            stageStoreVO.setPayTypeDesc(typeDesc);
        }
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
        //更新云信账号信息
        messageServiceClient.updateNeteaseAccount(neteaseAccountCondition);
        StoreMessageAccountVO storeMessageAccountVO = new StoreMessageAccountVO();
        //开店状态 有效
        storeUserInfo.setStoreStatus(StoreStatusEnum.VALID.getStatusCode());
        storeUserInfoMapper.updateByPrimaryKeySelective(storeUserInfo);
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

	@Override
	public List<StoreUserInfoVO> getStoreListByKeywords(StoreListByKeywordsCondition condition) {
		if (condition==null) {
			return new ArrayList<>();
		}
		if (CollectionUtils.isEmpty(condition.getStoreIds())&&CollectionUtils.isEmpty(condition.getStoreMobiles())) {
			return new ArrayList<>();
		}
		return storeUserInfoMapper.getStoreListByKeywords(condition);
	}

    @Override
    public StoreUserInfo findByPrimaryKey(Long id) {
        return storeUserInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Long> findStoreCustomerRegions(StoreCustomerRegionCondition storeCustomerRegionCondition) {
        return storeCustomerRelationMapper.selectCustomerIds(storeCustomerRegionCondition);
    }

    /**
     * 根据店铺ID，删除门店信息
     * @param storeUserInfoId
     * @return
     */
    @Override
    public int deleteStoreUserInfoById(Long storeUserInfoId){
        return storeUserInfoMapper.deleteByPrimaryKey(storeUserInfoId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int unBundling(List<CustomerBindingStatusCondition> condition) {
        List<Long> customerIdList = condition.stream().map(con -> con.getCustomerId()).collect(Collectors.toList());
        RedisLock lock = new RedisLock(cache, CHECK_STORE_UNBIND_KEY, 1000);
        int num = 0;
        try {
            if (lock.tryLock(1, TimeUnit.SECONDS)) {
                List<StoreCustomerRelation> relationList = storeCustomerRelationMapper.selectByCustomerIdList(customerIdList);
                if (relationList != null && relationList.size() > 0) {
                    customerIdList = relationList.stream().map(relation -> relation.getCustomerId()).collect(Collectors.toList());
                    // 先解绑
                    num = storeCustomerRelationMapper.unBundling(customerIdList);
                    // 往门店用户绑定关系日志表添加数据
                    List<CustomerBindingStatusCondition> conditionList = relationList.stream().map(relation -> {
                        CustomerBindingStatusCondition cond = new CustomerBindingStatusCondition();
                        cond.setCustomerId(relation.getCustomerId());
                        cond.setStoreId(relation.getStoreUserId());
                        return cond;
                    }).collect(Collectors.toList());
                    this.batchAddStoreCustomerRelationLog(conditionList, UNBIND);
                }
            } else {
                throw new BusinessException(BusinessCode.CODE_1001);
            }
        } finally {
            lock.lock();
        }
        return num;
    }

    private List<Long> getCustomerIds(List<CustomerBindingStatusCondition> condition) {
        List<Long> customerIds = condition.stream().map(con -> con.getCustomerId()).collect(Collectors.toList());
        if (org.springframework.util.CollectionUtils.isEmpty(customerIds)) {
            throw new BusinessException(BusinessCode.CODE_200010);
        }
        return customerIds;
    }

    private void checkStore(List<Long> customerIds) {
        List<Long> storeIds = storeCustomerRelationMapper.selectStoreIds(customerIds);
        if (org.springframework.util.CollectionUtils.isEmpty(storeIds)) {
            throw new BusinessException(BusinessCode.CODE_200009);
        }
    }

    private void batchAddStoreCustomerRelationLog(List<CustomerBindingStatusCondition> condition, Short stauts) {
        List<StoreCustomerRelationLog> list = condition.stream().map(con -> {
            StoreCustomerRelationLog storeCustomerRelationLog = new StoreCustomerRelationLog();
            storeCustomerRelationLog.setCustomerId(con.getCustomerId());
            storeCustomerRelationLog.setStoreId(con.getStoreId());
            storeCustomerRelationLog.setLogTime(new Date());
            if (CustomerBindStoreStatusEnum.UN_BIND.getStatus() == stauts.shortValue()) {
                storeCustomerRelationLog.setBindStatus((int) CustomerBindStoreStatusEnum.UN_BIND.getStatus());
            } else if (CustomerBindStoreStatusEnum.CHANGE_BIND.getStatus() == stauts.shortValue()) {
                storeCustomerRelationLog.setBindStatus((int) CustomerBindStoreStatusEnum.CHANGE_BIND.getStatus());
            }
            return storeCustomerRelationLog;
        }).collect(Collectors.toList());

        storeCustomerRelationLogMapper.batchAddStoreCustomerRelationLog(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeBind(List<CustomerBindingStatusCondition> conditionList) {
        //换绑storeId, 传的是ID而非storeId
        Long storeId = conditionList.get(0).getId();
        List<Long> customerIdList = conditionList.stream().map(con -> {
            con.setStoreId(storeId);
            return con.getCustomerId();
        }).collect(Collectors.toList());
        RedisLock lock = new RedisLock(cache, CHECK_STORE_CHANGEBIND_KEY, 1000);
        int num = 0;
        try {
            if (lock.tryLock(1, TimeUnit.SECONDS)) {
                List<StoreCustomerRelation> relationList = storeCustomerRelationMapper.selectByCustomerIdList(customerIdList);
                List<Long> customerIds = relationList.stream().map(relation -> relation.getCustomerId()).collect(Collectors.toList());
                if (relationList != null && relationList.size() > 0) {
                    // 已绑定的换绑
                    num = storeCustomerRelationMapper.changeBuind(storeId, customerIds);
                }
                //未绑定的绑定
                List<Long> unBindCustomerIdList = customerIdList.stream().filter(cusId -> !customerIds.contains(cusId)).collect(Collectors.toList());
                if (unBindCustomerIdList != null && unBindCustomerIdList.size() > 0) {
                    num = storeCustomerRelationMapper.batchBind(storeId, unBindCustomerIdList);
                }
                // 往门店用户绑定关系日志表添加数据
                this.batchAddStoreCustomerRelationLog(conditionList, CHANGE_BIND);
            } else {
                throw new BusinessException(BusinessCode.CODE_1001);
            }
        } finally {
            lock.lock();
        }
        return num;
    }

    private Map<String, Object> customerBindingStatusConditionToMap(List<CustomerBindingStatusCondition> condition) {
        Map<String, Object> paraMap = new HashMap<>();
        // 批量换绑，门店id是一样的。
        paraMap.put("storeId", condition.get(0).getStoreId());
        paraMap.put("list", condition);
        return paraMap;
    }
}
