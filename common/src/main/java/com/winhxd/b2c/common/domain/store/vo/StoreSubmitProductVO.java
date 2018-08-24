package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 审核商品VO
 * @ClassName: StoreSubmitProductVO 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月7日 下午2:08:28
 */
@ApiModel("审核商品VO")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreSubmitProductVO {
    @ApiModelProperty("id主键")
    private Long id;
    
    @ApiModelProperty("门店id")
    private Long storeId;
    
    @ApiModelProperty("商品名称")
    private String prodName;
    
    @ApiModelProperty("商品code")
    private String prodCode;
    
    @ApiModelProperty("提报商品图片1")
    private String prodImage1;

    @ApiModelProperty("审核状态（0 待审核 ，1 审核通过 ，2 审核不通过，3 已添加）")
    private Integer prodStatus;
    
    @ApiModelProperty("审核备注")
    private String auditRemark;

    @ApiModelProperty("商品sku")
    private String skuCode;
    
    @ApiModelProperty("商品信息（语音）")
    private String prodInfoVoice;
    
    @ApiModelProperty("商品信息（文字）")
    private String prodInfoText;
    
    @ApiModelProperty("商品规格")
    private String skuAttributeOption;
    
    @ApiModelProperty(value = "商品图片")
    private String skuImage;
}
