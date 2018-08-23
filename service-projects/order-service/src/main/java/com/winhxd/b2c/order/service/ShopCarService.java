package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.order.condition.ReadyShopCarCondition;
import com.winhxd.b2c.common.domain.order.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.order.condition.ShopCartProductCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.model.ShopCar;
import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderMoneyVO;
import com.winhxd.b2c.common.domain.pay.vo.ReadyOrderVO;

import java.util.List;

/**
 * @author: wangbaokuo
 * @date: 2018/8/2 20:19
 * @description:
 */
public interface ShopCarService {
    /**
     * 保存购物车
     * @author: wangbaokuo
     * @date: 2018/8/2 20:25
     * @param: CustomerShopCar
     * @return: int
     */
    int saveShopCar(ShopCarCondition shopCar, Long customerId);

    /**
     * 查询购物车
     * @author: wangbaokuo
     * @date: 2018/8/3 14:05
     * @param: condition
     * @return: ShopCarVO
     */
    List<ShopCarProdInfoVO> findShopCar(Long storeId, Long customerId);

    /**
     * 删除购物车
     * @author: wangbaokuo
     * @date: 2018/8/3 16:20
     * @param: condition
     * @return: int
     */
    int removeShopCar(Long customerId);

    /**
     *  预订单
     * @author: wangbaokuo
     * @date: 2018/8/6 16:41
     * @param: condition
     * @return: void
     */
    OrderInfo readyOrder(ReadyShopCarCondition condition, Long customerId);

    /**
     * 根据门店 用户 sku集合 获取集合信息
     * @param condition
     * @return
     */
    List<ShopCar> queryShopCartBySelective(ShopCartProductCondition condition);

    /**
     *  获取用户信息
     * @author: wangbaokuo
     * @date: 2018/8/18 18:02
     * @param: customerId
     * @return: CustomerUserInfoVO
     */
    CustomerUserInfoVO getCustomerUserInfoVO(Long customerId);

    /**
     * 获取预订单信息
     * @author: wangbaokuo
     * @date: 2018/8/23 12:43
     * @param:
     * @return: ReadyOrderVO
     */
    ReadyOrderVO findReadyOrder(Long storeId, Long customerId);

    /**
     * 计算订单金额及优惠金额
     * @author: wangbaokuo
     * @date: 2018/8/23 14:52
     * @param:
     * @return: OrderMoneyVO
     */
    OrderMoneyVO getOrderMoney(Long storeId, Long customerId, Long sendId);
}
