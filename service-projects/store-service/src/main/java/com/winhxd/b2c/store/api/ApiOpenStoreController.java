package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.OpenStoreCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreBaseInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreBusinessInfoCondition;
import com.winhxd.b2c.common.domain.store.vo.OpenStoreVO;
import com.winhxd.b2c.common.domain.store.vo.StoreBaseInfoVO;
import com.winhxd.b2c.common.domain.store.vo.StoreBusinessInfoVO;
import com.winhxd.b2c.common.domain.store.vo.StoreManageInfoVO;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO1;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.hxd.StoreHxdServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.store.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 惠小店开店相关接口
 *
 * @author liutong
 * @date 2018-08-03 09:35:32
 */
@Api
@RestController
@RequestMapping(value = "api-store/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiOpenStoreController {

    private static final Logger logger = LoggerFactory.getLogger(ApiOpenStoreController.class);

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreHxdServiceClient storeHxdServiceClient;

    @ApiOperation(value = "惠小店开店条件验证接口", notes = "惠小店开店条件验证接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002, message = "storeId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class)})
    @PostMapping(value = "1000/v1/checkStoreInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenStoreVO> checkStoreInfo(@RequestBody OpenStoreCondition openStoreCondition) {
        if (openStoreCondition == null || openStoreCondition.getStoreId() == null) {
            logger.error("惠小店开店条件验证接口 checkStoreInfo,storeId参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult<OpenStoreVO> responseResult = new ResponseResult<>();
        OpenStoreVO openStoreVO = new OpenStoreVO();
        try {
            logger.info("惠小店开店条件验证接口入参为：{}", JsonUtil.toJSONString(openStoreCondition));
            //是否开过小店
            StoreUserInfo storeUserInfo = storeService.selectByStoreId(openStoreCondition.getStoreId());
            if (storeUserInfo == null) {
                responseResult.setCode(BusinessCode.CODE_200004);
                responseResult.setMessage("门店信息不存在");
                return responseResult;
            }
            if (storeUserInfo.getStoreStatus() != 0) {
                openStoreVO.setStoreStatus((byte) 1);
                responseResult.setData(openStoreVO);
                return responseResult;
            } else {
                openStoreVO.setStoreStatus((byte) 0);
            }
            //是否完善信息
            ResponseResult<List<String>> noPerfectResult = storeHxdServiceClient.getStorePerfectInfo(openStoreCondition.getStoreId().toString());
            if (noPerfectResult.getCode() == 1) {
                List<String> list = noPerfectResult.getData();
                if (list.size() > 0) {
                    openStoreVO.setPerfectStatus((byte) 0);
                    openStoreVO.setNoPerfectMessage(noPerfectResult.getData());
                } else {
                    openStoreVO.setPerfectStatus((byte) 1);
                }
                responseResult.setData(openStoreVO);
            } else {
                responseResult.setCode(noPerfectResult.getCode());
                responseResult.setMessage(noPerfectResult.getMessage());
            }
            logger.info("惠小店开店条件验证接口返参为：{}", JsonUtil.toJSONString(responseResult));
        } catch (Exception e) {
            logger.error("惠小店开店条件验证接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店基础信息查询接口", notes = "惠小店开店基础信息查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OpenStoreVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002, message = "storeId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class)})
    @PostMapping(value = "1001/v1/getStoreBaseInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenStoreVO> getStoreBaseInfo(@RequestBody OpenStoreCondition openStoreCondition) {
        if (openStoreCondition == null || openStoreCondition.getStoreId() == null) {
            logger.error("惠小店开店基础信息查询接口 getStoreBaseInfo,storeId参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult<OpenStoreVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店开店基础信息查询接口入参为：{}", JsonUtil.toJSONString(openStoreCondition));
            storeHxdServiceClient.getStoreBaseInfo(openStoreCondition.getStoreId().toString());
            responseResult.setData(new OpenStoreVO());
        } catch (Exception e) {
            logger.error("惠小店开店基础信息查询接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店基础信息保存接口", notes = "惠小店开店基础信息保存接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OpenStoreVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200005, message = "门店基础信息保存参数错误！", response = ResponseResult.class)})
    @PostMapping(value = "1002/v1/modifyStoreBaseInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreBusinessInfoVO> modifyStoreBaseInfo(@RequestBody StoreBaseInfoCondition storeBaseInfoCondition) {
        if (storeBaseInfoCondition == null || storeBaseInfoCondition.getStoreId() == null ||
                StringUtils.isBlank(storeBaseInfoCondition.getStoreAddress()) || StringUtils.isBlank(storeBaseInfoCondition.getStoreName()) ||
                StringUtils.isBlank(storeBaseInfoCondition.getShopOwnerImg()) || StringUtils.isBlank(storeBaseInfoCondition.getShopkeeper()) ||
                StringUtils.isBlank(storeBaseInfoCondition.getStoreRegionCode())) {
            logger.error("惠小店开店基础信息保存接口 modifyStoreBaseInfo,参数错误:{}", JsonUtil.toJSONString(storeBaseInfoCondition));
            throw new BusinessException(BusinessCode.CODE_200004);
        }
        ResponseResult<StoreBusinessInfoVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店开店基础信息保存接口入参为：{}", JsonUtil.toJSONString(storeBaseInfoCondition));
            StoreUserInfo storeUserInfo = storeService.selectByStoreId(storeBaseInfoCondition.getStoreId());
            if (storeUserInfo == null) {
                logger.error("惠小店开店基础信息保存接口 modifyStoreBaseInfo,门店信息不存在:{}", storeBaseInfoCondition.getStoreId());
                throw new BusinessException(BusinessCode.CODE_200005);
            }
            BeanUtils.copyProperties(storeBaseInfoCondition, storeUserInfo);
            storeService.updateByPrimaryKeySelective(storeUserInfo);
            StoreBusinessInfoVO storeBusinessInfoVO = new StoreBusinessInfoVO();
            BeanUtils.copyProperties(storeBaseInfoCondition, storeBusinessInfoVO);
            responseResult.setData(storeBusinessInfoVO);
        } catch (Exception e) {
            logger.error("惠小店开店基础信息保存接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店店铺信息保存接口", notes = "惠小店开店店铺信息保存接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = StoreBaseInfoVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200006, message = "店铺营业信息保存参数错误！", response = ResponseResult.class)})
    @PostMapping(value = "1003/v1/modifyStoreBusinessInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreBaseInfoVO> modifyStoreBusinessInfo(@RequestBody StoreBusinessInfoCondition storeBusinessInfoCondition) {
        if (storeBusinessInfoCondition == null || storeBusinessInfoCondition.getStoreId() == null ||
                StringUtils.isBlank(storeBusinessInfoCondition.getStoreName()) || storeBusinessInfoCondition.getPickupWay() == null ||
                storeBusinessInfoCondition.getPaymentWay() == null || StringUtils.isBlank(storeBusinessInfoCondition.getShopkeeper()) ||
                StringUtils.isBlank(storeBusinessInfoCondition.getContactMobile()) || StringUtils.isBlank(storeBusinessInfoCondition.getStoreAddress())) {
            logger.error("惠小店开店店铺信息保存接口 saveStoreInfo,参数错误:{}", JsonUtil.toJSONString(storeBusinessInfoCondition));
            throw new BusinessException(BusinessCode.CODE_200006);
        }
        ResponseResult<StoreBaseInfoVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店开店店铺信息保存接口入参为：{}", JsonUtil.toJSONString(storeBusinessInfoCondition));
            responseResult.setData(new StoreBaseInfoVO());
        } catch (Exception e) {
            logger.error("惠小店开店店铺信息保存接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店管理首页获取数据接口", notes = "惠小店管理首页获取数据接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = StoreManageInfoVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200001, message = "customerId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002, message = "storeId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class)})
    @PostMapping(value = "1004/v1/getStoreManageInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreManageInfoVO> getStoreManageInfo(@RequestBody OpenStoreCondition openStoreCondition) {
        if (openStoreCondition == null || openStoreCondition.getCustomerId() == null) {
            logger.error("惠小店管理首页获取数据接口 getStoreBaseInfo,customerId参数为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (openStoreCondition.getStoreId() == null) {
            logger.error("惠小店管理首页获取数据接口 getStoreBaseInfo,storeId参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult<StoreManageInfoVO> responseResult = new ResponseResult<>();
        try {
            logger.info("惠小店管理首页获取数据接口入参为：{}", JsonUtil.toJSONString(openStoreCondition));
            responseResult.setData(new StoreManageInfoVO());
        } catch (Exception e) {
            logger.error("惠小店管理首页获取数据接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }


    /**
     * @param id 门店id
     * @return StoreUserInfoVO 返回当前门店信息数据
     * @author chengyy
     * @date 2018/8/3 16:04
     * @Description 获取门店信息
     * @param  id 门店id(主键)
     * @return StoreUserInfoVO 返回当前门店信息数据
     */
    @ApiOperation(value = "通过门店id查询门店信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_200002,message = "请求缺少参数门店id"),@ApiResponse(code = BusinessCode.CODE_OK,message = "操作成功")})
    @RequestMapping(value = "/1005/v1/findStoreUserInfo/{id}",method = RequestMethod.POST)
    public ResponseResult<StoreUserInfoVO1> findStoreUserInfo(@PathVariable("id")Long id){
        ResponseResult<StoreUserInfoVO1> result = new ResponseResult<>();
        if(id == null){
            logger.error("StoreServiceController -> findStoreUserInfo获取的参数storeUserId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        StoreUserInfoVO1 data = storeService.findStoreUserInfo(id);
        if(data == null){
            result.setCode(BusinessCode.CODE_200004);
        }
        result.setData(data);
        return result;
    }
}