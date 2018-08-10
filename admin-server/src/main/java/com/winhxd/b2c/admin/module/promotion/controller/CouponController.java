package com.winhxd.b2c.admin.module.promotion.controller;

import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.vo.*;
import com.winhxd.b2c.common.feign.promotion.*;
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
	@Autowired
	private CouponApplyServiceClient couponApplyServiceClient;

	//=====================================优惠券活动开始=============================================================
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
	 *@Deccription 根据id 查询出对应的实体类(查看和回显编辑页)
	 *@Params   id
	 *@Return   ResponseResult
	 *@User     sjx
	 *@Date   2018/8/8
	 */
	@ApiOperation("根据id 查询优惠券活动")
	@PostMapping(value = "/v1/getCouponActivityById")
	public ResponseResult getCouponActivityById(@RequestParam("id") String id){
		/**
		 * 参数验证
		 */
		return couponActivityServiceClient.getCouponActivityById(id);
	}
	/**
	 *
	 *@Deccription 编辑优惠券活动
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/7
	 */
	@ApiOperation("编辑优惠券活动")
	@PostMapping(value = "/v1/updateCouponActivity")
	public ResponseResult updateCouponActivity(@RequestBody CouponActivityAddCondition condition){
		/**
		 * 参数验证
		 */
		return couponActivityServiceClient.updateCouponActivity(condition);
	}
	/**
	 *
	 *@Deccription 删除优惠券活动（设为无效）
	 *@Params  id
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/8
	 */
	@ApiOperation("删除优惠券活动")
	@PostMapping(value = "/v1/deleteCouponActivity")
	public ResponseResult deleteCouponActivity(@RequestParam("id") String id){
		/**
		 * 参数验证
		 */
		return couponActivityServiceClient.deleteCouponActivity(id);
	}
	/**
	 *
	 *@Deccription 撤回活动优惠券
	 *@Params  id
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/9
	 */
	@ApiOperation("撤回活动优惠券")
	@PostMapping(value = "/v1/revocationActivityCoupon")
	public ResponseResult revocationActivityCoupon(@RequestParam("id") String id){
		/**
		 * 参数验证
		 */
		return couponActivityServiceClient.revocationActivityCoupon(id);
	}
	/**
	 *
	 *@Deccription 停用/开启活动
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/9
	 */
	@ApiOperation("停用/开启活动")
	@PostMapping(value = "/v1/updateCouponActivityStatus")
	public ResponseResult updateCouponActivityStatus(@RequestBody CouponActivityAddCondition condition){
		/**
		 * 参数验证
		 */
		return couponActivityServiceClient.updateCouponActivityStatus(condition);
	}
	/**
	 *
	 *@Deccription 根据活动获取优惠券列表
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/9
	 */
	@ApiOperation("根据活动获取优惠券列表")
	@PostMapping(value = "/v1/queryCouponByActivity")
	public ResponseResult queryCouponByActivity(@RequestBody CouponActivityCondition condition){
		/**
		 * 参数验证
		 */
		return couponActivityServiceClient.queryCouponByActivity(condition);
	}
	/**
	 *
	 *@Deccription 根据活动获取小店信息
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/9
	 */
	@ApiOperation("根据活动获取小店信息")
	@PostMapping(value = "/v1/queryStoreByActivity")
	public ResponseResult queryStoreByActivity(@RequestBody CouponActivityCondition condition){
		/**
		 * 参数验证
		 */
		return couponActivityServiceClient.queryStoreByActivity(condition);
	}

	//=====================================优惠券活动结束=============================================================


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
		 AdminUser adminUser = UserContext.getCurrentAdminUser();
		 String userId = adminUser.getId()+"";
		 String userName = adminUser.getUsername();

//		String userId = "100102";
//		String userName = "大花脸";
		String code = getUUID();
		condition.setCode(code);
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
		ResponseResult responseResult = couponTemplateServiceClient.addCouponTemplate(condition);
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

		 AdminUser adminUser = UserContext.getCurrentAdminUser();
		 String userId = adminUser.getId()+"";
		 String userName = adminUser.getUsername();
//		String userId = "100102";
//		String userName = "大花脸";

		ResponseResult responseResult = couponTemplateServiceClient.updateCouponTemplateToValid(ids,userId,userName);
		return responseResult;
	}




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
		AdminUser adminUser = UserContext.getCurrentAdminUser();
		String userId = adminUser.getId()+"";
		String userName = adminUser.getUsername();

//		String userId = "100102";
//		String userName = "大花脸";
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
		 String userId = adminUser.getId()+"";
		 String userName = adminUser.getUsername();

//		String userId = "100102";
//		String userName = "大花脸";
		ResponseResult responseResult = couponInvestorServiceClient.updateCouponInvestorToValid(id,userId,userName);
		return responseResult;
	}



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
	 String userId = adminUser.getId()+"";
	 String userName = adminUser.getUsername();
//	String userId = "100102";
//	String userName = "大花脸";
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
	String userId = adminUser.getId()+"";
	String userName = adminUser.getUsername();
//	String userId = "100102";
//	String userName = "大花脸";
	ResponseResult responseResult = couponGradeServiceClient.updateCouponGradeValid(id,userId,userName);
	return responseResult;
}




//============================================优惠券类型规则开始============================================================
	@ApiOperation("新建优惠券类型")
	@PostMapping(value = "/v1/addCouponApply")
	public ResponseResult addCouponApply(@RequestBody CouponApplyCondition  condition){
		/**
		 * 参数校验
		 */
		AdminUser adminUser = UserContext.getCurrentAdminUser();
		 String userId = adminUser.getId()+"";
		 String userName = adminUser.getUsername();
//		String userId = "100102";
//		String userName = "大花脸";
		String code = getUUID();
		condition.setCode(code);
		ResponseResult responseResult = couponApplyServiceClient.addCouponApply(condition);
		return responseResult;

	}

	@ApiOperation("查看优惠券类型")
	@PostMapping(value = "/v1/viewCouponApplyDetail")
	public ResponseResult viewCouponApplyDetail(@RequestParam("id") String id){
		ResponseResult responseResult = couponApplyServiceClient.viewCouponApplyDetail(id);
		return responseResult;
	}


	@ApiOperation("优惠券类型设置无效")
	@PostMapping(value = "/v1/updateCouponApplyToValid")
	public ResponseResult updateCouponApplyToValid(@RequestParam("id") String id){
		 AdminUser adminUser = UserContext.getCurrentAdminUser();
		 String userId = adminUser.getId()+"";
		 String userName = adminUser.getUsername();

//		String userId = "100102";
//		String userName = "大花脸";
		ResponseResult responseResult = couponApplyServiceClient.updateCouponApplyToValid(id,userId,userName);
		return responseResult;
	}


	@ApiOperation("优惠券类型列表分页条件查询")
	@PostMapping(value = "/v1/findCouponApplyPage")
	public ResponseResult findCouponApplyPage(@RequestBody CouponApplyCondition  condition){
		ResponseResult<PagedList<CouponApplyVO>> responseResult = couponApplyServiceClient.findCouponApplyPage(condition);
		return responseResult;
	}



//=====================================点模板引用数量显示分页===============================================

@ApiOperation("点出资方列表上模板引用数量表分页")
@GetMapping(value = "/v1/findInvertorTempleteCountPage")
public ResponseResult findInvertorTempleteCountPage(@RequestParam("invertorId") String invertorId,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
    if(pageNo!=null){
		pageNo = 1;
	}
	if(pageSize!=null){
		pageSize = 10;
	}
	ResponseResult<PagedList<InvertorTempleteCountVO>> responseResult = couponInvestorServiceClient.findInvertorTempleteCountPage(invertorId,pageNo,pageSize);
   return responseResult;
}


@ApiOperation("点优惠方式规则列表上模板引用数量表分页")
@GetMapping(value = "/v1/findGradeTempleteCountPage")
public ResponseResult findGradeTempleteCountPage(@RequestParam("gradeId") String gradeId,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
	if(pageNo!=null){
		pageNo = 1;
	}
	if(pageSize!=null){
		pageSize = 10;
	}
	ResponseResult<PagedList<GradeTempleteCountVO>> responseResult = couponGradeServiceClient.findGradeTempleteCountPage(gradeId,pageNo,pageSize);
	return responseResult;
}


@ApiOperation("点类型规则列表上模板引用数量表分页")
@GetMapping(value = "/v1/findApplyTempleteCountPage")
public ResponseResult findApplyTempleteCountPage(@RequestParam("applyId") String applyId,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
	if(pageNo!=null){
		pageNo = 1;
	}
	if(pageSize!=null){
		pageSize = 10;
	}
	ResponseResult<PagedList<ApplyTempleteCountVO>> responseResult = couponApplyServiceClient.findApplyTempleteCountPage(applyId,pageNo,pageSize);
	return responseResult;
}


	/**
	 * 自动生成32位的UUid，对应数据库的主键id进行插入用。
	 * @return
	 */
	public String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}





}
