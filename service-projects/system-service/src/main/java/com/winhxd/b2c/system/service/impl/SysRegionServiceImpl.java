package com.winhxd.b2c.system.service.impl;

import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.feign.system.enums.RegionLevelEnum;
import com.winhxd.b2c.system.dao.SysRegionMapper;
import com.winhxd.b2c.system.service.SysRegionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 地理区域服务实现类
 * @author: zhanglingke
 * @create: 2018-08-02 16:04
 **/
@Service
public class SysRegionServiceImpl implements SysRegionService {
    @Resource
    private SysRegionMapper sysRegionMapper;

    @Override
    public SysRegion getRegionByCode(String regionCode) {
        return sysRegionMapper.selectByPrimaryKey(regionCode);
    }

    @Override
    public List<SysRegion> findRegionByLevel(RegionLevelEnum level) {
        SysRegion sysRegion=new SysRegion();
        sysRegion.setLevel(level.getCode());
        return sysRegionMapper.selectRegionList(sysRegion);
    }

    @Override
    public List<SysRegion> findRegionList(SysRegion region) {
        return sysRegionMapper.selectRegionList(region);
    }

    @Override
    public List<SysRegion> findRegionByCodes(List<String> regionCodes) {
        return  sysRegionMapper.selectRegionRangeList(regionCodes);
    }
}
