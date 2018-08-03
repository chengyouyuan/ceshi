package com.winhxd.b2c.common.domain;

/**
 * @author lixiaodong
 */
public class ResponsePageResult<T> extends ResponseResult<T> {

    private int pageSize;
    private int pageNum;
    private long total;

    public ResponsePageResult() {
        super();
    }

    public ResponsePageResult(int code) {
        super(code);
    }

    public ResponsePageResult(int code, String message) {
        super(code, message);
    }

    public ResponsePageResult(T data) {
        super(data);
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
