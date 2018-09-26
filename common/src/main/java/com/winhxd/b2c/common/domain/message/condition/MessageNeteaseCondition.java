package com.winhxd.b2c.common.domain.message.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: Louis
 * @Date: 2018/9/26 10:17
 * @Description:  云信消息查询条件
 */
@Data
public class MessageNeteaseCondition extends ApiCondition {

    @ApiModelProperty("门店id")
    private Long storeId;

    @ApiModelProperty(hidden = true)
    private String accid;
    /**
     * 全部已读
     */
    @ApiModelProperty("消息已读状态0:未读，1：已读")
    private Short readStatus;
}
