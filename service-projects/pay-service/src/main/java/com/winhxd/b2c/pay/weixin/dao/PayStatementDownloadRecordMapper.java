package com.winhxd.b2c.pay.weixin.dao;

import com.winhxd.b2c.pay.weixin.model.PayStatementDownloadRecord;

public interface PayStatementDownloadRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStatementDownloadRecord record);

    int insertSelective(PayStatementDownloadRecord record);

    PayStatementDownloadRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStatementDownloadRecord record);

    int updateByPrimaryKey(PayStatementDownloadRecord record);
}