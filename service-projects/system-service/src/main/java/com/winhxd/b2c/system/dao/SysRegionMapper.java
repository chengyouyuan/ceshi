package com.winhxd.b2c.system.dao;

import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionPagedCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import java.util.List;

public interface SysRegionMapper {

    SysRegion selectByPrimaryKey(String regionCode);

    List<SysRegion> selectRegionList(SysRegion region);

    List<SysRegion> selectRegionRangeList(List<String> regionCodes);

    List<SysRegion> selectRegionFilterList(SysRegionPagedCondition condition);
}