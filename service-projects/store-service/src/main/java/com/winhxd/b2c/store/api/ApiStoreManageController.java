package com.winhxd.b2c.store.api;

import com.alibaba.druid.support.json.JSONUtils;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.OpenShopCondition;
import com.winhxd.b2c.common.domain.store.vo.OpenShopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 惠小店管理相关接口
 *
 * @author liutong
 * @date 2018-08-03 16:37:04
 */
@Api(value = "api storeManage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "api/storeManage/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiStoreManageController {

    private static final Logger logger = LoggerFactory.getLogger(ApiOpenShopController.class);

    @ApiOperation(value = "惠小店管理获取首页数据接口", response = ResponseResult.class, notes = "惠小店管理获取首页数据接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "1000/checkStoreInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenShopVO> checkStoreInfo(@RequestBody OpenShopCondition openShopCondition) {
        ResponseResult<OpenShopVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店管理获取首页数据接口入参为：{}", openShopCondition);
            responseResult.setData(new OpenShopVO());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }

}
