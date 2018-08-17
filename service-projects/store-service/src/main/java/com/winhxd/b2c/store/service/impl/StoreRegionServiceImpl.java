package com.winhxd.b2c.store.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.StoreRegionCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreRegionEnum;
import com.winhxd.b2c.common.domain.store.model.StoreRegion;
import com.winhxd.b2c.common.domain.store.vo.StoreRegionVO;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.system.RegionServiceClient;
import com.winhxd.b2c.store.dao.StoreRegionMapper;
import com.winhxd.b2c.store.service.StoreRegionService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author: wangbaokuo
 * @date: 2018/8/10 14:56
 */
@Service
public class StoreRegionServiceImpl implements StoreRegionService{

    private static final Logger logger = LoggerFactory.getLogger(StoreRegionServiceImpl.class);

    @Resource
    StoreRegionMapper storeRegionMapper;

    @Autowired
    private RegionServiceClient regionServiceClient;

    @Override
    public PagedList<StoreRegionVO> findStoreRegions(StoreRegionCondition condition) {
        checkCurrentAdminUser();
        Page page = PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        PagedList<StoreRegionVO> pagedList = new PagedList();
        StoreRegion region = new StoreRegion();
        BeanUtils.copyProperties(condition, region);
        pagedList.setData(storeRegionMapper.selectStoreRegions(region));
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;

    }

    @Override
    public int removeStoreRegion(Long id) {
        checkCurrentAdminUser();
        StoreRegion storeRegion = new StoreRegion();
        storeRegion.setId(id);
        storeRegion.setStatus(StoreRegionEnum.VALIDATE.getCode());
        storeRegion.setUpdated(new Date());
        storeRegion.setUpdatedBy(Long.parseLong(UserContext.getCurrentAdminUser().getAccount()));
        return storeRegionMapper.updateByPrimaryKey(storeRegion);
    }

    @Override
    public int saveStoreRegion(StoreRegionCondition condition) {
        checkCurrentAdminUser();
        String areaCode = condition.getAreaCode().substring(0, 3);
        List<StoreRegion> repeat = storeRegionMapper.selectRepeatStoreRegion(areaCode);
        if (CollectionUtils.isNotEmpty(repeat)) {
            logger.error("保存测试门店区域异常{} -> 销售区域重复");
            throw new BusinessException(BusinessCode.CODE_103901);
        }
        ResponseResult<SysRegion> result = regionServiceClient.getRegionByCode(condition.getAreaCode());
        if (null == result || null == result.getData()) {
            logger.error("保存测试门店区域异常{} -> 查询SysRegion失败 regionCode:"+condition.getAreaCode());
            throw new BusinessException(BusinessCode.CODE_103902);
        }
        SysRegion sysRegion = result.getData();
        StringBuffer sb = new StringBuffer();
        if (!sysRegion.getProvince().equals(sysRegion.getCity())) {
            sb.append(sysRegion.getProvince()).append(Objects.toString(sysRegion.getCity(), ""))
                    .append(Objects.toString(sysRegion.getTown(), ""))
                    .append(Objects.toString(sysRegion.getCounty(), ""))
                    .append(Objects.toString(sysRegion.getVillage(), ""));
        } else {
            sb.append(Objects.toString(sysRegion.getProvince(), ""))
                    .append(Objects.toString(sysRegion.getTown(), ""))
                    .append(Objects.toString(sysRegion.getCounty(), ""))
                    .append(Objects.toString(sysRegion.getVillage(), ""));
        }
        StoreRegion storeRegion = new StoreRegion();
        storeRegion.setLevel(sysRegion.getLevel().shortValue());
        storeRegion.setAreaName(sb.toString());
        storeRegion.setAreaCode(condition.getAreaCode());
        Date current = new Date();
        Long account = Long.parseLong(UserContext.getCurrentAdminUser().getAccount());
        storeRegion.setCreated(current);
        storeRegion.setUpdated(current);
        storeRegion.setCreatedBy(account);
        storeRegion.setUpdatedBy(account);
        storeRegion.setStatus(StoreRegionEnum.EFFICTIVE.getCode());
        return storeRegionMapper.insertSelective(storeRegion);
    }

    /**
     *
     * @author: wangbaokuo
     * @date: 2018/8/10 10:31
     * @return: 获取用户info
     */
    private void checkCurrentAdminUser() {
        AdminUser adminUser = UserContext.getCurrentAdminUser();
        if (null == adminUser) {
            logger.error("获取当前用户信息异常{} UserContext.getCurrentAdminUser():" + UserContext.getCurrentAdminUser());
            throw new BusinessException(BusinessCode.CODE_1004);
        }
    }

    @Override
    public StoreRegion getByRegionCode(String regionCode) {
        //查询省市县五级信息
        SysRegion sysRegion = regionServiceClient.getRegionByCode(regionCode).getData();
        if(sysRegion == null) {
            logger.error("开店测试区域查询，未查询到该门店的行政区域！区域编码为：{}", regionCode);
            throw new BusinessException(BusinessCode.CODE_200004);
        }
        return storeRegionMapper.selectByRegionCode(regionCode,sysRegion.getLevel(),regionCode.substring(0,3));
    }
}
