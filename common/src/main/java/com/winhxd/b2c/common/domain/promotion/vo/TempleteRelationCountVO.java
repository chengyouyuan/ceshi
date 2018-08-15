package com.winhxd.b2c.common.domain.promotion.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wl
 * @Date 2018/8/15 12:00
 * @Description 关联 模板数量
 **/
public class TempleteRelationCountVO {
    @ApiModelProperty(value = "关联模板数量")
    private Integer relTempleteCount;

    public Integer getRelTempleteCount() {
        return relTempleteCount;
    }

    public void setRelTempleteCount(Integer relTempleteCount) {
        this.relTempleteCount = relTempleteCount;
    }
}
