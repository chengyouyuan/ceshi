package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.OpenStoreCondition;
import com.winhxd.b2c.common.domain.store.vo.OpenStoreVO;
import com.winhxd.b2c.common.util.JsonUtil;
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
 * Created by caiyulong on 2018/8/4.
 */
@Api(value = "api product", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "api/product/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiProductController {

    private static final Logger logger = LoggerFactory.getLogger(ApiProductController.class);

    @ApiOperation(value = "小程序商品搜索列表", response = ResponseResult.class, notes = "小程序商品搜索列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "2001/checkStoreInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenStoreVO> checkStoreInfo(@RequestBody OpenStoreCondition openStoreCondition) {
        ResponseResult<OpenStoreVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店开店条件验证接口入参为：{}", JsonUtil.toJSONString(openStoreCondition));
            responseResult.setData(new OpenStoreVO());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }
}
