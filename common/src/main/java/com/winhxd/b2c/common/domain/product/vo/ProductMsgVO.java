package com.winhxd.b2c.common.domain.product.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品信息VO
 * @ClassName:  ProductMsgVO   
 * @Description:TODO   
 * @author: zhuchongchen 
 * @date:   2018年8月4日 下午1:24:27   
 *
 */
public class ProductMsgVO {
	
	@ApiModelProperty(value = "分类")
	private List<CategoryVO> categorys;

	@ApiModelProperty(value = "品牌")
	private List<BrandVO> brands;
	
	@ApiModelProperty(value = "商品")
	private List<ProductVO> products;
	
	@ApiModelProperty(value = "商品sku")
	private List<ProductSkuVO> productSkus;

	private Integer pageSize;
	
	private Integer pageNo;
	
	public List<BrandVO> getBrands() {
		return brands;
	}

	public void setBrands(List<BrandVO> brands) {
		this.brands = brands;
	}

	public List<CategoryVO> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<CategoryVO> categorys) {
		this.categorys = categorys;
	}

	public List<ProductVO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductVO> products) {
		this.products = products;
	}

	public List<ProductSkuVO> getProductSkus() {
		return productSkus;
	}

	public void setProductSkus(List<ProductSkuVO> productSkus) {
		this.productSkus = productSkus;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	
}
