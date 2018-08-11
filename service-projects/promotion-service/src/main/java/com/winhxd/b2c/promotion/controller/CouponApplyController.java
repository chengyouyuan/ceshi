package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponApplyCondition;
import com.winhxd.b2c.common.domain.promotion.vo.ApplyTempleteCountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.feign.promotion.CouponApplyServiceClient;
import com.winhxd.b2c.promotion.service.CouponApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wl
 * @Date 2018/8/9 12:10
 * @Description   适用对象Controller
 **/
@Api(tags = "CouponApply")
@RestController
public class CouponApplyController implements CouponApplyServiceClient {
    @Autowired
    private CouponApplyService couponApplyService;

    /**
     *
     *@Deccription 查看适用对象规则
     *@Params  id
     *@Return  ResponseResult<CouponApplyVO>
     *@User  wl
     *@Date   2018/8/11 14:34
     */
    @ApiOperation(value = "适用对象规则查看", notes = "适用对象规则查看",response = ResponseResult.class)
    @Override
    public ResponseResult<CouponApplyVO> viewCouponApplyDetail(@RequestParam("id") String id) {
        ResponseResult<CouponApplyVO> responseResult = couponApplyService.viewCouponApplyDetail(Long.parseLong(id));
        return responseResult;
    }


    /**
     *
     *@Deccription 适用对象规则设置无效
     *@Params  id userId userName
     *@Return ResponseResult<Integer> 0 成功
     *@User  wl
     *@Date   2018/8/11 14:35
     */
    @ApiOperation(value = "适用对象规则设置无效", notes = "适用对象规则设置无效",response = ResponseResult.class)
    @Override
    public ResponseResult<Integer> updateCouponApplyToValid(@RequestParam("id")String id, @RequestParam("userId")String userId,@RequestParam("userName") String userName) {
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

    /**
     *
     *@Deccription 适用对象规则分页查询
     *@Params  condition 查询条件
     *@Return  ResponseResult<PagedList<CouponApplyVO>>
     *@User  wl
     *@Date   2018/8/11 14:36
     */
    @ApiOperation(value = "适用对象规则分页查询", notes = "适用对象规则分页查询",response = ResponseResult.class)
    @Override
    public ResponseResult<PagedList<CouponApplyVO>> findCouponApplyPage(@RequestBody CouponApplyCondition condition) {
        ResponseResult<PagedList<CouponApplyVO>> result = couponApplyService.findCouponApplyPage(condition);
        return result;
    }

    /**
     *
     *@Deccription 添加适用对象规则
     *@Params  condition
     *@Return  ResponseResult<Integer> 0 表示成功
     *@User  wl
     *@Date   2018/8/11 14:38
     */
    @ApiOperation(value = "适用对象规则添加", notes = "适用对象规则添加",response = ResponseResult.class)
    @Override
    public ResponseResult<Integer> addCouponApply(@RequestBody CouponApplyCondition condition) {
        ResponseResult responseResult = new ResponseResult();
        try {
            int flag = couponApplyService.addCouponApply(condition);
            if(flag==0){
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

    /**
     *
     *@Deccription 适用对象规则关联模板分页查询
     *@Params  applyId pageNo pageSize
     *@Return  ResponseResult<PagedList<ApplyTempleteCountVO>>
     *@User  wl
     *@Date   2018/8/11 14:38
     */
    @ApiOperation(value = "适用对象规则关联模板分页查询", notes = "适用对象规则关联模板分页查询",response = ResponseResult.class)
    @Override
    public ResponseResult<PagedList<ApplyTempleteCountVO>> findApplyTempleteCountPage(@RequestParam("applyId") String applyId,@RequestParam("pageNo") Integer pageNo,@RequestParam("pageSize") Integer pageSize) {
        ResponseResult<PagedList<ApplyTempleteCountVO>> result = couponApplyService.findApplyTempleteCountPage(applyId,pageNo,pageSize);
        return result;
    }
}
