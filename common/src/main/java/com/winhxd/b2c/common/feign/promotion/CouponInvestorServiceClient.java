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
}
