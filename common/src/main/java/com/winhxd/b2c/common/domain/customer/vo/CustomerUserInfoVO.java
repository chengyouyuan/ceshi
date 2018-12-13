package com.winhxd.b2c.common.domain.customer.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author chengyy
 * @Description: 系统后台用户列表页vo
 * @date 2018/8/4 17:29
 */
@Data
@ApiModel(value = "用户信息VO", description = "后台用户列表用户信息VO")
public class CustomerUserInfoVO {


    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    @Excel(name = "用户名", orderNum = "1", width = 30)
    private String nickName;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long customerId;

    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    @Excel(name = "手机号", orderNum = "2", width = 30)
    private String customerMobile;

    /**
     * 微信openId
     */
    @ApiModelProperty("微信openId")
    @Excel(name = "微信openId", orderNum = "3", width = 30)
    private String openid;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户图像")
    private String headImg;

    /**
     * 用户创建日期
     */
    private Date created;

    @ApiModelProperty("门店名称")
    @Excel(name = "绑定门店", orderNum = "4", width = 30)
    private String storeName;

    /**
     * 用户状态,默认有效1，无效(黑名单)0,默认是有效
     */
    @ApiModelProperty("用户状态,默认有效1，无效(黑名单)0,默认是有效")
    @Excel(name = "用户状态", orderNum = "5", width = 30, replace = {"有效_1", "无效_0"})
    private Integer status = 1;

    @ApiModelProperty("名店id")
    private Long storeId;

    private static final long serialVersionUID = 1L;

}
