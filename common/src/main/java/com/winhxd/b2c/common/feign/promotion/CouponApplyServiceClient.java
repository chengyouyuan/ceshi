package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponApplyCondition;
import com.winhxd.b2c.common.domain.promotion.vo.ApplyTempleteCountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;
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
 * @Date 2018/8/9 12:02
 * @Description
 **/
@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponApplyServiceClientFallback.class)
public interface CouponApplyServiceClient {

    @RequestMapping(value = "/promotion/522/v1/viewCouponApplyDetail", method = RequestMethod.POST)
    ResponseResult<CouponApplyVO> viewCouponApplyDetail(@RequestParam("id") String id);

    @RequestMapping(value = "/promotion/523/v1/updateCouponApplyToValid", method = RequestMethod.POST)
    ResponseResult<Integer> updateCouponApplyToValid(@RequestParam("id")String id,@RequestParam("userId")String userId,@RequestParam("userName")String userName);

    @RequestMapping(value = "/promotion/524/v1/findCouponApplyPage", method = RequestMethod.POST)
    ResponseResult<PagedList<CouponApplyVO>> findCouponApplyPage(@RequestBody CouponApplyCondition condition);

    @RequestMapping(value = "/promotion/521/v1/addCouponApply", method = RequestMethod.POST)
    ResponseResult<Integer> addCouponApply(@RequestBody CouponApplyCondition condition);

    @RequestMapping(value = "/promotion/527/v1/findApplyTempleteCountPage", method = RequestMethod.POST)
    ResponseResult<PagedList<ApplyTempleteCountVO>> findApplyTempleteCountPage(@RequestParam("applyId") String applyId,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize);
}

@Component
class CouponApplyServiceClientFallback implements CouponApplyServiceClient{
    private static final Logger logger = LoggerFactory.getLogger(CouponApplyServiceClientFallback.class);
    private Throwable throwable;

    @Override
    public ResponseResult<CouponApplyVO> viewCouponApplyDetail(String id) {
        logger.error("CouponApplyServiceClient -> viewCouponApplyDetail", throwable);
        return new ResponseResult<CouponApplyVO>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> updateCouponApplyToValid(String id, String userId, String userName) {
        logger.error("CouponApplyServiceClient -> updateCouponApplyToValid", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<CouponApplyVO>> findCouponApplyPage(CouponApplyCondition condition) {
        logger.error("CouponApplyServiceClient -> findCouponApplyPage", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> addCouponApply(CouponApplyCondition condition) {
        logger.error("CouponApplyServiceClient -> addCouponApply", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<ApplyTempleteCountVO>> findApplyTempleteCountPage(String applyId, Integer pageNo, Integer pageSize) {
        logger.error("CouponApplyServiceClient -> findApplyTempleteCountPage", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }
}
