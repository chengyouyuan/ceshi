package com.winhxd.b2c.common.domain;

import com.winhxd.b2c.common.i18n.MessageHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lixiaodong
 */
@ApiModel("接口返回标准数据")
public class ResponseResult<T> {
    @ApiModelProperty("0-通过data获取数据, 其他-通过message输出错误")
    private int code = 0;
    @ApiModelProperty("当code不为0时, 表示错误信息")
    private String message;
    @ApiModelProperty("当code为0时, 可获取响应结果")
    private T data;

    public ResponseResult() {

    }

    public ResponseResult(int code) {
        this.code = code;
        this.message = MessageHelper.getInstance().getMessage(String.valueOf(code));
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
