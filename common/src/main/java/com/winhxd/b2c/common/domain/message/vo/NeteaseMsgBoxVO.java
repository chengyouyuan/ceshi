<<<<<<< HEAD
package com.winhxd.b2c.common.domain.message.vo;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jujinbiao
 * @className NeteaseMsgBoxVO
 * @description 云信用户消息历史
 */
@ApiModel("云信用户消息历史")
@Data
public class NeteaseMsgBoxVO extends BaseCondition {

    @ApiModelProperty("当前页")
    private int curpage;
    @ApiModelProperty("总页数")
    private int totalpage;
    @ApiModelProperty("数据库总条数")
    private int totalcount;
    @ApiModelProperty("消息盒子List")
    private List<NeteaseMsgVO> items;

}
=======
package com.winhxd.b2c.common.domain.message.vo;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jujinbiao
 * @className NeteaseMsgBoxVO
 * @description 云信用户消息历史
 */
@ApiModel("云信用户消息历史")
@Data
public class NeteaseMsgBoxVO extends PagedCondition {

    @ApiModelProperty("当前页")
    private int curpage;
    @ApiModelProperty("总页数")
    private int totalpage;
    @ApiModelProperty("数据库总条数")
    private int totalcount;
    @ApiModelProperty("消息盒子List")
    private List<NeteaseMsgVO> items;

}
>>>>>>> branch 'master' of git@192.168.1.101:retail2c/retail2c-backend.git
