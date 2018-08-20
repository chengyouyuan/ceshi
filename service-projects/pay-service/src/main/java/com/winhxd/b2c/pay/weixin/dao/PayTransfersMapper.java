package com.winhxd.b2c.pay.weixin.dao;

import com.winhxd.b2c.pay.weixin.model.PayTransfers;
import org.apache.ibatis.annotations.Param;

public interface PayTransfersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayTransfers record);

    int insertSelective(PayTransfers record);

    PayTransfers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayTransfers record);

    int updateByPrimaryKey(PayTransfers record);

    /**
     * 根据提现流水号获得提现记录
     * @param partnerTradeNo 提现流水号
     * @return 记录
     */
    PayTransfers selectTOP1TransfersByPartnerTradeNo(@Param(value = "partnerTradeNo") String partnerTradeNo);

}