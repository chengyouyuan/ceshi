package com.winhxd.b2c.store.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.winhxd.b2c.common.domain.store.condition.StorePutawayProdSearchCondition;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.condition.MobileCondition;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.condition.ProductConditionByPage;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.ProductMsgVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.product.vo.ProductVO;
import com.winhxd.b2c.common.domain.store.condition.AllowPutawayProdCondition;
import com.winhxd.b2c.common.domain.store.condition.ProdOperateCondition;
import com.winhxd.b2c.common.domain.store.condition.ProdOperateInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreSubmitProductCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreProdOperateEnum;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.enums.StoreSubmitProductStatusEnum;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.model.StoreSubmitProduct;
import com.winhxd.b2c.common.domain.store.vo.LoginCheckSellMoneyVO;
import com.winhxd.b2c.common.domain.store.vo.ProductImageVO;
import com.winhxd.b2c.common.domain.store.vo.StoreProdSimpleVO;
import com.winhxd.b2c.common.domain.store.vo.StoreSubmitProductVO;
import com.winhxd.b2c.common.feign.hxd.StoreHxdServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.store.service.StoreProductManageService;
import com.winhxd.b2c.store.service.StoreSubmitProductService;
import com.winhxd.b2c.store.util.ImageUploadUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 *
 * @ClassName: ApiStoreProductManageController
 * @Description: 惠小店门店商品相关接口
 * @author: wuyuanbao
 * @date: 2018年8月4日 下午2:58:37
 */
@Api(value = "api storeProductManage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "api-store/storeProductManage/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiStoreProductManageController {
	private static final Logger logger = LoggerFactory.getLogger(ApiStoreProductManageController.class);
	@Autowired
	private StoreProductManageService storeProductManageService;
	@Autowired
	private ProductServiceClient productServiceClient;
	@Autowired
	private StoreHxdServiceClient storeHxdServiceClient;
	@Autowired
	private StoreSubmitProductService storeSubmitProductService;
	@Autowired
	private Cache cache;
	@Autowired
	private ImageUploadUtil imageUploadUtil;
	/**
	 * 惠下单商品
	 */
	private static final Byte HXD_PROD_TYPE = 0;
	/**
	 * 普通商品
	 */
	private static final Byte COMMON_PROD_TYPE = 1;

	@ApiOperation(value = "B端获取可上架商品数据接口", notes = "B端获取可上架商品数据接口")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
			@ApiResponse(code = BusinessCode.CODE_1006, message = "账号未启用！"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效！") })
	@PostMapping(value = "1012/v1/allowPutawayProdList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<ProductMsgVO> allowPutawayProdList(@RequestBody AllowPutawayProdCondition condition) {
		ResponseResult<ProductMsgVO> responseResult = new ResponseResult<>();

		logger.info("B端获取可上架商品数据接口入参为：{}", condition);
		// 参数校验
		if (!checkParam(condition)) {
			responseResult.setCode(BusinessCode.CODE_1007);
			responseResult.setMessage("参数无效！");
			return responseResult;
		}
		// 账号信息校验
		StoreUser storeUser = UserContext.getCurrentStoreUser();
		if (storeUser == null) {
			responseResult.setCode(BusinessCode.CODE_1002);
			responseResult.setMessage("登录凭证无效");
			return responseResult;
		}

		// 判断查询可上架商品类型
		Byte prodType = condition.getProdType();
		// 门店编码
		Long storeId = storeUser.getBusinessId();
		// Long storeId = condition.getStoreId();
		// 已上架的商品sku
		List<String> putawayProdSkus = null;

		// 查询商品信息
		ProductConditionByPage prodConditionByPage = new ProductConditionByPage();
		prodConditionByPage.setPageNo(condition.getPageNo());
		prodConditionByPage.setPageSize(condition.getPageSize());
		prodConditionByPage.setSearchSkuCode(SearchSkuCodeEnum.NOT_IN_SKU_CODE);

		// 惠下单商品
		if (HXD_PROD_TYPE.equals(prodType)) {
			// 在惠下单下过单的sku
			List<String> hxdBuyedProdSkuList = null;
			Long storeCustomerId = storeUser.getStoreCustomerId();
			// 调用接口请求该用户惠下单商品sku
			hxdBuyedProdSkuList = getStoreBuyedHxdProdSkuCodes(storeCustomerId);

			if (hxdBuyedProdSkuList != null && hxdBuyedProdSkuList.size() > 0) {
				// 设置惠下单下过单的商品
				prodConditionByPage.setHxdProductSkus(hxdBuyedProdSkuList);
			} else {
				// 没有下过单
				responseResult.setCode(BusinessCode.CODE_1006);
				responseResult.setMessage("账号未启用！");
				return responseResult;
			}
		}
		// 查询该门店上架sku集合
		StoreProductManageCondition spmCondition = new StoreProductManageCondition();
		spmCondition.setStoreId(storeId);
		// 设置状态
		List<Short> status = new ArrayList<>();
		status.add(StoreProductStatusEnum.PUTAWAY.getStatusCode());
		status.add(StoreProductStatusEnum.UNPUTAWAY.getStatusCode());

		spmCondition.setProdStatus(status);

		putawayProdSkus = storeProductManageService.findSkusByConditon(spmCondition);
		// 放入已上架skus和已下架的
		prodConditionByPage.setProductSkus(putawayProdSkus);

		// 首次请求，会返回类型，品牌等信息
		if (condition.getFirst()) {// ProductBaseMsgVO

			responseResult = productServiceClient.getProductMsg(prodConditionByPage);
		} else {
			// 需要带一级分类编码跟品牌编码（如果有品牌的话）

			// 一级分类
			String categoryCode = condition.getCategoryCode();
			if (StringUtils.isNotEmpty(categoryCode)) {

				prodConditionByPage.setCategoryCode(categoryCode);
			}
			// 品牌
			List<String> brandCodeList = condition.getBrandCode();

			if (CollectionUtils.isNotEmpty(brandCodeList)) {

				prodConditionByPage.setBrandCodes(brandCodeList);
			}
			ResponseResult<PagedList<ProductVO>> prodResult = productServiceClient
					.getProductsByPage(prodConditionByPage);
			if (prodResult != null && prodResult.getCode() == 0) {
				ProductMsgVO pmVO = new ProductMsgVO();
				pmVO.setProducts(prodResult.getData());
				responseResult.setData(pmVO);
			}
		}

		return responseResult;
	}

	@ApiOperation(value = "B端商品操作接口(上架(包括批量)，下架，删除，编辑)", notes = "B端商品操作接口")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效！"),
			@ApiResponse(code = BusinessCode.CODE_200012, message = "部分skuCode无效！"),
			@ApiResponse(code = BusinessCode.CODE_200013, message = "部分商品已上架！") })
	@PostMapping(value = "1013/v1/storeProdOperate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<Void> storeProdOperate(@RequestBody ProdOperateCondition condition) {
		ResponseResult<Void> responseResult = new ResponseResult<>();
		// 获取当前门店用户
		StoreUser storeUser = UserContext.getCurrentStoreUser();
		if (storeUser == null || null == storeUser.getBusinessId()) {
			responseResult.setCode(BusinessCode.CODE_1002);
			responseResult.setMessage("登录凭证无效！");
			return responseResult;
		}
		logger.info("B端商品操作接口入参为：{}", condition);
		// 参数校验
		if (!checkParam(condition)) {
			responseResult.setCode(BusinessCode.CODE_1007);
			responseResult.setMessage("参数无效！");
			return responseResult;
		}
		// 操作类型
		Byte operateType = condition.getOperateType();
		// 门店id
		Long storeId = storeUser.getBusinessId();
		// skuCode集合
		List<ProdOperateInfoCondition> prodInfo = condition.getProducts();
		// skuCode数组
		List<String> skuCodes = new ArrayList<>();
		String[] skuCodeArray = new String[prodInfo.size()];
		for (int i = 0; i < prodInfo.size(); i++) {
			skuCodes.add(prodInfo.get(i).getSkuCode());
			skuCodeArray[i] = prodInfo.get(i).getSkuCode();
		}
		// 判断操作类型
		if (StoreProdOperateEnum.PUTAWAY.getOperateCode() == operateType) {

			// 查询过来的sku是否有的已经上架
			StoreProductManageCondition spmCondition = new StoreProductManageCondition();
			spmCondition.setStoreId(storeId);
			spmCondition.setProdStatus(Arrays.asList(StoreProductStatusEnum.PUTAWAY.getStatusCode()));
			List<StoreProductManage> spms = storeProductManageService.findPutawayProdBySkuCodes(storeId, skuCodeArray);
			// 上架操作
			// 表示还没上架过
			if (spms == null || spms.size() == 0) {
				// 调用商品接口获取商品相关信息
				ProductCondition prodCondition = new ProductCondition();
				prodCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
				prodCondition.setProductSkus(skuCodes);
				// 查询商品详情
				ResponseResult<List<ProductSkuVO>> productResult = productServiceClient.getProductSkus(prodCondition);
				if (productResult.getCode() == 0) {

					List<ProductSkuVO> productList = productResult.getData();
					// 查询信息一一对应
					if (productList != null && productList.size() == prodInfo.size()) {
						Map<String, ProdOperateInfoCondition> putawayInfo = new HashMap<>();
						for (ProdOperateInfoCondition p : prodInfo) {
							putawayInfo.put(p.getSkuCode(), p);
						}
						Map<String, ProductSkuVO> prodSkuInfo = new HashMap<>();
						for (ProductSkuVO prod : productList) {
							prodSkuInfo.put(prod.getSkuCode(), prod);
						}
						// 商品上架
						this.storeProductManageService.batchPutawayStoreProductManage(storeId, putawayInfo,
								prodSkuInfo);
					}
				} else {
					responseResult.setCode(BusinessCode.CODE_200012);
					responseResult.setMessage("部分skuCode无效！");
					return responseResult;
				}
			} else {
				// 表示商品中有部分已经上架过了
				responseResult.setCode(BusinessCode.CODE_200013);
				responseResult.setMessage("部分商品已上架");
				return responseResult;
			}

		} else if (StoreProdOperateEnum.UNPUTAWAY.getOperateCode() == operateType) {
			// 下架操作
			storeProductManageService.unPutawayStoreProductManage(storeId, skuCodeArray);
		} else if (StoreProdOperateEnum.DELETE.getOperateCode() == operateType) {
			// 删除操作
			storeProductManageService.removeStoreProductManage(storeId, skuCodeArray);
		} else if (StoreProdOperateEnum.EDIT.getOperateCode() == operateType) {
			// 编辑（不支持批量）
			storeProductManageService.modifyStoreProductManage(storeId, prodInfo.get(0));
		}
		return responseResult;
	}

	@ApiOperation(value = "B端添加门店提报商品接口", notes = "B端添加门店提报商品接口")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！") })
	@PostMapping(value = "1014/v1/saveStoreSubmitProduct", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<Void> saveStoreSubmitProduct(@RequestBody StoreSubmitProductCondition condition) {
		ResponseResult<Void> responseResult = new ResponseResult<>();

		logger.info("B端添加门店提报商品接口入参为：{}", condition);
		// 参数校验
		if (!checkParam(condition)) {
			responseResult.setCode(BusinessCode.CODE_1007);
			responseResult.setMessage("参数错误");
			return responseResult;
		}
		// 获取当前门店用户
		StoreUser storeUser = UserContext.getCurrentStoreUser();
		if (storeUser == null) {
			responseResult.setCode(BusinessCode.CODE_1002);
			responseResult.setMessage("登录凭证无效！");
			return responseResult;
		}
		Long storeId = storeUser.getBusinessId();
		StoreSubmitProduct storeSubmitProduct = new StoreSubmitProduct();
		BeanUtils.copyProperties(condition, storeSubmitProduct);
		storeSubmitProduct.setStoreId(storeId);
		storeSubmitProduct.setProdStatus(StoreSubmitProductStatusEnum.CREATE.getStatusCode());

		storeSubmitProductService.saveStoreSubmitProduct(storeId, storeSubmitProduct);

		return responseResult;
	}

	@ApiOperation(value = "B端门店提报商品列表接口", notes = "B端门店提报商品列表接口")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！") })
	@PostMapping(value = "1015/v1/findStoreSubmitProductList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<PagedList<StoreSubmitProductVO>> findStoreSubmitProductList(
			@RequestBody StoreSubmitProductCondition condition) {
		ResponseResult<PagedList<StoreSubmitProductVO>> responseResult = new ResponseResult<>();

		logger.info("B端门店提报商品列表接口入参为：{}", condition);
		// 参数校验
		if (!checkParam(condition)) {
			responseResult.setCode(BusinessCode.CODE_1007);
			responseResult.setMessage("参数错误");
			return responseResult;
		}
		// 获取当前门店用户
		StoreUser storeUser = UserContext.getCurrentStoreUser();
		if (storeUser == null) {
			responseResult.setCode(BusinessCode.CODE_1002);
			responseResult.setMessage("登录凭证无效！");
			return responseResult;
		}
		Long storeId = storeUser.getBusinessId();
		condition.setStoreId(storeId);

		PageHelper.startPage(condition.getPageNo(), condition.getPageSize());

		PagedList<StoreSubmitProductVO> pageList = storeSubmitProductService.findSimpelVOByCondition(condition);
		if (pageList == null) {
			pageList = new PagedList<>();
		}
		responseResult.setData(pageList);

		return responseResult;
	}

	@ApiOperation(value = "B端搜索商品接口", notes = "B端搜索商品接口")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数异常！") })
	@PostMapping(value = "1027/v1/searchProductByKey", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<PagedList<ProductVO>> searchProductByKey(@RequestBody StorePutawayProdSearchCondition condition) {
		ResponseResult<PagedList<ProductVO>> responseResult = new ResponseResult<>();
		StoreUser storeUser = UserContext.getCurrentStoreUser();
		if (null == storeUser || null == storeUser.getBusinessId() || null == storeUser.getStoreCustomerId()) {
			logger.error("B端搜索商品接口:登录凭证为空");
			return new ResponseResult<>(BusinessCode.CODE_1002);
		}
		if (!verifyParam(condition)) {
			return new ResponseResult<>(BusinessCode.CODE_1007);
		}
		Long storeCustomerId = storeUser.getStoreCustomerId();
		Long businessId = storeUser.getBusinessId();
		// 门店已经上架的商品
		StoreProductManageCondition manageCondition = new StoreProductManageCondition();
		manageCondition.setStoreId(businessId);
		List<Short> prodStatus = new ArrayList<>();
		prodStatus.add(StoreProductStatusEnum.PUTAWAY.getStatusCode());
		prodStatus.add(StoreProductStatusEnum.UNPUTAWAY.getStatusCode());
		manageCondition.setProdStatus(prodStatus);
		List<String> putwaySkusByConditon = storeProductManageService.findSkusByConditon(manageCondition);

		ProductConditionByPage productCondition = new ProductConditionByPage();
		productCondition.setProductName(condition.getProdName());
		productCondition.setProductSkus(putwaySkusByConditon);
		// hxd购买过的商品
		if (HXD_PROD_TYPE.equals(condition.getProdType())) {
			productCondition.setHxdProductSkus(getStoreBuyedHxdProdSkuCodes(storeCustomerId));
		}
		productCondition.setPageNo(condition.getPageNo());
		productCondition.setPageSize(condition.getPageSize());
		ResponseResult<PagedList<ProductVO>> productVo = productServiceClient.getProductsByPage(productCondition);
		responseResult.setData(productVo.getData());

		return responseResult;
	}

	private boolean verifyParam(StorePutawayProdSearchCondition condition) {
		boolean flag = true;
		if (StringUtils.isBlank(condition.getProdName())) {
			logger.error("B端搜索商品接口:参数ProdName为空");
			return false;
		}
		if (null == condition.getProdType()) {
			logger.error("B端搜索商品接口:参数ProdType为空");
			return false;
		}
		if (null == condition.getPageNo()) {
			logger.error("B端搜索商品接口:参数PageNo为空");
			return false;
		}
		if (null == condition.getPageSize()) {
			logger.error("B端搜索商品接口:参数PageSize为空");
			return false;
		}
		return flag;
	}

	@ApiOperation(value = "B端我的商品管理接口", notes = "B端我的商品管理接口")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！") })
	@PostMapping(value = "1028/v1/myStoreProdManage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<PagedList<StoreProdSimpleVO>> myStoreProdManage(
			@RequestBody StoreProductManageCondition condition) {
		ResponseResult<PagedList<StoreProdSimpleVO>> responseResult = new ResponseResult<>();

		logger.info("B端我的商品管理接口入参为：{}", condition);
		// 参数校验
		if (!checkParam(condition)) {
			responseResult.setCode(BusinessCode.CODE_1007);
			responseResult.setMessage("参数错误");
			return responseResult;
		}
		// 获取当前门店用户
		StoreUser storeUser = UserContext.getCurrentStoreUser();
		if (storeUser == null) {
			responseResult.setCode(BusinessCode.CODE_1002);
			responseResult.setMessage("登录凭证无效！");
			return responseResult;
		}
		Long storeId = storeUser.getBusinessId();
		condition.setStoreId(storeId);
		PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
		PagedList<StoreProdSimpleVO> list = storeProductManageService.findSimpelVOByCondition(condition);
		responseResult.setData(list);

		return responseResult;
	}
	
    /**
     *
     * @param
     * @return ResponseResult<LoginCheckSellMoneyVO>
     * @Title: loginCheckSellMoney
     * @Description: B端登入时校验改门店下上架商品未设置价格信息
     * @author wuyuanbao
     * @date 2018年8月6日下午1:40:49
     */
	@ApiOperation(value = "B端我的商品管理接口", notes = "B端我的商品管理接口")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！") })
	@PostMapping(value = "1018/v1/loginCheckSellMoney", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<LoginCheckSellMoneyVO> loginCheckSellMoney(@RequestBody MobileCondition condition){
		ResponseResult<LoginCheckSellMoneyVO> responseResult = new ResponseResult<>();
		LoginCheckSellMoneyVO vo=new LoginCheckSellMoneyVO();
		
		// 获取当前门店用户
		StoreUser storeUser = UserContext.getCurrentStoreUser();
		if (storeUser == null) {
				responseResult.setCode(BusinessCode.CODE_1002);
				responseResult.setMessage("登录凭证无效！");
				return responseResult;
		}
		Long storeId = storeUser.getBusinessId();
		
		vo.setStoreId(storeId);
		//查询上架未设置价格商品
		StoreProductManageCondition spmcondition=new StoreProductManageCondition();
		spmcondition.setStoreId(storeId);
		//未设置价格
		spmcondition.setPriceStatus((byte)0);
		//上架商品
		spmcondition.setProdStatus(Arrays.asList(StoreProductStatusEnum.PUTAWAY.getStatusCode()));
		int count=storeProductManageService.countSkusByConditon(spmcondition);
		//设置是否有未设置价格的商品
		if(count>0){
			vo.setCheckResult(true);	
		}else{
			vo.setCheckResult(false);
		}
		//设置为设置价格商品的数量
		vo.setNoSetPriceCount(count);
		
		responseResult.setData(vo);
		
		return responseResult;
	}
	
	@ApiOperation(value = "提报商品图片上传接口", notes = "提报商品图片上传接口")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
		@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！"),
		@ApiResponse(code = BusinessCode.CODE_1007, message = "参数异常！"),
		@ApiResponse(code = BusinessCode.CODE_200014, message = "图片格式不正确！")})
	@PostMapping(value = "1040/v1/uploadSubmitProductImg", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<List<ProductImageVO>> uploadSubmitProductImg(
			@RequestParam("imageFiles") MultipartFile imageFiles, HttpServletRequest request) throws IOException {
		ResponseResult<List<ProductImageVO>> responseResult = new ResponseResult<>();
		// 获取当前门店用户
		StoreUser storeUser = UserContext.getCurrentStoreUser();
		if (storeUser == null) {
			responseResult.setCode(BusinessCode.CODE_1002);
			responseResult.setMessage("登录凭证无效！");
			return responseResult;
		}
		logger.info("提报商品图片上传接口：imageFiles:" + imageFiles);
		if (imageFiles == null) {
			responseResult.setCode(BusinessCode.CODE_1007);
			responseResult.setMessage("参数错误");
			return responseResult;
		}
		String ip = request.getServerName();
		int port = request.getServerPort();
		Map<String, byte[]> mapFile = new HashMap<>();
		mapFile.put(imageFiles.getOriginalFilename(), imageFiles.getBytes());

		responseResult = imageUploadUtil.uploadImage(ip, port, mapFile);
		return responseResult;
	}

	/**
	 * 功能描述: 获取门店购买过商品sku list
	 * 
	 * @param storeCustomerId
	 * @return
	 * @auther lvsen
	 * @date 2018/8/9 11:22
	 */
	private List<String> getStoreBuyedHxdProdSkuCodes(Long storeCustomerId) {
		if (null == storeCustomerId) {
			return null;
		}
		String hxdSkuKey = CacheName.STORE_BUYED_HXDPROD_SKU + storeCustomerId;
		List<String> skuData = new ArrayList<>();
		if (StringUtils.isNotBlank(cache.get(hxdSkuKey))) {
			skuData = JsonUtil.parseJSONObject(cache.get(hxdSkuKey), List.class);
			return skuData;
		}
		Map<String, Object> request = new HashMap<>();
		request.put("customerId", storeCustomerId);
		ResponseResult<List<Map<String, Object>>> storeBuyedProdSku = storeHxdServiceClient
				.getStoreBuyedProdSku(request);
		List<Map<String, Object>> skuDataMap = storeBuyedProdSku.getData();
		for (Map<String, Object> skuMap : skuDataMap) {
			skuData.add(String.valueOf(skuMap.get("prodSku")));
		}
		cache.set(hxdSkuKey, JsonUtil.toJSONString(skuData));
		cache.expire(hxdSkuKey, 60 * 60 * 5);
		return skuData;
	}

	private boolean checkParam(Object condition) {
		boolean flag = false;
		if (condition != null) {
			// AllowPutawayProdCondition 必须传参数校验
			if (condition instanceof AllowPutawayProdCondition) {
				AllowPutawayProdCondition c = (AllowPutawayProdCondition) condition;
				if (c.getProdType() != null) {
					flag = true;
				}
			}
			// ProdOperateCondition 必须传参数校验
			if (condition instanceof ProdOperateCondition) {
				ProdOperateCondition c = (ProdOperateCondition) condition;
				if (c.getOperateType() != null && c.getProducts() != null && c.getProducts().size() > 0) {
					flag = true;
				}
			}
			// StoreSubmitProductCondition 必须传参数校验
			if (condition instanceof StoreSubmitProductCondition) {
				StoreSubmitProductCondition c = (StoreSubmitProductCondition) condition;
				flag = true;
			}
			// StoreProductManageCondition 必须传参数校验
			if (condition instanceof StoreProductManageCondition) {
				StoreProductManageCondition c = (StoreProductManageCondition) condition;
				if (c.getProdStatus() != null && c.getProdStatus().size() > 0) {
					flag = true;
				}
			}
		}
		return flag;
	}

}
