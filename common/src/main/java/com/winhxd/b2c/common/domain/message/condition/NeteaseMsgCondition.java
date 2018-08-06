package com.winhxd.b2c.common.domain.message.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jujinbiao
 * @className NeteaseHistoryCondition
 * @description 云信用户消息历史记录
 */
@ApiModel("云信用户消息历史记录")
@Data
public class NeteaseMsgCondition extends BaseCondition {
    @ApiModelProperty("B端用户主键")
    private Long customerId;
    @ApiModelProperty("用户登录的手机号")
    private String usr;
    @ApiModelProperty("模糊搜索关键字，目前只是对消息内容进行过滤")
    private String keyword;
    @ApiModelProperty("当前总条数，最小值是1.可以不传，不传按照第一页处理")
    private int curpage;
    @ApiModelProperty("手机端当前总条数，如果curpage不是1，则获取这些totalcount中的分页数据，需要从数据库中查询到这些旧数据分页 .")
    private int totalcount;

}