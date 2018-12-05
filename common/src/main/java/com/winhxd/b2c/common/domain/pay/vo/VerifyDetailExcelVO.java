package com.winhxd.b2c.common.domain.pay.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: retail2c
 * @description: 结算费用明细导出VO类
 * @author: ChenYanqi
 * @create: 2018-12-05 17:20
 **/
@Data
@ApiModel("结算费用明细导出VO类")
public class VerifyDetailExcelVO {

    @ApiModelProperty("结算状态")
    @Excel(name = "结算状态", width = 30)
    private String verifyStatusName;

    @ApiModelProperty("门店名称")
    @Excel(name = "门店名称", width = 30)
    private String storeName;

    @ApiModelProperty("门店ID")
    @Excel(name = "门店ID", width = 30)
    private Long storeId;

    @ApiModelProperty("完成时间")
    @Excel(name = "订单完成时间", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date finishedTime;

    @ApiModelProperty("入账时间")
    @Excel(name = "订单入账时间", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date recordedTime;

    @ApiModelProperty("与支付平台结算状态")
    @Excel(name = "微信入账", width = 30)
    private String thirdPartyVerifyStatusName;

    @ApiModelProperty("订单号")
    @Excel(name = "关联订单", width = 30)
    private String orderNo;

    @ApiModelProperty("费用类型")
    @Excel(name = "费用类型", width = 30)
    private String detailTypeName;

    @ApiModelProperty("费用金额")
    @Excel(name = "金额", width = 30)
    private BigDecimal detailMoney;

    @ApiModelProperty("支付平台手续费")
    @Excel(name = "手续费", width = 30)
    private BigDecimal thirdPartyFeeMoney;


    @ApiModelProperty("费用类型")
    private Integer detailType;


    @ApiModelProperty("订单是否完成：0-未完成；1-已完成；-1-取消")
    private Integer orderCompleteStatus;


    @ApiModelProperty("记录插入时间")
    private Date insertTime;

    @ApiModelProperty("与支付平台结算状态")
    private Integer thirdPartyVerifyStatus;

    @ApiModelProperty("与支付平台结算时间")
    private Date thirdPartyVerifyTime;

    @ApiModelProperty("结算状态")
    private Integer verifyStatus;

    @ApiModelProperty("处理批次编码")
    private String verifyCode;

    @ApiModelProperty("结算时间")
    private Date verifyTime;

    @ApiModelProperty("操作时间")
    @Excel(name = "操作时间", width = 30)
    private Date operatedTime;

    @ApiModelProperty("操作人")
    private Long operatedBy;

    @ApiModelProperty("操作人")
    @Excel(name = "操作人", width = 30)
    private String operatedByName;

    public Date getFinishendTime(Data time) {
        return recordedTime;
    }

    public String getDetailTypeName() {
        return AccountingDetail.DetailTypeEnum.getMemoOfCode(getDetailType());
    }

    public String getThirdPartyVerifyStatusName() {
        return AccountingDetail.ThirdPartyVerifyStatusEnum.getMemoOfCode(getThirdPartyVerifyStatus());
    }

    public String getVerifyStatusName() {
        return AccountingDetail.VerifyStatusEnum.getMemoOfCode(getVerifyStatus());
    }


}
