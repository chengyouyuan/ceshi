package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.feign.promotion.CouponTemplateServiceClient;
import com.winhxd.b2c.promotion.service.CouponTemplateService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wl  优惠券模板相关 CouponTemplateController
 * @Date 2018/8/6 10:46
 * @Description
 **/
@RestController
@Api(tags = "CouponTemplateService")
public class CouponTemplateController implements CouponTemplateServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(CouponTemplateController.class);
     @Autowired
     private CouponTemplateService couponTemplateService;

    /**
     *
     *@Deccription 添加优惠换模板
     *@Params  [couponTemplateCondition]
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/6 10:45
     */
    @Override
    public ResponseResult addCouponTemplate(CouponTemplateCondition couponTemplateCondition) {
        /**
         * 判断必填参数
         */

        ResponseResult responseResult = new ResponseResult();
        try {
            int count = couponTemplateService.saveCouponTemplate(couponTemplateCondition);
            if(count > 0) {
                responseResult.setCode(BusinessCode.CODE_OK);
            }else{
                responseResult.setCode(BusinessCode.CODE_1001);
            }
        }catch (Exception e){
          e.printStackTrace();
        }




        return null;
    }


}
