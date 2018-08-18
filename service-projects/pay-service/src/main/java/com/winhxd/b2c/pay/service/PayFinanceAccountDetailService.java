package com.winhxd.b2c.pay.service;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/18 11 41
 * @Description
 */
public interface PayFinanceAccountDetailService {
    /**
     * @author wangxiaoshun
     * @date  2018年8月18日
     * @Description 保存出账明细
     * @param condition
     */
    int saveFinanceAccountDetail(com.winhxd.b2c.common.domain.pay.model.PayFinanceAccountDetail condition);
}
