package com.winhxd.b2c.common.domain.customer.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author sunwenwu
 * @Description: 系统后台用户列表页vo
 * @date 2018年10月8日14:32:00
 */
@Data
@ApiModel(value = "推券用户信息导出VO", description = "推券用户信息导出VO")
public class CustomerUserInfoExportVO {

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long customerId;

    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    @Excel(name = "手机号", orderNum = "1", width = 30)
    private String customerMobile;


    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    @Excel(name = "用户名", orderNum = "2", width = 30)
    private String nickName;

    private static final long serialVersionUID = 1L;

}
