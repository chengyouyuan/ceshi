package com.winhxd.b2c.store.service;

import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.CustomerSearchProductCondition;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuMsgVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;

/**
 * @author caiyulong
 */
public interface ProductService {

    /**
     * 商品初始化接口
     * @param condition
     * @param currentCustomerUser
     * @return
     */
    ResponseResult<ProductSkuMsgVO> filtrateProductList(CustomerSearchProductCondition condition, CustomerUser currentCustomerUser);

    /**
     * 商品搜索 筛选接口
     * @param condition
     * @param currentCustomerUser
     * @return
     */
    ResponseResult<PagedList<ProductSkuVO>> searchProductList(CustomerSearchProductCondition condition, CustomerUser currentCustomerUser);

    /**
     * 热销商品
     * @param condition
     * @return
     */
    ResponseResult<PagedList<ProductSkuVO>> hotProductList(CustomerSearchProductCondition condition);
}
