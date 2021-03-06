package com.winhxd.b2c.common.domain.system.login.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 后台用户列表页请求参数
 * @author chengyy
 * @date 2018/8/4 17:56
 */
@Data
@ApiModel(value = "用户请求参数",description = "后台用户列表请求参数")
public class BackStageCustomerInfoCondition extends PagedCondition {
    /**用户id*/
    private Long customerId;
    /**用户账号*/
    @ApiModelProperty("用户账号")
    private String customerMobile;

    /**微信openId*/
    @ApiModelProperty("微信openId")
    private String openid;

    /**昵称*/
    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("门店id")
    private Long storeId;

    /**用户状态,默认有效true，无效(黑名单)false,默认是有效*/
    @ApiModelProperty("用户状态,默认有效1，无效(黑名单)0,默认是有效")
    private Integer status;

    @ApiModelProperty("是否绑定门店，1是，0否")
    private Integer bindStatus;

    @ApiModelProperty("是否查询全部数据")
    private Boolean isQueryAll = false;

}
