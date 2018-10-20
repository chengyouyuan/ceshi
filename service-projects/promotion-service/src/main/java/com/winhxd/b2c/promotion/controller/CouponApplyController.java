package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponApplyCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponSetToValidCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.vo.ApplyTempleteCountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.promotion.CouponApplyServiceClient;
import com.winhxd.b2c.promotion.service.CouponApplyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wl
 * @Date 2018/8/9 12:10
 * @Description
 **/
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
    @Override
    public ResponseResult<CouponApplyVO> viewCouponApplyDetail(@RequestParam("id") String id,@RequestParam("type") Short type) {
        ResponseResult<CouponApplyVO> responseResult = new ResponseResult<CouponApplyVO>();
        if(StringUtils.isBlank(id) ||type==null ){
            throw new BusinessException(BusinessCode.CODE_500010);
        }
        CouponApplyVO vo = couponApplyService.viewCouponApplyDetail(id,type);
        responseResult.setData(vo);
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
    @Override
    public ResponseResult<Integer> updateCouponApplyToValid(@RequestBody CouponSetToValidCondition condition) {
        ResponseResult<Integer> responseResult = new ResponseResult<Integer>();
        if(condition.getId()==null || condition.getUserId()==null || StringUtils.isBlank(condition.getUserName())){
            throw new BusinessException(BusinessCode.CODE_500010,"必传参数错误");
        }
        int count = couponApplyService.updateCouponApplyToValid(condition.getId(),condition.getUserId(),condition.getUserName());
        if(count>0){
            responseResult.setCode(BusinessCode.CODE_OK);
            responseResult.setMessage("设置成功");
        }else {
            throw new BusinessException(BusinessCode.CODE_1001,"设置失败");
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
    @Override
    public ResponseResult<PagedList<CouponApplyVO>> findCouponApplyPage(@RequestBody CouponApplyCondition condition) {
        ResponseResult<PagedList<CouponApplyVO>> result = new ResponseResult<PagedList<CouponApplyVO>>();
        PagedList<CouponApplyVO> pagedList = couponApplyService.findCouponApplyPage(condition);
        result.setData(pagedList);
        return result;
    }

    /**
     *
     *@Deccription 适用对象规则添加
     *@Params  condition
     *@Return  ResponseResult<Integer> 0 表示成功
     *@User  wl
     *@Date   2018/8/11 14:38
     */
    @Override
    public ResponseResult<Integer> addCouponApply(@RequestBody CouponApplyCondition condition) {
        ResponseResult<Integer> responseResult = new ResponseResult<Integer>();
            int flag = couponApplyService.addCouponApply(condition);
            if(flag==0){
                responseResult.setCode(BusinessCode.CODE_OK);
                responseResult.setMessage("添加成功");
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
    @Override
    public ResponseResult<PagedList<ApplyTempleteCountVO>> findApplyTempleteCountPage(@RequestBody RuleRealationCountCondition condition) {
        ResponseResult<PagedList<ApplyTempleteCountVO>> result = new ResponseResult<PagedList<ApplyTempleteCountVO>>();
        PagedList<ApplyTempleteCountVO> pagedList = couponApplyService.findApplyTempleteCountPage(condition);
        result.setData(pagedList);
        return result;
    }
}
