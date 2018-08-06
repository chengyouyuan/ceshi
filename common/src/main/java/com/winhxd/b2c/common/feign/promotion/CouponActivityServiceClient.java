package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author sjx
 * @date 2018/8/6
 * @Description 优惠券活动相关接口
 */
@FeignClient(value = ServiceName.COUPON_SERVICE, fallbackFactory = CouponActivityServiceFallback.class)
public interface CouponActivityServiceClient {
    @RequestMapping(value = "/promotion/v1/queryPullCouponActivity/", method = RequestMethod.POST)
    ResponseResult<PagedList<CouponActivityVO>> queryPullCouponActivity(@RequestBody CouponActivityCondition condition);
}

@Component
class CouponActivityServiceFallback implements CouponActivityServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(CouponActivityServiceFallback.class);
    private Throwable throwable;

    @Override
    public ResponseResult<PagedList<CouponActivityVO>> queryPullCouponActivity(CouponActivityCondition condition) {

        logger.error("CouponActivityServiceFallback -> queryPullCouponActivity", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

}
