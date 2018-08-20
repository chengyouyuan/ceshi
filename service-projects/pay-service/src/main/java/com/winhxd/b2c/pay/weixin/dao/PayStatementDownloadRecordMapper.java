package com.winhxd.b2c.pay.weixin.dao;

import java.util.List;

import com.winhxd.b2c.common.domain.pay.model.PayStatementDownloadRecord;

public interface PayStatementDownloadRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PayStatementDownloadRecord record);

    int insertSelective(PayStatementDownloadRecord record);

    PayStatementDownloadRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PayStatementDownloadRecord record);

    int updateByPrimaryKey(PayStatementDownloadRecord record);

	/**
	 * @Description 根据model条件查询数据
	 * @author yuluyuan
	 * @date 2018年8月20日 下午2:16:15
	 * @param record
	 * @return
	 */
	List<PayStatementDownloadRecord> selectByModel(
			PayStatementDownloadRecord record);
}