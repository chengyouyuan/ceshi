package com.winhxd.b2c.common.domain.message.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @ClassName: NeteaseMsgReadStatusCondition
 * @Description: TODO
 * @Author fanzhanzhan
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

	/**
	 * 已读信息是
	 */
	@ApiModelProperty("设置已读消息是当天还是历史，0：当天，1：历史")
	private Short timeType;

	@ApiModelProperty(hidden = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String startTime;

	@ApiModelProperty(hidden = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String endTime;
}
