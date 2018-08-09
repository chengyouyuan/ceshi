package com.winhxd.b2c.admin.module.promotion.controller;

import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.vo.CouponActivityVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.common.feign.promotion.CouponActivityServiceClient;
import com.winhxd.b2c.common.feign.promotion.CouponGradeServiceClient;
import com.winhxd.b2c.common.feign.promotion.CouponInvestorServiceClient;
import com.winhxd.b2c.common.feign.promotion.CouponTemplateServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;


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
	private CouponInvestorServiceClient couponInvestorServiceClient;
	@Autowired
	private CouponActivityServiceClient couponActivityServiceClient;
	@Autowired
	private CouponGradeServiceClient couponGradeServiceClient;

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


	//=====================================优惠券模板开始=============================================================
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


//============================================优惠券模板结束============================================================


//============================================出资方开始============================================================


	@ApiOperation("获取出资方分页列表")
	@PostMapping(value = "/v1/getCouponInvestorPage")
	public ResponseResult<PagedList<CouponInvestorVO>> getCouponInvestorPage(@RequestBody CouponInvestorCondition condition){
		ResponseResult<PagedList<CouponInvestorVO>> responseResult = couponInvestorServiceClient.getCouponInvestorPage(condition);
		return responseResult;
	}

	@ApiOperation("新建出资方")
	@PostMapping(value = "/v1/addCouponInvestor")
	public ResponseResult addCouponInvestor(@RequestBody LinkedHashMap detailData){
		/**
		 *  校验参数
		 */
		String name = detailData.get("name").toString();
		String remark = detailData.get("remark").toString();
		ArrayList list  = (ArrayList)detailData.get("listDetail");
		/**
		AdminUser adminUser = UserContext.getCurrentAdminUser();
		String userId = adminUser.getId()+"";
		String userName = adminUser.getUsername();
		**/
		String userId = "100102";
		String userName = "大花脸";
		String code = getUUID();
		CouponInvestorCondition condition = new CouponInvestorCondition();
		condition.setCode(code);
		condition.setName(name);
		condition.setRemarks(remark);
		condition.setUserId(userId);
		condition.setUserName(userName);
		condition.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
		condition.setDetails(list);
		ResponseResult responseResult = couponInvestorServiceClient.addCouponInvestor(condition);
		return responseResult;
	}




	@ApiOperation("查看出资方详情")
	@GetMapping(value = "/v1/viewCouponInvestorDetail")
	public ResponseResult viewCouponInvestorDetail(@RequestParam("id") String id){
		ResponseResult responseResult = couponInvestorServiceClient.viewCouponInvestorDetail(id);
		return responseResult;
	}


	@ApiOperation("删除出资方出资方")
	@GetMapping(value = "/v1/updateCouponInvestorToValid")
	public ResponseResult updateCouponInvestorToValid(@RequestParam("id") String id){
		AdminUser adminUser = UserContext.getCurrentAdminUser();
		/**
		 String userId = adminUser.getId()+"";
		 String userName = adminUser.getUsername();
		 */
		String userId = "100102";
		String userName = "大花脸";
		ResponseResult responseResult = couponInvestorServiceClient.updateCouponInvestorToValid(id,userId,userName);
		return responseResult;
	}

//============================================出资方结束============================================================


//============================================优惠方式规则开始============================================================

@ApiOperation("多条件分页查询优惠方式规则")
@PostMapping(value = "/v1/getCouponGradePage")
public ResponseResult<PagedList<CouponGradeVO>> getCouponGradePage(@RequestBody CouponGradeCondition condition){
	ResponseResult<PagedList<CouponGradeVO>> responseResult = couponGradeServiceClient.getCouponGradePage(condition);
	return responseResult;
}

@ApiOperation("新建优惠方式规则")
@PostMapping(value = "/v1/addCouponGrade")
public ResponseResult addCouponGrade(@RequestBody CouponGradeCondition couponGradeCondition){
	/**
	 * 参数校验
	 */
	AdminUser adminUser = UserContext.getCurrentAdminUser();
	/**
	 String userId = adminUser.getId()+"";
	 String userName = adminUser.getUsername();
	 */
	String userId = "100102";
	String userName = "大花脸";
	 String code = getUUID();
	 couponGradeCondition.setCode(code);
	 couponGradeCondition.setUserId(userId);
	couponGradeCondition.setUserName(userName);
	 ResponseResult responseResult = couponGradeServiceClient.addCouponGrade(couponGradeCondition);
	 return responseResult;
}

@ApiOperation("查看优惠方式规则详情")
@GetMapping(value = "/v1/viewCouponGradeDetail")
public ResponseResult viewCouponGradeDetail(@RequestParam("id") String id){
	ResponseResult responseResult = couponGradeServiceClient.viewCouponGradeDetail(id);
	return responseResult;
}


@ApiOperation("优惠方式规则逻辑删除/设置为无效")
@GetMapping (value = "/v1/updateCouponGradeValid")
public ResponseResult updateCouponGradeValid(@RequestParam("id") String id){
	AdminUser adminUser = UserContext.getCurrentAdminUser();
	/**
	String userId = adminUser.getId()+"";
	String userName = adminUser.getUsername();
	*/
	String userId = "100102";
	String userName = "大花脸";
	ResponseResult responseResult = couponGradeServiceClient.updateCouponGradeValid(id,userId,userName);
	return responseResult;
}

//============================================优惠方式规则结束============================================================


//============================================优惠券类型规则开始============================================================



//============================================优惠券类型规则开始============================================================




	/**
	 * 自动生成32位的UUid，对应数据库的主键id进行插入用。
	 * @return
	 */
	public String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}





}
