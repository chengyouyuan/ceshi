package com.winhxd.b2c.common.feign.promotion;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponSetToValidCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.InvertorTempleteCountVO;
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
 * @Date 2018/8/6 09:35
 * @Description  出资方管理相关接口
 **/

@FeignClient(value = ServiceName.PROMOTION_SERVICE, fallbackFactory = CouponInvestorServiceFallback.class)
public interface CouponInvestorServiceClient {
    /**
     *
     *@Deccription 添加出资方
     *@Params  condition
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/7 20:59
     */
    @RequestMapping(value = "/promotion/5014/v1/addCouponInvestor", method = RequestMethod.POST)
    ResponseResult<Integer> addCouponInvestor(@RequestBody CouponInvestorCondition condition);

    /**
     *
     *@Deccription 查看出资方详情
     *@Params
     *@Return
     *@User  wl
     *@Date   2018/8/8 13:59
     */
    @RequestMapping(value = "/promotion/5015/v1/viewCouponInvestorDetail", method = RequestMethod.GET)
    ResponseResult<CouponInvestorVO> viewCouponInvestorDetail(@RequestParam("id") String id);

    /**
     *
     *@Deccription 删除出资方（非物理删除）
     *@Params  id
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/8 14:47
     */
    @RequestMapping(value = "/promotion/5016/v1/updateCouponInvestorToValid", method = RequestMethod.POST)
    ResponseResult<Integer> updateCouponInvestorToValid(@RequestBody CouponSetToValidCondition condition);

    /**
     *
     *@Deccription 根据条件获取出资方分页
     *@Params
     *@Return
     *@User  wl
     *@Date   2018/8/8 20:31
     */
    @RequestMapping(value = "/promotion/5028/v1/getCouponInvestorPage", method = RequestMethod.POST)
    ResponseResult<PagedList<CouponInvestorVO>> getCouponInvestorPage(CouponInvestorCondition condition);

    /**
     *
     *@Deccription 出资方规则引用模板列表
     *@Params  id pageNo pageSize
     *@Return  ResponseResult<PagedList<InvertorTempleteCountVO>>
     *@User  wl
     *@Date   2018/8/11 14:30
     */
    @RequestMapping(value = "/promotion/5025/v1/findInvertorTempleteCountPage", method = RequestMethod.POST)
    ResponseResult<PagedList<InvertorTempleteCountVO>> findInvertorTempleteCountPage(@RequestBody RuleRealationCountCondition condition);
}

@Component
class CouponInvestorServiceFallback implements CouponInvestorServiceClient{
    private static final Logger logger = LoggerFactory.getLogger(CouponInvestorServiceFallback.class);
    private Throwable throwable;

    @Override
    public ResponseResult<Integer> addCouponInvestor(CouponInvestorCondition condition) {
        logger.error("CouponInvestorServiceClient -> addCouponInvestor", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<CouponInvestorVO> viewCouponInvestorDetail(String id) {
        logger.error("CouponInvestorServiceClient -> viewCouponInvestorDetail", throwable);
        return new ResponseResult<CouponInvestorVO>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> updateCouponInvestorToValid(CouponSetToValidCondition condition) {
        logger.error("CouponInvestorServiceClient -> updateCouponInvestorToValid", throwable);
        return new ResponseResult<Integer>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<CouponInvestorVO>> getCouponInvestorPage(CouponInvestorCondition condition) {
        logger.error("CouponInvestorServiceClient -> getCouponInvestorPage", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<InvertorTempleteCountVO>> findInvertorTempleteCountPage(RuleRealationCountCondition condition) {
        logger.error("CouponInvestorServiceClient -> findInvertorTempleteCountPage", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }

}
