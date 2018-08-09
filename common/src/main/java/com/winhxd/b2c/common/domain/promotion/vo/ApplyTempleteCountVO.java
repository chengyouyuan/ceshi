package com.winhxd.b2c.common.domain.promotion.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wl
 * @Date 2018/8/9 18:10
 * @Description
 **/
public class ApplyTempleteCountVO {

    @ApiModelProperty(value = "优惠券类型规则主键")
    private String applyId;
    @ApiModelProperty(value = "优惠券类型规则名称")
     private String applyName;
    @ApiModelProperty(value = "优惠券类型规则编码")
     private String applyCode;
    @ApiModelProperty(value = "优惠券模板名称")
     private String templeteName;
    @ApiModelProperty(value = "优惠券模板编码")
     private String templeteCode;
    @ApiModelProperty(value = "优惠券状态")
     private String templeteStatus;
    @ApiModelProperty(value = "优惠券使用时间")
     private String templeteUseTime;


    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getTempleteName() {
        return templeteName;
    }

    public void setTempleteName(String templeteName) {
        this.templeteName = templeteName;
    }

    public String getTempleteCode() {
        return templeteCode;
    }

    public void setTempleteCode(String templeteCode) {
        this.templeteCode = templeteCode;
    }

    public String getTempleteStatus() {
        return templeteStatus;
    }

    public void setTempleteStatus(String templeteStatus) {
        this.templeteStatus = templeteStatus;
    }

    public String getTempleteUseTime() {
        return templeteUseTime;
    }

    public void setTempleteUseTime(String templeteUseTime) {
        this.templeteUseTime = templeteUseTime;
    }
}
