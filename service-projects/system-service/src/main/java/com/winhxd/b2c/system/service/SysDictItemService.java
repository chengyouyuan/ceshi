package com.winhxd.b2c.system.service;

import com.winhxd.b2c.common.domain.system.dict.condition.AppVersionCondition;

/**
 * 字典项数据管理服务
 *
 * @author chenyanqi
 * @date 2018/10/16
 */
public interface SysDictItemService {

    /**
     * 根据字典项的值检查是否存在该字典项
     *
     * @param appVersionCondition
     * @return int
     * @author chenyanqi
     * @date 2018/10/16
     */
    Integer checkDictItem(AppVersionCondition appVersionCondition);

}
