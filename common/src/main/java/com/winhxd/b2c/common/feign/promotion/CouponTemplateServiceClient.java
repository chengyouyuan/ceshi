package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author wl
 * @Date 2018/8/6 09:51
 * @Description  优惠券模板接口
 **/
@FeignClient(value = ServiceName.COUPON_SERVICE, fallbackFactory = CouponTemplateServiceFallback.class)
public interface CouponTemplateServiceClient {
@RequestMapping(value = "/promotion/v1/addCouponTemplate", method = RequestMethod.POST)
/**
 *
 *@Deccription 添加优惠换模板
 *@Params  [couponTemplateCondition]
 *@Return  ResponseResult
 *@User  wl
 *@Date   2018/8/6 10:42
 */
public ResponseResult addCouponTemplate(@RequestBody CouponTemplateCondition couponTemplateCondition);

}

@Component
class CouponTemplateServiceFallback implements CouponTemplateServiceClient{
    private static final Logger logger = LoggerFactory.getLogger(CouponTemplateServiceFallback.class);
    private Throwable throwable;

    /**
     *
     *@Deccription 添加优惠换模板
     *@Params  [couponTemplateCondition]
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/6 10:42
     */
    @Override
    public ResponseResult addCouponTemplate(CouponTemplateCondition couponTemplateCondition) {
        logger.error("CouponTemplateServiceClient -> addCouponTemplate", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

}

