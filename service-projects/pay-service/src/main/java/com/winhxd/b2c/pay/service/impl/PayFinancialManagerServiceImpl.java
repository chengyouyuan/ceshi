package com.winhxd.b2c.pay.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.pay.condition.OrderInfoFinancialInDetailCondition;
import com.winhxd.b2c.common.domain.pay.condition.OrderInfoFinancialOutDetailCondition;
import com.winhxd.b2c.common.domain.pay.enums.PayDataStatusEnum;
import com.winhxd.b2c.common.domain.pay.enums.PayFinanceTypeEnum;
import com.winhxd.b2c.common.domain.pay.enums.PayOutTypeEnum;
import com.winhxd.b2c.common.domain.pay.model.PayFinancialSummary;
import com.winhxd.b2c.common.domain.pay.vo.OrderInfoFinancialInDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderInfoFinancialOutDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.dao.PayFinanceAccountDetailMapper;
import com.winhxd.b2c.pay.service.PayFinancialManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PayFinancialManagerServiceImpl implements PayFinancialManagerService{
	private static final Logger LOGGER = LoggerFactory.getLogger(PayFinancialManagerServiceImpl.class);
	@Autowired
	private PayFinanceAccountDetailMapper payFinanceAccountDetailMapper;

	@Override
	public PayFinanceAccountDetailVO findFinanceAccountDetail(Long storeId) {
		PayFinanceAccountDetailVO financeDetial = new PayFinanceAccountDetailVO();
		//查询总出账和总入账金额以及总手续费
		financeDetial = queryTotalMoney(financeDetial,storeId);
		//查询总入账 中今日预入账今日实入账以及今日入账
		financeDetial = queryTodayMoney(financeDetial,storeId);
		// 查询总出账中今日提款 以及今日退款以及今日出账以及今日提现手续费
		financeDetial = queryTodayOutMoney(financeDetial,storeId);
		//计算优惠券抵用总金额 以及今日抵用金额
		financeDetial = queryCouponUsedMoney(financeDetial,storeId);
		financeDetial = queryCouponTodayUsedMoney(financeDetial,storeId);
		// TODO 公司补充总入账待定
		//TODO 计算余额   ---->当前余额=总进账金额+公司总入账金额 - 总出账金额
		if(financeDetial.getAllInMoney() == null){
			financeDetial.setAllInMoney((BigDecimal.valueOf(0)));
		} 
		if(financeDetial.getAllOutMoney() == null){
			financeDetial.setAllOutMoney((BigDecimal.valueOf(0)));
		} 
		financeDetial.setCurLeftMoney(financeDetial.getAllInMoney().subtract(financeDetial.getAllOutMoney()));
		return financeDetial;
	}

	@Override
	public PayFinanceAccountDetailVO findStoreFinancialSummary() {
		BigDecimal todayRealFee = BigDecimal.valueOf(0);
		BigDecimal todayCmmsAmt = BigDecimal.valueOf(0);
		BigDecimal sumRealfee = BigDecimal.valueOf(0);
		BigDecimal sumCmmsAmt = BigDecimal.valueOf(0);
		BigDecimal todayRefundAmout = BigDecimal.valueOf(0);
		BigDecimal sumRefundAmout = BigDecimal.valueOf(0);
		PayFinancialSummary todayWithdrawals = payFinanceAccountDetailMapper.getWithdrawals("today");
		if (todayWithdrawals != null){
			todayRealFee = todayWithdrawals.getRealFee();
			todayCmmsAmt = todayWithdrawals.getCmmsAmt();
		}
		PayFinancialSummary sumWithdrawals = payFinanceAccountDetailMapper.getWithdrawals("total");
		if(sumWithdrawals != null){
			sumRealfee = sumWithdrawals.getRealFee();
			sumCmmsAmt = sumWithdrawals.getCmmsAmt();
		}
		PayFinancialSummary todayRefund = payFinanceAccountDetailMapper.getRefund("today");
		if(todayRefund != null){
			todayRefundAmout = todayRefund.getRefundAmout();
		}
		PayFinancialSummary sumRefund = payFinanceAccountDetailMapper.getRefund("total");
		if(sumRefund != null){
			sumRefundAmout = sumRefund.getRefundAmout();
		}
		BigDecimal todayOutMoney = todayRealFee.add(todayRefundAmout);
		BigDecimal allOutMoney = sumRealfee.add(sumRefundAmout);

		PayFinanceAccountDetailVO payFinanceAccountDetailVO = new PayFinanceAccountDetailVO();
		//总出账
		payFinanceAccountDetailVO.setTodayStoreWithdraw(todayRealFee);
		payFinanceAccountDetailVO.setTodayCharge(todayCmmsAmt);
		payFinanceAccountDetailVO.setTodayCustomerRefund(todayRefundAmout);
		payFinanceAccountDetailVO.setTodayOutMoney(todayOutMoney);
		payFinanceAccountDetailVO.setAllOutMoney(allOutMoney);
		payFinanceAccountDetailVO.setAllCharge(sumCmmsAmt);
		//总进账
		BigDecimal todayPreMoney = payFinanceAccountDetailMapper.getIncome("todayPreMoney");
		if(todayPreMoney == null){
			todayPreMoney = BigDecimal.valueOf(0);
		}
		BigDecimal todayRealMoney = payFinanceAccountDetailMapper.getIncome("todayRealMoney");
		if(todayRealMoney == null){
			todayRealMoney = BigDecimal.valueOf(0);
		}
		BigDecimal allInMoney = payFinanceAccountDetailMapper.getIncome("allInMoney");
		if(allInMoney == null){
			allInMoney = BigDecimal.valueOf(0);
		}
		payFinanceAccountDetailVO.setTodayPreMoney(todayPreMoney);
		payFinanceAccountDetailVO.setTodayRealMoney(todayRealMoney);
		payFinanceAccountDetailVO.setTodayInMoney(todayPreMoney.add(todayRealMoney));
		payFinanceAccountDetailVO.setAllInMoney(allInMoney);
		//优惠券抵用金额
		BigDecimal useTodayCouponAllMoney = payFinanceAccountDetailMapper.getIncome("useTodayCouponAllMoney");
		if(useTodayCouponAllMoney == null){
			useTodayCouponAllMoney = BigDecimal.valueOf(0);
		}
		BigDecimal useCouponAllMoney = payFinanceAccountDetailMapper.getIncome("useCouponAllMoney");
		if(useCouponAllMoney == null){
			useCouponAllMoney = BigDecimal.valueOf(0);
		}
		payFinanceAccountDetailVO.setUseTodayCouponAllMoney(useTodayCouponAllMoney);
		payFinanceAccountDetailVO.setUseCouponAllMoney(useCouponAllMoney);
		//公司补充总入账
		BigDecimal companySupplementInMoney = payFinanceAccountDetailMapper.getCompanySupplementInMoney();
		if(companySupplementInMoney == null){
			companySupplementInMoney = BigDecimal.valueOf(0);
		}
		payFinanceAccountDetailVO.setCompanySupplementInMoney(companySupplementInMoney);
		//营收金额
		BigDecimal revenueMoney = payFinanceAccountDetailMapper.getRevenueMoney();
		if(revenueMoney == null){
			revenueMoney = BigDecimal.valueOf(0);
		}
		payFinanceAccountDetailVO.setRevenueMoney(revenueMoney);
		//当前余额=总进账金额+公司总入账金额 - 总出账金额，curLeftMoney
		BigDecimal curLeftMoney = allInMoney.add(payFinanceAccountDetailVO.getCompanySupplementInMoney()).subtract(payFinanceAccountDetailVO.getAllOutMoney());
		payFinanceAccountDetailVO.setCurLeftMoney(curLeftMoney);
		return payFinanceAccountDetailVO;
	}

	//查询总出账和总入账金额以及总手续费
	public PayFinanceAccountDetailVO queryTotalMoney(PayFinanceAccountDetailVO financeDetial,Long storeId){
		List<PayFinanceAccountDetailVO> toalInoutMoney = payFinanceAccountDetailMapper.selectTotalInOutMoney(storeId);
		if(toalInoutMoney.size() != 0){
			if(toalInoutMoney.size() > 2 || toalInoutMoney.size() < 0){
				LOGGER.info("查询总出账和总入账金额返回的数据条数有误:queryTotalMoney---");
				throw new BusinessException(BusinessCode.CODE_610021);
			}else{
				for(PayFinanceAccountDetailVO detail:toalInoutMoney){
					short type = detail.getType();
					short inCode = PayFinanceTypeEnum.INMONEY.getStatusCode();//进账
					short outCode = PayFinanceTypeEnum.OUTMONEY.getStatusCode();//出账
					if(inCode == type){
						financeDetial.setAllInMoney(detail.getAllInMoney());
					}else if(outCode == type){
						financeDetial.setAllOutMoney(detail.getAllInMoney());
						// 总出账手续费
						financeDetial.setAllCharge(detail.getAllCharge());
					}
				}
				LOGGER.info("查询总出账和总入账金额以及总手续费：----"+financeDetial);
			}
		}else{
			LOGGER.info("当前无数据");
		}
		return financeDetial;
	}
	
	//查询今日预入账今日实入账以及今日入账
	public PayFinanceAccountDetailVO queryTodayMoney(PayFinanceAccountDetailVO financeDetial,Long storeId){
		List<PayFinanceAccountDetailVO> todayInMoney = payFinanceAccountDetailMapper.selectTodayInMoney(storeId);
		if(todayInMoney.size() != 0){
			if(todayInMoney.size() > 2 || todayInMoney.size() < 0){
				LOGGER.info("查询今日预入账今日实入账数据条数有误：queryTodayMoney---");
				throw new BusinessException(BusinessCode.CODE_610021);
			}else{
				for(PayFinanceAccountDetailVO detail:todayInMoney){
					short dataStatus = detail.getDataStatus();
					short preStatus = PayDataStatusEnum.PRE_INCOUNT.getStatusCode();
					short realStatus = PayDataStatusEnum.REAL_INCOUNT.getStatusCode();
					if(preStatus == dataStatus){
						financeDetial.setTodayPreMoney(detail.getTodayInMoney());
					}else if(realStatus == dataStatus){
						financeDetial.setTodayRealMoney(detail.getTodayInMoney());
					}
				}
				// 计算今日进账
				financeDetial.setTodayInMoney(financeDetial.getTodayPreMoney().add(financeDetial.getTodayRealMoney()));	
				LOGGER.info("今日进账计算结果：----"+financeDetial);
			}
		}else{
			LOGGER.info("当前无数据");
		}
		return financeDetial;
	}
	
	//查询总出账中今日提款 以及今日退款以及今日出账
	public PayFinanceAccountDetailVO queryTodayOutMoney(PayFinanceAccountDetailVO financeDetial,Long storeId){
		List<PayFinanceAccountDetailVO> todayOutMoney = payFinanceAccountDetailMapper.selectTodayOutMoney(storeId);
		if(todayOutMoney.size() != 0){
			if(todayOutMoney.size() > 2 || todayOutMoney.size() < 0){
				LOGGER.info("查询总出账中今日提款 以及今日退款以及今日出账数据条数有误：queryTodayOutMoney---");
				throw new BusinessException(BusinessCode.CODE_610021);
			}else{
				for(PayFinanceAccountDetailVO detail:todayOutMoney){
					 short outType = detail.getOutType();
					 short cusRefundCode = PayOutTypeEnum.CUSTOMER_REFUND.getStatusCode();
					 short storeWithdrawCode = PayOutTypeEnum.STORE_WITHDRAW.getStatusCode();
					if(outType == cusRefundCode){
						financeDetial.setTodayCustomerRefund(detail.getTodayOutMoney());
					}else if(outType == storeWithdrawCode){
						financeDetial.setTodayStoreWithdraw(detail.getTodayOutMoney());
						// 今日提现手续费
						financeDetial.setTodayCharge(detail.getTodayCharge());
					}
				}
				// 计算今日出账
				financeDetial.setTodayOutMoney(financeDetial.getTodayCustomerRefund().add(financeDetial.getTodayStoreWithdraw()));	
				LOGGER.info("今日出账计算结果：----"+financeDetial);
			}
		}else{
			LOGGER.info("当前无数据");
		}
		return financeDetial;
	}
	
	//计算优惠券抵用总金额 
	public PayFinanceAccountDetailVO queryCouponUsedMoney(PayFinanceAccountDetailVO financeDetial,Long storeId){
		PayFinanceAccountDetailVO couponUsedMoney = payFinanceAccountDetailMapper.selectCouponUsedMoney(storeId);
		if(couponUsedMoney != null){
			financeDetial.setUseCouponAllMoney(couponUsedMoney.getUseCouponAllMoney());
			LOGGER.info("计算优惠券抵用总金额：----"+financeDetial);
		}else{
			LOGGER.info("计算优惠券抵用总金额数据有误：queryCouponUsedMoney---");
			throw new BusinessException(BusinessCode.CODE_610021);
		}
		return financeDetial;
	}
	//今日优惠券抵用金额
	public PayFinanceAccountDetailVO queryCouponTodayUsedMoney(PayFinanceAccountDetailVO financeDetial,Long storeId){
		PayFinanceAccountDetailVO todayTodayCouponUsedMoney = payFinanceAccountDetailMapper.selectTodayCouponUsedMoney(storeId);
		if(todayTodayCouponUsedMoney != null){
			financeDetial.setUseTodayCouponAllMoney(todayTodayCouponUsedMoney.getUseTodayCouponAllMoney());
			LOGGER.info("今日优惠券抵用金额：----"+financeDetial);
		}else{
			LOGGER.info("今日优惠券抵用金额数据有误：queryCouponTodayUsedMoney---");
			throw new BusinessException(BusinessCode.CODE_610021);
		}
		return financeDetial;
	}

	@Override
	public PagedList<OrderInfoFinancialInDetailVO> queryFinancialInDetail(
			OrderInfoFinancialInDetailCondition condition) {
		Page<OrderInfoFinancialInDetailVO> page = PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
		PagedList<OrderInfoFinancialInDetailVO> pagedList = new PagedList<OrderInfoFinancialInDetailVO>();
		List<OrderInfoFinancialInDetailVO> financialInDetail = payFinanceAccountDetailMapper.selectFinancialInDetail(condition);
		LOGGER.info("findFinancialOutDetail:===="+financialInDetail);
		pagedList.setData(financialInDetail);
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
		return pagedList;
	}

	@Override
	public PagedList<OrderInfoFinancialOutDetailVO> queryFinancialOutDetail(
			OrderInfoFinancialOutDetailCondition condition) {
		Page<OrderInfoFinancialOutDetailVO> page = PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
		PagedList<OrderInfoFinancialOutDetailVO> pagedList = new PagedList<OrderInfoFinancialOutDetailVO>();
		List<OrderInfoFinancialOutDetailVO> financialOutDetail = payFinanceAccountDetailMapper.selectFinancialOutDetail(condition);
		LOGGER.info("findFinancialOutDetail:===="+financialOutDetail);
        pagedList.setData(financialOutDetail);
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
		return pagedList;
	}
}
