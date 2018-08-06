package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.feign.promotion.CouponActivityServiceClient;
import com.winhxd.b2c.promotion.service.CouponActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author sjx
 * @date 2018/8/6
 * @Description 优惠券活动相关入口
 */
@Api(tags = "CouponActivity")
@RestController
@RequestMapping(value = "/couponActivity/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CouponActivityController implements CouponActivityServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(CouponActivityController.class);
    @Autowired
    private CouponActivityService couponActivityService;

    @Override
    @ApiOperation(value = "领券活动列表接口", response = CouponActivityVO.class, notes = "领券活动列表接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = CouponActivityVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    public ResponseResult<PagedList<CouponActivityVO>> queryPullCouponActivity(CouponActivityCondition condition) {
        logger.info("/promotion/v1/queryPullCouponActivity/ 领券活动列表查询开始");
        ResponseResult<PagedList<CouponActivityVO>> result = new ResponseResult<PagedList<CouponActivityVO>>();
        try {
            PagedList<CouponActivityVO> page = couponActivityService.queryPullCouponActivity(condition);
            result.setData(page);
        }catch (Exception e){
            logger.error("/promotion/v1/queryPullCouponActivity/ 领券活动列表查询=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        logger.info("/promotion/v1/queryPullCouponActivity/ 领券活动列表查询结束");
        return result;
    }
}
