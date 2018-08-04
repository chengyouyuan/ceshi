package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.OpenShopCondition;
import com.winhxd.b2c.common.domain.store.condition.SaveShopCondition;
import com.winhxd.b2c.common.domain.store.vo.OpenShopVO;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.store.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 惠小店开店相关接口
 *
 * @author liutong
 * @date 2018-08-03 09:35:32
 */
@Api(value = "api openShop", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "api/openShop/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiOpenShopController {

    private static final Logger logger = LoggerFactory.getLogger(ApiOpenShopController.class);

    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "惠小店开店条件验证接口", response = ResponseResult.class, notes = "惠小店开店条件验证接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "1000/checkStoreInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenShopVO> checkStoreInfo(@RequestBody OpenShopCondition openShopCondition) {
        ResponseResult<OpenShopVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店开店条件验证接口入参为：{}", JsonUtil.toJSONString(openShopCondition));
            responseResult.setData(new OpenShopVO());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店基础信息查询接口", response = ResponseResult.class, notes = "惠小店开店基础信息查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "1001/getShopBaseInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenShopVO> getShopBaseInfo(@RequestBody OpenShopCondition openShopCondition) {
        ResponseResult<OpenShopVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店开店基础信息查询接口入参为：{}", JsonUtil.toJSONString(openShopCondition));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店信息保存接口", response = ResponseResult.class, notes = "惠小店开店信息保存接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "1002/saveShopInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenShopVO> saveShopInfo(@RequestBody SaveShopCondition saveShopCondition) {
        ResponseResult<OpenShopVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店开店信息保存接口入参为：{}", JsonUtil.toJSONString(saveShopCondition));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店管理获取首页数据接口", response = ResponseResult.class, notes = "惠小店管理获取首页数据接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "1000/getShopIndexInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenShopVO> getShopIndexInfo(@RequestBody OpenShopCondition openShopCondition) {
        ResponseResult<OpenShopVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店管理获取首页数据接口入参为：{}", JsonUtil.toJSONString(openShopCondition));
            responseResult.setData(new OpenShopVO());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }


    /**
     * @author chengyy
     * @date 2018/8/3 16:04
     * @Description 获取门店信息
     * @param  storeUserId 门店id
     * @return StoreUserInfoVO 返回当前门店信息数据
     */
    @ApiOperation(value = "通过门店id查询门店信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_200002,message = "请求缺少参数门店id"),@ApiResponse(code = BusinessCode.CODE_OK,message = "操作成功")})
    @RequestMapping(value = "/api/store/2002/v1/findStoreUserInfo/{storeUserId}",method = RequestMethod.GET)
    public ResponseResult<StoreUserInfoVO> findStoreUserInfo(@PathVariable("storeUserId")Long storeUserId){
        ResponseResult<StoreUserInfoVO> result = new ResponseResult<>();
        if(storeUserId == null){
            logger.error("StoreServiceController -> findStoreUserInfo获取的参数storeUserId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        StoreUserInfoVO data = storeService.findStoreUserInfo(storeUserId);
        if(data == null){
            result.setCode(BusinessCode.CODE_200004);
        }
        result.setData(data);
        return result;
    }
}