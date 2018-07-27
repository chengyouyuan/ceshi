package com.winhxd.b2c.common.domain;

/**
 * @author lixiaodong
 */
public class RestResult<T> {
    private int code = 0;
    private String message;
    private T data;

    public RestResult() {

    }

    public RestResult(int code) {
        this.code = code;
    }

    public RestResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestResult(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
