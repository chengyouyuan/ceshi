package com.winhxd.b2c.admin.module.promotion.controller;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.enums.CouponApplyEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponGradeEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.vo.*;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.promotion.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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
	 *@Deccription  获取优惠券活动列表（领券）
	 *@Params  condition
	 *@Return  ResponseResult<PagedList<CouponActivityVO>>
	 *@User  sjx
	 *@Date   2018/8/7
	 */
	@ApiOperation("获取优惠券活动领券列表")
	@PostMapping(value = "/5029/v1/queryCouponActivityPull")
	public ResponseResult<PagedList<CouponActivityVO>> queryCouponActivityPull(@RequestBody CouponActivityCondition condition){
		condition.setType((short) 1);
		logger.info("获取优惠券活动领券列表，type="+condition.getType());
		return couponActivityServiceClient.queryCouponActivity(condition);
	}
	/**
	 *
	 *@Deccription  获取优惠券活动列表（推券）
	 *@Params  condition
	 *@Return  ResponseResult<PagedList<CouponActivityVO>>
	 *@User  sjx
	 *@Date   2018/8/7
	 */
	@ApiOperation("获取优惠券活动推券列表")
	@PostMapping(value = "/5049/v1/queryCouponActivityPush")
	public ResponseResult<PagedList<CouponActivityVO>> queryCouponActivityPush(@RequestBody CouponActivityCondition condition){
		condition.setType((short) 2);
		logger.info("获取优惠券活动推券列表，type="+condition.getType());
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
	@PostMapping(value = "/5030/v1/addCouponActivity")
	public ResponseResult<Integer> addCouponActivity(@RequestBody CouponActivityAddCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		Long userId = userInfo.getId();
		String userName = userInfo.getUsername();
		String code = getUUID();
		condition.setCode(code);
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
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
	@PostMapping(value = "/5031/v1/getCouponActivityById")
	public ResponseResult<CouponActivityVO> getCouponActivityById(@RequestParam("id") String id){
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
	@PostMapping(value = "/5032/v1/updateCouponActivity")
	public ResponseResult<Integer> updateCouponActivity(@RequestBody CouponActivityAddCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		Long userId = userInfo.getId();
		String userName = userInfo.getUsername();
		String code = getUUID();
		condition.setCode(code);
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
		return couponActivityServiceClient.updateCouponActivity(condition);
	}
	/**
	 *
	 *@Deccription 删除优惠券活动（设为无效）
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/8
	 */
	@ApiOperation("删除优惠券活动")
	@PostMapping(value = "/5033/v1/deleteCouponActivity")
	public ResponseResult<Integer> deleteCouponActivity(@RequestBody CouponActivityCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		Long userId = userInfo.getId();
		String userName = userInfo.getUsername();
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
		return couponActivityServiceClient.deleteCouponActivity(condition);
	}
	/**
	 *
	 *@Deccription 撤回活动优惠券
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/9
	 */
	@ApiOperation("撤回活动优惠券")
	@PostMapping(value = "/5034/v1/revocationActivityCoupon")
	public ResponseResult<Integer> revocationActivityCoupon(@RequestBody CouponActivityCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		Long userId = userInfo.getId();
		String userName = userInfo.getUsername();
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
		return couponActivityServiceClient.revocationActivityCoupon(condition);
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
	@PostMapping(value = "/5035/v1/updateCouponActivityStatus")
	public ResponseResult<Integer> updateCouponActivityStatus(@RequestBody CouponActivityAddCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		Long userId = userInfo.getId();
		String userName = userInfo.getUsername();
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
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
	@PostMapping(value = "/5036/v1/queryCouponByActivity")
	public ResponseResult<PagedList<CouponActivityStoreVO>> queryCouponByActivity(@RequestBody CouponActivityCondition condition){
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
	@PostMapping(value = "/5037/v1/queryStoreByActivity")
	public ResponseResult<PagedList<CouponActivityStoreVO>> queryStoreByActivity(@RequestBody CouponActivityCondition condition){
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
	@PostMapping(value = "/5010/v1/getCouponTemplatePage")
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
	@PostMapping(value = "/5011/v1/addCouponTemplate")
	public ResponseResult<Integer> addCouponTemplate(@RequestBody CouponTemplateCondition condition){
		/**
		 * 参数校验还未完善
		 */
		if(condition==null || condition.getTitle()==null || condition.getInvestorId()==null
		   || condition.getGradeId()==null || condition.getApplyRuleId()==null
		   || condition.getPayType()==null || condition.getCalType()==null	){
           throw new BusinessException(BusinessCode.CODE_500010,"优惠券模板必填参数错误");
		}
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
	@GetMapping(value = "/5012/v1/viewCouponTemplateDetail")
	public ResponseResult<CouponTemplateVO> viewCouponTemplateDetail(@RequestParam("id") String id){
		ResponseResult<CouponTemplateVO> responseResult = couponTemplateServiceClient.viewCouponTemplateDetail(id);
		return responseResult;
	}



	/**
	 *
	 *@Deccription  设为无效
	 *@Params  id
	 *@Return  ResponseResult 删除是否成功
	 *@User  wl
	 *@Date   2018/8/6 20:39
	 */
	@ApiOperation("优惠券模板设为无效")
	@PostMapping(value = "/5013/v1/updateCouponTemplateToValid")
	public ResponseResult<Integer> updateCouponTemplateToValid(@RequestBody CouponSetToValidCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		condition.setUserId(userInfo.getId());
		condition.setUserName(userInfo.getUsername());
		ResponseResult<Integer> responseResult = couponTemplateServiceClient.updateCouponTemplateToValid(condition);
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
	@PostMapping(value = "/5028/v1/getCouponInvestorPage")
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
	@PostMapping(value = "/5014/v1/addCouponInvestor")
	public ResponseResult<Integer> addCouponInvestor(@RequestBody LinkedHashMap detailData){
		/**
		 *  校验参数
		 */
		 CouponInvestorCondition condition = new CouponInvestorCondition();
         if(detailData.get("name")==null || detailData.get("listDetail")==null){
			 throw new BusinessException(BusinessCode.CODE_500010,"新建出资方必填参数错误");
		 }
		 String name = detailData.get("name").toString();
         if(detailData.get("remark")!=null){
			 condition.setRemarks(detailData.get("remark").toString());
		 }
		ArrayList list  = (ArrayList)detailData.get("listDetail");
		UserInfo userInfo = UserManager.getCurrentUser();
		String userId = userInfo.getId()+"";
		String userName = userInfo.getUsername();
		String code = getUUID();

		condition.setCode(code);
		condition.setName(name);
		condition.setUserId(userId);
		condition.setUserName(userName);
		condition.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
		condition.setDetails(list);
		checkCondition(condition);
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
	@GetMapping(value = "/5015/v1/viewCouponInvestorDetail")
	public ResponseResult<CouponInvestorVO> viewCouponInvestorDetail(@RequestParam("id") String id){
		ResponseResult<CouponInvestorVO> responseResult = couponInvestorServiceClient.viewCouponInvestorDetail(id);
		return responseResult;
	}


	@ApiOperation("出资方规则设置无效")
	@PostMapping(value = "/5016/v1/updateCouponInvestorToValid")
	public ResponseResult<Integer> updateCouponInvestorToValid(@RequestBody CouponSetToValidCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		condition.setUserId(userInfo.getId());
		condition.setUserName(userInfo.getUsername());
		ResponseResult<Integer> responseResult = couponInvestorServiceClient.updateCouponInvestorToValid(condition);
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
@PostMapping(value = "/5017/v1/getCouponGradePage")
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
@PostMapping(value = "/5018/v1/addCouponGrade")
public ResponseResult<Integer> addCouponGrade(@RequestBody CouponGradeCondition couponGradeCondition){
	/**
	 * 参数校验
	 */
	if(couponGradeCondition.getName()==null || couponGradeCondition.getType()==null ||
			couponGradeCondition.getReducedAmt()==null || couponGradeCondition.getReducedType()==null){
            throw new BusinessException(BusinessCode.CODE_500010,"必填参数为空");
	}
	if(couponGradeCondition.getReducedType()!=null){
         if(couponGradeCondition.getReducedType().equals(CouponGradeEnum.UP_TO_REDUCE_CASH.getCode())){
			 if(couponGradeCondition.getCost()==null || couponGradeCondition.getDiscountedAmt()==null){
				 throw new BusinessException(BusinessCode.CODE_500010,"必填参数为空");
			 }
		 }
	}

	if(couponGradeCondition.getReducedType()!=null){
		if(couponGradeCondition.getReducedType().equals(CouponGradeEnum.UP_TO_REDUCE_DISC.getCode())){
			if(couponGradeCondition.getDiscountedMaxAmt()==null || couponGradeCondition.getDiscounted()==null){
				throw new BusinessException(BusinessCode.CODE_500010,"必填参数为空");
			}
		}
	}

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
@GetMapping(value = "/5019/v1/viewCouponGradeDetail")
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
@PostMapping (value = "/5020/v1/updateCouponGradeValid")
public ResponseResult<Integer> updateCouponGradeValid(@RequestBody CouponSetToValidCondition condition){
	UserInfo userInfo = UserManager.getCurrentUser();
	condition.setUserId(userInfo.getId());
	condition.setUserName(userInfo.getUsername());
	ResponseResult<Integer> responseResult = couponGradeServiceClient.updateCouponGradeValid(condition);
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
	@PostMapping(value = "/5021/v1/addCouponApply")
	public ResponseResult<Integer> addCouponApply(@RequestBody CouponApplyCondition  condition){
		/**
		 * 参数校验
		 */
		if(condition!=null || condition.getName()==null || condition.getApplyRuleType()==null){
			throw new BusinessException(BusinessCode.CODE_500010,"必填参数为空");
		}
		if(condition.getApplyRuleType()!=null){
			if(condition.getApplyRuleType().equals(CouponApplyEnum.BRAND_COUPON.getCode())){
				if(condition.getCouponApplyBrandList()==null){
					throw new BusinessException(BusinessCode.CODE_500010,"品牌券必填参数为空");
				}
			}
			if(condition.getApplyRuleType().equals(CouponApplyEnum.PRODUCT_COUPON.getCode())){
				if(condition.getCouponApplyProductList()==null){
					throw new BusinessException(BusinessCode.CODE_500010,"商品券必填参数为空");
				}
			}
		}

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
	@PostMapping(value = "/5022/v1/viewCouponApplyDetail")
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
	@PostMapping(value = "/5023/v1/updateCouponApplyToValid")
	public ResponseResult<Integer> updateCouponApplyToValid(@RequestBody CouponSetToValidCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		condition.setUserId(userInfo.getId());
		condition.setUserName(userInfo.getUsername());
		ResponseResult<Integer> responseResult = couponApplyServiceClient.updateCouponApplyToValid(condition);
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
	@PostMapping(value = "/5024/v1/findCouponApplyPage")
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
@GetMapping(value = "/5025/v1/findInvertorTempleteCountPage")
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
@GetMapping(value = "/5026/v1/findGradeTempleteCountPage")
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
@GetMapping(value = "/5027/v1/findApplyTempleteCountPage")
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


	/**
	 *
	 *@Deccription 新增出资方信息时校验是否符合提交规则
	 *@Params  condition
	 *@Return  flag
	 *@User  wl
	 *@Date   2018/8/8 15:18
	 */
	public void checkCondition(CouponInvestorCondition condition){

		List deatils = condition.getDetails();
		if(deatils == null){
			throw new BusinessException(BusinessCode.CODE_500011,"出资方详情必填参数为空");
		}
		if(deatils!=null && deatils.size()>0){
			Float tempPercent = 0.00f;
			for(int i=0;i<deatils.size();i++){
				LinkedHashMap<String,Object> map =  (LinkedHashMap)deatils.get(i);
				tempPercent += Float.parseFloat(map.get("percent").toString());
			}
			//占比之和必须等于100
			if(tempPercent!=100.00f){
				throw new BusinessException(BusinessCode.CODE_500012,"出资方占比之和不等于等于100");
			}
			//如果不止一个出资方，但是参数中的出资方的类型 或者品牌编码相等 , 返回出资方不能重复
			if(deatils.size()>1){
				Set set = new HashSet();
				for(int i=0;i<deatils.size();i++){
					LinkedHashMap<String,Object> map =  (LinkedHashMap)deatils.get(i);
					boolean isContains =  set.add(map.get("investor_type").toString());
					if(isContains == false){
						throw new BusinessException(BusinessCode.CODE_500013,"出资方不能重复");
					}
				}
			}
		}
	}

}
