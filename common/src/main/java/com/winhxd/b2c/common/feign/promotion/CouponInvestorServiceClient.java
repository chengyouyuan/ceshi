package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author wl
 * @Date 2018/8/6 09:35
 * @Description  出资方管理相关接口
 **/

@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponInvestorServiceFallback.class)
public interface CouponInvestorServiceClient {
    /**
     *
     *@Deccription 添加出资方
     *@Params  condition
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/7 20:59
     */
    @RequestMapping(value = "/promotion/v1/addCouponInvestor", method = RequestMethod.POST)
    ResponseResult addCouponInvestor(@RequestBody CouponInvestorCondition condition);

    /**
     *
     *@Deccription 查看出资方详情
     *@Params
     *@Return
     *@User  wl
     *@Date   2018/8/8 13:59
     */
    @RequestMapping(value = "/promotion/v1/viewCouponInvestorDetail", method = RequestMethod.GET)
    ResponseResult viewCouponInvestorDetail(@RequestParam("id") String id);

    /**
     *
     *@Deccription 删除出资方（非物理删除）
     *@Params  id
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/8 14:47
     */
    @RequestMapping(value = "/promotion/v1/updateCouponInvestorToValid", method = RequestMethod.GET)
    ResponseResult updateCouponInvestorToValid(@RequestParam("id") String id,@RequestParam("userId")String userId,@RequestParam("userName")String userName);

}

@Component
class CouponInvestorServiceFallback implements CouponInvestorServiceClient{
    private static final Logger logger = LoggerFactory.getLogger(CouponInvestorServiceClient.class);
    private Throwable throwable;

    @Override
    public ResponseResult addCouponInvestor(CouponInvestorCondition condition) {
        logger.error("CouponInvestorServiceClient -> addCouponInvestor", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult viewCouponInvestorDetail(String id) {
        logger.error("CouponInvestorServiceClient -> viewCouponInvestorDetail", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult updateCouponInvestorToValid(String id,String userId,String userName) {
        logger.error("CouponInvestorServiceClient -> updateCouponInvestorToValid", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

}
