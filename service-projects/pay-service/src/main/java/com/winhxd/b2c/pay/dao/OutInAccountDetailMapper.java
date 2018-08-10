package com.winhxd.b2c.pay.dao;

import com.winhxd.b2c.common.domain.pay.model.OutInAccountDetail;

public interface OutInAccountDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OutInAccountDetail record);

    int insertSelective(OutInAccountDetail record);

    OutInAccountDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OutInAccountDetail record);

    int updateByPrimaryKey(OutInAccountDetail record);
}