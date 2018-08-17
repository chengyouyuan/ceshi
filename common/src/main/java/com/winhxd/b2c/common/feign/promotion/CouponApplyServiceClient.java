package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponApplyCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponSetToValidCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
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
 * @Description  优惠券适用对象接口
 **/
@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponApplyServiceClientFallback.class)
public interface CouponApplyServiceClient {
    /**
     *
     *@Deccription  适用对象查看详情
     *@Params  id
     *@Return  ResponseResult<CouponApplyVO>
     *@User  wl
     *@Date   2018/8/11 14:19
     */
    @RequestMapping(value = "/promotion/5022/v1/viewCouponApplyDetail", method = RequestMethod.POST)
    ResponseResult<CouponApplyVO> viewCouponApplyDetail(@RequestParam("id") String id);

    /**
     *
     *@Deccription  适用对象设置无效
     *@Params  id  userId userName
     *@Return  ResponseResult<Integer> 0 表示成功
     *@User  wl
     *@Date   2018/8/11 14:20
     */
    @RequestMapping(value = "/promotion/5023/v1/updateCouponApplyToValid", method = RequestMethod.POST)
    ResponseResult<Integer> updateCouponApplyToValid(@RequestBody CouponSetToValidCondition condition);

    /**
     *
     *@Deccription 适用对象分页查询
     *@Params  condition 查询条件
     *@Return
     *@User  wl
     *@Date   2018/8/11 14:22
     */
    @RequestMapping(value = "/promotion/5024/v1/findCouponApplyPage", method = RequestMethod.POST)
    ResponseResult<PagedList<CouponApplyVO>> findCouponApplyPage(@RequestBody CouponApplyCondition condition);

    /**
     *
     *@Deccription 添加适用对象
     *@Params  condition
     *@Return  ResponseResult<Integer>  0 表示成功
     *@User  wl
     *@Date   2018/8/11 14:23
     */
    @RequestMapping(value = "/promotion/5021/v1/addCouponApply", method = RequestMethod.POST)
    ResponseResult<Integer> addCouponApply(@RequestBody CouponApplyCondition condition);

    /**
     *
     *@Deccription 适用对象引用模板列表
     *@Params
     *@Return  ResponseResult<PagedList<ApplyTempleteCountVO>>
     *@User  wl
     *@Date   2018/8/11 14:24
     */
    @RequestMapping(value = "/promotion/5027/v1/findApplyTempleteCountPage", method = RequestMethod.POST)
    ResponseResult<PagedList<ApplyTempleteCountVO>> findApplyTempleteCountPage(@RequestBody RuleRealationCountCondition condition);
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
    public ResponseResult<Integer> updateCouponApplyToValid(CouponSetToValidCondition condition) {
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
    public ResponseResult<PagedList<ApplyTempleteCountVO>> findApplyTempleteCountPage(RuleRealationCountCondition condition) {
        logger.error("CouponApplyServiceClient -> findApplyTempleteCountPage", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }
}
