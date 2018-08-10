package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.StoreOrderSalesSummaryCondition;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;
import com.winhxd.b2c.common.domain.store.condition.StoreBaseInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreBusinessInfoCondition;
import com.winhxd.b2c.common.domain.store.vo.OpenStoreVO;
import com.winhxd.b2c.common.domain.store.vo.StoreBaseInfoVO;
import com.winhxd.b2c.common.domain.store.vo.StoreBusinessInfoVO;
import com.winhxd.b2c.common.domain.store.vo.StoreManageInfoVO;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.hxd.StoreHxdServiceClient;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.store.service.StoreBrowseLogService;
import com.winhxd.b2c.store.service.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;


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

    @Resource(name = "storeBrowseLogService")
    private StoreBrowseLogService storeBrowseLogService;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @ApiOperation(value = "惠小店开店条件验证接口", notes = "惠小店开店条件验证接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class)})
    @PostMapping(value = "/1000/v1/checkStoreInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OpenStoreVO> checkStoreInfo() {
        ResponseResult<OpenStoreVO> responseResult = new ResponseResult<>();
//        if(UserContext.getCurrentStoreUser() == null){
//            responseResult.setCode(BusinessCode.CODE_1001);
//            logger.info("惠小店开店条件验证接口 未获取到当前用户信息");
//            throw new BusinessException(BusinessCode.CODE_1001);
//        }
        try {

//            Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
            Long storeCustomerId = 1607479L;
            logger.info("惠小店开店基础信息查询接口 门店用户编码:{}", storeCustomerId);
            //是否开过小店
            StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
            if (storeUserInfo == null) {
                responseResult.setCode(BusinessCode.CODE_200004);
                responseResult.setMessage("门店信息不存在");
                logger.info("惠小店开店条件验证接口 门店信息不存在 storeCustomerId：{}", storeCustomerId);
                return responseResult;
            }
            OpenStoreVO openStoreVO = new OpenStoreVO();
            if (storeUserInfo.getStoreStatus() != 0) {
                openStoreVO.setStoreStatus((byte) 1);
                responseResult.setData(openStoreVO);
                return responseResult;
            } else {
                openStoreVO.setStoreStatus((byte) 0);
            }
            //是否完善信息
            ResponseResult<List<Integer>> noPerfectResult = storeHxdServiceClient.getStorePerfectInfo(storeCustomerId.toString());
            if (noPerfectResult.getCode() == 1) {
                List<Integer> list = noPerfectResult.getData();
                byte flag = 1;
                for(int i: list){
                    if (i == 0) {
                        flag = 0;
                        break;
                    }
                }
                openStoreVO.setPerfectStatus(flag);
                openStoreVO.setNoPerfectMessage(noPerfectResult.getData());
                responseResult.setData(openStoreVO);
            } else {
                responseResult.setCode(BusinessCode.CODE_1001);
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
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class)})
    @PostMapping(value = "/1001/v1/getStoreBaseInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreBaseInfoVO> getStoreBaseInfo() {
        ResponseResult<StoreBaseInfoVO> responseResult = new ResponseResult<>();

//        if(UserContext.getCurrentStoreUser() == null){
//            responseResult.setCode(BusinessCode.CODE_1001);
//            logger.info("惠小店开店基础信息查询接口 未获取到当前用户信息");
//            throw new BusinessException(BusinessCode.CODE_1001);
//        }
        try {
//            Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
            Long storeCustomerId = 1607479L;
            logger.info("惠小店开店基础信息查询接口 门店用户编码:{}", storeCustomerId);
            ResponseResult<Object> result = storeHxdServiceClient.getStoreBaseInfo(storeCustomerId.toString());
            StoreBaseInfoVO storeBaseInfoVO = new StoreBaseInfoVO();
            BeanUtils.copyProperties(result,storeBaseInfoVO);
            responseResult.setData(storeBaseInfoVO);
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
    @PostMapping(value = "/1002/v1/modifyStoreBaseInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreBusinessInfoVO> modifyStoreBaseInfo(@RequestBody StoreBaseInfoCondition storeBaseInfoCondition) {
        logger.info("惠小店开店基础信息保存接口入参为：{}", storeBaseInfoCondition.toString());
        if (StringUtils.isBlank(storeBaseInfoCondition.getStoreAddress()) || StringUtils.isBlank(storeBaseInfoCondition.getStoreName()) ||
                StringUtils.isBlank(storeBaseInfoCondition.getShopOwnerImg()) || StringUtils.isBlank(storeBaseInfoCondition.getShopkeeper()) ||
                StringUtils.isBlank(storeBaseInfoCondition.getStoreRegionCode())) {
            logger.error("惠小店开店基础信息保存接口 modifyStoreBaseInfo,参数错误:{}", JsonUtil.toJSONString(storeBaseInfoCondition));
            throw new BusinessException(BusinessCode.CODE_200005);
        }
        ResponseResult<StoreBusinessInfoVO> responseResult = new ResponseResult<>();
//        if(UserContext.getCurrentStoreUser() == null){
//            responseResult.setCode(BusinessCode.CODE_1001);
//            logger.info("惠小店开店基础信息保存接口 未获取到当前用户信息");
//            throw new BusinessException(BusinessCode.CODE_1001);
//        }
        try {
//            Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
            Long storeCustomerId = 1607479L;
            logger.info("惠小店开店基础信息保存接口 门店用户编码:{}", storeCustomerId);
            StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
            if (storeUserInfo == null) {
                logger.error("惠小店开店基础信息保存接口 modifyStoreBaseInfo,门店信息不存在:{}", storeCustomerId);
                throw new BusinessException(BusinessCode.CODE_200004);
            }
            BeanUtils.copyProperties(storeBaseInfoCondition, storeUserInfo);
            //开店状态
            storeUserInfo.setStoreStatus((short) 1);
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
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200006, message = "店铺营业信息保存参数错误！", response = ResponseResult.class)})
    @PostMapping(value = "/1003/v1/modifyStoreBusinessInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult modifyStoreBusinessInfo(@RequestBody StoreBusinessInfoCondition storeBusinessInfoCondition) {
        logger.info("惠小店开店店铺信息保存接口入参为：{}", storeBusinessInfoCondition.toString());
        if (StringUtils.isBlank(storeBusinessInfoCondition.getStoreName()) || storeBusinessInfoCondition.getPickupWay() == null ||
                storeBusinessInfoCondition.getPaymentWay() == null || StringUtils.isBlank(storeBusinessInfoCondition.getShopkeeper()) ||
                StringUtils.isBlank(storeBusinessInfoCondition.getContactMobile()) || StringUtils.isBlank(storeBusinessInfoCondition.getStoreAddress())) {
            logger.error("惠小店开店店铺信息保存接口 saveStoreInfo,参数错误:{}", JsonUtil.toJSONString(storeBusinessInfoCondition));
            throw new BusinessException(BusinessCode.CODE_200006);
        }
        ResponseResult responseResult = new ResponseResult();
//        if(UserContext.getCurrentStoreUser() == null){
//            responseResult.setCode(BusinessCode.CODE_1001);
//            logger.info("惠小店开店店铺信息保存接口 未获取到当前用户信息");
//            throw new BusinessException(BusinessCode.CODE_1001);
//        }
        try {
//            Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
            Long storeCustomerId = 1607479L;
            logger.info("惠小店开店店铺信息保存接口 门店用户编码:{}", storeCustomerId);
            StoreUserInfo storeUserInfo = storeService.findByStoreCustomerId(storeCustomerId);
            if (storeUserInfo == null) {
                logger.error("惠小店开店基础信息保存接口 modifyStoreBaseInfo,门店信息不存在:{}", storeCustomerId);
                throw new BusinessException(BusinessCode.CODE_200004);
            }
            BeanUtils.copyProperties(storeBusinessInfoCondition, storeUserInfo);
            storeService.updateByPrimaryKeySelective(storeUserInfo);
        } catch (Exception e) {
            logger.error("惠小店开店店铺信息保存接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店管理首页获取数据接口", notes = "惠小店管理首页获取数据接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = StoreManageInfoVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "/1004/v1/getStoreManageInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreManageInfoVO> getStoreManageInfo() {
        ResponseResult<StoreManageInfoVO> responseResult = new ResponseResult<>();
//        if(UserContext.getCurrentStoreUser() == null){
//            responseResult.setCode(BusinessCode.CODE_1001);
//            logger.info("惠小店管理首页获取数据接口 未获取到当前用户信息");
//            throw new BusinessException(BusinessCode.CODE_1001);
//        }
        try {
//            Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
            Long storeCustomerId = 1607479L;
            logger.info("惠小店管理首页获取数据接口 门店用户编码:{}", storeCustomerId);
            Date currentDate = new Date();
            Date beginTime = getBeginTimeOfDate(currentDate);
            responseResult.setData(this.getStoreSummaryInfo(storeCustomerId, beginTime, currentDate));
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

    public ResponseResult<StoreUserInfoVO> findStoreUserInfo(@PathVariable("id")Long id){
        ResponseResult<StoreUserInfoVO> result = new ResponseResult<>();
        if(UserContext.getCurrentStoreUser() == null){
            logger.error("ApiOpenStoreController -> findStoreUserInfo未获取到门店登录信息");
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        if(id == null){
            logger.error("StoreServiceController -> findStoreUserInfo获取的参数storeUserId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
        StoreUserInfoVO data = storeService.findStoreUserInfo(storeCustomerId);
        if(data == null){
            result.setCode(BusinessCode.CODE_200004);
        }
        result.setData(data);
        return result;
    }

    @ApiOperation(value = "惠小店是否在测试区域接口", notes = "惠小店是否在测试区域接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Integer.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "/1025/v1/getStoreRegionStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> getStoreRegionStatus() {
        ResponseResult<Integer> responseResult = new ResponseResult<>();

        if(UserContext.getCurrentStoreUser() == null){
            responseResult.setCode(BusinessCode.CODE_1001);
            logger.info("惠小店开店基础信息查询接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        try {
            Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
            logger.info("惠小店是否在测试区域接口 门店用户编码:{}", storeCustomerId);

            responseResult.setData(0);
        } catch (Exception e) {
            logger.error("惠小店是否在测试区域接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "惠小店获取营业查询数据接口", notes = "惠小店获取营业查询数据接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = StoreManageInfoVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "/1026/v1/getStoreTurnoverInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreManageInfoVO> getStoreTurnoverInfo() {
        ResponseResult<StoreManageInfoVO> responseResult = new ResponseResult<>();
        if(UserContext.getCurrentStoreUser() == null){
            responseResult.setCode(BusinessCode.CODE_1001);
            logger.info("惠小店获取营业查询数据接口 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        try {
            Long storeCustomerId = UserContext.getCurrentStoreUser().getStoreCustomerId();
            logger.info("惠小店获取营业查询数据接口 门店用户编码:{}", storeCustomerId);
            Date currentDate = new Date();
            Date beginTime = getBeginTimeOfDate(currentDate);
            Date yesterdayEndTime = DateUtils.addDays(currentDate,-1);
            Date yesterdayBeginTime = DateUtils.addDays(beginTime,-1);
            //今日的
            StoreManageInfoVO todayInfo = this.getStoreSummaryInfo(storeCustomerId, beginTime, currentDate);
            //昨日的
            StoreManageInfoVO yeaterdayInfo = this.getStoreSummaryInfo(storeCustomerId, yesterdayBeginTime, yesterdayEndTime);

            responseResult.setData(todayInfo);
        } catch (Exception e) {
            logger.error("惠小店获取营业查询数据接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    /**
     * 查询门店营业信息，包括营业额、浏览人数、下单人数、订单数
     *
     * @param storeCustomerId 门店用户id
     * @param startDatetime 开始时间
     * @param endDatetime 结束时间
     * @return
     */
    private StoreManageInfoVO getStoreSummaryInfo(Long storeCustomerId, Date startDatetime, Date endDatetime){
        //浏览人数
        Integer browseNum = storeBrowseLogService.getBrowseNum(storeCustomerId, startDatetime, endDatetime);
        StoreOrderSalesSummaryCondition todayCondition = new StoreOrderSalesSummaryCondition();
        todayCondition.setStartDateTime(startDatetime);
        todayCondition.setEndDateTime(endDatetime);
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO =
                orderServiceClient.queryStoreOrderSalesSummaryByDateTimePeriod(todayCondition).getData();
        StoreManageInfoVO storeManageInfoVO = new StoreManageInfoVO();
        storeManageInfoVO.setBrowseNum(browseNum);
        if(storeOrderSalesSummaryVO != null) {
            storeManageInfoVO.setTurnover(storeOrderSalesSummaryVO.getDailyTurnover());
            storeManageInfoVO.setCreateNum(storeOrderSalesSummaryVO.getDailyCustomerNum());
            storeManageInfoVO.setCompleteNum(storeOrderSalesSummaryVO.getDailyOrderNum());
        } else {
            storeManageInfoVO.setTurnover(BigDecimal.ZERO);
            storeManageInfoVO.setCreateNum(0);
            storeManageInfoVO.setCompleteNum(0);
        }
        return storeManageInfoVO;
    }

    /**
     * <p>
     * Gets first time of {@code date}
     * </p>
     *
     * @param date
     *            may be null
     * @return {@code Date} first time 00:00:00 or null
     */
    private static Date getBeginTimeOfDate(final Date date) {
        Date beginTime = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            beginTime = calendar.getTime();
        }
        return beginTime;
    }
}