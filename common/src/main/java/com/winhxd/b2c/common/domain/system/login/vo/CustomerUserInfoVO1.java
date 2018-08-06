package com.winhxd.b2c.common.domain.system.login.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 系统后台用户列表页vo
 * @author chengyy
 * @date 2018/8/4 17:29
 */
@Data
@ApiModel(value = "用户信息VO",description = "后台用户列表用户信息VO")
public class CustomerUserInfoVO1 {

    /**用户id*/
    @ApiModelProperty("用户id")
    @Excel(name = "用户id", orderNum = "1",width=30)
    private Long customerId;

    /**用户账号*/
    @ApiModelProperty("用户账号")
    @Excel(name = "手机号码", orderNum = "2",width=30)
    private String customerMobile;

    /**微信openId*/
    @ApiModelProperty("微信openId")
    @Excel(name = "微信openId", orderNum = "3",width=30)
    private String openId;

    /**昵称*/
    @ApiModelProperty("昵称")
    @Excel(name = "用户昵称", orderNum = "5",width=30)
    private String nickName;

    @ApiModelProperty("门店名称")
    @Excel(name = "门店名称", orderNum = "4",width=30)
    private String storeName;

    /**用户状态,默认有效1，无效(黑名单)0,默认是有效*/
    @ApiModelProperty("用户状态,默认有效1，无效(黑名单)0,默认是有效")
    @Excel(name = "用户状态", orderNum = "6",width=30, replace ={"有效_1","无效_0"})
    private Integer status = 1;

    private static final long serialVersionUID = 1L;

}
