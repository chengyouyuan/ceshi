package com.winhxd.b2c.admin.module.promotion.controller;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.vo.*;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
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
@RequestMapping(value = "/promotion")
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
	@PostMapping(value = "/529/v1/queryCouponActivity")
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
	@PostMapping(value = "/530/v1/addCouponActivity")
	public ResponseResult addCouponActivity(@RequestBody CouponActivityAddCondition condition){
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
	@PostMapping(value = "/531/v1/getCouponActivityById")
	public ResponseResult getCouponActivityById(@RequestParam("id") String id){
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
	@PostMapping(value = "/532/v1/updateCouponActivity")
	public ResponseResult updateCouponActivity(@RequestBody CouponActivityAddCondition condition){
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
	@PostMapping(value = "/533/v1/deleteCouponActivity")
	public ResponseResult deleteCouponActivity(@RequestParam("id") String id){
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
	@PostMapping(value = "/534/v1/revocationActivityCoupon")
	public ResponseResult revocationActivityCoupon(@RequestParam("id") String id){
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
	@PostMapping(value = "/535/v1/updateCouponActivityStatus")
	public ResponseResult updateCouponActivityStatus(@RequestBody CouponActivityAddCondition condition){
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
	@PostMapping(value = "/536/v1/queryCouponByActivity")
	public ResponseResult queryCouponByActivity(@RequestBody CouponActivityCondition condition){
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
	@PostMapping(value = "/537/v1/queryStoreByActivity")
	public ResponseResult queryStoreByActivity(@RequestBody CouponActivityCondition condition){
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
	@ApiOperation("优惠券模板分页列表")
	@PostMapping(value = "/510/v1/getCouponTemplatePage")
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
	@ApiOperation("优惠券模板新建")
	@PostMapping(value = "/511/v1/addCouponTemplate")
	public ResponseResult<Integer> addCouponTemplate(@RequestBody CouponTemplateCondition condition){
		/**
		 * 参数校验还未完善
		 */
		UserInfo userInfo = UserManager.getCurrentUser();
		String userId = userInfo.getId()+"";
		String userName = userInfo.getUsername();
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
	@ApiOperation("优惠券模板详情查看")
	@GetMapping(value = "/512/v1/viewCouponTemplateDetail")
	public ResponseResult<CouponTemplateVO> viewCouponTemplateDetail(@RequestParam("id") String id){
		ResponseResult<CouponTemplateVO> responseResult = couponTemplateServiceClient.viewCouponTemplateDetail(id);
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
	@ApiOperation("优惠券模板设为无效")
	@PostMapping(value = "/513/v1/updateCouponTemplateToValid")
	public ResponseResult<Integer> updateCouponTemplateToValid(@RequestParam("ids") String ids){
		UserInfo userInfo = UserManager.getCurrentUser();
		String userId = userInfo.getId()+"";
		String userName = userInfo.getUsername();
		ResponseResult<Integer> responseResult = couponTemplateServiceClient.updateCouponTemplateToValid(ids,userId,userName);
		return responseResult;
	}




//============================================出资方开始============================================================

   /**
    *
    *@Deccription 获取出资方分页列表
    *@Params  condition  查询条件
    *@Return  ResponseResult<PagedList<CouponInvestorVO>>
    *@User  wl
    *@Date   2018/8/11 12:27
    */
	@ApiOperation("出资方分页列表")
	@PostMapping(value = "/528/v1/getCouponInvestorPage")
	public ResponseResult<PagedList<CouponInvestorVO>> getCouponInvestorPage(@RequestBody CouponInvestorCondition condition){
		ResponseResult<PagedList<CouponInvestorVO>> responseResult = couponInvestorServiceClient.getCouponInvestorPage(condition);
		return responseResult;
	}

	/**
	 *
	 *@Deccription 新建出资方
	 *@Params  detailData  新建参数
	 *@Return  是否成功  0 成功
	 *@User  wl
	 *@Date   2018/8/11 12:26
	 */
	@ApiOperation("出资方规则新建")
	@PostMapping(value = "/514/v1/addCouponInvestor")
	public ResponseResult<Integer> addCouponInvestor(@RequestBody LinkedHashMap detailData){
		/**
		 *  校验参数
		 */
		String name = detailData.get("name").toString();
		String remark = detailData.get("remark").toString();
		ArrayList list  = (ArrayList)detailData.get("listDetail");

		UserInfo userInfo = UserManager.getCurrentUser();
		String userId = userInfo.getId()+"";
		String userName = userInfo.getUsername();
		String code = getUUID();
		CouponInvestorCondition condition = new CouponInvestorCondition();
		condition.setCode(code);
		condition.setName(name);
		condition.setRemarks(remark);
		condition.setUserId(userId);
		condition.setUserName(userName);
		condition.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
		condition.setDetails(list);
		ResponseResult<Integer> responseResult = couponInvestorServiceClient.addCouponInvestor(condition);
		return responseResult;
	}



	/**
	 *
	 *@Deccription 查看出资方详情
	 *@Params  id  出资方id
	 *@Return  ResponseResult<CouponInvestorVO>
	 *@User  wl
	 *@Date   2018/8/11 12:25
	 */
	@ApiOperation("出资方规则详情查看")
	@GetMapping(value = "/515/v1/viewCouponInvestorDetail")
	public ResponseResult<CouponInvestorVO> viewCouponInvestorDetail(@RequestParam("id") String id){
		ResponseResult<CouponInvestorVO> responseResult = couponInvestorServiceClient.viewCouponInvestorDetail(id);
		return responseResult;
	}


	@ApiOperation("出资方规则设置无效")
	@GetMapping(value = "/516/v1/updateCouponInvestorToValid")
	public ResponseResult<Integer> updateCouponInvestorToValid(@RequestParam("id") String id){
		UserInfo userInfo = UserManager.getCurrentUser();
		String userId = userInfo.getId()+"";
		String userName = userInfo.getUsername();
		ResponseResult<Integer> responseResult = couponInvestorServiceClient.updateCouponInvestorToValid(id,userId,userName);
		return responseResult;
	}



//============================================优惠方式规则开始============================================================
/**
 *
 *@Deccription 多条件分页查询坎级
 *@Params  condition  查询条件
 *@Return  ResponseResult<PagedList<CouponGradeVO>>
 *@User  wl
 *@Date   2018/8/11 12:24
 */
@ApiOperation("坎级多条件分页查询")
@PostMapping(value = "/517/v1/getCouponGradePage")
public ResponseResult<PagedList<CouponGradeVO>> getCouponGradePage(@RequestBody CouponGradeCondition condition){
	ResponseResult<PagedList<CouponGradeVO>> responseResult = couponGradeServiceClient.getCouponGradePage(condition);
	return responseResult;
}

/**
 *
 *@Deccription 新建坎级规则
 *@Params  couponGradeCondition  新建参数
 *@Return  是否成功 0 表示成功
 *@User  wl
 *@Date   2018/8/11 12:23
 */
@ApiOperation("坎级规则新建")
@PostMapping(value = "/518/v1/addCouponGrade")
public ResponseResult<Integer> addCouponGrade(@RequestBody CouponGradeCondition couponGradeCondition){
	/**
	 * 参数校验
	 */
	 UserInfo userInfo = UserManager.getCurrentUser();
	 String userId = userInfo.getId()+"";
	 String userName = userInfo.getUsername();
	 String code = getUUID();
	 couponGradeCondition.setCode(code);
	 couponGradeCondition.setUserId(userId);
	 couponGradeCondition.setUserName(userName);
	 ResponseResult<Integer> responseResult = couponGradeServiceClient.addCouponGrade(couponGradeCondition);
	 return responseResult;
}

/**
 *
 *@Deccription 查看坎级详情
 *@Params   id  坎级id
 *@Return  CouponGradeVO
 *@User  wl
 *@Date   2018/8/11 12:22
 */
@ApiOperation("坎级详情查看")
@GetMapping(value = "/519/v1/viewCouponGradeDetail")
public ResponseResult<CouponGradeVO> viewCouponGradeDetail(@RequestParam("id") String id){
	ResponseResult<CouponGradeVO> responseResult = couponGradeServiceClient.viewCouponGradeDetail(id);
	return responseResult;
}

/**
 *
 *@Deccription 坎级逻辑删除/设置为无效
 *@Params  id  坎级id
 *@Return  是否成功 0 表示成功
 *@User  wl
 *@Date   2018/8/11 12:21
 */
@ApiOperation("坎级设置为无效")
@GetMapping (value = "/520/v1/updateCouponGradeValid")
public ResponseResult<Integer> updateCouponGradeValid(@RequestParam("id") String id){
	UserInfo userInfo = UserManager.getCurrentUser();
	String userId = userInfo.getId()+"";
	String userName = userInfo.getUsername();
	ResponseResult<Integer> responseResult = couponGradeServiceClient.updateCouponGradeValid(id,userId,userName);
	return responseResult;
}




//============================================优惠券类型规则开始============================================================
	/**
	 *
	 *@Deccription 新建适用对象
	 *@Params  condition  新建参数
	 *@Return  是否成功 0表示成功
	 *@User  wl
	 *@Date   2018/8/11 12:20
	 */
	@ApiOperation("适用对象新建")
	@PostMapping(value = "/521/v1/addCouponApply")
	public ResponseResult<Integer> addCouponApply(@RequestBody CouponApplyCondition  condition){
		/**
		 * 参数校验
		 */
		UserInfo userInfo = UserManager.getCurrentUser();
		String userId = userInfo.getId()+"";
		String userName = userInfo.getUsername();
		String code = getUUID();
		condition.setCode(code);
		condition.setUserId(userId);
		condition.setUserName(userName);
		ResponseResult<Integer> responseResult = couponApplyServiceClient.addCouponApply(condition);
		return responseResult;

	}

	/**
	 *
	 *@Deccription 查看适用对象类型
	 *@Params  id  查询条件
	 *@Return   CouponApplyVO
	 *@User  wl
	 *@Date   2018/8/11 12:16
	 */
	@ApiOperation("适用对象类型查看")
	@PostMapping(value = "/522/v1/viewCouponApplyDetail")
	public ResponseResult<CouponApplyVO> viewCouponApplyDetail(@RequestParam("id") String id){
		ResponseResult<CouponApplyVO> responseResult = couponApplyServiceClient.viewCouponApplyDetail(id);
		return responseResult;
	}

	/**
	 *
	 *@Deccription 适用对象设置无效
	 *@Params  id
	 *@Return  是否成功 0 表示成功
	 *@User  wl
	 *@Date   2018/8/11 12:16
	 */
	@ApiOperation("适用对象设置无效")
	@PostMapping(value = "/523/v1/updateCouponApplyToValid")
	public ResponseResult<Integer> updateCouponApplyToValid(@RequestParam("id") String id){
		UserInfo userInfo = UserManager.getCurrentUser();
		String userId = userInfo.getId()+"";
		String userName = userInfo.getUsername();
		ResponseResult<Integer> responseResult = couponApplyServiceClient.updateCouponApplyToValid(id,userId,userName);
		return responseResult;
	}

	/**
	 *
	 *@Deccription 适用对象列表分页条件查询
	 *@Params  condition  查询条件
	 *@Return   ResponseResult<PagedList<CouponApplyVO>>
	 *@User  wl
	 *@Date   2018/8/11 12:16
	 */
	@ApiOperation("适用对象列表分页条件查询")
	@PostMapping(value = "/524/v1/findCouponApplyPage")
	public ResponseResult<PagedList<CouponApplyVO>> findCouponApplyPage(@RequestBody CouponApplyCondition  condition){
		ResponseResult<PagedList<CouponApplyVO>> responseResult = couponApplyServiceClient.findCouponApplyPage(condition);
		return responseResult;
	}



//=====================================点模板引用数量显示分页===============================================
	/**
	 *
	 *@Deccription 点出资方列表上模板引用数量表分页
	 *@Params  invertorId  出资方规则ID  pageNo  页码  pageSize 页大小
	 *@Return  ResponseResult<PagedList<InvertorTempleteCountVO>>
	 *@User  wl
	 *@Date   2018/8/11 12:11
	 */
@ApiOperation("点出资方列表上模板引用数量表分页")
@GetMapping(value = "/525/v1/findInvertorTempleteCountPage")
public ResponseResult<PagedList<InvertorTempleteCountVO>> findInvertorTempleteCountPage(@RequestParam("invertorId") String invertorId,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
    if(pageNo!=null){
		pageNo = 1;
	}
	if(pageSize!=null){
		pageSize = 10;
	}
	ResponseResult<PagedList<InvertorTempleteCountVO>> responseResult = couponInvestorServiceClient.findInvertorTempleteCountPage(invertorId,pageNo,pageSize);
   return responseResult;
}

	/**
	 *
	 *@Deccription 点坎级规则列表上模板引用数量表分页
	 *@Params  gradeId  坎级ID  pageNo  页码  pageSize 页大小
	 *@Return  ResponseResult<PagedList<GradeTempleteCountVO>>
	 *@User  wl
	 *@Date   2018/8/11 12:11
	 */
@ApiOperation("点坎级列表上模板引用数量表分页")
@GetMapping(value = "/526/v1/findGradeTempleteCountPage")
public ResponseResult<PagedList<GradeTempleteCountVO>> findGradeTempleteCountPage(@RequestParam("gradeId") String gradeId,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
	if(pageNo!=null){
		pageNo = 1;
	}
	if(pageSize!=null){
		pageSize = 10;
	}
	ResponseResult<PagedList<GradeTempleteCountVO>> responseResult = couponGradeServiceClient.findGradeTempleteCountPage(gradeId,pageNo,pageSize);
	return responseResult;
}



/**
 *
 *@Deccription 点适用对象列表上模板引用数量表分页
 *@Params  applyId  应用对象ID  pageNo  页码  pageSize 页大小
 *@Return  ResponseResult<PagedList<ApplyTempleteCountVO>>
 *@User  wl
 *@Date   2018/8/11 12:11
 */
@ApiOperation("点适用对象列表上模板引用数量表分页")
@GetMapping(value = "/527/v1/findApplyTempleteCountPage")
public ResponseResult<PagedList<ApplyTempleteCountVO>> findApplyTempleteCountPage(@RequestParam("applyId") String applyId,@RequestParam("pageNo")Integer pageNo,@RequestParam("pageSize")Integer pageSize){
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
