package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author sjx
 * @date 2018/8/6
 * @Description 优惠券活动相关接口
 */
@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponActivityServiceFallback.class)
public interface CouponActivityServiceClient {
    /**
     *
     *@Deccription 查询优惠券活动领券推券列表
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/6
     */
    @RequestMapping(value = "/promotion/v1/queryCouponActivity/", method = RequestMethod.POST)
    ResponseResult<PagedList<CouponActivityVO>> queryCouponActivity(@RequestBody CouponActivityCondition condition);
    /**
     *
     *@Deccription 添加优惠券活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/6
     */
    @RequestMapping(value = "/promotion/v1/addCouponActivity/", method = RequestMethod.POST)
    public ResponseResult addCouponActivity(@RequestBody CouponActivityAddCondition condition);

    /**
     *
     *@Deccription 根据id 查询出对应的实体类(查看和回显编辑页)
     *@Params   id  模板id
     *@Return   ResponseResult
     *@User     sjx
     *@Date   2018/8/8
     */
    @RequestMapping(value = "/promotion/v1/getCouponActivityById/", method = RequestMethod.GET)
    public ResponseResult getCouponActivityById(@RequestParam("id") String id);
    /**
     *
     *@Deccription 编辑优惠券活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/7
     */
    @RequestMapping(value = "/promotion/v1/updateCouponActivity/", method = RequestMethod.POST)
    public ResponseResult updateCouponActivity(@RequestBody CouponActivityAddCondition condition);
    /**
     *
     *@Deccription 删除优惠券活动（设为无效）
     *@Params  id
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/8
     */
    @RequestMapping(value = "/promotion/v1/deleteCouponActivity/", method = RequestMethod.POST)
    public ResponseResult deleteCouponActivity(@RequestParam("id") String id);
    /**
     *
     *@Deccription 撤回活动优惠券
     *@Params  id
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @RequestMapping(value = "/promotion/v1/revocationActivityCoupon/", method = RequestMethod.POST)
    public ResponseResult revocationActivityCoupon(@RequestParam("id") String id);

    /**
     *
     *@Deccription 停用/开启活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @RequestMapping(value = "/promotion/v1/updateCouponActivityStatus/", method = RequestMethod.POST)
    public ResponseResult updateCouponActivityStatus(@RequestBody CouponActivityAddCondition condition);
    /**
     *
     *@Deccription 根据活动获取优惠券列表
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @RequestMapping(value = "/promotion/v1/queryCouponByActivity/", method = RequestMethod.POST)
    public ResponseResult<PagedList<CouponActivityStoreVO>> queryCouponByActivity(@RequestBody CouponActivityCondition condition);
    /**
     *
     *@Deccription 根据活动获取小店信息
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @RequestMapping(value = "/promotion/v1/queryStoreByActivity/", method = RequestMethod.POST)
    public ResponseResult<PagedList<CouponActivityStoreVO>> queryStoreByActivity(@RequestBody CouponActivityCondition condition);

}

@Component
class CouponActivityServiceFallback implements CouponActivityServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(CouponActivityServiceFallback.class);
    private Throwable throwable;

    @Override
    public ResponseResult<PagedList<CouponActivityVO>> queryCouponActivity(CouponActivityCondition condition) {
        logger.error("CouponActivityServiceFallback -> queryCouponActivity", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult addCouponActivity(CouponActivityAddCondition condition) {
        logger.error("CouponActivityServiceFallback -> addCouponActivity", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult getCouponActivityById(String id) {
        logger.error("CouponActivityServiceFallback -> getCouponActivityById", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult updateCouponActivity(CouponActivityAddCondition condition) {
        logger.error("CouponActivityServiceFallback -> updateCouponActivity", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult deleteCouponActivity(String id) {
        logger.error("CouponActivityServiceFallback -> deleteCouponActivity", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult revocationActivityCoupon(String id) {
        logger.error("CouponActivityServiceFallback -> revocationActivityCoupon", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult updateCouponActivityStatus(CouponActivityAddCondition condition) {
        logger.error("CouponActivityServiceFallback -> updateCouponActivityStatus", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<CouponActivityStoreVO>> queryCouponByActivity(CouponActivityCondition condition) {
        logger.error("CouponActivityServiceFallback -> queryCouponByActivity", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<CouponActivityStoreVO>> queryStoreByActivity(CouponActivityCondition condition) {
        logger.error("CouponActivityServiceFallback -> queryStoreByActivity", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

}
