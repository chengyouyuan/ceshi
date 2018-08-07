package com.winhxd.b2c.system.region.dao;

import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCodeCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SysRegionMapper {

    SysRegion selectByPrimaryKey(String regionCode);

    List<SysRegion> selectRegionList(SysRegion region);

    List<SysRegion> selectRegionRangeList(List<SysRegionCodeCondition> regionCodes);
}