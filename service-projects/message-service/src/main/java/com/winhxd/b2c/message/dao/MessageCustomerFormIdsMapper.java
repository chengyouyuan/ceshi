package com.winhxd.b2c.message.dao;

import com.winhxd.b2c.common.domain.message.model.MessageCustomerFormIds;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageCustomerFormIdsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MessageCustomerFormIds record);

    int insertSelective(MessageCustomerFormIds record);

    MessageCustomerFormIds selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageCustomerFormIds record);

    int updateByPrimaryKey(MessageCustomerFormIds record);

    int insertFormIds(@Param("formIds") List<MessageCustomerFormIds> formIds);

    MessageCustomerFormIds getProd(String openid);
}