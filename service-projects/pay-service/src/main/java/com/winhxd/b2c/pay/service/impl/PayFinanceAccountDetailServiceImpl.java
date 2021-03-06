package com.winhxd.b2c.pay.service.impl;


import com.winhxd.b2c.common.domain.pay.model.PayFinanceAccountDetail;
import com.winhxd.b2c.pay.dao.PayFinanceAccountDetailMapper;
import com.winhxd.b2c.pay.service.PayFinanceAccountDetailService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        List<PayFinanceAccountDetail> payFinanceAccountDetails =  payFinanceAccountDetailMapper.selectByExample(condition);
        if(CollectionUtils.isEmpty(payFinanceAccountDetails)){
            return payFinanceAccountDetailMapper.insertSelective(condition);
        }else{
            condition.setId(payFinanceAccountDetails.get(0).getId());
            condition.setUpdated(new Date());
            return payFinanceAccountDetailMapper.updateByPrimaryKeySelective(condition);
        }
    }
}
