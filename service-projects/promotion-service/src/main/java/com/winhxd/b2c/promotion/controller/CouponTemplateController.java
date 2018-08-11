package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.common.feign.promotion.CouponTemplateServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.promotion.service.CouponTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseResult<Integer> addCouponTemplate(@RequestBody CouponTemplateCondition couponTemplateCondition) {
        /**
         * 校验必填参数
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
     *@Deccription 多条件分页查询 优惠券模板列表
     *@Params  CouponTemplateCondition
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/6 17:53
     */
    @ApiOperation(value = "多条件分页查询 优惠券模板列表", notes = "多条件分页查询 优惠券模板列表",response = ResponseResult.class)
    @Override
    public ResponseResult<PagedList<CouponTemplateVO>> findCouponTemplatePageByCondition(CouponTemplateCondition couponTemplateCondition) {
        logger.info("优惠券模板列表findCouponTemplatePageByCondition  方法入参：info:" + JsonUtil.toJSONString(couponTemplateCondition));
        ResponseResult<PagedList<CouponTemplateVO>> responseResult =  couponTemplateService.findCouponTemplatePageByCondition(couponTemplateCondition);
        return responseResult;
    }

    /**
     *
     *@Deccription  单个删除/批量删除（非物理删除）/ 设为无效
     *@Params  ids  多个页面勾选的ID 用逗号","隔开
     *@Return  ResponseResult 删除是否成功
     *@User  wl
     *@Date   2018/8/6 20:39
     */
    @ApiOperation(value = "单个删除/批量删除（非物理删除）/ 设为无效", notes = "单个删除/批量删除（非物理删除）/ 设为无效",response = ResponseResult.class)
    @Override
    public ResponseResult<Integer> updateCouponTemplateToValid(@RequestParam("ids") String ids,@RequestParam("userId") String userId,@RequestParam("userName") String userName) {
        ResponseResult responseResult = new ResponseResult();
        if(StringUtils.isBlank(ids)){
            responseResult.setCode(BusinessCode.CODE_1007);
            responseResult.setMessage("参数为空错误");
            return responseResult;
        }
        Long updateBy = Long.parseLong(userId);
        Date updated = new Date();
        String updateByName = userName ;
        String[] idsArr = ids.split(",");
        List<String> idsList = Arrays.asList(idsArr);
        couponTemplateService.updateCouponTemplateToValid(idsList,updateBy,updated,updateByName);
        responseResult.setCode(BusinessCode.CODE_OK);
        responseResult.setMessage("修改成功");
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
    @ApiOperation(value = "查看优惠券模板详情", notes = "查看优惠券模板详情",response = ResponseResult.class)
    @Override
    public ResponseResult<CouponTemplateVO> viewCouponTemplateDetail(@RequestParam("id") String id) {
        ResponseResult<CouponTemplateVO> responseResult = couponTemplateService.viewCouponTemplateDetailById(id);
        return responseResult;
    }



}
