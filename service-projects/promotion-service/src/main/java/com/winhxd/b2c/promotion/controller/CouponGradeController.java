package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponSetToValidCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.GradeTempleteCountVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.promotion.CouponGradeServiceClient;
import com.winhxd.b2c.promotion.service.CouponGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wl
 * @Date 2018/8/8 18:24
 * @Description
 **/
@Api(tags = "CouponGrade")
@RestController
public class CouponGradeController implements CouponGradeServiceClient {
     @Autowired
     private CouponGradeService couponGradeService;

    /**
     *
     *@Deccription
     *@Params  couponGradeCondition
     *@Return  ResponseResult<Integer> 0 表示成功
     *@User  wl
     *@Date   2018/8/11 14:40
     */
    @ApiOperation(value = "坎级规则添加", notes = "坎级规则添加")
    @Override
    public ResponseResult<Integer> addCouponGrade(@RequestBody CouponGradeCondition couponGradeCondition) {
        ResponseResult<Integer> responseResult = new ResponseResult();
            int flag = couponGradeService.addCouponGrade(couponGradeCondition);
            if(flag!=0){
             throw new BusinessException(BusinessCode.CODE_500003,"坎级规则添加异常");
            }
            if(flag==0){
                responseResult.setCode(BusinessCode.CODE_OK);
                responseResult.setMessage("添加成功");
            }
        return responseResult;
    }

    /**
     *
     *@Deccription 坎级规则详情查看
     *@Params
     *@Return
     *@User  wl
     *@Date   2018/8/11 14:41
     */
    @ApiOperation(value = "坎级规则详情查看", notes = "坎级规则详情查看")
    @Override
    public ResponseResult<CouponGradeVO> viewCouponGradeDetail(@RequestParam("id") String id) {
        ResponseResult<CouponGradeVO> responseResult = couponGradeService.viewCouponGradeDetail(Long.parseLong(id));
        return responseResult;
    }

    /**
     *
     *@Deccription 坎级规则设为无效/逻辑删除
     *@Params  id  userId  userName
     *@Return ResponseResult<Integer> 0 表示成功
     *@User  wl
     *@Date   2018/8/11 14:42
     */
    @ApiOperation(value = "坎级规则设为无效/逻辑删除", notes = "坎级规则设为无效/逻辑删除")
    @Override
    public ResponseResult<Integer> updateCouponGradeValid(@RequestBody CouponSetToValidCondition condition) {
        ResponseResult<Integer> responseResult = new ResponseResult();
            int count = couponGradeService.updateCouponGradeValid(condition.getId(),condition.getUserId(),condition.getUserName());
            if(count>0){
                responseResult.setCode(BusinessCode.CODE_OK);
                responseResult.setMessage("删除成功");
            }else {
                throw  new BusinessException(BusinessCode.CODE_1001,"设置失败");
            }
        return responseResult;
    }

    /**
     *
     *@Deccription 坎级规则分页查询
     *@Params  condition
     *@Return  ResponseResult<PagedList<CouponGradeVO>>
     *@User  wl
     *@Date   2018/8/11 14:44
     */
    @ApiOperation(value = "坎级规则分页查询", notes = "坎级规则分页查询")
    @Override
    public ResponseResult<PagedList<CouponGradeVO>> getCouponGradePage(@RequestBody CouponGradeCondition condition) {
        ResponseResult<PagedList<CouponGradeVO>> result = couponGradeService.getCouponGradePage(condition);
        return result;
    }

    /**
     *
     *@Deccription 坎级规则关联模板分页查询
     *@Params  gradeId,pageNo,pageSize
     *@Return  ResponseResult<PagedList<GradeTempleteCountVO>>
     *@User  wl
     *@Date   2018/8/11 14:44
     */
    @ApiOperation(value = "坎级规则关联模板分页查询", notes = "坎级规则关联模板分页查询")
    @Override
    public ResponseResult<PagedList<GradeTempleteCountVO>> findGradeTempleteCountPage(@RequestBody RuleRealationCountCondition condition) {
        ResponseResult<PagedList<GradeTempleteCountVO>> result = couponGradeService.findGradeTempleteCountPage(condition);
        return result;
    }


}
