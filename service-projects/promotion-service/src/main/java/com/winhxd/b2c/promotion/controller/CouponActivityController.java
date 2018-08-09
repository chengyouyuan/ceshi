package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityStoreVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.promotion.CouponActivityServiceClient;
import com.winhxd.b2c.promotion.service.CouponActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
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
    @ApiOperation(value = "领券推券活动列表接口", response = CouponActivityVO.class, notes = "领券推券活动列表接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = CouponActivityVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    public ResponseResult<PagedList<CouponActivityVO>> queryCouponActivity(CouponActivityCondition condition) {
        logger.info("/promotion/v1/queryCouponActivity/ 领券推券活动列表查询开始");
        ResponseResult<PagedList<CouponActivityVO>> result = new ResponseResult<PagedList<CouponActivityVO>>();
        try {
            result = couponActivityService.queryCouponActivity(condition);
        }catch (Exception e){
            logger.error("/promotion/v1/queryCouponActivity/ 领券推券活动列表查询=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        logger.info("/promotion/v1/queryCouponActivity/ 领券推券活动列表查询结束");
        return result;
    }

    /**
     *
     *@Deccription 添加优惠券活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/6
     */
    @Override
    @ApiOperation(value = "添加优惠券活动", notes = "添加优惠券活动")
    public ResponseResult addCouponActivity(CouponActivityAddCondition condition) {
        /**
         * 判断必填参数
         */
        if(null == condition){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(null == condition.getType()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(condition.getType() == CouponActivityEnum.PULL_COUPON.getCode()){
            if (condition.getName() == null && condition.getCouponActivityTemplateList() == null
                    && condition.getActivityStart()==null && condition.getActivityEnd() == null) {
                throw new BusinessException(BusinessCode.CODE_1007);
            }
        }
        if(condition.getType() == CouponActivityEnum.PUSH_COUPON.getCode()){
            if (condition.getName() == null && condition.getCouponActivityTemplateList() == null
                    && condition.getActivityStart() == null && condition.getActivityEnd() == null) {
                throw new BusinessException(BusinessCode.CODE_1007);
            }
        }

        ResponseResult responseResult = new ResponseResult();
        try {
            couponActivityService.saveCouponActivity(condition);
            responseResult.setCode(BusinessCode.CODE_OK);
        }catch (Exception e){
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    /**
     *
     *@Deccription 查看优惠券活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/8
     */
    @ApiOperation(value = "优惠券活动查看&回显编辑页", notes = "优惠券活动查看&回显编辑页",response = ResponseResult.class)
    @Override
    public ResponseResult getCouponActivityById(String id) {
        logger.info("优惠券活动查看&回显编辑页入参id: " +id);
        ResponseResult responseResult = new ResponseResult();
        CouponActivityVO couponActivityVO = couponActivityService.getCouponActivityById(id);
        if(couponActivityVO!=null){
            responseResult.setData(couponActivityVO);
            responseResult.setCode(BusinessCode.CODE_OK);
        }
        return responseResult;
    }



    /**
     *
     *@Deccription 编辑优惠券活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/7
     */
    @Override
    @ApiOperation(value = "编辑优惠券活动", notes = "编辑优惠券活动")
    public ResponseResult updateCouponActivity(CouponActivityAddCondition condition) {
        /**
         * 判断必填参数
         * 活动有效期内不允许修改活动！！
         */
        ResponseResult responseResult = new ResponseResult();
        try {
            couponActivityService.updateCouponActivity(condition);
            responseResult.setCode(BusinessCode.CODE_OK);
        }catch (Exception e){
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    /**
     *
     *@Deccription 删除优惠券活动（设为无效）
     *@Params  id
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/8
     */
    @ApiOperation(value = "删除优惠券活动", notes = "删除优惠券活动")
    @Override
    public ResponseResult deleteCouponActivity(String id) {
        logger.info("deleteCouponActivity--Id:" + id);
        ResponseResult responseResult = new ResponseResult();
        if(StringUtils.isBlank(id)){
            responseResult.setCode(BusinessCode.CODE_1007);
            responseResult.setMessage("参数为空错误");
            return responseResult;
        }
        couponActivityService.deleteCouponActivity(id);
        responseResult.setCode(BusinessCode.CODE_OK);
        responseResult.setMessage("删除成功");

        return responseResult;
    }

    /**
     *
     *@Deccription 撤回活动优惠券
     *@Params  id
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @ApiOperation(value = "撤回活动优惠券", notes = "撤回活动优惠券")
    @Override
    public ResponseResult revocationActivityCoupon(String id) {
        logger.info("revocationActivityCoupon--Id:" + id);
        ResponseResult responseResult = new ResponseResult();
        if(StringUtils.isBlank(id)){
            responseResult.setCode(BusinessCode.CODE_1007);
            responseResult.setMessage("参数为空错误");
            return responseResult;
        }
        couponActivityService.revocationActivityCoupon(id);
        responseResult.setCode(BusinessCode.CODE_OK);
        responseResult.setMessage("撤销成功");

        return responseResult;
    }

    /**
     *
     *@Deccription 停用/开启活动
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @Override
    public ResponseResult updateCouponActivityStatus(CouponActivityAddCondition condition) {
        /**
         * 判断必填参数
         * 活动有效期内不允许修改活动！！
         */
        ResponseResult responseResult = new ResponseResult();
        try {
            couponActivityService.updateCouponActivityStatus(condition);
            responseResult.setCode(BusinessCode.CODE_OK);
        }catch (Exception e){
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    /**
     *
     *@Deccription 根据活动获取优惠券列表
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @ApiOperation(value = "根据活动获取优惠券列表", response = CouponActivityStoreVO.class, notes = "根据活动获取优惠券列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = CouponActivityStoreVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    @Override
    public ResponseResult<PagedList<CouponActivityStoreVO>> queryCouponByActivity(CouponActivityCondition condition) {
        logger.info("queryCouponByActivity--Id:" + condition.getId());
        ResponseResult<PagedList<CouponActivityStoreVO>> result = new ResponseResult<PagedList<CouponActivityStoreVO>>();
        try {
            result = couponActivityService.queryCouponByActivity(condition);
        }catch (Exception e){
            logger.error("/promotion/v1/queryCouponByActivity/ 根据活动获取优惠券列表=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    /**
     *
     *@Deccription 根据活动获取小店信息
     *@Params  condition
     *@Return  ResponseResult
     *@User  sjx
     *@Date   2018/8/9
     */
    @ApiOperation(value = "根据活动获取小店信息", response = CouponActivityVO.class, notes = "根据活动获取小店信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = CouponActivityVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    @Override
    public ResponseResult<PagedList<CouponActivityStoreVO>> queryStoreByActivity(CouponActivityCondition condition) {
        logger.info("queryStoreByActivity--Id:" + condition.getId());
        ResponseResult<PagedList<CouponActivityStoreVO>> result = new ResponseResult<PagedList<CouponActivityStoreVO>>();
        try {
            result = couponActivityService.queryStoreByActivity(condition);
        }catch (Exception e){
            logger.error("/promotion/v1/queryStoreByActivity/ 根据活动获取小店信息=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }


}
