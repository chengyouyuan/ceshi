package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponSetToValidCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @Author wl
 * @Date 2018/8/6 09:51
 * @Description 优惠券模板接口
 **/
@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponTemplateServiceFallback.class)
public interface CouponTemplateServiceClient {

    /**
     * @Deccription 添加优惠换模板
     * @Params [couponTemplateCondition]
     * @Return ResponseResult
     * @User wl
     * @Date 2018/8/6 10:42
     */
    @RequestMapping(value = "/promotion/5011/v1/addCouponTemplate", method = RequestMethod.POST)
    public ResponseResult<Integer> addCouponTemplate(@RequestBody CouponTemplateCondition couponTemplateCondition);


    /**
     * @Deccription 多条件分页查询 优惠券模板列表
     * @Params CouponTemplateCondition
     * @Return ResponseResult
     * @User wl
     * @Date 2018/8/6 17:53
     */
    @RequestMapping(value = "/promotion/5010/v1/findCouponTemplatePageByCondition", method = RequestMethod.POST)
    public ResponseResult<PagedList<CouponTemplateVO>> findCouponTemplatePageByCondition(@RequestBody CouponTemplateCondition couponTemplateCondition);

    /**
     * @Deccription 设为无效
     * @Params id
     * @Return ResponseResult 删除是否成功
     * @User wl
     * @Date 2018/8/6 20:39
     */
    @RequestMapping(value = "/promotion/5013/v1/updateCouponTemplateToValid", method = RequestMethod.POST)
    public ResponseResult<Integer> updateCouponTemplateToValid(@RequestBody CouponSetToValidCondition condition);

    /**
     * @Deccription 查看优惠券模板详情
     * @Params id
     * @Return ResponseResult
     * @User wl
     * @Date 2018/8/6 20:45
     */
    @RequestMapping(value = "/promotion/5012/v1/viewCouponTemplateDetail", method = RequestMethod.GET)
    public ResponseResult<CouponTemplateVO> viewCouponTemplateDetail(@RequestParam("id") String id);


}


@Component
class CouponTemplateServiceFallback implements FallbackFactory<CouponTemplateServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(CouponTemplateServiceFallback.class);

    @Override
    public CouponTemplateServiceClient create(Throwable throwable) {
        return new CouponTemplateServiceClient() {
            @Override
            public ResponseResult<Integer> addCouponTemplate(CouponTemplateCondition couponTemplateCondition) {
                logger.error("CouponTemplateServiceClient -> addCouponTemplate", throwable);
                return new ResponseResult<Integer>(BusinessCode.CODE_1001);
            }


            @Override
            public ResponseResult<PagedList<CouponTemplateVO>> findCouponTemplatePageByCondition(CouponTemplateCondition couponTemplateCondition) {
                logger.error("CouponTemplateServiceClient -> findCouponTemplatePageByCondition", throwable);
                return new ResponseResult<PagedList<CouponTemplateVO>>(BusinessCode.CODE_1001);
            }


            @Override
            public ResponseResult<Integer> updateCouponTemplateToValid(CouponSetToValidCondition condition) {
                logger.error("CouponTemplateServiceClient -> updateCouponTemplateToValid", throwable);
                return new ResponseResult<Integer>(BusinessCode.CODE_1001);
            }


            @Override
            public ResponseResult<CouponTemplateVO> viewCouponTemplateDetail(String id) {
                logger.error("CouponTemplateServiceClient -> viewCouponTemplateDetail", throwable);
                return new ResponseResult<CouponTemplateVO>(BusinessCode.CODE_1001);
            }
        };
    }
}

