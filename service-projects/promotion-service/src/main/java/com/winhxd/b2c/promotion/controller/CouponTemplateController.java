package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponSetToValidCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.promotion.CouponTemplateServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.promotion.service.CouponTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author wl  优惠券模板相关 CouponTemplateController
 * @Date 2018/8/6 10:46
 * @Description
 **/
@RestController
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
    public ResponseResult<Integer> addCouponTemplate(@RequestBody CouponTemplateCondition couponTemplateCondition) {
        /**
         * 校验必填参数
         */
        ResponseResult<Integer> responseResult = new ResponseResult<Integer>();
            int flag = couponTemplateService.saveCouponTemplate(couponTemplateCondition);
            if(flag == 0) {
                responseResult.setCode(BusinessCode.CODE_OK);
            }else{
                responseResult.setCode(BusinessCode.CODE_1001);
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
    public ResponseResult<PagedList<CouponTemplateVO>> findCouponTemplatePageByCondition(@RequestBody CouponTemplateCondition couponTemplateCondition) {
        logger.info("优惠券模板列表findCouponTemplatePageByCondition  方法入参：info:" + JsonUtil.toJSONString(couponTemplateCondition));
        ResponseResult<PagedList<CouponTemplateVO>> responseResult = new ResponseResult<PagedList<CouponTemplateVO>>();
        PagedList<CouponTemplateVO> pagedList =  couponTemplateService.findCouponTemplatePageByCondition(couponTemplateCondition);
        responseResult.setData(pagedList);
        return responseResult;
    }

    /**
     *
     *@Deccription  设为无效
     *@Params  condition
     *@Return  ResponseResult 删除是否成功
     *@User  wl
     *@Date   2018/8/6 20:39
     */
    @Override
    public ResponseResult<Integer> updateCouponTemplateToValid(@RequestBody CouponSetToValidCondition condition) {
        ResponseResult<Integer> responseResult = new ResponseResult<Integer>();
        Date updated = new Date();
        int count = couponTemplateService.updateCouponTemplateToValid(condition.getId(),condition.getUserId(),updated,condition.getUserName());
        if(count!=1){
           throw  new BusinessException(BusinessCode.CODE_1001,"设置失败");
        }
        responseResult.setCode(BusinessCode.CODE_OK);
        responseResult.setMessage("设置成功");
        return responseResult;
    }

    /**
     *
     *@Deccription 查看优惠券模板详情
     *@Params  id
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/6 20:45
     */
    @Override
    public ResponseResult<CouponTemplateVO> viewCouponTemplateDetail(@RequestParam("id") String id) {
        ResponseResult<CouponTemplateVO> responseResult = new ResponseResult<CouponTemplateVO>();
        CouponTemplateVO vo = couponTemplateService.viewCouponTemplateDetailById(id);
        responseResult.setData(vo);
        return responseResult;
    }



}
