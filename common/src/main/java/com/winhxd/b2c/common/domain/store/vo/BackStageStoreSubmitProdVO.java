package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后台提报商品返回VO
 * 
 * @ClassName: BackStageStoreSubmitProdVO
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月14日 下午3:33:00
 */
@Data
@ApiModel("后台-门店商品管理返参")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BackStageStoreSubmitProdVO {
	@ApiModelProperty("id主键")
	private Long id;
	@ApiModelProperty("门店id")
	private Long storeId;
	@ApiModelProperty("门店名称")
	private String storeName;
	@ApiModelProperty("商品名称")
	private String prodName;
	@ApiModelProperty("商品code")
	private String prodCode;
	@ApiModelProperty("商品图片1")
	private String prodImage1;
	@ApiModelProperty("商品图片2")
	private String prodImage2;
	@ApiModelProperty("商品图片3")
	private String prodImage3;
	@ApiModelProperty("商品规则")
	private String skuAttributeOption;
	@ApiModelProperty("商品状态（0 待审核 ，1 审核不通过 ，2 审核通过，3 已添加）")
	private Short prodStatus;
	@ApiModelProperty("审核备注")
	private String auditRemark;
	@ApiModelProperty("商品信息（语音）")
	private String prodInfoVoice;
	@ApiModelProperty("商品信息（文字）")
	private String prodInfoText;
	@ApiModelProperty("sku编码")
	private String skuCode;
	@ApiModelProperty("用户账号")
	private String storeMobile;
}
