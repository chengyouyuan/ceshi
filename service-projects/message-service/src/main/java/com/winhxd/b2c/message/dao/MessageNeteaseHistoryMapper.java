package com.winhxd.b2c.message.dao;

import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgBoxCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgReadStatusCondition;
import com.winhxd.b2c.common.domain.message.model.MessageNeteaseHistory;
import com.winhxd.b2c.common.domain.message.vo.NeteaseMsgVO;
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
	 * 修改消息已读状态
	 * @param condition
	 * @return
	 */
	int updateReadStatusByCondition(@Param("condition") NeteaseMsgReadStatusCondition condition);

	/**
	 * 查询用户消息盒子消息
	 *
	 * @return
	 */
	List<NeteaseMsgVO> selectVoByCondition(@Param("condition") NeteaseMsgBoxCondition condition);

	int insertHistories(List<MessageNeteaseHistory> list);
}