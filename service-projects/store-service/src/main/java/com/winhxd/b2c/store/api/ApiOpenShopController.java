package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.OpenShopCondition;
import com.winhxd.b2c.common.domain.store.condition.ShopBaseInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.ShopBusinessInfoCondition;
import com.winhxd.b2c.common.domain.store.vo.OpenShopVO;
import com.winhxd.b2c.common.domain.store.vo.ShopBaseInfoVO;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.domain.store.vo.ShopManageInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.store.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 惠小店开店相关接口
 *
 * @author liutong
 * @date 2018-08-03 09:35:32
 */
@Api
@RestController
@RequestMapping(value = "api/openShop/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiOpenShopController {

    private static final Logger logger = LoggerFactory.getLogger(ApiOpenShopController.class);

    @Autowired
    private StoreService storeService;

    @ApiOperation(value = "惠小店开店条件验证接口", response = ResponseResult.class, notes = "惠小店开店条件验证接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200001, message = "customerId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002, message = "storeId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class)})
    @PostMapping(value = "1000/v1/checkStoreInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenShopVO> checkStoreInfo(@RequestBody OpenShopCondition openShopCondition) {
        if (openShopCondition == null) {
            logger.error("惠小店开店条件验证接口 checkStoreInfo,参数全部为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (openShopCondition.getCustomerId() == null) {
            logger.error("惠小店开店条件验证接口 checkStoreInfo,customerId参数为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (openShopCondition.getStoreId() == null) {
            logger.error("惠小店开店条件验证接口 checkStoreInfo,storeId参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult<OpenShopVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店开店条件验证接口入参为：{}", JsonUtil.toJSONString(openShopCondition));
            responseResult.setData(new OpenShopVO());
            logger.info("惠小店开店条件验证接口返参为：{}", JsonUtil.toJSONString(responseResult));
        } catch (Exception e) {
            logger.error("惠小店开店条件验证接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店基础信息查询接口", notes = "惠小店开店基础信息查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OpenShopVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200001, message = "customerId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002, message = "storeId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class)})
    @PostMapping(value = "1001/v1/getShopBaseInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenShopVO> getShopBaseInfo(@RequestBody OpenShopCondition openShopCondition) {
        if (openShopCondition == null) {
            logger.error("惠小店开店基础信息查询接口 getShopBaseInfo,参数全部为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (openShopCondition.getCustomerId() == null) {
            logger.error("惠小店开店基础信息查询接口 getShopBaseInfo,customerId参数为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (openShopCondition.getStoreId() == null) {
            logger.error("惠小店开店基础信息查询接口 getShopBaseInfo,storeId参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult<OpenShopVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店开店基础信息查询接口入参为：{}", JsonUtil.toJSONString(openShopCondition));
            responseResult.setData(new OpenShopVO());
        } catch (Exception e) {
            logger.error("惠小店开店基础信息查询接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店基础信息保存接口", notes = "惠小店开店基础信息保存接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OpenShopVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200005, message = "门店基础信息保存参数错误！", response = ResponseResult.class)})
    @PostMapping(value = "1002/v1/modifyShopBaseInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult modifyShopBaseInfo(@RequestBody ShopBaseInfoCondition shopBaseInfoCondition) {
        if (shopBaseInfoCondition == null || shopBaseInfoCondition.getCustomerId() == null || shopBaseInfoCondition.getStoreId() == null ||
                StringUtils.isBlank(shopBaseInfoCondition.getStorePhoto()) || StringUtils.isBlank(shopBaseInfoCondition.getStoreName()) ||
                StringUtils.isBlank(shopBaseInfoCondition.getShopOwnerImg()) || StringUtils.isBlank(shopBaseInfoCondition.getShopkeeper()) ||
                StringUtils.isBlank(shopBaseInfoCondition.getStoreRegionCode())) {
            logger.error("惠小店开店基础信息保存接口 saveShopInfo,参数错误");
            throw new BusinessException(BusinessCode.CODE_200005);
        }

        ResponseResult responseResult = new ResponseResult();
        try {
            logger.info("惠小店开店基础信息保存接口入参为：{}", JsonUtil.toJSONString(shopBaseInfoCondition));
        } catch (Exception e) {
            logger.error("惠小店开店基础信息保存接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店店铺信息保存接口", notes = "惠小店开店店铺信息保存接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ShopBaseInfoVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200006, message = "店铺营业信息保存参数错误！", response = ResponseResult.class)})
    @PostMapping(value = "1003/v1/modifyShopBusinessInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<ShopBaseInfoVO> modifyShopBusinessInfo(@RequestBody ShopBusinessInfoCondition shopBaseInfoCondition) {
        if (shopBaseInfoCondition == null || shopBaseInfoCondition.getCustomerId() == null || shopBaseInfoCondition.getStoreId() == null ||
                StringUtils.isBlank(shopBaseInfoCondition.getStoreName()) || shopBaseInfoCondition.getPickupWay() == null ||
                shopBaseInfoCondition.getPaymentWay() == null || StringUtils.isBlank(shopBaseInfoCondition.getShopkeeper()) ||
                StringUtils.isBlank(shopBaseInfoCondition.getContactMobile())) {
            logger.error("惠小店开店店铺信息保存接口 saveShopInfo,参数错误");
            throw new BusinessException(BusinessCode.CODE_200006);
        }
        ResponseResult<ShopBaseInfoVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店开店店铺信息保存接口入参为：{}", JsonUtil.toJSONString(shopBaseInfoCondition));
            responseResult.setData(new ShopBaseInfoVO());
        } catch (Exception e) {
            logger.error("惠小店开店店铺信息保存接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店管理首页获取数据接口", notes = "惠小店管理首页获取数据接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ShopManageInfoVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200001, message = "customerId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002, message = "storeId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class)})
    @PostMapping(value = "1004/v1/getShopManageInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<ShopManageInfoVO> getShopManageInfo(@RequestBody OpenShopCondition shopManageInfoCondition) {
        if (shopManageInfoCondition.getCustomerId() == null) {
            logger.error("惠小店管理首页获取数据接口 getShopBaseInfo,customerId参数为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (shopManageInfoCondition.getStoreId() == null) {
            logger.error("惠小店管理首页获取数据接口 getShopBaseInfo,storeId参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult<ShopManageInfoVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店管理首页获取数据接口入参为：{}", JsonUtil.toJSONString(shopManageInfoCondition));
            responseResult.setData(new ShopManageInfoVO());
        } catch (Exception e) {
            logger.error("惠小店管理首页获取数据接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
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