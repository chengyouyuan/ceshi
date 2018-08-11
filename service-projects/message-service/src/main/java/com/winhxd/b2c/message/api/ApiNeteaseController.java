package com.winhxd.b2c.message.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgBoxCondition;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import com.winhxd.b2c.common.domain.message.vo.NeteaseMsgBoxVO;
import com.winhxd.b2c.common.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author jujinbiao
 * @className ApiNeteaseController
 * @description 网易云信接口
 */
@Api(value = "api message", tags = "api message")
@RestController
@RequestMapping(value = "api-message/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiNeteaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiNeteaseController.class);

    @ApiOperation(value = "获取云信用户消息接口", notes = "获取云信用户消息接口")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "netease/701/v1/findNeteaseMsgBox", method = RequestMethod.POST)
    public ResponseResult<NeteaseMsgBoxVO> findNeteaseMsgBox(@RequestBody NeteaseMsgBoxCondition neteaseMsgBoxCondition){
        ResponseResult<NeteaseMsgBoxVO> result = new ResponseResult<>();
        try {
            result.setData(null);
        }catch (BusinessException e) {
            LOGGER.error("/api-message/netease/701/v1/findNeteaseMsgBox,获取云信用户消息接口，异常信息{}" + e.getMessage(), e.getErrorCode());
            result.setCode(e.getErrorCode());
        } catch (Exception e) {
            LOGGER.error("/api-message/netease/701/v1/findNeteaseMsgBox,获取云信用户消息接口，异常信息{}" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

}
