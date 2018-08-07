package com.winhxd.b2c.system.region.service;

import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCodeCondition;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.domain.system.region.vo.SysSubRegionVO;
import com.winhxd.b2c.common.feign.system.enums.RegionLevelEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 地理区域管理
 * @author: zhanglingke
 * @create: 2018-08-02 15:52
 **/
public interface SysRegionService {

    /**
     * 功能描述:根据区域编码获取地理区域
     * @auther: zhanglingke
     * @date: 2018-08-02 17:08
     * @param: 区域编码
     * @return: 地理区域
     */
    SysRegion getRegionByCode(String regionCode);

    /**
     * 功能描述:根据行政区域级别获取地理区域
     * @auther: zhanglingke
     * @date: 2018-08-02 17:09
     * @param: 行政区域级别
     * @return: 地理区域列表
     */
    List<SysRegion> findRegionByLevel(RegionLevelEnum level);

    /**
     * 功能描述:通用查询接口
     * @auther: zhanglingke
     * @date: 2018-08-02 17:09
     * @param: SysRegion
     * @return: 地理区域列表
     */
    List<SysRegion> findRegionList(SysRegion region);

    /**
     * 功能描述:查询指定地理区域列表
     * @auther: zhanglingke
     * @date: 2018-08-02 17:09
     * @param: 地理区域编号数组
     * @return: 地理区域列表
     */
    List<SysRegion> findRegionByCodes(List<SysRegionCodeCondition> regionCodes);
}
