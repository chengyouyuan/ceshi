package com.winhxd.b2c.common.domain.message.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: NeteaseMsgReadStatusCondition
 * @Description: TODO
 * @Author Jesse Fan
 * @Date 2018-08-15 19:51
 **/
@Data
public class NeteaseMsgReadStatusCondition extends ApiCondition {

	@ApiModelProperty(hidden = true)
	private String accid;
	/**
	 * 多个消息 msgId
	 */
	@ApiModelProperty("消息ID列表")
	private List<Long> msgIds;

	/**
	 * 全部已读
	 */
	@ApiModelProperty("全部已读 1-全部已读 0-不是全部已读")
	private Short allRead;
}
