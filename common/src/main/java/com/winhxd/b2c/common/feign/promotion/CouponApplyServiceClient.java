package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author wl
 * @Date 2018/8/9 12:02
 * @Description
 **/
@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponApplyServiceClientFallback.class)
public interface CouponApplyServiceClient {

    @RequestMapping(value = "/promotion/v1/viewCouponApplyDetail", method = RequestMethod.POST)
    ResponseResult viewCouponApplyDetail(@RequestParam("id") String id);

    @RequestMapping(value = "/promotion/v1/updateCouponApplyToValid", method = RequestMethod.POST)
    ResponseResult updateCouponApplyToValid(@RequestParam("id")String id,@RequestParam("userId")String userId,@RequestParam("userName")String userName);

}

@Component
class CouponApplyServiceClientFallback implements CouponApplyServiceClient{
    private static final Logger logger = LoggerFactory.getLogger(CouponApplyServiceClientFallback.class);
    private Throwable throwable;

    @Override
    public ResponseResult viewCouponApplyDetail(String id) {
        logger.error("CouponApplyServiceClient -> viewCouponApplyDetail", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult updateCouponApplyToValid(String id, String userId, String userName) {
        logger.error("CouponApplyServiceClient -> updateCouponApplyToValid", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }
}