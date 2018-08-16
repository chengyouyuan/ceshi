package com.winhxd.b2c.message.dao;

import com.winhxd.b2c.common.domain.message.model.MessageNeteaseAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface MessageNeteaseAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageNeteaseAccount record);

    int insertSelective(MessageNeteaseAccount record);

    MessageNeteaseAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageNeteaseAccount record);

    int updateByPrimaryKey(MessageNeteaseAccount record);

    MessageNeteaseAccount getNeteaseAccountByCustomerId(Long customerId);

    MessageNeteaseAccount updateByCustomerId(@Param("customerId") Long customerId, @Param("accid") String accid, @Param("token") String token);
}