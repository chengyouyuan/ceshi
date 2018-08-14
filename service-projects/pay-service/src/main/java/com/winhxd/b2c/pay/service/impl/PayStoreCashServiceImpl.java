package com.winhxd.b2c.pay.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreCashCondition;
import com.winhxd.b2c.common.domain.pay.model.StoreBankroll;
import com.winhxd.b2c.common.domain.pay.vo.PayStoreTransactionRecordVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankrollVO;
import com.winhxd.b2c.pay.dao.PayStoreTransactionRecordMapper;
import com.winhxd.b2c.pay.dao.PayWithdrawalsMapper;
import com.winhxd.b2c.pay.dao.StoreBankrollMapper;
import com.winhxd.b2c.pay.service.PayStoreCashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/14 17:04
 * @Description
 **/
@Service
public class PayStoreCashServiceImpl implements PayStoreCashService {
      @Autowired
      private StoreBankrollMapper storeBankrollMapper;
      @Autowired
      private PayStoreTransactionRecordMapper payStoreTransactionRecordMapper;
      @Autowired
      private PayWithdrawalsMapper payWithdrawalsMapper;

    /**
     *
     *@Deccription 门店金额提现首页信息
     *@Params condition
     *@Return  ResponseResult<StoreBankrollVO>
     *@User  wl
     *@Date   2018/8/14 20:30
     */
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
        return result;
    }

    
    /**
     *
     *@Deccription 获取门店收支明细
     *@Params  condition
     *@Return  ResponseResult<PagedList<PayStoreTransactionRecordVO>> 分页列表
     *@User  wl
     *@Date   2018/8/14 20:31
     */
    @Override
    public ResponseResult<PagedList<PayStoreTransactionRecordVO>> getPayStoreTransRecordByStoreId(PayStoreCashCondition condition) {
        ResponseResult<PagedList<PayStoreTransactionRecordVO>> result = new ResponseResult<PagedList<PayStoreTransactionRecordVO>>();
        PagedList<PayStoreTransactionRecordVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<PayStoreTransactionRecordVO> voList = payStoreTransactionRecordMapper.getPayStoreTransRecordByStoreId(condition.getStoreId());
        PageInfo<PayStoreTransactionRecordVO> pageInfo = new PageInfo<>(voList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }

    /**
     *
     *@Deccription 提现明细
     *@Params  condition
     *@Return  ResponseResult<PagedList<PayWithdrawalsVO>>  分页列表
     *@User  wl
     *@Date   2018/8/14 20:31
     */
    @Override
    public ResponseResult<PagedList<PayWithdrawalsVO>> getPayWithdrawalsByStoreId(PayStoreCashCondition condition) {
        ResponseResult<PagedList<PayWithdrawalsVO>> result = new ResponseResult<PagedList<PayWithdrawalsVO>>();
        PagedList<PayWithdrawalsVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<PayWithdrawalsVO> voList = payWithdrawalsMapper.getPayWithdrawalsByStoreId(condition.getStoreId());
        PageInfo<PayWithdrawalsVO> pageInfo = new PageInfo<>(voList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }


}
