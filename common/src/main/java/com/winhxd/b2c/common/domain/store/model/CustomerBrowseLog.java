package com.winhxd.b2c.common.domain.store.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * C端用户访问门店记录
 *
 * @author liutong
 * @date 2018/8/7 14:34
 */
@ApiModel("C端用户访问门店记录")
@Data
public class CustomerBrowseLog {

    @ApiModelProperty("id主键")
    private Long id;

    @ApiModelProperty("C端用户浏览的门店编码")
    private Long storeId;

    @ApiModelProperty("C端用户的id")
    private Long customerId;

    @ApiModelProperty("进入时间（浏览开始时间）")
    private Date loginTime;

    @ApiModelProperty("退出时间（浏览结束时间）")
    private Date logoutTime;

}