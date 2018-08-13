package com.winhxd.b2c.system.region.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionPagedCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.feign.system.enums.RegionLevelEnum;
import com.winhxd.b2c.system.region.dao.SysRegionMapper;
import com.winhxd.b2c.system.region.service.SysRegionService;
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

    @Override
    public PagedList<SysRegion> findRegionByPage(SysRegionPagedCondition condition) {
        PagedList<SysRegion> pagedList=new PagedList<>();
        Page page = PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        List<SysRegion> list= sysRegionMapper.selectRegionFilterList(condition);
        pagedList.setPageNo(page.getPageNum());
        pagedList.setPageSize(page.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        pagedList.setData(list);
        return pagedList;
    }
}
