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

    @ApiModelProperty("用户浏览的门店编码(store_user_info表的store_customer_id)")
    private Long storeCustomerId;

    @ApiModelProperty("C端用户的id(customer_user_info表的customer_id)")
    private Long customerId;

    @ApiModelProperty("进入时间（浏览开始时间）")
    private Date loginDatetime;

    @ApiModelProperty("退出时间（浏览结束时间）")
    private Date logoutDatetime;

    @ApiModelProperty("浏览时长,毫秒值")
    private Long stayTimeMillis;

}