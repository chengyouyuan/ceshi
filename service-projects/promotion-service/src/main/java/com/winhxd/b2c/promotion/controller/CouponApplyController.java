package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponApplyCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.feign.promotion.CouponApplyServiceClient;
import com.winhxd.b2c.promotion.service.CouponApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wl
 * @Date 2018/8/9 12:10
 * @Description
 **/
@Api(tags = "CouponApply")
@RestController
public class CouponApplyController implements CouponApplyServiceClient {
    @Autowired
    private CouponApplyService couponApplyService;

    @ApiOperation(value = "查看优惠券类型规则", notes = "查看优惠券类型规则",response = ResponseResult.class)
    @Override
    public ResponseResult viewCouponApplyDetail(@RequestParam("id") String id) {
        ResponseResult<CouponApplyVO> responseResult = couponApplyService.viewCouponApplyDetail(Long.parseLong(id));
        return responseResult;
    }
    @ApiOperation(value = "优惠券类型规则设置无效", notes = "优惠券类型规则设置无效",response = ResponseResult.class)
    @Override
    public ResponseResult updateCouponApplyToValid(String id, String userId, String userName) {
        ResponseResult responseResult = new ResponseResult();
        try {
            int count = couponApplyService.updateCouponApplyToValid(Long.parseLong(id),Long.parseLong(userId),userName);
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


    @ApiOperation(value = "优惠券类型规则分页查询", notes = "优惠券类型规则分页查询",response = ResponseResult.class)
    @Override
    public ResponseResult<PagedList<CouponApplyVO>> findCouponApplyPage(CouponApplyCondition condition) {
        ResponseResult<PagedList<CouponApplyVO>> result = couponApplyService.findCouponApplyPage(condition);
        return result;
    }


    @ApiOperation(value = "添加优惠券类型规则", notes = "添加优惠券类型规则",response = ResponseResult.class)
    @Override
    public ResponseResult addCouponApply(CouponApplyCondition condition) {
        ResponseResult responseResult = new ResponseResult();
        try {
            int flag = couponApplyService.addCouponApply(condition);
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
}
