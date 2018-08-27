package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.RegexConstant;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.order.condition.StoreOrderSalesSummaryCondition;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;
import com.winhxd.b2c.common.domain.store.condition.StoreBaseInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreBusinessInfoCondition;
import com.winhxd.b2c.common.domain.store.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.store.enums.PickupTypeEnum;
import com.winhxd.b2c.common.domain.store.model.StoreRegion;
import com.winhxd.b2c.common.domain.store.model.StoreStatusEnum;
import com.winhxd.b2c.common.domain.store.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.store.vo.*;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserInfoCondition;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.hxd.StoreHxdServiceClient;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.store.service.StoreBrowseLogService;
import com.winhxd.b2c.store.service.StoreRegionService;
import com.winhxd.b2c.store.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


/**
 * 惠小店开店相关接口
 *
 * @author liutong
 * @date 2018-08-03 09:35:32
 */
@Api(tags = "惠小店开店相关接口")
@RestController
@RequestMapping(value = "/api-store/store", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiOpenStoreController {

    private static final Logger logger = LoggerFactory.getLogger(ApiOpenStoreController.class);

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreHxdServiceClient storeHxdServiceClient;

    @Autowired
    private StoreBrowseLogService storeBrowseLogService;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private StoreRegionService storeRegionService;

    @ApiOperation(value = "惠小店开店条件验证接口", notes = "惠小店开店条件验证接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "服务器内部错误！")})
    @PostMapping(value = "/1000/v1/getStoreRegionStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenStoreVO> getStoreRegionStatus(@RequestBody ApiCondition apiCondition) {
        if (UserContext.getCurrentStoreUser() == null) {
            logger.error("惠小店开店条件验证接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店开店条件验证接口 门店用户编码:{}", storeCustomerId);
        ResponseResult<OpenStoreVO> responseResult = new ResponseResult<>();
        //是否有登录注册信息
        StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
        if (storeUserInfo == null) {
            responseResult.setCode(BusinessCode.CODE_200004);
            responseResult.setMessage("门店信息不存在");
            logger.error("惠小店开店条件验证接口 门店信息不存在 storeCustomerId：{}", storeCustomerId);
            return responseResult;
        }
        OpenStoreVO openStoreVO = new OpenStoreVO();
        //是否在测试区域
        StoreRegion storeRegion = storeRegionService.getByRegionCode(storeUserInfo.getStoreRegionCode());
        if (storeRegion != null) {
            openStoreVO.setRegionStatus((byte) 1);
        } else {
            openStoreVO.setRegionStatus((byte) 0);
            responseResult.setData(openStoreVO);
            return responseResult;
        }
        //是否开过小店，0未开店
        if (storeUserInfo.getStoreStatus() == StoreStatusEnum.UN_OPEN.getStatusCode()) {
            openStoreVO.setStoreStatus((byte) 0);
            //未开店是否完善信息
            ResponseResult<List<Integer>> noPerfectResult = storeHxdServiceClient.getStorePerfectInfo(storeCustomerId.toString());
            Byte flag = 1;
            for (int i : noPerfectResult.getData()) {
                if (i == 0) {
                    flag = 0;
                    break;
                }
            }
            openStoreVO.setStorePerfectStatus(flag);
        } else {
            openStoreVO.setStoreStatus((byte) 1);
        }
        responseResult.setData(openStoreVO);
        logger.info("惠小店开店条件验证接口 返参为：{}", JsonUtil.toJSONString(openStoreVO));
        return responseResult;
    }

    @ApiOperation(value = "惠小店未完善信息接口", notes = "惠小店未完善信息接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！")})
    @PostMapping(value = "/1001/v1/checkStoreInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenStoreVO> checkStoreInfo(@RequestBody ApiCondition apiCondition) {
        if (UserContext.getCurrentStoreUser() == null) {
            logger.error("惠小店未完善信息接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店未完善信息接口 门店用户编码:{}", storeCustomerId);
        ResponseResult<OpenStoreVO> responseResult = new ResponseResult<>();
        //查询账号基础信息
        StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
        if (storeUserInfo == null) {
            responseResult.setCode(BusinessCode.CODE_200004);
            responseResult.setMessage("门店信息不存在");
            logger.error("惠小店未完善信息接口 门店信息不存在 storeCustomerId：{}", storeCustomerId);
            return responseResult;
        }
        OpenStoreVO openStoreVO = new OpenStoreVO();
        //是否完善信息
        ResponseResult<List<Integer>> noPerfectResult = storeHxdServiceClient.getStorePerfectInfo(storeCustomerId.toString());
        openStoreVO.setNoPerfectMessage(noPerfectResult.getData());
        responseResult.setData(openStoreVO);
        logger.info("惠小店未完善信息接口 返参为：{}", JsonUtil.toJSONString(responseResult));
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店基础信息查询接口", notes = "惠小店开店基础信息查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！")})
    @PostMapping(value = "/1002/v1/getStoreBaseInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreBaseInfoVO> getStoreBaseInfo(@RequestBody ApiCondition apiCondition) {
        if (UserContext.getCurrentStoreUser() == null) {
            logger.error("惠小店开店基础信息查询接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店开店基础信息查询接口 门店用户编码:{}", storeCustomerId);
        ResponseResult<StoreBaseInfoVO> responseResult = new ResponseResult<>();
        //是否开过小店
        StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
        if (storeUserInfo == null) {
            logger.error("惠小店开店基础信息查询接口 门店信息不存在 storeCustomerId：{}", storeCustomerId);
            throw new BusinessException(BusinessCode.CODE_200004);
        }
        ResponseResult<Map<String, Object>> result = storeHxdServiceClient.getStoreBaseInfo(storeCustomerId.toString());
        StoreBaseInfoVO storeBaseInfoVO = new StoreBaseInfoVO();
        if (!result.getData().isEmpty()) {
            Map<String, Object> map = result.getData();
            storeBaseInfoVO.setStoreImg(StringUtils.isBlank(storeUserInfo.getStorePicImg()) ? "" : storeUserInfo.getStorePicImg());
            storeBaseInfoVO.setShopOwnerImg(StringUtils.isBlank(storeUserInfo.getShopOwnerImg()) ? "" : storeUserInfo.getShopOwnerImg());
            storeBaseInfoVO.setStoreName(Objects.toString(map.get("storeName"), ""));
            storeBaseInfoVO.setShopkeeper(Objects.toString(map.get("shopkeeper"), ""));
            storeBaseInfoVO.setProvince(Objects.toString(map.get("province"), ""));
            storeBaseInfoVO.setCity(Objects.toString(map.get("city"), ""));
            storeBaseInfoVO.setCounty(Objects.toString(map.get("county"), ""));
            storeBaseInfoVO.setStoreRegionCode(Objects.toString(map.get("storeRegionCode"), ""));
            storeBaseInfoVO.setStoreAddress(Objects.toString(map.get("storeAddress"), ""));
            //先更新门店信息，再返回给前端展示
            StoreUserInfo storeUserInfoUpdate = new StoreUserInfo();
            storeUserInfoUpdate.setId(storeUserInfo.getId());
            storeUserInfoUpdate.setStoreName(Objects.toString(map.get("storeName"), ""));
            storeUserInfoUpdate.setShopkeeper(Objects.toString(map.get("shopkeeper"), ""));
            storeUserInfoUpdate.setStoreAddress(Objects.toString(map.get("storeAddress"), ""));
            storeUserInfoUpdate.setStoreRegionCode(Objects.toString(map.get("storeRegionCode"), ""));
            storeUserInfoUpdate.setLon(Double.parseDouble(Objects.toString(map.get("longitude"), "0")));
            storeUserInfoUpdate.setLat(Double.parseDouble(Objects.toString(map.get("latitude"), "0")));
            storeUserInfoUpdate.setContactMobile(storeUserInfo.getStoreMobile());
            storeService.updateByPrimaryKeySelective(storeUserInfoUpdate);
        } else {
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage(StringUtils.isBlank(result.getMessage()) ? "请求发送失败" : result.getMessage());
            return responseResult;
        }
        responseResult.setData(storeBaseInfoVO);
        logger.info("惠小店开店基础信息查询接口 返参为：{}", JsonUtil.toJSONString(storeBaseInfoVO));
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店基础信息保存接口，暂时不用", notes = "惠小店开店基础信息保存接口，暂时不用")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！"),
            @ApiResponse(code = BusinessCode.CODE_200005, message = "门店基础信息保存参数错误！")})
    @PostMapping(value = "/1003/v1/modifyStoreBaseInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> modifyStoreBaseInfo(@RequestBody StoreBaseInfoCondition storeBaseInfoCondition) {
        logger.info("惠小店开店基础信息保存接口入参为：{}", storeBaseInfoCondition.toString());
        if (StringUtils.isBlank(storeBaseInfoCondition.getStoreAddress()) || StringUtils.isBlank(storeBaseInfoCondition.getStoreName()) ||
                StringUtils.isBlank(storeBaseInfoCondition.getShopkeeper()) || StringUtils.isBlank(storeBaseInfoCondition.getStoreRegionCode())) {
            logger.warn("惠小店开店基础信息保存接口 modifyStoreBaseInfo,参数错误:{}", JsonUtil.toJSONString(storeBaseInfoCondition));
            throw new BusinessException(BusinessCode.CODE_200005);
        }
        if (UserContext.getCurrentStoreUser() == null) {
            logger.error("惠小店开店基础信息保存接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店开店基础信息保存接口 门店用户编码:{}", storeCustomerId);
        logger.info("惠小店开店基础信息保存接口 入参:{}", JsonUtil.toJSONString(storeBaseInfoCondition));
        StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
        if (storeUserInfo == null) {
            logger.error("惠小店开店基础信息保存接口 modifyStoreBaseInfo,门店信息不存在:{}", storeCustomerId);
            throw new BusinessException(BusinessCode.CODE_200004);
        }
        BeanUtils.copyProperties(storeBaseInfoCondition, storeUserInfo);
        storeService.updateByPrimaryKeySelective(storeUserInfo);
        return new ResponseResult<>();
    }

    @ApiOperation(value = "惠小店开店店铺信息查询接口", notes = "惠小店开店店铺信息查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！")})
    @PostMapping(value = "/1004/v1/getStoreBusinessInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreBusinessInfoVO> getStoreBusinessInfo(@RequestBody ApiCondition apiCondition) {
        if (UserContext.getCurrentStoreUser() == null) {
            logger.error("惠小店开店店铺信息查询接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店开店店铺信息查询接口 门店用户编码:{}", storeCustomerId);
        StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
        if (storeUserInfo == null) {
            logger.error("惠小店开店店铺信息查询接口 modifyStoreBaseInfo,门店信息不存在:{}", storeCustomerId);
            throw new BusinessException(BusinessCode.CODE_200004);
        }
        StoreBusinessInfoVO storeBusinessInfoVO = new StoreBusinessInfoVO();
        BeanUtils.copyProperties(storeUserInfo, storeBusinessInfoVO);
        //判断取货方式是否是该小店的配置
        List<StoreEnumObject> pickupTypeList = new ArrayList<>();
        for (PickupTypeEnum pickupType : PickupTypeEnum.values()) {
            StoreEnumObject storeEnumObject = new StoreEnumObject();
            storeEnumObject.setCode(pickupType.getTypeCode());
            storeEnumObject.setName(pickupType.getTypeDesc());
            storeEnumObject = this.typeMatch(storeUserInfo.getPickupType(), pickupType.getTypeCode(), storeEnumObject);
            pickupTypeList.add(storeEnumObject);
            //一期先写死，只传第一个
            break;
        }
        storeBusinessInfoVO.setPickupType(pickupTypeList);
        //判断付款方式是否是该小店的配置
        List<StoreEnumObject> payTypeList = new ArrayList<>();
        for (PayTypeEnum payType : PayTypeEnum.values()) {
            StoreEnumObject storeEnumObject = new StoreEnumObject();
            storeEnumObject.setCode(payType.getTypeCode());
            storeEnumObject.setName(payType.getTypeDesc());
            storeEnumObject = this.typeMatch(storeUserInfo.getPayType(), payType.getTypeCode(), storeEnumObject);
            payTypeList.add(storeEnumObject);
            //一期先写死，只传第一个
            break;
        }
        storeBusinessInfoVO.setPayType(payTypeList);
        logger.info("惠小店开店店铺信息查询接口 返参为：{}", JsonUtil.toJSONString(storeBusinessInfoVO));
        return new ResponseResult<>(storeBusinessInfoVO);
    }

    /**
     * 匹配门店配置和全部配置的关系状态
     *
     * @param storeType 门店的多个配置类型
     * @param type      待匹配类型
     * @return 列表
     */
    private StoreEnumObject typeMatch(String storeType, short type, StoreEnumObject storeEnumObject) {
        if (StringUtils.isNotBlank(storeType)) {
            String[] typeArray = storeType.split(",");
            if (typeArray.length > 0) {
                for (String store : typeArray) {
                    if (Short.parseShort(store) == type) {
                        storeEnumObject.setStatus((short) 1);
                        break;
                    } else {
                        storeEnumObject.setStatus((short) 0);
                    }
                }
            } else {
                storeEnumObject.setStatus((short) 0);
            }
        }
        return storeEnumObject;
    }

    @ApiOperation(value = "惠小店开店店铺信息保存接口", notes = "惠小店开店店铺信息保存接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！"),
            @ApiResponse(code = BusinessCode.CODE_200006, message = "店铺营业信息保存参数错误！")})
    @PostMapping(value = "/1025/v1/modifyStoreBusinessInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreMessageAccountVO> modifyStoreBusinessInfo(@RequestBody StoreBusinessInfoCondition storeBusinessInfoCondition) {
        logger.info("惠小店开店店铺信息保存接口入参为：{}", storeBusinessInfoCondition.toString());
        if (StringUtils.isBlank(storeBusinessInfoCondition.getPickupType()) || StringUtils.isBlank(storeBusinessInfoCondition.getPayType())) {
            logger.warn("惠小店开店店铺信息保存接口 saveStoreInfo,参数错误");
            throw new BusinessException(BusinessCode.CODE_200006);
        }
        boolean storeNameMatcher = RegexConstant.STORE_NAME_PATTERN.matcher(storeBusinessInfoCondition.getStoreName()).matches();
        if (!storeNameMatcher) {
            throw new BusinessException(BusinessCode.CODE_102501, "店铺名称不能有特殊字符且长度不能超过15");
        }
        boolean shopkeeperMatcher = RegexConstant.SHOPKEEPER_PATTERN.matcher(storeBusinessInfoCondition.getShopkeeper()).matches();
        if (!shopkeeperMatcher) {
            throw new BusinessException(BusinessCode.CODE_102502, "联系人不能有特殊字符且长度不能超过10");
        }
        boolean contactMobileMatcher = RegexConstant.CONTACT_MOBILE_PATTERN.matcher(storeBusinessInfoCondition.getContactMobile()).matches();
        if (!contactMobileMatcher) {
            throw new BusinessException(BusinessCode.CODE_102503, "联系方式格式不正确");
        }
        boolean storeAddressMatcher = RegexConstant.STORE_ADDRESS_PATTERN.matcher(storeBusinessInfoCondition.getStoreAddress()).matches();
        if (!storeAddressMatcher) {
            throw new BusinessException(BusinessCode.CODE_102504, "提货地址不能有特殊字符且长度不能超过50");
        }
        if (UserContext.getCurrentStoreUser() == null) {
            logger.error("惠小店开店店铺信息保存接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店开店店铺信息保存接口 门店用户编码:{}", storeCustomerId);
        logger.info("惠小店开店店铺信息保存接口 入参:{}", JsonUtil.toJSONString(storeBusinessInfoCondition));
        StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
        if (storeUserInfo == null) {
            logger.error("惠小店开店基础信息保存接口 modifyStoreBaseInfo,门店信息不存在:{}", storeCustomerId);
            throw new BusinessException(BusinessCode.CODE_200004);
        }
        BeanUtils.copyProperties(storeBusinessInfoCondition, storeUserInfo);
        StoreMessageAccountVO storeMessageAccountVO = storeService.modifyStoreAndCreateAccount(storeUserInfo);
        ResponseResult<StoreMessageAccountVO> responseResult = new ResponseResult<>();
        responseResult.setData(storeMessageAccountVO);
        logger.info("惠小店开店店铺信息保存接口 返参为：{}", JsonUtil.toJSONString(responseResult));
        return responseResult;
    }

    @ApiOperation(value = "惠小店管理首页获取数据接口", notes = "惠小店管理首页获取数据接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！")})
    @PostMapping(value = "/1048/v1/getStoreManageInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreManageInfoVO> getStoreManageInfo(@RequestBody ApiCondition apiCondition) {
        ResponseResult<StoreManageInfoVO> responseResult = new ResponseResult<>();
        if (UserContext.getCurrentStoreUser() == null) {
            logger.error("惠小店管理首页获取数据接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        Long businessId = UserContext.getCurrentStoreUser().getBusinessId();
        logger.info("惠小店管理首页获取数据接口 门店用户编码:{}", storeCustomerId);
        StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
        StoreManageInfoVO storeManageInfoVO = this.getStoreSummaryInfo(businessId, storeCustomerId, null, null, false);
        storeManageInfoVO.setBusinessId(businessId);
        storeManageInfoVO.setStoreName(storeUserInfo.getStoreName());
        responseResult.setData(storeManageInfoVO);
        logger.info("惠小店管理首页获取数据接口 返参为：{}", JsonUtil.toJSONString(responseResult));
        return responseResult;
    }

    @ApiOperation(value = "根据门店的id查询门店的信息[C端在没有登录时通过storeId查询信息，不会校验token]")
    @PostMapping(value = "/security/1056/v1/findStoreInfoByStoreId")
    public ResponseResult<StoreUserInfoVO> findStoreInfoByStoreId(@RequestBody StoreUserInfoCondition condition) {
        ResponseResult<StoreUserInfoVO> responseResult = new ResponseResult<>();
        if (condition.getId() == null) {
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        StoreUserInfoVO storeUserInfoVO = storeService.findStoreUserInfo(condition.getId());
        if (storeUserInfoVO != null) {
            storeUserInfoVO.setMonthlySales(queryMonthlySkuQuantity(storeUserInfoVO.getId()));
        }
        responseResult.setData(storeUserInfoVO);
        return responseResult;
    }

    /**
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/10 15:17
     * @Description 根据token查询用户绑定的门店信息
     */
    @ApiOperation(value = "根据用户token查询绑定门店信息，有则返回，没有则不返回[C端在登录的情况下有token时调用]")
    @PostMapping(value = "/1029/v1/findBindingStoreInfo")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功，如果有绑定的门店则返回门店信息否则不返回")})
    public ResponseResult<StoreUserInfoVO> findBindingStoreInfo(ApiCondition apiCondition) {
        ResponseResult<StoreUserInfoVO> result = new ResponseResult<>();
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        StoreUserInfoVO storeUserInfoVO = storeService.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
        if (storeUserInfoVO != null) {
            //设置月销售量
            storeUserInfoVO.setMonthlySales(queryMonthlySkuQuantity(storeUserInfoVO.getId()));
            result.setData(storeUserInfoVO);
        }
        return result;

    }

    /**
     * @param
     * @return
     * @throws
     * @author chengyy
     * @date 2018/8/15 13:34
     * @Description 根据门店id查询月销售量
     */
    public Integer queryMonthlySkuQuantity(Long storeId) {
        if (storeId == null) {
            return null;
        }
        StoreOrderSalesSummaryCondition condition = new StoreOrderSalesSummaryCondition();
        Date now = new Date();
        condition.setStoreId(storeId);
        condition.setQueryPeriodType(StoreOrderSalesSummaryCondition.MONTH_ORDER_SALES_QUERY_TYPE);
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO = orderServiceClient.queryStoreOrderSalesSummaryByDateTimePeriod(condition).getData();
        if (storeOrderSalesSummaryVO != null) {
            return storeOrderSalesSummaryVO.getSkuCategoryQuantity();
        }
        return null;
    }

    /**
     * @return StoreUserInfoVO 返回当前门店信息数据
     * @author chengyy
     * @date 2018/8/3 16:04
     * @Description 获取门店信息
     */
    @ApiOperation(value = "查询门店信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"), @ApiResponse(code = BusinessCode.CODE_200002, message = "门店id参数为空")})
    @RequestMapping(value = "/1005/v1/findStoreUserInfo", method = RequestMethod.POST)
    public ResponseResult<StoreUserInfoVO> findStoreUserInfo(ApiCondition apiCondition) {
        ResponseResult<StoreUserInfoVO> result = new ResponseResult<>();
        StoreUser storeUser = UserContext.getCurrentStoreUser();
        if (storeUser == null) {
            logger.error("ApiOpenStoreController -> findStoreUserInfo未获取到门店登录信息");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        if (storeUser.getBusinessId() == null) {
            logger.error("StoreServiceController -> findStoreUserInfo获取当前登录用户的id为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        StoreUserInfoVO data = storeService.findStoreUserInfo(storeUser.getBusinessId());
        if (data == null) {
            result.setCode(BusinessCode.CODE_200004);
        }
        result.setData(data);
        return result;
    }

    @ApiOperation(value = "惠小店获取营业数据查询接口", notes = "惠小店获取营业数据查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！")})
    @PostMapping(value = "/1026/v1/getStoreTurnoverInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreManageInfoVO> getStoreTurnoverInfo(@RequestBody ApiCondition apiCondition) {
        ResponseResult<StoreManageInfoVO> responseResult = new ResponseResult<>();
        if (UserContext.getCurrentStoreUser() == null) {
            responseResult.setCode(BusinessCode.CODE_1001);
            logger.error("惠小店获取营业数据查询接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        Long businessId = UserContext.getCurrentStoreUser().getBusinessId();
        logger.info("惠小店获取营业数据查询接口 门店用户编码:{}", storeCustomerId);
        Date currentDate = new Date();
        Date beginTime = DateUtils.truncate(currentDate, Calendar.DATE);
        Date yesterdayEndTime = DateUtils.addDays(currentDate, -1);
        Date yesterdayBeginTime = DateUtils.addDays(beginTime, -1);
        //今日的
        StoreManageInfoVO todayInfo = this.getStoreSummaryInfo(businessId, storeCustomerId, null, null, false);
        //昨日的
        StoreManageInfoVO yesterdayInfo = this.getStoreSummaryInfo(businessId, storeCustomerId, yesterdayBeginTime, yesterdayEndTime, true);
        //对比昨日百分比
        todayInfo.setTurnoverCompare(calculatePercent(todayInfo.getTurnover(), yesterdayInfo.getTurnover()));
        todayInfo.setBrowseNumCompare(calculatePercent(new BigDecimal(todayInfo.getBrowseNum()), new BigDecimal(yesterdayInfo.getBrowseNum())));
        todayInfo.setCreateNumCompare(calculatePercent(new BigDecimal(todayInfo.getCreateNum()), new BigDecimal(yesterdayInfo.getCreateNum())));
        todayInfo.setCompleteNumCompare(calculatePercent(new BigDecimal(todayInfo.getCompleteNum()), new BigDecimal(yesterdayInfo.getCompleteNum())));
        responseResult.setData(todayInfo);
        logger.info("惠小店获取营业数据查询接口 返参为：{}", JsonUtil.toJSONString(responseResult));
        return responseResult;
    }

    /**
     * 查询门店营业信息，包括营业额、浏览人数、下单人数、订单数
     *
     * @param businessId      门店主键id
     * @param storeCustomerId 门店用户id
     * @param startDatetime   开始时间
     * @param endDatetime     结束时间
     * @param flag            是否查询昨日
     * @return
     */
    private StoreManageInfoVO getStoreSummaryInfo(Long businessId, Long storeCustomerId, Date startDatetime, Date endDatetime, boolean flag) {
        //浏览人数
        Integer browseNum = storeBrowseLogService.getBrowseNum(storeCustomerId, startDatetime, endDatetime);
        StoreOrderSalesSummaryCondition todayCondition = new StoreOrderSalesSummaryCondition();
        if (flag) {
            todayCondition.setStoreId(businessId);
            todayCondition.setQueryPeriodType(StoreOrderSalesSummaryCondition.TIME_PERIOD_ORDER_SALES_QUERY_TYPE);
            todayCondition.setStartDateTime(startDatetime);
            todayCondition.setEndDateTime(endDatetime);
        } else {
            todayCondition.setStoreId(businessId);
            todayCondition.setQueryPeriodType(StoreOrderSalesSummaryCondition.INTRADAY_ORDER_SALES_QUERY_TYPE);
        }
        //营业额、下单人数、订单数
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO =
                orderServiceClient.queryStoreOrderSalesSummaryByDateTimePeriod(todayCondition).getData();
        StoreManageInfoVO storeManageInfoVO = new StoreManageInfoVO();
        storeManageInfoVO.setBrowseNum(browseNum);
        if (storeOrderSalesSummaryVO != null) {
            storeManageInfoVO.setTurnover(storeOrderSalesSummaryVO.getTurnover());
            storeManageInfoVO.setCreateNum(storeOrderSalesSummaryVO.getCustomerNum());
            storeManageInfoVO.setCompleteNum(storeOrderSalesSummaryVO.getOrderNum());
        } else {
            storeManageInfoVO.setTurnover(BigDecimal.ZERO);
            storeManageInfoVO.setCreateNum(0);
            storeManageInfoVO.setCompleteNum(0);
        }
        return storeManageInfoVO;
    }

    /**
     * 计算百分比，保留2位小数
     *
     * @param a
     * @param b
     * @return
     */
    private static String calculatePercent(BigDecimal a, BigDecimal b) {
        String result = "暂无对比数据";
        if (a.compareTo(BigDecimal.ZERO) != 0 && b.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal percent = a.subtract(b).multiply(new BigDecimal(100)).divide(b, 2, RoundingMode.HALF_UP);
            if (percent.compareTo(BigDecimal.ZERO) > 0) {
                result = "+" + percent + "%";
            } else {
                result = percent + "%";
            }
        }
        return result;
    }
}