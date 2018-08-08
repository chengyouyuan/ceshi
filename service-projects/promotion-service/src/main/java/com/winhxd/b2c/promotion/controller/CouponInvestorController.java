package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestorDetail;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
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
       *@Deccription
       *@Params
       *@Return
       *@User  wl
       *@Date   2018/8/8 12:30
       */
      @Override
      public ResponseResult addCouponInvestor(@RequestBody CouponInvestorCondition condition) {
            ResponseResult responseResult = new ResponseResult();
            // flag  0 成功  1占比之和不等于100  2 出资方重复  1001失败  3 出资方明细为空
            int flag = couponInvestorService.saveCouponInvestor(condition);
            responseResult.setCode(flag);
            if(flag==0){
                  responseResult.setMessage("添加成功");
            }else if(flag==1){
                  responseResult.setMessage("提交失败！占比之和不等于");
            }else if(flag==2){
                  responseResult.setMessage("提交失败！出资方重复");
            }else if(flag==3){
                  responseResult.setMessage("提交失败！出资方明细为空");
            }else {
                  responseResult.setMessage("服务器内部错误");
            }
            return responseResult;
      }
}
