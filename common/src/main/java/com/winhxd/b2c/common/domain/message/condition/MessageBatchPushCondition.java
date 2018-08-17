package com.winhxd.b2c.common.domain.message.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author jujinbiao
 * @className MessageBatchPushCondition
 * @description
 */
@Data
@ApiModel(value = "手动推送消息请求参数",description = "后台消息管理-手动推送消息列表请求参数")
public class MessageBatchPushCondition extends PagedCondition {
}
