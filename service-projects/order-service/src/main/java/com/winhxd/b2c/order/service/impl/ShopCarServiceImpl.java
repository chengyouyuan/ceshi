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
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.vo.ShopCartProdVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.dao.ShopCarMapper;
import com.winhxd.b2c.order.service.OrderQueryService;
import com.winhxd.b2c.order.service.OrderService;
import com.winhxd.b2c.order.service.ShopCarService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private Cache cache;

    @Transactional(rollbackFor= {Exception.class})
    @Override
    public int saveShopCar(ShopCarCondition condition, Long customerId){
        logger.info("saveShopCar {}-> 新增购物车执行...");
        // 校验商品
        checkShopCarProdInfo(condition);
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
    }

    @Override
    public List<ShopCarProdInfoVO> findShopCar(Long storeId, Long customerId) {
        logger.info("findShopCar {}-> 查询购物车执行...");
        List<ShopCarProdInfoVO> result = new ArrayList<>();
        List<ShopCar> shopCars = queryShopCars(customerId, storeId);
        if (CollectionUtils.isEmpty(shopCars)) {
            return result;
        }
        // 商品详情
        List<ShopCartProdVO> shopCarProdVOs = getShopCarProdVO(getSkuCodeListByShopCar(shopCars), shopCars.get(0).getStoreId());
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
                    result.add(shopCarProdInfoVO);
                }
            }
        }
        return result;
    }

    @Override
    public int removeShopCar(Long customerId) {
        logger.info("removeShopCar {}-> 清空购物车执行...");
        return shopCarMapper.deleteShopCars(customerId);
    }

    @Override
    public OrderInfo readyOrder(ReadyShopCarCondition condition, Long customerId) {
        logger.info(READY_ORDER + "{}-> 执行...");
        String lockKey = CacheName.CACHE_KEY_CUSTOMER_ORDER_REPEAT + customerId;
        Lock lock = new RedisLock(cache, lockKey, 1000);
        try {
            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                // 后台去获取购物车信息
                List<ShopCar> shopCars = queryShopCars(customerId, condition.getStoreId());
                if (CollectionUtils.isEmpty(shopCars)) {
                    logger.error(READY_ORDER + "{}-> 购物车信息shopCars:" + JsonUtil.toJSONString(shopCars));
                    throw new BusinessException(BusinessCode.CODE_402011);
                }
                logger.info(READY_ORDER + "{}-> 校验购物车商品状态执行...");
                checkShopCarProdInfo(shopCars);

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
                logger.info(READY_ORDER + "{}-> 订单接口submitOrder开始...");
                OrderInfo orderInfo = orderService.submitOrder(orderCreateCondition);
                logger.info(READY_ORDER + "{}-> 订单接口submitOrder结束...");
                // 保存成功删除此用户门店的购物车
                shopCarMapper.deleteShopCarsByStoreId(shopCars.get(0));
                this.removeShopCar(customerId);
                return orderInfo;
            } else {
                throw new BusinessException(BusinessCode.CODE_402014);
            }
        } catch (InterruptedException e){
            throw new BusinessException(BusinessCode.CODE_1001);
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

    private void checkShopCarProdInfo(List<ShopCar> shopCars){
        List<ShopCartProdVO> list = getShopCarProdVO(getSkuCodeListByShopCar(shopCars), shopCars.get(0).getStoreId());
        for (ShopCartProdVO shopCarProdVO : list) {
            if (!StoreProductStatusEnum.PUTAWAY.getStatusCode().equals(shopCarProdVO.getProdStatus())) {
                logger.error("商品加购异常{}  购物车商品下架或已被删除！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                throw new BusinessException(BusinessCode.CODE_402010);
            }
        }
    }

    private void checkShopCarProdInfo(ShopCarCondition condition){
        List<ShopCartProdVO> list = getShopCarProdVO(Arrays.asList(condition.getSkuCode()), condition.getStoreId());
        for (ShopCartProdVO shopCarProdVO : list) {
            if (!StoreProductStatusEnum.PUTAWAY.getStatusCode().equals(shopCarProdVO.getProdStatus())) {
                logger.error("商品加购异常{}  购物车商品下架或已被删除！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                throw new BusinessException(BusinessCode.CODE_402010);
            }
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

    private List<ShopCartProdVO> getShopCarProdVO(List<String> skuCodes, Long storeId){
        ResponseResult<List<ShopCartProdVO>> shopCarProds = storeServiceClient.findShopCarProd(skuCodes, storeId);
        if (CollectionUtils.isEmpty(shopCarProds.getData()) || skuCodes.size() != shopCarProds.getData().size()) {
            logger.error("获取ShopCarProdVO异常{} -> 商品信息不存在或获取商品数量不正确");
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
