package com.winhxd.b2c.promotion.controller;

import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.common.feign.promotion.CouponTemplateServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.promotion.service.CouponTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    @ApiOperation(value = "添加优惠换模板", notes = "添加优惠换模板",response = ResponseResult.class)
    @Override
    public ResponseResult addCouponTemplate(@RequestBody CouponTemplateCondition couponTemplateCondition) {
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
        return responseResult;
    }

    /**
     *
     *@Deccription 模板列表页面跳转到修改页面 根据id 查询出对应的实体类
     *@Params   id  模板id
     *@Return   ResponseResult
     *@User     wl
     *@Date   2018/8/6 14:41
     */
    @ApiOperation(value = "跳转到优惠券模板编辑页面", notes = "跳转到优惠券模板编辑页面",response = ResponseResult.class)
    @Override
    public ResponseResult toEditCouponTemplate(String id) {
        logger.info("跳转到优惠券模板编辑页面入参: " +id);
        ResponseResult responseResult = new ResponseResult();
        CouponTemplateVO couponTemplateVO = couponTemplateService.getCouponTemplateById(id);
        if(couponTemplateVO!=null){
            responseResult.setData(couponTemplateVO);
            responseResult.setCode(BusinessCode.CODE_OK);
        }
        return responseResult;
    }


    /**
     *
     *@Deccription 多条件分页查询 优惠券模板列表
     *@Params  CouponTemplateCondition
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/6 17:53
     */
    @Override
    public ResponseResult<Object> findCouponTemplatePageByCondition(CouponTemplateCondition couponTemplateCondition) {
        logger.info("优惠券模板列表findCouponTemplatePageByCondition  方法入参：info:" + JsonUtil.toJSONString(couponTemplateCondition));
         return null;
    }


}
