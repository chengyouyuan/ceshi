package com.winhxd.b2c.message.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgBoxCondition;
import com.winhxd.b2c.common.domain.message.model.MessageNeteaseHistory;
import com.winhxd.b2c.common.domain.message.vo.NeteaseMsgVO;
import com.winhxd.b2c.common.domain.store.vo.StoreSubmitProductVO;
import feign.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageNeteaseHistoryMapper {
	int deleteByPrimaryKey(Long id);

	int insert(MessageNeteaseHistory record);

	int insertSelective(MessageNeteaseHistory record);

	MessageNeteaseHistory selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(MessageNeteaseHistory record);

	int updateByPrimaryKey(MessageNeteaseHistory record);

	/**
	 * 查询用户消息盒子消息
	 *
	 * @param accid     用户云信ID
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return
	 */
	List<NeteaseMsgVO> selectVoByCondition(@Param("accid") String accid, @Param("startTime") String startTime, @Param("endTime") String endTime);
}