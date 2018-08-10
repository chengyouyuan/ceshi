package com.winhxd.b2c.common.domain.promotion.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wl
 * @Date 2018/8/9 18:09
 * @Description  出资方规则列表引用模板列表查询
 **/
public class InvertorTempleteCountVO {

    @ApiModelProperty(value = "出资方规则主键")
    private String invertorId;
    @ApiModelProperty(value = "出资方规则名称")
    private String invertorName;
    @ApiModelProperty(value = "出资方规则编码")
    private String invertorCode;
    @ApiModelProperty(value = "优惠券模板名称")
    private String templeteName;
    @ApiModelProperty(value = "优惠券模板编码")
    private String templeteCode;
    @ApiModelProperty(value = "优惠券状态")
    private String templeteStatus;
    @ApiModelProperty(value = "优惠券使用时间")
    private String templeteUseTime;

    public String getInvertorId() {
        return invertorId;
    }

    public void setInvertorId(String invertorId) {
        this.invertorId = invertorId;
    }

    public String getInvertorName() {
        return invertorName;
    }

    public void setInvertorName(String invertorName) {
        this.invertorName = invertorName;
    }

    public String getInvertorCode() {
        return invertorCode;
    }

    public void setInvertorCode(String invertorCode) {
        this.invertorCode = invertorCode;
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
