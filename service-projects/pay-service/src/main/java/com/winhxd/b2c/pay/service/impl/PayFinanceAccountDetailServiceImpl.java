package com.winhxd.b2c.pay.service.impl;


import com.winhxd.b2c.common.domain.pay.model.PayFinanceAccountDetail;
import com.winhxd.b2c.pay.dao.PayFinanceAccountDetailMapper;
import com.winhxd.b2c.pay.service.PayFinanceAccountDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/18 11 41
 * @Description
 */
@Service
public class PayFinanceAccountDetailServiceImpl implements PayFinanceAccountDetailService {
    @Autowired
    PayFinanceAccountDetailMapper payFinanceAccountDetailMapper;
    @Override
    public int saveFinanceAccountDetail(PayFinanceAccountDetail condition) {
        return payFinanceAccountDetailMapper.insertSelective(condition);
    }
}
