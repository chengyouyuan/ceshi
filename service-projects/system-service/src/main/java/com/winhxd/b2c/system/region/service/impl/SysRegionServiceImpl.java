package com.winhxd.b2c.system.region.service.impl;

import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCodeCondition;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.domain.system.region.vo.SysSubRegionVO;
import com.winhxd.b2c.system.region.dao.SysRegionMapper;
import com.winhxd.b2c.system.region.service.SysRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 地理区域服务实现类
 * @author: zhanglingke
 * @create: 2018-08-02 16:04
 **/
@Service
public class SysRegionServiceImpl implements SysRegionService {
    /**
     * 地域级别
     * 1.	省
     * 2.	市
     * 3.	区
     * 4.	县/镇（街道）
     * 5.	村（居委会）
     */
    private static final int PROVINCELEVEL  = 1;
    private static final int CITYLEVEL  = 2;
    private static final int COUNTYLEVEL  = 3;
    private static final int TOWNLEVEL  = 4;
    private static final int VILLAGELEVEL  = 5;
    @Resource
    private SysRegionMapper sysRegionMapper;

    @Override
    public SysRegion getRegionByCode(String regionCode) {
        return sysRegionMapper.selectByPrimaryKey(regionCode);
    }

    @Override
    public List<SysRegion> findRegionByLevel(int level) {
        SysRegion sysRegion=new SysRegion();
        sysRegion.setLevel(level);
        return sysRegionMapper.selectRegionList(sysRegion);
    }

    @Override
    public List<SysRegion> findRegionList(SysRegion region) {
        return sysRegionMapper.selectRegionList(region);
    }

    @Override
    public List<SysRegion> findChilds(SysRegionCondition condition) {
        SysRegion region=new SysRegion();

        switch (condition.getLevel()) {
            case PROVINCELEVEL:
                region.setProvinceCode(condition.getRegionCode());
                break;
            case CITYLEVEL:
                region.setCityCode(condition.getRegionCode());
                break;
            case COUNTYLEVEL:
                region.setCountyCode(condition.getRegionCode());
                break;
            case TOWNLEVEL:
                region.setTownCode(condition.getRegionCode());
                break;
            case VILLAGELEVEL:
                region.setVillageCode(condition.getRegionCode());
                break;
            default:
        }
        region.setLevel(condition.getLevel()+1);
        return  sysRegionMapper.selectRegionList(region);
    }

    @Override
    public List<SysRegion> findRegionByCodes(List<SysRegionCodeCondition> regionCodes) {
        return  sysRegionMapper.selectRegionRangeList(regionCodes);
    }
}
