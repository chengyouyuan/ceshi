package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author WL
 * @Date 2018/8/8 18:00
 * @Description
 **/
@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponGradeServiceClientFallback.class)
public interface CouponGradeServiceClient {

    @RequestMapping(value = "/promotion/v1/addCouponGrade", method = RequestMethod.POST)
    ResponseResult addCouponGrade(@RequestBody CouponGradeCondition couponGradeCondition);

    @RequestMapping(value = "/promotion/v1/viewCouponGradeDetail", method = RequestMethod.POST)
    ResponseResult viewCouponGradeDetail(@RequestParam("id")String id);

    @RequestMapping(value = "/promotion/v1/updateCouponGradeValid", method = RequestMethod.POST)
    ResponseResult updateCouponGradeValid(@RequestParam("id")String id,@RequestParam("userId")String userId,@RequestParam("userName")String userName);

}


@Component
class CouponGradeServiceClientFallback implements CouponGradeServiceClient{
    private static final Logger logger = LoggerFactory.getLogger(CouponGradeServiceClient.class);
    private Throwable throwable;

    @Override
    public ResponseResult addCouponGrade(CouponGradeCondition couponGradeCondition) {
        logger.error("CouponGradeServiceClient -> addCouponGrade", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult viewCouponGradeDetail(String id) {
        logger.error("CouponGradeServiceClient -> viewCouponGradeDetail", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult updateCouponGradeValid(String id,String userId,String userName) {
        logger.error("CouponGradeServiceClient -> updateCouponGradeValid", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }
}

