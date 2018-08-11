package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponApplyCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.GradeTempleteCountVO;
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

    @RequestMapping(value = "/promotion/518/v1/addCouponGrade", method = RequestMethod.POST)
    ResponseResult<Integer> addCouponGrade(@RequestBody CouponGradeCondition couponGradeCondition);

    @RequestMapping(value = "/promotion/519/v1/viewCouponGradeDetail", method = RequestMethod.POST)
    ResponseResult<CouponGradeVO> viewCouponGradeDetail(@RequestParam("id")String id);

    @RequestMapping(value = "/promotion/520/v1/updateCouponGradeValid", method = RequestMethod.POST)
    ResponseResult<Integer> updateCouponGradeValid(@RequestParam("id")String id,@RequestParam("userId")String userId,@RequestParam("userName")String userName);

    @RequestMapping(value = "/promotion/517/v1/getCouponGradePage", method = RequestMethod.POST)
    ResponseResult<PagedList<CouponGradeVO>> getCouponGradePage(@RequestBody CouponGradeCondition condition);

    @RequestMapping(value = "/promotion/526/v1/findGradeTempleteCountPage", method = RequestMethod.POST)
    ResponseResult<PagedList<GradeTempleteCountVO>> findGradeTempleteCountPage(@RequestParam("gradeId") String gradeId,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize);
}


@Component
class CouponGradeServiceClientFallback implements CouponGradeServiceClient{
    private static final Logger logger = LoggerFactory.getLogger(CouponGradeServiceClientFallback.class);
    private Throwable throwable;

    @Override
    public ResponseResult<Integer> addCouponGrade(CouponGradeCondition couponGradeCondition) {
        logger.error("CouponGradeServiceClient -> addCouponGrade", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<CouponGradeVO> viewCouponGradeDetail(String id) {
        logger.error("CouponGradeServiceClient -> viewCouponGradeDetail", throwable);
        return new ResponseResult<CouponGradeVO>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> updateCouponGradeValid(String id,String userId,String userName) {
        logger.error("CouponGradeServiceClient -> updateCouponGradeValid", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<CouponGradeVO>> getCouponGradePage(CouponGradeCondition condition) {
        logger.error("CouponGradeServiceClient -> getCouponGradePage", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<GradeTempleteCountVO>> findGradeTempleteCountPage(String gradeId, Integer pageNo, Integer pageSize) {
        logger.error("CouponGradeServiceClient -> findGradeTempleteCountPage", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }


}

