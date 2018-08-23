package com.winhxd.b2c.order.service.impl;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.order.condition.*;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.model.ShopCar;
import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
import com.winhxd.b2c.common.domain.order.vo.ShopCarVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderMoneyVO;
import com.winhxd.b2c.common.domain.pay.vo.ReadyOrderVO;
import com.winhxd.b2c.common.domain.promotion.condition.CouponPreAmountCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponProductCondition;
import com.winhxd.b2c.common.domain.promotion.condition.OrderAvailableCouponCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponDiscountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.vo.ShopCartProdVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.promotion.CouponServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.dao.ShopCarMapper;
import com.winhxd.b2c.order.service.OrderService;
import com.winhxd.b2c.order.service.ShopCarService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: wangbaokuo
 * @date: 2018/8/2 20:20
 * @description:
 */
@Service
public class ShopCarServiceImpl implements ShopCarService {

    private static final Logger logger = LoggerFactory.getLogger(ShopCarServiceImpl.class);

    private static final String READY_ORDER = "预订单接口readyOrder";

    private static final String SHOP_CAR = "ShopCarServiceImpl {} ";

    private static final Byte PROD_STATUS = 1;

    private static final Integer INTEGER_ZERO = 0;

    @Autowired
    private ShopCarMapper shopCarMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StoreServiceClient storeServiceClient;

    @Autowired
    private CustomerServiceClient customerServiceClient;

    @Autowired
    private CouponServiceClient couponServiceClient;

    @Autowired
    private Cache cache;

    @Transactional(rollbackFor= {Exception.class})
    @Override
    public int saveShopCar(ShopCarCondition condition, Long customerId){
        logger.info("saveShopCar {}-> 新增购物车执行...");
        String lockKey = CacheName.CACHE_KEY_CUSTOMER_SHOPCART_REPEAT + customerId;
        Lock lock = new RedisLock(cache, lockKey, 1000);
        try {
            if (lock.tryLock()) {
                // 校验商品
                checkShopCarProdInfo(condition, customerId);
                // 加购：存在则更新数量，不存在保存
                ShopCar shopCar = new ShopCar();
                // 获取当前用户信息
                shopCar.setCustomerId(customerId);
                shopCar.setStoreId(condition.getStoreId());
                shopCar.setSkuCode(condition.getSkuCode());
                ShopCar result = shopCarMapper.selectShopCarsBySkuCode(shopCar);
                if (null != result) {
                    if (INTEGER_ZERO.equals(condition.getAmount())) {
                        return shopCarMapper.deleteByPrimaryKey(result.getId());
                    }
                    result.setAmount(condition.getAmount());
                    return shopCarMapper.updateByPrimaryKey(result);
                }
                Date current = new Date();
                shopCar.setCreated(current);
                shopCar.setCreatedBy(customerId);
                shopCar.setUpdated(current);
                shopCar.setUpdatedBy(customerId);
                shopCar.setAmount(condition.getAmount());
                return shopCarMapper.insertSelective(shopCar);
            } else {
                throw new BusinessException(BusinessCode.CODE_402016);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ShopCarVO findShopCar(Long storeId, Long customerId) {
        logger.info("findShopCar {}-> 查询购物车执行...");
        ShopCarVO result = new ShopCarVO();
        List<ShopCar> shopCars = queryShopCars(customerId, storeId);
        if (CollectionUtils.isEmpty(shopCars)) {
            return result;
        }
        // 商品详情
        List<String> skuCodes = getSkuCodeListByShopCar(shopCars);
        List<ShopCartProdVO> shopCarProdVOs = getShopCarProdVO(skuCodes, shopCars.get(0).getStoreId(), customerId);
        if (skuCodes.size() != shopCarProdVOs.size()) {
            result.setProdStatus(PROD_STATUS);
        }
        List<ShopCarProdInfoVO> list = new ArrayList<>();
        ShopCarProdInfoVO shopCarProdInfoVO;
        for (ShopCar shopCar2 : shopCars){
            for (ShopCartProdVO shopCarProdVO : shopCarProdVOs){
                if (shopCar2.getSkuCode().equals(shopCarProdVO.getSkuCode())) {
                    shopCarProdInfoVO = new ShopCarProdInfoVO();
                    shopCarProdInfoVO.setSkuCode(shopCar2.getSkuCode());
                    shopCarProdInfoVO.setAmount(shopCar2.getAmount());
                    BeanUtils.copyProperties(shopCarProdVO, shopCarProdInfoVO);
                    shopCarProdInfoVO.setPrice(shopCarProdVO.getSellMoney());
                    shopCarProdInfoVO.setCompanyCode(shopCarProdVO.getCompanyCode());
                    list.add(shopCarProdInfoVO);
                }
            }
        }
        result.setShopCarts(list);
        return result;
    }

    @Override
    public int removeShopCar(Long customerId) {
        logger.info("removeShopCar {}-> 清空购物车执行...");
        return shopCarMapper.deleteShopCars(customerId);
    }

    @Override
    public OrderInfo readyOrder(ReadyShopCarCondition condition, Long customerId) {
        logger.info(SHOP_CAR + READY_ORDER + "{}-> 执行...");
        String lockKey = CacheName.CACHE_KEY_CUSTOMER_ORDER_REPEAT + customerId;
        Lock lock = new RedisLock(cache, lockKey, 1000);
        try {
            if (lock.tryLock()) {
                // 后台去获取购物车信息
                List<ShopCar> shopCars = queryShopCars(customerId, condition.getStoreId());
                if (CollectionUtils.isEmpty(shopCars)) {
                    logger.error(SHOP_CAR + READY_ORDER + "{}-> 购物车信息shopCars:" + JsonUtil.toJSONString(shopCars));
                    throw new BusinessException(BusinessCode.CODE_402011);
                }
                logger.info(SHOP_CAR + READY_ORDER + "{}-> 校验购物车商品状态执行...");
                checkShopCarProdInfo(shopCars, customerId);

                List<OrderItemCondition> items = new ArrayList<>(shopCars.size());
                shopCars.stream().forEach( shopCar1 ->{
                    OrderItemCondition orderItemCondition = new OrderItemCondition();
                    orderItemCondition.setAmount(shopCar1.getAmount());
                    orderItemCondition.setSkuCode(shopCar1.getSkuCode());
                    items.add(orderItemCondition);
                });
                // 保存订单
                OrderCreateCondition orderCreateCondition = new OrderCreateCondition();
                BeanUtils.copyProperties(condition, orderCreateCondition);
                orderCreateCondition.setCustomerId(customerId);
                orderCreateCondition.setOrderItemConditions(items);
                logger.info(SHOP_CAR + READY_ORDER + "{}-> 订单接口submitOrder开始...");
                OrderInfo orderInfo = orderService.submitOrder(orderCreateCondition);
                logger.info(SHOP_CAR + READY_ORDER + "{}-> 订单接口submitOrder结束...");
                // 保存成功删除此用户门店的购物车
                shopCarMapper.deleteShopCarsByStoreId(shopCars.get(0));
                this.removeShopCar(customerId);
                return orderInfo;
            } else {
                throw new BusinessException(BusinessCode.CODE_402014);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public CustomerUserInfoVO getCustomerUserInfoVO(Long customerId) {
        ResponseResult<List<CustomerUserInfoVO>> ret = customerServiceClient.findCustomerUserByIds(Arrays.asList(customerId));
        if (ret == null || ret.getCode() != BusinessCode.CODE_OK || CollectionUtils.isEmpty(ret.getData())) {
            throw new BusinessException(BusinessCode.WRONG_CUSTOMER_ID);
        }
        logger.info("根据customerId={} 获取用户信息成功，用户信息：{}", ret.getData().get(0));
        return ret.getData().get(0);
    }

    private StoreUserInfoVO getStoreUserInfoVO(Long storeId) {
        ResponseResult<StoreUserInfoVO> ret = storeServiceClient.findStoreUserInfo(storeId);
        if (ret == null || ret.getCode() != BusinessCode.CODE_OK || ret.getData() == null) {
            throw new BusinessException(BusinessCode.WRONG_CUSTOMER_ID);
        }
        logger.info("storeId={} 获取用户信息成功，用户信息：{}", ret.getData());
        return ret.getData();
    }

    @Override
    public ReadyOrderVO findReadyOrder(Long storeId, Long customerId) {
        ReadyOrderVO vo = new ReadyOrderVO();
        logger.info(SHOP_CAR + "findReadyOrder{} -> 统计订单实付金额，优惠金额执行...");
        CouponVO coupon = getDefaultCoupon(storeId);
        OrderMoneyVO orderTotalMoney = this.getOrderMoney(storeId, customerId, coupon == null ? null : coupon.getSendId());
        vo.setOrderMoney(orderTotalMoney);
        logger.info(SHOP_CAR + "findReadyOrder{} -> 获取门店信息执行...");
        StoreUserInfoVO storeUserInfoVO = getStoreUserInfoVO(storeId);
        vo.setStoreAddress(storeUserInfoVO.getStoreAddress());
        vo.setPayType(storeUserInfoVO.getPayType());
        vo.setCoupon(getDefaultCoupon(storeId));
        vo.setShopCarts(this.findShopCar(storeId, customerId).getShopCarts());
        return vo;
    }

    private CouponVO getDefaultCoupon(Long storeId){
        OrderAvailableCouponCondition orderAvailableCouponCondition = new OrderAvailableCouponCondition();
        orderAvailableCouponCondition.setPayType(getStoreUserInfoVO(storeId).getPayType());
        orderAvailableCouponCondition.setStoreId(storeId);
        ResponseResult<CouponVO> result = couponServiceClient.findDefaultCoupon(orderAvailableCouponCondition);
        if (null == result || result.getCode() != BusinessCode.CODE_OK ) {
            logger.info(SHOP_CAR + "getDefaultCoupon接口异常{} -> CouponVO:" + result);
            throw new BusinessException(BusinessCode.CODE_402018);
        }
        return result.getData();
    }

    @Override
    public OrderMoneyVO getOrderMoney(Long storeId, Long customerId, Long sendId){
        OrderMoneyVO result = new OrderMoneyVO();
        logger.info(SHOP_CAR + "getOrderMoney{} 执行...");
        // 计算无优惠券时订单价格
        List<ShopCar> shopCars = queryShopCars(customerId, storeId);
        if (CollectionUtils.isEmpty(shopCars)) {
            logger.info(SHOP_CAR + "getOrderMoney{} 购物车不存在商品，不计算价格：shopCars="+JsonUtil.toJSONString(shopCars));
            return result;
        }
        List<ShopCartProdVO> shopCarProdVOs = getShopCarProdVO(getSkuCodeListByShopCar(shopCars), storeId, customerId);
        for(ShopCartProdVO vo : shopCarProdVOs) {
            if (null == vo.getSellMoney()) {
                logger.info(SHOP_CAR + "getOrderMoney{} 存在售卖价格为空的商品，不计算价格：skuCode="+vo.getSkuCode());
                return result;
            }
        }
        for(ShopCar shopCar : shopCars) {
            for (ShopCartProdVO shopCartProdVO : shopCarProdVOs) {
                if (shopCar.getSkuCode().equals(shopCartProdVO.getSkuCode())) {
                    BigDecimal money = shopCartProdVO.getSellMoney().setScale(2, RoundingMode.HALF_UP);
                    result.setOrderTotalMoney(result.getOrderTotalMoney().add(money.multiply(new BigDecimal(shopCar.getAmount()))));
                }
            }
        }
        // 减去优惠金额
        if (null != sendId  && result.getOrderTotalMoney().compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal discountAmount = getDiscountAmount(Arrays.asList(sendId), findCouponProduct(storeId, customerId), getStoreUserInfoVO(storeId).getPayType());
            result.setOrderTotalMoney(result.getOrderTotalMoney().subtract(discountAmount == null ? BigDecimal.ZERO : discountAmount));
            result.setDiscountAmount(result.getDiscountAmount().add(discountAmount));
        }
        return result;
    }

    private List<CouponProductCondition> findCouponProduct(Long storeId, Long customerId){
        List<ShopCarProdInfoVO> shopCars = this.findShopCar(storeId, customerId).getShopCarts();
        if (CollectionUtils.isEmpty(shopCars)) {
            return Collections.emptyList();
        }
        List<CouponProductCondition> result = new ArrayList<>(shopCars.size());
        shopCars.stream().forEach(shopCar -> {
            CouponProductCondition condition = new CouponProductCondition();
            condition.setBrandBusinessCode(shopCar.getCompanyCode());
            condition.setBrandCode(shopCar.getBrandCode());
            condition.setPrice(shopCar.getPrice());
            condition.setSkuCode(shopCar.getSkuCode());
            condition.setSkuNum(shopCar.getAmount());
            result.add(condition);
        });
        return result;
    }

    private BigDecimal getDiscountAmount(List<Long> sendIds, List<CouponProductCondition> products, String payType){
        CouponPreAmountCondition couponPreAmountCondition = new CouponPreAmountCondition();
        couponPreAmountCondition.setSendIds(sendIds);
        couponPreAmountCondition.setProducts(products);
        couponPreAmountCondition.setPayType(payType);
        ResponseResult<CouponDiscountVO> result = couponServiceClient.couponDiscountAmount(couponPreAmountCondition);
        if (null == result || result.getCode() != BusinessCode.CODE_OK || null == result.getData()) {
            logger.info(SHOP_CAR + "getDiscountAmount{} 计算优惠金额失败 CouponDiscountVO:" + result);
            throw new BusinessException(BusinessCode.CODE_402017);
        }
        return result.getData().getDiscountAmount();
    }

    @Override
    public List<ShopCar> queryShopCartBySelective(ShopCartProductCondition condition) {
        return shopCarMapper.queryShopCartBySelective(condition);
    }

    private List<String> getSkuCodeListByShopCar(List<ShopCar> shopCars){
        List<String> skuCodes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(shopCars)) {
            skuCodes = shopCars.stream()
                    .map(shopCar -> shopCar.getSkuCode()).collect(Collectors.toList());
        }
        return skuCodes;
    }

    private void checkShopCarProdInfo(List<ShopCar> shopCars, Long customerId){
        List<ShopCartProdVO> list = getShopCarProdVO(getSkuCodeListByShopCar(shopCars), shopCars.get(0).getStoreId(), customerId);
        for (ShopCartProdVO shopCarProdVO : list) {
            if (!StoreProductStatusEnum.PUTAWAY.getStatusCode().equals(shopCarProdVO.getProdStatus())) {
                logger.error("商品加购异常{}  购物车商品下架或已被删除！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                throw new BusinessException(BusinessCode.CODE_402010);
            }
        }
    }

    private void checkShopCarProdInfo(ShopCarCondition condition ,Long customerId){
        List<ShopCartProdVO> list = getShopCarProdVO(Arrays.asList(condition.getSkuCode()), condition.getStoreId(), customerId);
        for (ShopCartProdVO shopCarProdVO : list) {
            // 程序能走到这的一定是上加中的商品
            /*if (!StoreProductStatusEnum.PUTAWAY.getStatusCode().equals(shopCarProdVO.getProdStatus())) {
                logger.error("商品加购异常{}  购物车商品下架或已被删除！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                throw new BusinessException(BusinessCode.CODE_402010);
            }*/
            if (shopCarProdVO.getSkuCode().equals(condition.getSkuCode())) {
                BigDecimal sellMoney = shopCarProdVO.getSellMoney() == null ? BigDecimal.ZERO : shopCarProdVO.getSellMoney();
                BigDecimal price = condition.getPrice() == null ? BigDecimal.ZERO : condition.getPrice();
                if(sellMoney.compareTo(price) != 0) {
                    logger.error("商品加购异常{}  购物车商品价格有变动！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                    throw new BusinessException(BusinessCode.CODE_402012);
                }
            }
        }
    }

    private List<ShopCartProdVO> getShopCarProdVO(List<String> skuCodes, Long storeId, Long customerId){
        ResponseResult<List<ShopCartProdVO>> shopCarProds = storeServiceClient.findShopCarProd(skuCodes, storeId);
        if (null == shopCarProds || shopCarProds.getCode() != BusinessCode.CODE_OK || CollectionUtils.isEmpty(shopCarProds.getData())) {
            if (CollectionUtils.isNotEmpty(skuCodes)) {
                shopCarMapper.deleteShopCarts(storeId, customerId, skuCodes);
            }
            logger.error("ShopCarServiceImpl{} -> 获取ShopCarProdVO异常{} 商品信息不存在或被删除");
            throw new BusinessException(BusinessCode.CODE_402011);
        }
        return shopCarProds.getData();
    }

    private List<ShopCar> queryShopCars(Long customerId, Long storeId){
        if (null == customerId || null == storeId) {
            return Collections.emptyList();
        }
        ShopCar shopCar = new ShopCar();
        shopCar.setCustomerId(customerId);
        shopCar.setStoreId(storeId);
        return shopCarMapper.selectShopCars(shopCar);
    }

}
