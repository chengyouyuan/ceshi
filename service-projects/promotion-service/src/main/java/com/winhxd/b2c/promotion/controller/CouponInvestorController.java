package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestorDetail;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.common.domain.promotion.vo.InvertorTempleteCountVO;
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
@Api(tags = "CouponInvestor")
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
      @ApiOperation(value = "出资方添加", notes = "出资方添加",response = ResponseResult.class)
      @Override
      public ResponseResult<Integer> addCouponInvestor(@RequestBody CouponInvestorCondition condition) {
            ResponseResult responseResult = new ResponseResult();
            // flag  0 成功  1占比之和不等于100  2 出资方重复  1001失败  3 出资方明细为空
            int flag = couponInvestorService.saveCouponInvestor(condition);
            responseResult.setCode(flag);
            if(flag==0){
                  responseResult.setMessage("添加成功");
            }else if(flag==1){
                  responseResult.setMessage("新增失败！占比之和不等于100");
            }else if(flag==2){
                  responseResult.setMessage("新增失败！出资方重复");
            }else if(flag==3){
                  responseResult.setMessage("新增失败！出资方明细为空");
            }else {
                  responseResult.setMessage("服务器内部错误");
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
    @ApiOperation(value = "出资方详情查看", notes = "出资方详情查看",response = ResponseResult.class)
    @Override
    public ResponseResult<CouponInvestorVO> viewCouponInvestorDetail(@RequestParam("id") String id) {
        ResponseResult<CouponInvestorVO> responseResult = couponInvestorService.getCouponInvestorDetailById(Long.parseLong(id));
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
    @ApiOperation(value = "出资方设置无效", notes = "出资方设置无效",response = ResponseResult.class)
    @Override
    public ResponseResult<Integer> updateCouponInvestorToValid(@RequestParam("id") String id,@RequestParam("userId")String userId,@RequestParam("userName")String userName) {
        ResponseResult responseResult = new ResponseResult();
        try {
            int count = couponInvestorService.updateCouponInvestorToValid(Long.parseLong(id),Long.parseLong(userId),userName);
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
    *@Deccription 出资方列表多条件分页查询
    *@Params  condition
    *@Return  ResponseResult<PagedList<CouponInvestorVO>>
    *@User  wl
    *@Date   2018/8/11 14:59
    */
    @ApiOperation(value = "出资方列表多条件分页查询", notes = "出资方列表多条件分页查询",response = ResponseResult.class)
    @Override
    public ResponseResult<PagedList<CouponInvestorVO>> getCouponInvestorPage(@RequestBody CouponInvestorCondition condition) {
        ResponseResult<PagedList<CouponInvestorVO>> responseResult =  couponInvestorService.getCouponInvestorPage(condition);
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
    @ApiOperation(value = "出资方关联模板分页列表", notes = "出资方关联模板分页列表",response = ResponseResult.class)
    @Override
    public ResponseResult<PagedList<InvertorTempleteCountVO>> findInvertorTempleteCountPage(@RequestParam("invertorId") String invertorId,@RequestParam("pageNo") Integer pageNo,@RequestParam("pageSize") Integer pageSize) {
        ResponseResult<PagedList<InvertorTempleteCountVO>> responseResult =  couponInvestorService.findInvertorTempleteCountPage(invertorId,pageNo,pageSize);
        return responseResult;
    }


}
