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
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author wl
 * @Date 2018/8/6 09:51
 * @Description  优惠券模板接口
 **/
@FeignClient(value = ServiceName.COUPON_SERVICE, fallbackFactory = CouponTemplateServiceFallback.class)
public interface CouponTemplateServiceClient {

/**
 *
 *@Deccription 添加优惠换模板
 *@Params  [couponTemplateCondition]
 *@Return  ResponseResult
 *@User  wl
 *@Date   2018/8/6 10:42
 */
@RequestMapping(value = "/promotion/v1/addCouponTemplate", method = RequestMethod.POST)
public ResponseResult addCouponTemplate(@RequestBody CouponTemplateCondition couponTemplateCondition);


/**
 *
 *@Deccription 模板列表页面跳转到修改页面 根据id 查询出对应的实体类
 *@Params   id  模板id
 *@Return   ResponseResult
 *@User     wl
 *@Date   2018/8/6 14:41
 */
public ResponseResult toEditCouponTemplate(@RequestParam("id") String id);

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

    /**
     *
     *@Deccription 模板列表页面跳转到修改页面 根据id 查询出对应的实体类
     *@Params   id  模板id
     *@Return   ResponseResult
     *@User     wl
     *@Date   2018/8/6 14:41
     */
    @Override
    public ResponseResult toEditCouponTemplate(String id) {
        logger.error("CouponTemplateServiceClient -> toEditCouponTemplate", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

}

