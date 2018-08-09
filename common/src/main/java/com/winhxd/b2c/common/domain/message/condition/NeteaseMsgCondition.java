package com.winhxd.b2c.common.domain.message.condition;

import lombok.Data;

import java.util.List;

/**
 * @author jujinbiao
 * @className NeteaseMsgCondition
 * @description
 */
@Data
public class NeteaseMsgCondition {
    /**
     * 多个B端用户id
     */
    private List<Long> customerIds;
    /**
     * B端用户id
     */
    private Long  customerId;
    /**
     * 云信消息
     */
    private NeteaseMsg neteaseMsg ;
}
