package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.order.condition.StoreOrderSalesSummaryCondition;
import com.winhxd.b2c.common.domain.order.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;
import com.winhxd.b2c.common.domain.store.condition.StoreBaseInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreBusinessInfoCondition;
import com.winhxd.b2c.common.domain.store.model.StoreRegion;
import com.winhxd.b2c.common.domain.store.vo.*;
import com.winhxd.b2c.common.domain.system.login.enums.StoreStatusEnum;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
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
import org.apache.catalina.User;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@RequestMapping(value = "/api-store/store/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiOpenStoreController {

    private static final Logger logger = LoggerFactory.getLogger(ApiOpenStoreController.class);

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreHxdServiceClient storeHxdServiceClient;

    @Resource(name = "storeBrowseLogService")
    private StoreBrowseLogService storeBrowseLogService;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private StoreRegionService storeRegionService;

    @ApiOperation(value = "惠小店是否在测试区域接口", notes = "惠小店是否在测试区域接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "服务器内部错误！")})
    @PostMapping(value = "/1000/v1/getStoreRegionStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenStoreVO> getStoreRegionStatus(@RequestBody ApiCondition apiCondition) {
        if (UserContext.getCurrentStoreUser() == null) {
            logger.error("惠小店开店基础信息查询接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店是否在测试区域接口 门店用户编码:{}", storeCustomerId);
        ResponseResult<OpenStoreVO> responseResult = new ResponseResult<>();
        //是否有登录注册信息
        StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
        if (storeUserInfo == null) {
            responseResult.setCode(BusinessCode.CODE_200004);
            responseResult.setMessage("门店信息不存在");
            logger.error("惠小店是否在测试区域接口 门店信息不存在 storeCustomerId：{}", storeCustomerId);
            return responseResult;
        }
        OpenStoreVO openStoreVO = new OpenStoreVO();
        //是否在测试区域
        StoreRegion storeRegion = storeRegionService.getByRegionCode(storeUserInfo.getStoreRegionCode());
        if (storeRegion != null) {
            openStoreVO.setRegionStatus((byte) 1);
        } else {
            openStoreVO.setRegionStatus((byte) 0);
            openStoreVO.setStoreStatus((byte) 0);
            responseResult.setData(openStoreVO);
            return responseResult;
        }
        //是否开过小店，0未开店
        if (storeUserInfo.getStoreStatus() != 0) {
            openStoreVO.setStoreStatus((byte) 1);
            responseResult.setData(openStoreVO);
            return responseResult;
        } else {
            openStoreVO.setStoreStatus((byte) 0);
        }

        responseResult.setData(openStoreVO);
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店条件验证接口", notes = "惠小店开店条件验证接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！")})
    @PostMapping(value = "/1001/v1/checkStoreInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenStoreVO> checkStoreInfo(@RequestBody ApiCondition apiCondition) {
        if (UserContext.getCurrentStoreUser() == null) {
            logger.error("惠小店开店条件验证接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店开店基础信息查询接口 门店用户编码:{}", storeCustomerId);
        ResponseResult<OpenStoreVO> responseResult = new ResponseResult<>();
        //查询账号基础信息
        StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
        if (storeUserInfo == null) {
            responseResult.setCode(BusinessCode.CODE_200004);
            responseResult.setMessage("门店信息不存在");
            logger.error("惠小店开店条件验证接口 门店信息不存在 storeCustomerId：{}", storeCustomerId);
            return responseResult;
        }
        OpenStoreVO openStoreVO = new OpenStoreVO();
        //是否完善信息
        ResponseResult<List<Integer>> noPerfectResult = storeHxdServiceClient.getStorePerfectInfo(storeCustomerId.toString());
        openStoreVO.setNoPerfectMessage(noPerfectResult.getData());
        responseResult.setData(openStoreVO);
        logger.info("惠小店开店条件验证接口返参为：{}", JsonUtil.toJSONString(responseResult));
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
        BeanUtils.copyProperties(result, storeBaseInfoVO);
        if (!result.getData().isEmpty()) {
            Map<String, Object> map = result.getData();
            storeBaseInfoVO.setStoreImg(Objects.toString(map.get("storeImg"), ""));
            storeBaseInfoVO.setStoreName(Objects.toString(map.get("storeName"), ""));
            storeBaseInfoVO.setShopkeeper(Objects.toString(map.get("shopkeeper"), ""));
            storeBaseInfoVO.setShopOwnerImg(StringUtils.isBlank(storeUserInfo.getShopOwnerImg()) ? Objects.toString(map.get("shopOwnerImg"), "") : storeUserInfo.getShopOwnerImg());
            storeBaseInfoVO.setProvince(Objects.toString(map.get("province"), ""));
            storeBaseInfoVO.setCity(Objects.toString(map.get("city"), ""));
            storeBaseInfoVO.setCounty(Objects.toString(map.get("county"), ""));
            storeBaseInfoVO.setStoreRegionCode(Objects.toString(map.get("storeRegionCode"), ""));
            storeBaseInfoVO.setStoreAddress(Objects.toString(map.get("storeAddress"), ""));
        } else {
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage(StringUtils.isBlank(result.getMessage()) ? "请求发送失败" : result.getMessage());
            return responseResult;
        }
        responseResult.setData(storeBaseInfoVO);
        return responseResult;
    }

    @ApiOperation(value = "惠小店开店基础信息保存接口", notes = "惠小店开店基础信息保存接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！"),
            @ApiResponse(code = BusinessCode.CODE_200005, message = "门店基础信息保存参数错误！")})
    @PostMapping(value = "/1003/v1/modifyStoreBaseInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> modifyStoreBaseInfo(@RequestBody StoreBaseInfoCondition storeBaseInfoCondition) {
        logger.info("惠小店开店基础信息保存接口入参为：{}", storeBaseInfoCondition.toString());
        if (StringUtils.isBlank(storeBaseInfoCondition.getStoreAddress()) || StringUtils.isBlank(storeBaseInfoCondition.getStoreName()) ||
                StringUtils.isBlank(storeBaseInfoCondition.getShopOwnerImg()) || StringUtils.isBlank(storeBaseInfoCondition.getShopkeeper()) ||
                StringUtils.isBlank(storeBaseInfoCondition.getStoreRegionCode())) {
            logger.warn("惠小店开店基础信息保存接口 modifyStoreBaseInfo,参数错误:{}", JsonUtil.toJSONString(storeBaseInfoCondition));
            throw new BusinessException(BusinessCode.CODE_200005);
        }
        if (UserContext.getCurrentStoreUser() == null) {
            logger.error("惠小店开店基础信息保存接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店开店基础信息保存接口 门店用户编码:{}", storeCustomerId);
        StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
        if (storeUserInfo == null) {
            logger.error("惠小店开店基础信息保存接口 modifyStoreBaseInfo,门店信息不存在:{}", storeCustomerId);
            throw new BusinessException(BusinessCode.CODE_200004);
        }
        BeanUtils.copyProperties(storeBaseInfoCondition, storeUserInfo);
        //开店状态 有效
        storeUserInfo.setStoreStatus(StoreStatusEnum.VALID.getStatusCode());
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
        List<StoreEnumObject> pickupTypeList = new ArrayList<>();
        for (PickUpTypeEnum pickupType : PickUpTypeEnum.values()) {
            StoreEnumObject storeEnumObject = new StoreEnumObject();
            storeEnumObject.setCode(pickupType.getTypeCode());
            storeEnumObject.setName(pickupType.getTypeDesc());
            pickupTypeList.add(storeEnumObject);
            //一期先写死，只传第一个
            break;
        }
        storeBusinessInfoVO.setPickupType(pickupTypeList);
        List<StoreEnumObject> payTypeList = new ArrayList<>();
        for (PayTypeEnum payType : PayTypeEnum.values()) {
            StoreEnumObject storeEnumObject = new StoreEnumObject();
            storeEnumObject.setCode(payType.getTypeCode());
            storeEnumObject.setName(payType.getTypeDesc());
            payTypeList.add(storeEnumObject);
            //一期先写死，只传第一个
            break;
        }
        storeBusinessInfoVO.setPayType(payTypeList);
        return new ResponseResult<>(storeBusinessInfoVO);
    }

    @ApiOperation(value = "惠小店开店店铺信息保存接口", notes = "惠小店开店店铺信息保存接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！"),
            @ApiResponse(code = BusinessCode.CODE_200006, message = "店铺营业信息保存参数错误！")})
    @PostMapping(value = "/1025/v1/modifyStoreBusinessInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> modifyStoreBusinessInfo(@RequestBody StoreBusinessInfoCondition storeBusinessInfoCondition) {
        logger.info("惠小店开店店铺信息保存接口入参为：{}", storeBusinessInfoCondition.toString());
        if (StringUtils.isBlank(storeBusinessInfoCondition.getStoreName()) || storeBusinessInfoCondition.getPickupWay() == null ||
                storeBusinessInfoCondition.getPaymentWay() == null || StringUtils.isBlank(storeBusinessInfoCondition.getShopkeeper()) ||
                StringUtils.isBlank(storeBusinessInfoCondition.getContactMobile()) || StringUtils.isBlank(storeBusinessInfoCondition.getStoreAddress())) {
            logger.warn("惠小店开店店铺信息保存接口 saveStoreInfo,参数错误:{}", JsonUtil.toJSONString(storeBusinessInfoCondition));
            throw new BusinessException(BusinessCode.CODE_200006);
        }
        if (UserContext.getCurrentStoreUser() == null) {
            logger.error("惠小店开店店铺信息保存接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店开店店铺信息保存接口 门店用户编码:{}", storeCustomerId);
        StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
        if (storeUserInfo == null) {
            logger.error("惠小店开店基础信息保存接口 modifyStoreBaseInfo,门店信息不存在:{}", storeCustomerId);
            throw new BusinessException(BusinessCode.CODE_200004);
        }
        BeanUtils.copyProperties(storeBusinessInfoCondition, storeUserInfo);
        storeService.updateByPrimaryKeySelective(storeUserInfo);
        return new ResponseResult<>();
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
        Long businessId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店管理首页获取数据接口 门店用户编码:{}", storeCustomerId);
        Date currentDate = new Date();
        responseResult.setData(this.getStoreSummaryInfo(businessId, storeCustomerId, DateUtils.truncate(currentDate, Calendar.DATE), currentDate));
        return responseResult;
    }

    /**
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/10 15:17
     * @Description 根据token查询用户绑定的门店信息
     */
    @ApiOperation(value = "根据用户token查询绑定门店信息，有则返回，没有则不返回")
    @PostMapping(value = "/security/1029/v1/findBindingStoreInfo")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功，如果有绑定的门店则返回门店信息否则不返回")})
    public ResponseResult<StoreUserInfoVO> findBindingStoreInfo(ApiCondition apiCondition) {
        ResponseResult<StoreUserInfoVO> result = new ResponseResult<>();
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        StoreUserInfoVO storeUserInfoVO = storeService.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
        if (storeUserInfoVO != null) {
            StoreOrderSalesSummaryCondition condition = new StoreOrderSalesSummaryCondition();
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DATE, -30);
            condition.setEndDateTime(now);
            condition.setStartDateTime(calendar.getTime());
            condition.setStoreId(storeUserInfoVO.getId());
            StoreOrderSalesSummaryVO storeOrderSalesSummaryVO = orderServiceClient.queryStoreOrderSalesSummaryByDateTimePeriod(condition).getData();
            //设置月销售量
            storeUserInfoVO.setMonthlySales(storeOrderSalesSummaryVO.getSkuQuantity());
            result.setData(storeUserInfoVO);
        }
        return result;
    }

    /**
     * @return StoreUserInfoVO 返回当前门店信息数据
     * @author chengyy
     * @date 2018/8/3 16:04
     * @Description 获取门店信息
     */
    @ApiOperation(value = "通过门店id查询门店信息")
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

    @ApiOperation(value = "惠小店获取营业查询数据接口", notes = "惠小店获取营业查询数据接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！")})
    @PostMapping(value = "/1026/v1/getStoreTurnoverInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreManageInfoVO> getStoreTurnoverInfo(@RequestBody ApiCondition apiCondition) {
        ResponseResult<StoreManageInfoVO> responseResult = new ResponseResult<>();
        if (UserContext.getCurrentStoreUser() == null) {
            responseResult.setCode(BusinessCode.CODE_1001);
            logger.error("惠小店获取营业查询数据接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        Long businessId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        logger.info("惠小店获取营业查询数据接口 门店用户编码:{}", storeCustomerId);
        Date currentDate = new Date();
        Date beginTime = DateUtils.truncate(currentDate, Calendar.DATE);
        Date yesterdayEndTime = DateUtils.addDays(currentDate, -1);
        Date yesterdayBeginTime = DateUtils.addDays(beginTime, -1);
        //今日的
        StoreManageInfoVO todayInfo = this.getStoreSummaryInfo(businessId, storeCustomerId, beginTime, currentDate);
        //昨日的
        StoreManageInfoVO yesterdayInfo = this.getStoreSummaryInfo(businessId, storeCustomerId, yesterdayBeginTime, yesterdayEndTime);
        //对比昨日百分比
        todayInfo.setTurnoverCompare(calculatePercent(todayInfo.getTurnover(), yesterdayInfo.getTurnover()));
        todayInfo.setBrowseNumCompare(calculatePercent(new BigDecimal(todayInfo.getBrowseNum()), new BigDecimal(yesterdayInfo.getBrowseNum())));
        todayInfo.setCreateNumCompare(calculatePercent(new BigDecimal(todayInfo.getCreateNum()), new BigDecimal(yesterdayInfo.getCreateNum())));
        todayInfo.setCompleteNumCompare(calculatePercent(new BigDecimal(todayInfo.getCompleteNum()), new BigDecimal(yesterdayInfo.getCompleteNum())));
        responseResult.setData(todayInfo);
        return responseResult;
    }

    /**
     * 查询门店营业信息，包括营业额、浏览人数、下单人数、订单数
     *
     * @param businessId      门店主键id
     * @param storeCustomerId 门店用户id
     * @param startDatetime   开始时间
     * @param endDatetime     结束时间
     * @return
     */
    private StoreManageInfoVO getStoreSummaryInfo(Long businessId, Long storeCustomerId, Date startDatetime, Date endDatetime) {
        //浏览人数
        Integer browseNum = storeBrowseLogService.getBrowseNum(storeCustomerId, startDatetime, endDatetime);
        StoreOrderSalesSummaryCondition todayCondition = new StoreOrderSalesSummaryCondition();
        todayCondition.setStoreId(businessId);
        todayCondition.setStartDateTime(startDatetime);
        todayCondition.setEndDateTime(endDatetime);
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
            result = a.subtract(b).multiply(new BigDecimal(100)).divide(b, 2, RoundingMode.HALF_UP).toBigInteger().toString();
        }
        return result;
    }
}