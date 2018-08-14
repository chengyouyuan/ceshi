package com.winhxd.b2c.pay.weixin.dao;

import com.winhxd.b2c.pay.weixin.model.PayTransfers;

public interface PayTransfersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayTransfers record);

    int insertSelective(PayTransfers record);

    PayTransfers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayTransfers record);

    int updateByPrimaryKey(PayTransfers record);
}