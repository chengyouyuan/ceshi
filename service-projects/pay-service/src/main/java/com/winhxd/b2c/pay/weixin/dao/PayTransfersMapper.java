package com.winhxd.b2c.pay.weixin.dao;

import com.winhxd.b2c.pay.weixin.model.PayTransfers;
import org.apache.ibatis.annotations.Param;

/**
 * @author yindanqing
 */
public interface PayTransfersMapper {
    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入全部
     * @param record
     * @return
     */
    int insert(PayTransfers record);

    /**
     * 插入有值的字段
     * @param record
     * @return
     */
    int insertSelective(PayTransfers record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    PayTransfers selectByPrimaryKey(Long id);

    /**
     * 根据主键更新有值的字段
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PayTransfers record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(PayTransfers record);

    /**
     * 根据提现流水号获得提现记录
     * @param partnerTradeNo 提现流水号
     * @return 记录
     */
    PayTransfers selectTOP1TransfersByPartnerTradeNo(@Param(value = "partnerTradeNo") String partnerTradeNo);

}