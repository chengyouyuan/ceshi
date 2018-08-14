package com.winhxd.b2c.pay.service;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreCashCondition;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankrollVO;

/**
 * @Author wl
 * @Date 2018/8/14 17:03
 * @Description
 **/
public interface PayStoreCashService {
    ResponseResult<StoreBankrollVO> getStoreBankrollByStoreId(PayStoreCashCondition condition);
}
