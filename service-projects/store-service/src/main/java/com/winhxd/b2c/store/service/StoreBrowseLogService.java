package com.winhxd.b2c.store.service;

import com.winhxd.b2c.common.domain.store.model.CustomerBrowseLog;

/**
 * C端用户浏览门店记录日志
 *
 * @author liutong
 * @date 2018-8-7 18:45:37
 */
public interface StoreBrowseLogService {

    /**
     * 新增进入记录
     *
     * @param customerBrowseLog
     */
    void saveBrowseLogLogin(CustomerBrowseLog customerBrowseLog);

    /**
     * 查询最近记录id
     *
     * @param storeId
     * @param customerId
     * @return
     */
    Long getIdForLoginOut(Long storeId, Long customerId);

    /**
     * 退出修改记录
     *
     * @param customerBrowseLog
     */
    void modifyBrowseLogLogout(CustomerBrowseLog customerBrowseLog);
}
