package com.winhxd.b2c.store.service;

import com.winhxd.b2c.common.domain.store.model.CustomerBrowseLog;

import java.util.Date;

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
    CustomerBrowseLog getIdForLoginOut(Long storeId, Long customerId);

    /**
     * 退出修改记录
     *
     * @param customerBrowseLog
     */
    void modifyByPrimaryKey(CustomerBrowseLog customerBrowseLog);

    /**
     * 获取某门店时间段内浏览人数
     *
     * @param storeCustomerId 门店用户编码
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    int getBrowseNum(Long storeCustomerId, Date beginDate, Date endDate);
}
