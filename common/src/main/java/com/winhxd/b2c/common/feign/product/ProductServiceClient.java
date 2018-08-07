package com.winhxd.b2c.common.feign.product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.condition.ProductConditionByPage;
import com.winhxd.b2c.common.domain.product.vo.ProductMsgVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuMsgVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.product.vo.ProductVO;

import feign.hystrix.FallbackFactory;

@FeignClient(value = ServiceName.PRODUCT_SERVICE, fallbackFactory = ProductServiceFallback.class)
public interface ProductServiceClient {

	 @RequestMapping(value = "/product/v1/getProductMsg/", method = RequestMethod.POST)
	 ResponseResult<ProductMsgVO> getProductMsg(@RequestBody ProductConditionByPage condition);
	 
	 @RequestMapping(value = "/product/v1/getProductSkuMsg/", method = RequestMethod.POST)
	 ResponseResult<ProductSkuMsgVO> getProductSkuMsg(ProductConditionByPage condition);
	 
	 @RequestMapping(value = "/product/v1/getProductsByPage/", method = RequestMethod.POST)
	 ResponseResult<PagedList<ProductVO>> getProductsByPage(@RequestBody ProductConditionByPage condition);
	 
	 @RequestMapping(value = "/product/v1/getProductSkusByPage/", method = RequestMethod.POST)
	 ResponseResult<PagedList<ProductSkuVO>> getProductSkusByPage(@RequestBody ProductConditionByPage condition);
	 
	 @RequestMapping(value = "/product/v1/getProducts/", method = RequestMethod.POST)
	 ResponseResult<List<ProductVO>> getProducts(@RequestBody ProductCondition condition);
	 
	 @RequestMapping(value = "/product/v1/getProductSkus/", method = RequestMethod.POST)
	 ResponseResult<List<ProductSkuVO>> getProductSkus(@RequestBody ProductCondition condition);
	 
}

class ProductServiceFallback implements ProductServiceClient, FallbackFactory<ProductServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceFallback.class);
    private Throwable throwable;

    public ProductServiceFallback() {
    	
    }

    private ProductServiceFallback(Throwable throwable) {
        this.throwable = throwable;
    }

	@Override
	public ProductServiceClient create(Throwable arg0) {
		return new ProductServiceFallback(throwable);
	}

	@Override
	public ResponseResult<ProductMsgVO> getProductMsg(ProductConditionByPage condition) {
		logger.error("ProductServiceClient -> getProductMsg", throwable);
		return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<ProductSkuMsgVO> getProductSkuMsg(ProductConditionByPage condition) {
		logger.error("ProductServiceClient -> getProductMsg", throwable);
		return new ResponseResult<>(BusinessCode.CODE_1001);
	}
	
	@Override
	public ResponseResult<PagedList<ProductVO>> getProductsByPage(ProductConditionByPage condition) {
		logger.error("ProductServiceClient -> getProductsByPage", throwable);
		return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<PagedList<ProductSkuVO>> getProductSkusByPage(ProductConditionByPage condition) {
		logger.error("ProductServiceClient -> getProductSkusByPage", throwable);
		return new ResponseResult<>(BusinessCode.CODE_1001);
	}
	
	@Override
	public ResponseResult<List<ProductVO>> getProducts(ProductCondition condition) {
		logger.error("ProductServiceClient -> getProducts", throwable);
		return new ResponseResult<>(BusinessCode.CODE_1001);
	}
	
	@Override
	public ResponseResult<List<ProductSkuVO>> getProductSkus(ProductCondition condition) {
		logger.error("ProductServiceClient -> getProductSkus", throwable);
		return new ResponseResult<>(BusinessCode.CODE_1001);
	}

}
