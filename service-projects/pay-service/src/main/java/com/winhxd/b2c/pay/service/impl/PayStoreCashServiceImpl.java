package com.winhxd.b2c.pay.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreCashCondition;
import com.winhxd.b2c.common.domain.pay.enums.StatusEnums;
import com.winhxd.b2c.common.domain.pay.model.PayBankroll;
import com.winhxd.b2c.common.domain.pay.model.PayStoreTransactionRecord;
import com.winhxd.b2c.common.domain.pay.vo.PayStoreTransactionRecordVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankrollVO;
import com.winhxd.b2c.pay.dao.PayBankrollMapper;
import com.winhxd.b2c.pay.dao.PayStoreTransactionRecordMapper;
import com.winhxd.b2c.pay.dao.PayWithdrawalsMapper;
import com.winhxd.b2c.pay.service.PayStoreCashService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/14 17:04
 * @Description
 **/
@Service
public class PayStoreCashServiceImpl implements PayStoreCashService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayStoreCashServiceImpl.class);
      @Autowired
      private PayBankrollMapper payBankrollMapper;
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
    public StoreBankrollVO getStoreBankrollByStoreId(PayStoreCashCondition condition, Long storeId) {
        StoreBankrollVO vo = new StoreBankrollVO();

        //获取今日的收入总和
        BigDecimal totalPayRecordToday = payStoreTransactionRecordMapper.getTotalPayRecordToday(storeId);
        vo.setTotalMoneyToday(totalPayRecordToday == null ? BigDecimal.valueOf(0.00) : totalPayRecordToday);
        PayBankroll payBankroll = payBankrollMapper.selectStoreBankrollByStoreId(storeId);
        if (payBankroll != null) {
            vo.setId(payBankroll.getId());
            vo.setStoreId(payBankroll.getStoreId());
            vo.setTotalMoeny(payBankroll.getTotalMoeny() == null ? BigDecimal.valueOf(0.00) : payBankroll.getTotalMoeny());
            vo.setPresentedFrozenMoney(payBankroll.getPresentedFrozenMoney() == null ? BigDecimal.valueOf(0.00) : payBankroll.getPresentedFrozenMoney());
            vo.setSettlementSettledMoney(payBankroll.getSettlementSettledMoney() == null ? BigDecimal.valueOf(0.00) : payBankroll.getSettlementSettledMoney());
            vo.setPresentedMoney(payBankroll.getPresentedMoney() == null ? BigDecimal.valueOf(0.00) : payBankroll.getPresentedMoney());
            vo.setAlreadyPresentedMoney(vo.getPresentedFrozenMoney().add(payBankroll.getAlreadyPresentedMoney() == null ? BigDecimal.valueOf(0.00) : payBankroll.getAlreadyPresentedMoney()));
        } else {
            vo.setTotalMoeny(BigDecimal.valueOf(0.00));
            vo.setPresentedFrozenMoney(BigDecimal.valueOf(0.00));
            vo.setSettlementSettledMoney(BigDecimal.valueOf(0.00));
            vo.setPresentedMoney(BigDecimal.valueOf(0.00));
            vo.setAlreadyPresentedMoney(BigDecimal.valueOf(0.00));
        }
        LOGGER.info("门店金额提现首页信息"+vo.toString());
        return vo;
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
    public PagedList<PayStoreTransactionRecordVO> getPayStoreTransRecordByStoreId(PayStoreCashCondition condition, Long storeId) {
        PagedList<PayStoreTransactionRecordVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<PayStoreTransactionRecordVO> voList = payStoreTransactionRecordMapper.getPayStoreTransRecordByStoreId(storeId);
        PageInfo<PayStoreTransactionRecordVO> pageInfo = new PageInfo<>(voList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        LOGGER.info("提现明细列表结果"+pagedList);
        return pagedList;
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
    public PagedList<PayWithdrawalsVO> getPayWithdrawalsByStoreId(PayStoreCashCondition condition, Long storeId) {
        PagedList<PayWithdrawalsVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<PayWithdrawalsVO> voList = payWithdrawalsMapper.getPayWithdrawalsByStoreId(storeId);
        PageInfo<PayWithdrawalsVO> pageInfo = new PageInfo<>(voList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        LOGGER.info("提现明细列表结果"+pagedList);
        return pagedList;
    }


    /**
     *
     *@Deccription  保存门店交易记录
     *@Params payStoreTransactionRecord
     *@Return
     *@User  wl
     *@Date   2018/8/22 9:32
     */
	@Override
	public void savePayStoreTransactionRecord(PayStoreTransactionRecord payStoreTransactionRecord) {
		if (payStoreTransactionRecord!=null) {
			//根据门店订单号和交易类型  查询交易记录
			List<PayStoreTransactionRecord> records=payStoreTransactionRecordMapper.getPayStoreTransRecordByOrderNo(payStoreTransactionRecord.getOrderNo());
			if (CollectionUtils.isEmpty(records)) {
				payStoreTransactionRecord.setCreated(new Date());
				payStoreTransactionRecord.setStatus(StatusEnums.EFFECTIVE.getCode());
				payStoreTransactionRecordMapper.insertSelective(payStoreTransactionRecord);
            } else {
				PayStoreTransactionRecord record=records.get(0);
				record.setUpdated(new Date());
				payStoreTransactionRecordMapper.updateByPrimaryKeySelective(record);

            }
        }

    }


}
