package com.winhxd.b2c.pay.weixin.dao;

import java.util.List;

import com.winhxd.b2c.common.domain.pay.model.PayStatementDownloadRecord;

/**
 * @author yuluyuan
 */
public interface PayStatementDownloadRecordMapper {
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
    int insert(PayStatementDownloadRecord record);

	/**
	 * 插入有值的字段
	 * @param record
	 * @return
	 */
    int insertSelective(PayStatementDownloadRecord record);

	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
    PayStatementDownloadRecord selectByPrimaryKey(Long id);

	/**
	 * 根据主键更新有值的字段
	 * @param record
	 * @return
	 */
    int updateByPrimaryKeySelective(PayStatementDownloadRecord record);

	/**
	 * 根据主键更新
	 * @param record
	 * @return
	 */
    int updateByPrimaryKey(PayStatementDownloadRecord record);

	/**
	 * 根据model条件查询数据
	 * @Description 根据model条件查询数据
	 * @author yuluyuan
	 * @date 2018年8月20日 下午2:16:15
	 * @param record
	 * @return
	 */
	List<PayStatementDownloadRecord> selectByModel(
			PayStatementDownloadRecord record);
}