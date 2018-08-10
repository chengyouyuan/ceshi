package com.winhxd.b2c.common.domain.promotion.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wl
 * @Date 2018/8/9 18:09
 * @Description  用于坎级规则列表引用模板列表查询
 **/
public class GradeTempleteCountVO {
    @ApiModelProperty(value = "优惠方式规则主键")
    private String gradeId;
    @ApiModelProperty(value = "优惠方式规则名称")
    private String gradeName;
    @ApiModelProperty(value = "优惠方式规则编码")
    private String gradeCode;
    @ApiModelProperty(value = "优惠券模板名称")
    private String templeteName;
    @ApiModelProperty(value = "优惠券模板编码")
    private String templeteCode;
    @ApiModelProperty(value = "优惠券状态")
    private String templeteStatus;
    @ApiModelProperty(value = "优惠券使用时间")
    private String templeteUseTime;

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
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
