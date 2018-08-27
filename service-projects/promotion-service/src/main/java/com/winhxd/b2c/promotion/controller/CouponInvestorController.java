package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponSetToValidCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestorDetail;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.common.domain.promotion.vo.InvertorTempleteCountVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.promotion.CouponInvestorServiceClient;
import com.winhxd.b2c.promotion.service.CouponInvestorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/4 19:19
 * @Description 出资方相关接口入口
 **/
@RestController
public class CouponInvestorController implements CouponInvestorServiceClient {
      @Autowired
      private CouponInvestorService couponInvestorService;

      /**
       *
       *@Deccription  新建出资方
       *@Params  condition
       *@Return  ResponseResult
       *@User  wl
       *@Date   2018/8/8 12:30
       */
      @Override
      public ResponseResult<Integer> addCouponInvestor(@RequestBody CouponInvestorCondition condition) {
            ResponseResult<Integer> responseResult = new ResponseResult<Integer>();
            // flag  0 成功  1占比之和不等于100  2 出资方重复  1001失败  3 出资方明细为空
            int flag = couponInvestorService.saveCouponInvestor(condition);
            responseResult.setCode(flag);
            if(flag==0){
                  responseResult.setMessage("添加成功");
            }
            return responseResult;
      }



     /**
      *
      *@Deccription 查看出资方详情
      *@Params id
      *@Return  ResponseResult
      *@User  wl
      *@Date   2018/8/8 14:06
      */
    @Override
    public ResponseResult<CouponInvestorVO> viewCouponInvestorDetail(@RequestParam("id") String id) {
        ResponseResult<CouponInvestorVO> responseResult = new ResponseResult<CouponInvestorVO>();
        CouponInvestorVO vo = couponInvestorService.getCouponInvestorDetailById(id);
        responseResult.setData(vo);
        return responseResult;
    }


    /**
     *
     *@Deccription 出资方设置无效
     *@Params  id  userId userName
     *@Return  ResponseResult<Integer> 0 表示成功
     *@User  wl
     *@Date   2018/8/11 14:58
     */
    @Override
    public ResponseResult<Integer> updateCouponInvestorToValid(@RequestBody CouponSetToValidCondition condition) {
        ResponseResult<Integer> responseResult = new ResponseResult<Integer>();
            int count = couponInvestorService.updateCouponInvestorToValid(condition.getId(),condition.getUserId(),condition.getUserName());
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
     *@Deccription 出资方列表多条件分页查询
     *@Params  condition
     *@Return  ResponseResult<PagedList<CouponInvestorVO>>
     *@User  wl
     *@Date   2018/8/11 14:59
     */
    @Override
    public ResponseResult<PagedList<CouponInvestorVO>> getCouponInvestorPage(@RequestBody CouponInvestorCondition condition) {
        ResponseResult<PagedList<CouponInvestorVO>> responseResult = new ResponseResult<PagedList<CouponInvestorVO>>();
        PagedList<CouponInvestorVO> pagedList =  couponInvestorService.getCouponInvestorPage(condition);
        responseResult.setData(pagedList);
        return responseResult;
    }

    /**
     *
     *@Deccription  出资方关联模板分页列表
     *@Params  invertorId,pageNo,pageSize
     *@Return  ResponseResult<PagedList<InvertorTempleteCountVO>>
     *@User  wl
     *@Date   2018/8/11 15:00
     */
    @Override
    public ResponseResult<PagedList<InvertorTempleteCountVO>> findInvertorTempleteCountPage(@RequestBody RuleRealationCountCondition condition) {
        ResponseResult<PagedList<InvertorTempleteCountVO>> responseResult = new ResponseResult<PagedList<InvertorTempleteCountVO>>();
        PagedList<InvertorTempleteCountVO> pagedList  =  couponInvestorService.findInvertorTempleteCountPage(condition);
        responseResult.setData(pagedList);
        return responseResult;
    }


}
