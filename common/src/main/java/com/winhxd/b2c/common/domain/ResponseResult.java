package com.winhxd.b2c.common.domain;

import com.winhxd.b2c.common.i18n.MessageHelper;

/**
 * @author lixiaodong
 */
public class ResponseResult<T> {
    private int code = 0;
    private String message;
    private T data;

    public ResponseResult() {

    }

    public ResponseResult(int code) {
        this.code = code;
        this.message = MessageHelper.getInstance().getMessage(String.valueOf(code));
    }

    public ResponseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult(T data) {
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
