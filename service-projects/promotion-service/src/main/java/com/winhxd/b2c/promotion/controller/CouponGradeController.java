package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.GradeTempleteCountVO;
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


    @ApiOperation(value = "添加优惠方式规则", notes = "添加优惠方式规则")
    @Override
    public ResponseResult<Integer> addCouponGrade(@RequestBody CouponGradeCondition couponGradeCondition) {
        ResponseResult responseResult = new ResponseResult();
        try {
            int flag = couponGradeService.addCouponGrade(couponGradeCondition);
            if(flag>0){
                responseResult.setCode(BusinessCode.CODE_OK);
                responseResult.setMessage("添加成功");
            }
        }catch (Exception e){
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("添加失败");
            e.printStackTrace();
        }
        return responseResult;
    }

    @ApiOperation(value = "优惠方式规则详情查看", notes = "优惠方式规则详情查看")
    @Override
    public ResponseResult<CouponGradeVO> viewCouponGradeDetail(@RequestParam("id") String id) {
        ResponseResult<CouponGradeVO> responseResult = couponGradeService.viewCouponGradeDetail(Long.parseLong(id));
        return responseResult;
    }

    @ApiOperation(value = "优惠方式规则设为无效/逻辑删除", notes = "优惠方式规则设为无效/逻辑删除")
    @Override
    public ResponseResult<Integer> updateCouponGradeValid(@RequestParam("id")String id,@RequestParam("userId")String userId,@RequestParam("userName")String userName) {
        ResponseResult responseResult = new ResponseResult();
        try {
            int count = couponGradeService.updateCouponGradeValid(Long.parseLong(id),Long.parseLong(userId),userName);
            if(count>0){
                responseResult.setCode(BusinessCode.CODE_OK);
                responseResult.setMessage("删除成功");
            }
        }catch (Exception e){
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("删除失败");
            e.printStackTrace();
        }
        return responseResult;
    }

    @ApiOperation(value = "优惠方式规则分页查询", notes = "优惠方式规则分页查询")
    @Override
    public ResponseResult<PagedList<CouponGradeVO>> getCouponGradePage(CouponGradeCondition condition) {
        ResponseResult<PagedList<CouponGradeVO>> result = couponGradeService.getCouponGradePage(condition);
        return result;
    }

    @ApiOperation(value = "优惠方式规则关联模板分页查询", notes = "优惠方式规则关联模板分页查询")
    @Override
    public ResponseResult<PagedList<GradeTempleteCountVO>> findGradeTempleteCountPage(@RequestParam("gradeId") String gradeId,@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize")Integer pageSize) {
        ResponseResult<PagedList<GradeTempleteCountVO>> result = couponGradeService.findGradeTempleteCountPage(gradeId,pageNo,pageSize);
        return result;
    }


}
