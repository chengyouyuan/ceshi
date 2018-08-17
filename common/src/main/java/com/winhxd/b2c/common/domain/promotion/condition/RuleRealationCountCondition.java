package com.winhxd.b2c.common.domain.promotion.condition;

/**
 * @Author wl
 * @Date 2018/8/17 16:17
 * @Description
 **/
public class RuleRealationCountCondition {
    private Long id;
    private Integer pageNo;
    private Integer pageSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
