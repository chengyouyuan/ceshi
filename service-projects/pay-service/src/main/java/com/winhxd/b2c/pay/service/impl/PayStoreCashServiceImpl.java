package com.winhxd.b2c.pay.service.impl;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreCashCondition;
import com.winhxd.b2c.common.domain.pay.model.StoreBankroll;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankrollVO;
import com.winhxd.b2c.pay.dao.StoreBankrollMapper;
import com.winhxd.b2c.pay.service.PayStoreCashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author liangliang
 * @Date 2018/8/14 17:04
 * @Description
 **/
@Service
public class PayStoreCashServiceImpl implements PayStoreCashService {
      @Autowired
      private StoreBankrollMapper storeBankrollMapper;

    @Override
    public ResponseResult<StoreBankrollVO> getStoreBankrollByStoreId(PayStoreCashCondition condition) {
        ResponseResult<StoreBankrollVO> result = new ResponseResult<StoreBankrollVO>();
        StoreBankrollVO vo = new StoreBankrollVO();
        Long storeId = condition.getStoreId();
        StoreBankroll storeBankroll = storeBankrollMapper.selectStoreBankrollByStoreId(storeId);
        if(storeBankroll != null){
            vo.setId(storeBankroll.getId());
            vo.setStoreId(storeBankroll.getStoreId());
            vo.setTotalMoeny(storeBankroll.getTotalMoeny());
            vo.setPresentedFrozenMoney(storeBankroll.getPresentedFrozenMoney());
            vo.setSettlementSettledMoney(storeBankroll.getSettlementSettledMoney());
        }
        result.setData(vo);
        return null;
    }
}
