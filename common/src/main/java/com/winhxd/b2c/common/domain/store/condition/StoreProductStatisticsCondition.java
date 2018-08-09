package com.winhxd.b2c.common.domain.store.condition;

import lombok.Data;

/**
 * 门店商品统计信息查询条件
 * @ClassName: StoreProductStatisticsCondition 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月7日 上午10:04:44
 */
@Data
public class StoreProductStatisticsCondition {
	private Long id;

    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 商品id
     */
    private String prodCode;
    /**
     * sku编码
     */
    private String skuCode;
    /**
     * 售出数据
     */
    private Integer quantitySoldOut;
    /**
     * 浏览数量
     */
    private Integer browseNumber;

}
