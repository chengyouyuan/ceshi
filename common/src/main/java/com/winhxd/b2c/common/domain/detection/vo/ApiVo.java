package com.winhxd.b2c.common.domain.detection.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: Louis
 * @Date: 2018/9/5 17:56
 * @Description:
 */
@ApiModel("API接口监控信息")
@Data
public class ApiVo {

    @ApiModelProperty("接口调用时间")
    private String dateTime;

    @ApiModelProperty("接口调用时长")
    private String duration;

    @ApiModelProperty("api编号")
    private String apiCode;

    @ApiModelProperty("api结果编号")
    private String apiResult;

    @ApiModelProperty("api接口请求")
    private String apiRequest;

    @ApiModelProperty("api接口结果")
    private String apiResponse;

    @ApiModelProperty("错误信息")
    private String error;

    @ApiModelProperty("httpMethod")
    private String httpMethod;

    @ApiModelProperty("接口URL")
    private String httpPath;

    @ApiModelProperty("controllerClass")
    private String controllerClass;

    @ApiModelProperty("controllerMethod")
    private String controllerMethod;

    @ApiModelProperty("clientAddress")
    private String clientAddress;

    @ApiModelProperty("zipkin中traceId")
    private String traceId;

    @ApiModelProperty("zipkin中spanId")
    private String spanId;

    @ApiModelProperty("zipkin中parentId")
    private String parentId;

}
