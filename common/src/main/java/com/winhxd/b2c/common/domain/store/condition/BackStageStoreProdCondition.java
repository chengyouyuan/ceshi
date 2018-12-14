package com.winhxd.b2c.common.domain.store.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 门店商品后台condition
 * @ClassName: BackStageStoreProdCondition 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月11日 下午3:26:39
 */
@Data
public class BackStageStoreProdCondition {

    @ApiModelProperty("id主键")
    private Long id;
    
    @ApiModelProperty("门店id")
    private Long storeId;
    
    @ApiModelProperty("商品规格")
    private String skuAttributeOption;
    
    @ApiModelProperty("售卖价格")
    private BigDecimal sellMoney;
    
    @ApiModelProperty("是否推荐 0不推荐 1推荐")
    private Short recommend;
    
    @ApiModelProperty("商品状态 0下架1上架2已删除")
    private Short prodStatus;
    
    @ApiModelProperty("商品sku")
    private String prodCode;
    
    @ApiModelProperty("商品名称")
    private String prodName;
    
    @ApiModelProperty("商品sku")
    private String skuCode;
    
    @ApiModelProperty("上架时间")
    private Date putawayTime;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("用户账号")
    private String storeMobile;
    
    @ApiModelProperty("商品sku集合")
    private List<String> skuCodeList;
    
    @ApiModelProperty("查询数据是否包含删除状态 默认0， 0不包含 1包含")
    private Integer deleted=0;
    
    @ApiModelProperty("页大小")
    private Integer pageSize=10;
    
    @ApiModelProperty("页号")
    private Integer pageNo=1;

    @ApiModelProperty("是否查询全部数据")
    private Boolean isQueryAll = false;

}
