package com.winhxd.b2c.admin.module.promotion.controller;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.utils.ExcelUtils;
import com.winhxd.b2c.admin.utils.ExcelVerifyResult;
import com.winhxd.b2c.admin.utils.ImportResult;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoExportVO;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.enums.CouponApplyEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponGradeEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.vo.*;
import com.winhxd.b2c.common.domain.store.condition.StoreCustomerRegionCondition;
import com.winhxd.b2c.common.domain.store.vo.BackStoreCustomerCountVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.promotion.*;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author liuhanning
 * @date  2018年8月7日 上午9:35:01
 * @Description 优惠券管理
 * @version 
 */
@Api(value = "后台优惠券管理", tags = "后台优惠券管理接口")
@RestController
@RequestMapping(value = "/promotion")
@CheckPermission(PermissionEnum.PROMOTION_MANAGEMENT)
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
	@Autowired
	private StoreServiceClient storeServiceClient;


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
	@PostMapping(value = "/queryCouponActivityPull")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PULL_LIST)
	public ResponseResult<PagedList<CouponActivityVO>> queryCouponActivityPull(@RequestBody CouponActivityCondition condition){
		condition.setType((short) 1);
		logger.info("获取优惠券活动领券列表，type="+condition.getType());
		return couponActivityServiceClient.queryCouponActivity(condition);
	}

	@ApiOperation("领券列表导出")
	@PostMapping(value = "/couponActivityPullExport")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PULL_LIST)
	public ResponseEntity<byte[]> couponActivityPullExport(@RequestBody CouponActivityCondition condition) {
		condition.setIsQueryAll(true);
		condition.setType((short) 1);
		logger.info("优惠券活动领券列表导出，type=" + condition.getType());
		ResponseResult<PagedList<CouponActivityVO>> responseResult = couponActivityServiceClient.queryCouponActivity(condition);
		if (responseResult != null && responseResult.getCode() == 0) {
			List<CouponActivityVO> list = responseResult.getData().getData();
			return ExcelUtils.exp(list, "领券管理列表");
		}
		return null;
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
	@PostMapping(value = "/queryCouponActivityPush")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PUSH_LIST)
	public ResponseResult<PagedList<CouponActivityVO>> queryCouponActivityPush(@RequestBody CouponActivityCondition condition){
		condition.setType((short) 2);
		logger.info("获取优惠券活动推券列表，type="+condition.getType());
		return couponActivityServiceClient.queryCouponActivity(condition);
	}

	@ApiOperation("优惠券活动推券列表导出")
	@PostMapping(value = "/couponActivityPushExport")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PUSH_LIST)
	public ResponseEntity<byte[]> couponActivityPushExport(@RequestBody CouponActivityCondition condition) {
		condition.setIsQueryAll(true);
		condition.setType((short) 2);
		logger.info("优惠券活动推券列表导出，type=" + condition.getType());
		ResponseResult<PagedList<CouponActivityVO>> responseResult = couponActivityServiceClient.queryCouponActivity(condition);
		if (responseResult != null && responseResult.getCode() == 0) {
			List<CouponActivityVO> list = responseResult.getData().getData();
			List<CouponActivityPushVO> pushList = list.stream().map(couponActivityVO -> {
				CouponActivityPushVO couponActivityPushVO = new CouponActivityPushVO();
				BeanUtils.copyProperties(couponActivityVO, couponActivityPushVO);
				return couponActivityPushVO;
			}).collect(Collectors.toList());
            return ExcelUtils.exp(pushList, "推券管理列表");
		}
		return null;
	}

	@ApiOperation("优惠券活动导入小店信息")
	@PostMapping(value = "/couponActivityStoreImportExcel")
	public ResponseResult<List<StoreUserInfoVO>> couponActivityStoreImportExcel(@RequestParam("inputfile") MultipartFile inputfile){
		ResponseResult<List<StoreUserInfoVO>> result = new ResponseResult<List<StoreUserInfoVO>>();
		if (!inputfile.getOriginalFilename().endsWith(".xls")
				&& !inputfile.getOriginalFilename().endsWith(".xlsx")){
			result.setMessage("文件扩展名错误！");
			result.setCode(BusinessCode.CODE_1001);
			return result;
		}
        List<CouponActivityImportStoreVO> list = null;
		ImportResult<CouponActivityImportStoreVO> importResult = null;
		try {
			importResult = ExcelUtils.importExcelVerify(inputfile.getInputStream(), CouponActivityImportStoreVO.class);
		} catch (Exception e) {
			throw new BusinessException(BusinessCode.CODE_1001, e);
		}
		List<ExcelVerifyResult> invalidList = importResult.getInvalidList(1);
        if (invalidList.isEmpty()) {
            list = importResult.getList();
        }
        if(CollectionUtils.isEmpty(list) || null == list.get(0).getStoreId()){
            result.setCode(BusinessCode.CODE_1007);
            result.setMessage("导入数据为空");
            return result;
        }
		return couponActivityServiceClient.couponActivityStoreImportExcel(list);
	}

    @ApiOperation("优惠券活动导入小店信息并统计门店下总用户数")
    @PostMapping(value = "/couponActivityStoreUserCountImportExcel")
    public ResponseResult<BackStoreCustomerCountVO> couponActivityStoreUserCountImportExcel(@RequestParam("inputfile") MultipartFile inputfile){
        ResponseResult<BackStoreCustomerCountVO> result = new ResponseResult<BackStoreCustomerCountVO>();
		BackStoreCustomerCountVO bscc = new BackStoreCustomerCountVO();
		result.setData(bscc);
        if (!inputfile.getOriginalFilename().endsWith(".xls")
                && !inputfile.getOriginalFilename().endsWith(".xlsx")){
            result.setMessage("文件扩展名错误！");
            result.setCode(BusinessCode.CODE_1001);
            return result;
        }
        List<CouponActivityImportStoreVO> list = null;
		ImportResult<CouponActivityImportStoreVO> importResult = null;
		try {
			importResult = ExcelUtils.importExcelVerify(inputfile.getInputStream(), CouponActivityImportStoreVO.class);
		} catch (Exception e) {
			throw new BusinessException(BusinessCode.CODE_1001, e);
		}
		List<ExcelVerifyResult> invalidList = importResult.getInvalidList(1);
        if (invalidList.isEmpty()) {
            list = importResult.getList();
        }
        if(CollectionUtils.isEmpty(list) || null == list.get(0).getStoreId()){
            result.setCode(BusinessCode.CODE_1007);
            result.setMessage("导入数据为空");
            return result;
        }
		ResponseResult<List<StoreUserInfoVO>> resultStore = couponActivityServiceClient.couponActivityStoreImportExcel(list);
		if(CollectionUtils.isEmpty(resultStore.getDataWithException())){
			result.setCode(BusinessCode.CODE_1007);
			result.setMessage("导入的小店ID不存在");
			return result;
		}
		StoreCustomerRegionCondition scrc = new StoreCustomerRegionCondition();
		List<String> storeIdList = list.stream().map(caiv -> caiv.getStoreId()).distinct().collect(Collectors.toList());
		scrc.setStoreUserInfoIds(storeIdList);
		ResponseResult<List<Long>> storeCustomerRegions = storeServiceClient.findStoreCustomerRegions(scrc);
		bscc.setStoreCustomerNum(storeCustomerRegions.getData() == null ?0:(long)storeCustomerRegions.getData().size());
		bscc.setStoresInfos(resultStore.getData());

		return result;
    }



	@ApiOperation("优惠券活动导入用戶信息")
	@PostMapping(value = "/couponActivityCustomerImportExcel")
	public ResponseResult<List<CustomerUserInfoVO>> couponActivityUserImportExcel(@RequestParam("inputfile") MultipartFile inputfile){
		ResponseResult<List<CustomerUserInfoVO>> result = new ResponseResult<List<CustomerUserInfoVO>>();
		if (!inputfile.getOriginalFilename().endsWith(".xls")
				&& !inputfile.getOriginalFilename().endsWith(".xlsx")){
			result.setMessage("文件扩展名错误！");
			result.setCode(BusinessCode.CODE_1001);
			return result;
		}
		List<CouponActivityImportCustomerVO> list = null;
		ImportResult<CouponActivityImportCustomerVO> importResult = null;
		try {
			importResult = ExcelUtils.importExcelVerify(inputfile.getInputStream(), CouponActivityImportCustomerVO.class);
		} catch (Exception e) {
			throw new BusinessException(BusinessCode.CODE_1001, e);
		}
		List<ExcelVerifyResult> invalidList = importResult.getInvalidList(1);
		if (invalidList.isEmpty()) {
			list = importResult.getList();
		}
        if (CollectionUtils.isEmpty(list)  || null == list.get(0).getPhone()) {
			result.setCode(BusinessCode.CODE_1007);
			result.setMessage("导入数据为空");
			return result;
		}
		return couponActivityServiceClient.couponActivityCustomerUserImportExcel(list);
	}

    @ApiOperation("优惠券活动导出小店信息")
    @GetMapping(value = "/couponActivityExportStoreExcel")
    public ResponseEntity<byte[]> couponActivityExportStoreExcel(CouponActivityCondition condition){
        ResponseResult<PagedList<CouponActivityStoreVO>> responseResult = couponActivityServiceClient.queryStoreByActivity(condition);
        List<CouponActivityStoreVO> list = responseResult.getData().getData();
        return ExcelUtils.exp(list, "惠小店信息");
    }

    @ApiOperation("优惠券活动导出指定用户信息")
    @GetMapping(value = "/couponActivityExportCustomerExcel")
    public ResponseEntity<byte[]> couponActivityExportCustomerExcel(CouponActivityCondition condition){
        ResponseResult<List<CustomerUserInfoExportVO>> listResponseResult = couponActivityServiceClient.queryCustomerByActivity(condition);
        List<CustomerUserInfoExportVO> list = listResponseResult.getData();
        return ExcelUtils.exp(list, "活动指定用户信息");
    }


	/**
	 *
	 *@Deccription 添加优惠券活动（领券）
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/7
	 */
	@ApiOperation("添加优惠券活动-领券")
	@PostMapping(value = "/addCouponActivityPull")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PULL_ADD)
	public ResponseResult<Integer> addCouponActivityPull(@RequestBody CouponActivityAddCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		Long userId = userInfo.getId();
		String userName = userInfo.getUsername();
		String code = "LQ_"+this.getTimeStr()+"_"+(int)((Math.random()*9+1)*10000);
		condition.setCode(code);
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
		return couponActivityServiceClient.addCouponActivity(condition);
	}
	/**
	 *
	 *@Deccription 添加优惠券活动（推券）
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/7
	 */
	@ApiOperation("添加优惠券活动-推券")
	@PostMapping(value = "/addCouponActivityPush")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PUSH_ADD)
	public ResponseResult<Integer> addCouponActivityPush(@RequestBody CouponActivityAddCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		Long userId = userInfo.getId();
		String userName = userInfo.getUsername();
		String code = "TQ_"+this.getTimeStr()+"_"+(int)((Math.random()*9+1)*10000);
		condition.setCode(code);
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
		return couponActivityServiceClient.addCouponActivity(condition);
	}

	/**
	 *
	 *@Deccription 根据id 查询出对应的实体类(查看和回显编辑页)（领券）
	 *@Params   id
	 *@Return   ResponseResult
	 *@User     sjx
	 *@Date   2018/8/8
	 */
	@ApiOperation("根据id 查询优惠券活动")
	@PostMapping(value = "/getCouponActivityByIdPull")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PULL_VIEW)
	public ResponseResult<CouponActivityVO> getCouponActivityByIdPull(@RequestParam("id") String id){
		return couponActivityServiceClient.getCouponActivityById(id);
	}
	/**
	 *
	 *@Deccription 根据id 查询出对应的实体类(查看和回显编辑页)（推券）
	 *@Params   id
	 *@Return   ResponseResult
	 *@User     sjx
	 *@Date   2018/8/8
	 */
	@ApiOperation("根据id 查询优惠券活动")
	@PostMapping(value = "/getCouponActivityByIdPush")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PUSH_VIEW)
	public ResponseResult<CouponActivityVO> getCouponActivityByIdPush(@RequestParam("id") String id){
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
	@PostMapping(value = "/updateCouponActivity")
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
	@PostMapping(value = "/deleteCouponActivity")
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
	 *@Deccription 撤回活动优惠券（领券）
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/9
	 */
	@ApiOperation("撤回活动优惠券")
	@PostMapping(value = "/revocationActivityCouponPull")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PULL_REVOCATION)
	public ResponseResult<Integer> revocationActivityCouponPull(@RequestBody CouponActivityCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		Long userId = userInfo.getId();
		String userName = userInfo.getUsername();
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
		return couponActivityServiceClient.revocationActivityCoupon(condition);
	}
	/**
	 *
	 *@Deccription 撤回活动优惠券（推券）
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/9
	 */
	@ApiOperation("撤回活动优惠券")
	@PostMapping(value = "/revocationActivityCouponPush")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PUSH_REVOCATION)
	public ResponseResult<Integer> revocationActivityCouponPush(@RequestBody CouponActivityCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		Long userId = userInfo.getId();
		String userName = userInfo.getUsername();
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
		return couponActivityServiceClient.revocationActivityCoupon(condition);
	}
	/**
	 *
	 *@Deccription 停用/开启活动（领券）
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/9
	 */
	@ApiOperation("停用/开启活动")
	@PostMapping(value = "/updateCouponActivityStatusPull")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PULL_UPDATE_STATUS)
	public ResponseResult<Integer> updateCouponActivityStatusPull(@RequestBody CouponActivityAddCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		Long userId = userInfo.getId();
		String userName = userInfo.getUsername();
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
		return couponActivityServiceClient.updateCouponActivityStatus(condition);
	}
	/**
	 *
	 *@Deccription 停用/开启活动（推券）
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/9
	 */
	@ApiOperation("停用/开启活动")
	@PostMapping(value = "/updateCouponActivityStatusPush")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PUSH_UPDATE_STATUS)
	public ResponseResult<Integer> updateCouponActivityStatusPush(@RequestBody CouponActivityAddCondition condition){
		UserInfo userInfo = UserManager.getCurrentUser();
		Long userId = userInfo.getId();
		String userName = userInfo.getUsername();
		condition.setCreatedBy(userId);
		condition.setCreatedByName(userName);
		return couponActivityServiceClient.updateCouponActivityStatus(condition);
	}
	/**
	 *
	 *@Deccription 根据活动获取优惠券列表（领券）
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/9
	 */
	@ApiOperation("根据活动获取优惠券列表")
	@PostMapping(value = "/queryCouponByActivityPull")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PULL_TEMPLATE_VIEW)
	public ResponseResult<PagedList<CouponActivityStoreVO>> queryCouponByActivityPull(@RequestBody CouponActivityCondition condition){
		return couponActivityServiceClient.queryCouponByActivity(condition);
	}
	/**
	 *
	 *@Deccription 根据活动获取优惠券列表（推券）
	 *@Params  condition
	 *@Return  ResponseResult
	 *@User  sjx
	 *@Date   2018/8/9
	 */
	@ApiOperation("根据活动获取优惠券列表")
	@PostMapping(value = "/queryCouponByActivityPush")
	@CheckPermission(PermissionEnum.PROMOTION_ACTIVITY_PUSH_TEMPLATE_VIEW)
	public ResponseResult<PagedList<CouponActivityStoreVO>> queryCouponByActivityPush(@RequestBody CouponActivityCondition condition){
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
	@PostMapping(value = "/queryStoreByActivity")
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
	@PostMapping(value = "/getCouponTemplatePage")
	@CheckPermission(PermissionEnum.PROMOTION_TEMPLETE_MANAGEMENT_LIST)
	public ResponseResult<PagedList<CouponTemplateVO>> getCouponTemplatePage(@RequestBody CouponTemplateCondition condition){
		return couponTemplateServiceClient.findCouponTemplatePageByCondition(condition);
	}

	@ApiOperation("优惠券模板列表导出")
	@PostMapping(value = "/couponTemplateListExport")
	@CheckPermission(PermissionEnum.PROMOTION_TEMPLETE_MANAGEMENT_LIST)
	public ResponseEntity<byte[]> couponTemplateListExport(@RequestBody CouponTemplateCondition condition) {
		condition.setIsQueryAll(true);
		ResponseResult<PagedList<CouponTemplateVO>> responseResult = couponTemplateServiceClient.findCouponTemplatePageByCondition(condition);
		if (responseResult != null && responseResult.getCode() == 0) {
			List<CouponTemplateVO> list = responseResult.getData().getData();
			return ExcelUtils.exp(list, "优惠券模板列表");
		}
		return null;
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
	@PostMapping(value = "/addCouponTemplate")
	@CheckPermission(PermissionEnum.PROMOTION_TEMPLETE_MANAGEMENT_ADD)
	public ResponseResult<Integer> addCouponTemplate(@RequestBody CouponTemplateCondition condition){
		/**
		 * 参数校验还未完善
		 */
		if(condition==null || StringUtils.isBlank(condition.getTitle()) || condition.getInvestorId()==null
		   || condition.getGradeId()==null || condition.getApplyRuleId()==null
		   || condition.getPayType()==null 	){
           throw new BusinessException(BusinessCode.CODE_500010,"优惠券模板必填参数错误");
		}

		if(condition.getTitle().length()>50){
			throw new BusinessException(BusinessCode.CODE_500016,"优惠券标题超过最大长度");
		}
		if(StringUtils.isNotBlank(condition.getExolian())){
            if(condition.getExolian().length()>50){
				throw new BusinessException(BusinessCode.CODE_500016,"优惠券说明超过最大长度");
			}
		}
		if(StringUtils.isNotBlank(condition.getRemarks())){
			if(condition.getRemarks().length()>200){
				throw new BusinessException(BusinessCode.CODE_500016,"优惠券详细说明说明超过最大长度");
			}
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
	@GetMapping(value = "/viewCouponTemplateDetail")
	@CheckPermission(PermissionEnum.PROMOTION_TEMPLETE_MANAGEMENT_LIST_VIEW)
	public ResponseResult<CouponTemplateVO> viewCouponTemplateDetail(@RequestParam("id") String id){
		ResponseResult<CouponTemplateVO> responseResult = couponTemplateServiceClient.viewCouponTemplateDetail(id);
		return responseResult;
	}



	/**
	 *
	 *@Deccription  设为无效(暂时未使用)
	 *@Params  id
	 *@Return  ResponseResult 删除是否成功
	 *@User  wl
	 *@Date   2018/8/6 20:39
	 */
	@ApiOperation("优惠券模板设为无效")
	@PostMapping(value = "/updateCouponTemplateToValid")
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
	@PostMapping(value = "/getCouponInvestorPage")
	@CheckPermission(PermissionEnum.PROMOTION_INVESTOR_RULE_LIST)
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
	@PostMapping(value = "/addCouponInvestor")
	@CheckPermission(PermissionEnum.PROMOTION_INVESTOR_RULE_ADD)
	public ResponseResult<Integer> addCouponInvestor(@RequestBody LinkedHashMap detailData){
		/**
		 *  校验参数
		 */
		if(detailData==null){
			throw new BusinessException(BusinessCode.CODE_500010,"参数为空");
		}
		 CouponInvestorCondition condition = new CouponInvestorCondition();
         if(detailData.get("name")==null || "".equals(detailData.get("name"))|| detailData.get("listDetail")==null || "".equals(detailData.get("listDetail"))){
			 throw new BusinessException(BusinessCode.CODE_500010,"新建出资方必填参数错误");
		 }
		 String name = detailData.get("name").toString();
         if(name.length()>50){
			 throw new BusinessException(BusinessCode.CODE_500016,"出资方名称超过最大长度");
		 }

         if(detailData.get("remark")!=null){
			 condition.setRemarks(detailData.get("remark").toString());
			 if(detailData.get("remark").toString().length()>200){
				 throw new BusinessException(BusinessCode.CODE_500016,"出资方备注超过最大长度");
			 }
		 }
		ArrayList list  = (ArrayList)detailData.get("listDetail");
		UserInfo userInfo = UserManager.getCurrentUser();
		String userId = userInfo.getId()+"";
		String userName = userInfo.getUsername();
		String code = "WS_CZF"+this.getTimeStr()+"_"+(int)((Math.random()*9+1)*10000);
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
	@GetMapping(value = "/viewCouponInvestorDetail")
	@CheckPermission(PermissionEnum.PROMOTION_INVESTOR_RULE_LIST_VIEW)
	public ResponseResult<CouponInvestorVO> viewCouponInvestorDetail(@RequestParam("id") String id){
		ResponseResult<CouponInvestorVO> responseResult = couponInvestorServiceClient.viewCouponInvestorDetail(id);
		return responseResult;
	}


	/**
	 *
	 *@Deccription   (暂时未使用)
	 *@Params
	 *@Return
	 *@User  wl
	 *@Date   2018/8/22 10:23
	 */
	@ApiOperation("出资方规则设置无效")
	@PostMapping(value = "/updateCouponInvestorToValid")
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
@PostMapping(value = "/getCouponGradePage")
@CheckPermission(PermissionEnum.PROMOTION_GRADE_RULE_LIST)
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
@PostMapping(value = "/addCouponGrade")
@CheckPermission(PermissionEnum.PROMOTION_GRADE_RULE_ADD)
public ResponseResult<Integer> addCouponGrade(@RequestBody CouponGradeCondition couponGradeCondition){
	/**
	 * 参数校验
	 */
	if(couponGradeCondition==null){
		throw new BusinessException(BusinessCode.CODE_500010,"必填参数为空");
	}
	if(StringUtils.isBlank(couponGradeCondition.getName())|| couponGradeCondition.getType()==null ||
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

	if(couponGradeCondition.getReducedAmt()!=null && couponGradeCondition.getDiscountedAmt()!= null){
		if(!BigDecimal.valueOf(0).equals(couponGradeCondition.getReducedAmt())){
			if(couponGradeCondition.getDiscountedAmt().doubleValue()>couponGradeCondition.getReducedAmt().doubleValue()){
				throw new BusinessException(BusinessCode.CODE_500015,"优惠金额不能大于满减金额");
			}
		}
	}

	if(couponGradeCondition.getName().length()>50){
		throw new BusinessException(BusinessCode.CODE_500016,"坎级规则名称超过最大长度");
	}
	if(StringUtils.isNotBlank(couponGradeCondition.getRemarks())){
		if(couponGradeCondition.getRemarks().length()>200){
			throw new BusinessException(BusinessCode.CODE_500016,"坎级规则备注超过最大长度");
		}
	}

	 UserInfo userInfo = UserManager.getCurrentUser();
	 String userId = userInfo.getId()+"";
	 String userName = userInfo.getUsername();
	 String code = "WS_KJGZ"+this.getTimeStr()+"_"+(int)((Math.random()*9+1)*10000);
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
@GetMapping(value = "/viewCouponGradeDetail")
@CheckPermission(PermissionEnum.PROMOTION_GRADE_RULE_LIST_VIEW)
public ResponseResult<CouponGradeVO> viewCouponGradeDetail(@RequestParam("id") String id){
	ResponseResult<CouponGradeVO> responseResult = couponGradeServiceClient.viewCouponGradeDetail(id);
	return responseResult;
}

/**
 *
 *@Deccription 坎级逻辑删除/设置为无效 (按时未使用)
 *@Params  id  坎级id
 *@Return  是否成功 0 表示成功
 *@User  wl
 *@Date   2018/8/11 12:21
 */
@ApiOperation("坎级设置为无效")
@PostMapping (value = "/updateCouponGradeValid")
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
	@PostMapping(value = "/addCouponApply")
	@CheckPermission(PermissionEnum.PROMOTION_APPLY_RULE_ADD)
	public ResponseResult<Integer> addCouponApply(@RequestBody CouponApplyCondition  condition){
		/**
		 * 参数校验
		 */
		if(condition==null || StringUtils.isBlank(condition.getName()) || condition.getApplyRuleType()==null){
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
		if(condition.getName().length()>50){
			throw new BusinessException(BusinessCode.CODE_500016,"适用对象规则名称超过最大长度");
		}
		if(StringUtils.isNotBlank(condition.getRemarks())){
			if(condition.getRemarks().length()>200){
				throw new BusinessException(BusinessCode.CODE_500016,"适用对象规则备注超过最大长度");
			}
		}
		String code  = "WS_SYDX"+this.getTimeStr()+"_"+(int)((Math.random()*9+1)*10000);
		condition.setCode(code);
		UserInfo userInfo = UserManager.getCurrentUser();
		String userId = userInfo.getId()+"";
		String userName = userInfo.getUsername();
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
	@PostMapping(value = "/viewCouponApplyDetail")
	@CheckPermission(PermissionEnum.PROMOTION_APPLY_RULE_LIST_VIEW)
	public ResponseResult<CouponApplyVO> viewCouponApplyDetail(@RequestParam("id") String id,@RequestParam("type") Short type){
		ResponseResult<CouponApplyVO> responseResult = couponApplyServiceClient.viewCouponApplyDetail(id,type);
		return responseResult;
	}

	/**
	 *
	 *@Deccription 适用对象设置无效(暂时未使用)
	 *@Params  id
	 *@Return  是否成功 0 表示成功
	 *@User  wl
	 *@Date   2018/8/11 12:16
	 */
	@ApiOperation("适用对象设置无效")
	@PostMapping(value = "/updateCouponApplyToValid")
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
	@PostMapping(value = "/findCouponApplyPage")
	@CheckPermission(PermissionEnum.PROMOTION_APPLY_RULE_LIST)
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
@PostMapping(value = "/findInvertorTempleteCountPage")
@CheckPermission(PermissionEnum.PROMOTION_INVESTOR_RULE_LIST_REL)
public ResponseResult<PagedList<InvertorTempleteCountVO>> findInvertorTempleteCountPage(@RequestBody RuleRealationCountCondition condition){
	ResponseResult<PagedList<InvertorTempleteCountVO>> responseResult = couponInvestorServiceClient.findInvertorTempleteCountPage(condition);
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
@PostMapping(value = "/findGradeTempleteCountPage")
@CheckPermission(PermissionEnum.PROMOTION_GRADE_RULE_LIST_REL)
public ResponseResult<PagedList<GradeTempleteCountVO>> findGradeTempleteCountPage(@RequestBody RuleRealationCountCondition condition){
	ResponseResult<PagedList<GradeTempleteCountVO>> responseResult = couponGradeServiceClient.findGradeTempleteCountPage(condition);
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
@PostMapping(value = "/findApplyTempleteCountPage")
@CheckPermission(PermissionEnum.PROMOTION_APPLY_RULE_LIST_REL)
public ResponseResult<PagedList<ApplyTempleteCountVO>> findApplyTempleteCountPage(@RequestBody RuleRealationCountCondition condition){
	ResponseResult<PagedList<ApplyTempleteCountVO>> responseResult = couponApplyServiceClient.findApplyTempleteCountPage(condition);
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


	public String getTimeStr(){
		Calendar cal = Calendar.getInstance();
		String timw = cal.get(Calendar.YEAR)+""
				+(cal.get(Calendar.MONTH)+1)+""
				+cal.get(Calendar.DAY_OF_MONTH)+""
				+cal.get(Calendar.HOUR_OF_DAY)+""
				+cal.get(Calendar.MINUTE)+""
				+cal.get(Calendar.SECOND)+"";
        return timw;
	}

}
