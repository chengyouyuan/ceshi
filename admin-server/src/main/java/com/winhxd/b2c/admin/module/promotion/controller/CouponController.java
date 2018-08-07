package com.winhxd.b2c.admin.module.promotion.controller;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityAddCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponActivityCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.common.feign.promotion.CouponActivityServiceClient;
import com.winhxd.b2c.common.feign.promotion.CouponTemplateServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author liuhanning
 * @date  2018年8月7日 上午9:35:01
 * @Description 优惠券管理
 * @version 
 */
@Api(value = "后台优惠券管理", tags = "后台优惠券管理接口")
@RestController
@RequestMapping(value = "/coupon")
public class CouponController {
	private Logger logger = LoggerFactory.getLogger(CouponController.class);
	@Autowired
	private CouponTemplateServiceClient couponTemplateServiceClient;
	@Autowired
	private CouponActivityServiceClient couponActivityServiceClient;

//==================================================================================================
	/**
	 *
	 *@Deccription  获取优惠券活动列表（领券、推券）
	 *@Params  condition
	 *@Return  ResponseResult<PagedList<CouponActivityVO>>
	 *@User  sjx
	 *@Date   2018/8/7
	 */
	@ApiOperation("获取优惠券活动列表")
	@PostMapping(value = "/v1/queryCouponActivity")
	public ResponseResult<PagedList<CouponActivityVO>> queryCouponActivity(@RequestBody CouponActivityCondition condition){
		return couponActivityServiceClient.queryCouponActivity(condition);
	}
	/**
	 *
	 *@Deccription 添加优惠券活动
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/7
	 */
	@ApiOperation("添加优惠券活动")
	@PostMapping(value = "/v1/addCouponActivity")
	public ResponseResult addCouponActivity(@RequestBody CouponActivityAddCondition condition){
		/**
		 * 参数验证
		 */
		return couponActivityServiceClient.addCouponActivity(condition);
	}
	/**
	 *
	 *@Deccription  获取优惠券模板分页列表
	 *@Params  condition
	 *@Return  ResponseResult<PagedList<CouponTemplateVO>>
	 *@User  wl
	 *@Date   2018/8/7 14:49
	 */
	@ApiOperation("获取优惠券模板分页列表")
	@PostMapping(value = "/v1/getCouponTemplatePage")
	public ResponseResult<PagedList<CouponTemplateVO>> getCouponTemplatePage(@RequestBody CouponTemplateCondition condition){
		return couponTemplateServiceClient.findCouponTemplatePageByCondition(condition);
	}

	/**
	 *
	 *@Deccription 新建优惠券模板
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  wl
	 *@Date   2018/8/7 16:01
	 */
	@ApiOperation("新建优惠券模板")
	@PostMapping(value = "/v1/addCouponTemplate")
	public ResponseResult addCouponTemplate(@RequestBody CouponTemplateCondition condition){
		/**
		 * 参数校验还未完善
		 */

		ResponseResult responseResult = couponTemplateServiceClient.addCouponTemplate(condition);
		return responseResult;
	}

    /**
     *
     *@Deccription 点击修改优惠券模板返回对应的数据信息
     *@Params  id  被点击的记录id
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/7 16:01
     */
	@ApiOperation("点击修改优惠券模板返回对应的数据信息")
	@GetMapping(value = "/v1/toEditCouponTemplate")
	public ResponseResult toEditCouponTemplate(@RequestParam("id") String id){
		ResponseResult responseResult = couponTemplateServiceClient.toEditCouponTemplate(id);
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
	@ApiOperation("查看优惠券模板详情")
	@GetMapping(value = "/v1/viewCouponTemplateDetail")
	public ResponseResult viewCouponTemplateDetail(@RequestParam("id") String id){
		ResponseResult responseResult = couponTemplateServiceClient.viewCouponTemplateDetail(id);
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
	@ApiOperation("单个删除/批量删除（非物理删除）/ 设为无效")
	@PostMapping(value = "/v1/updateCouponTemplateToValid")
	public ResponseResult updateCouponTemplateToValid(@RequestParam("ids") String ids){
		ResponseResult responseResult = couponTemplateServiceClient.updateCouponTemplateToValid(ids);
		return responseResult;
	}


	/**
	 *
	 *@Deccription 修改优惠券模板
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  wl
	 *@Date   2018/8/7 16:01
	 */
	@ApiOperation("修改优惠券模板")
	@PostMapping(value = "/v1/confirmEditCouponTemplate")
	public ResponseResult confirmUpdateCouponTemplate(@RequestBody CouponTemplateCondition condition){
		ResponseResult responseResult = couponTemplateServiceClient.confirmUpdateCouponTemplate(condition);
		return responseResult;
	}


//========================================================================================================

}
