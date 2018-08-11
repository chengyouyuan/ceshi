package com.winhxd.b2c.order.service.impl;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderItemCondition;
import com.winhxd.b2c.common.domain.order.condition.ReadyShopCarCondition;
import com.winhxd.b2c.common.domain.order.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.order.model.ShopCar;
import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.vo.ShopCartProdVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.order.dao.ShopCarMapper;
import com.winhxd.b2c.order.service.OrderService;
import com.winhxd.b2c.order.service.ShopCarService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: wangbaokuo
 * @date: 2018/8/2 20:20
 * @description:
 */
@Service
public class ShopCarServiceImpl implements ShopCarService {

    private static final Logger logger = LoggerFactory.getLogger(ShopCarServiceImpl.class);

    @Resource
    private ShopCarMapper shopCarMapper;

    @Resource
    private OrderService orderService;

    @Resource
    private StoreServiceClient storeServiceClient;
    @Resource
    private Cache cache;

    @Transactional(rollbackFor= {Exception.class})
    @Override
    public int saveShopCar(ShopCarCondition condition, Long customerId){
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
            result.setSkuNum(condition.getSkuNum());
            return shopCarMapper.updateByPrimaryKey(result);
        }
        Date current = new Date();
        shopCar.setCreated(current);
        shopCar.setCreatedBy(customerId);
        shopCar.setUpdated(current);
        shopCar.setUpdatedBy(customerId);
        shopCar.setSkuNum(condition.getSkuNum());
        return shopCarMapper.insertSelective(shopCar);
    }

    @Override
    public List<ShopCarProdInfoVO> findShopCar(Long storeId, Long customerId) {
        List<ShopCarProdInfoVO> result = new ArrayList<>();
        ShopCar shopCar = new ShopCar();
        // 获取当前用户信息
        shopCar.setCustomerId(customerId);
        shopCar.setStoreId(storeId);
        List<ShopCar> shopCars = shopCarMapper.selectShopCars(shopCar);
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
                    shopCarProdInfoVO.setAmount(shopCar2.getSkuNum());
                    shopCarProdInfoVO.setPrice(shopCarProdVO.getSellMoney());
                    shopCarProdInfoVO.setProdImg(shopCarProdVO.getProdImage());
                    shopCarProdInfoVO.setProdName(shopCarProdVO.getProdName());
                    result.add(shopCarProdInfoVO);
                }
            }
        }
        return result;
    }

    @Override
    public int removeShopCar(Long customerId) {
        return shopCarMapper.deleteShopCars(customerId);
    }

    @Override
    public void readyOrder(ReadyShopCarCondition condition, Long customerId) throws InterruptedException {
        String lockKey = CacheName.CACHE_KEY_CUSTOMER_ORDER_REPEAT + customerId;
        Lock lock = new RedisLock(cache, lockKey, 1000);

        if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
            try {
                List<OrderItemCondition> orderItemConditions = condition.getOrderItemConditions();
                checkShopCarProdInfo(orderItemConditions, condition.getStoreId());
                // 保存订单
                OrderCreateCondition orderCreateCondition = new OrderCreateCondition();
                BeanUtils.copyProperties(condition, orderCreateCondition);
                orderCreateCondition.setCustomerId(customerId);
                orderService.submitOrder(orderCreateCondition);
                // 保存成功删除此用户门店的购物车
                ShopCar shopCar = new ShopCar();
                shopCar.setCustomerId(customerId);
                shopCar.setStoreId(condition.getStoreId());
                shopCarMapper.deleteShopCarsByStoreId(shopCar);
                removeShopCar(customerId);
            } finally {
                lock.unlock();
            }
        } else {
            throw new BusinessException(BusinessCode.CODE_402014);
        }

    }

    private List<String> getSkuCodeListByShopCar(List<ShopCar> shopCars){
        List<String> skuCodes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(shopCars)) {
            shopCars.stream().forEach(shopCar -> {
                skuCodes.add(shopCar.getSkuCode());
            });
        }
        return skuCodes;
    }

    private void checkShopCarProdInfo(List<OrderItemCondition> orderItemConditions, Long storeId){
        List<ShopCartProdVO> list = getShopCarProdVO(getSkuCodeListByOrderItem(orderItemConditions), storeId);
        for (ShopCartProdVO shopCarProdVO : list) {
            if (StoreProductStatusEnum.PUTAWAY.getStatusCode().equals(shopCarProdVO.getProdStatus())) {
                logger.error("商品加购异常{}  购物车商品下架或已被删除！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                throw new BusinessException(BusinessCode.CODE_402010);
            }
            for(OrderItemCondition orderItem : orderItemConditions) {
                if (shopCarProdVO.getSkuCode().equals(orderItem.getSkuCode())
                        && !shopCarProdVO.getSellMoney().equals(orderItem.getPrice())) {
                    logger.error("商品加购异常{}  购物车商品价格有变动！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                    throw new BusinessException(BusinessCode.CODE_402012);
                }
            }
        }
    }

    private void checkShopCarProdInfo(ShopCarCondition condition){
        List<ShopCartProdVO> list = getShopCarProdVO(Arrays.asList(condition.getSkuCode()), condition.getStoreId());
        for (ShopCartProdVO shopCarProdVO : list) {
            if (StoreProductStatusEnum.PUTAWAY.getStatusCode().equals(shopCarProdVO.getProdStatus())) {
                logger.error("商品加购异常{}  购物车商品下架或已被删除！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                throw new BusinessException(BusinessCode.CODE_402010);
            }
            if (shopCarProdVO.getSkuCode().equals(condition.getSkuCode())
                    && !shopCarProdVO.getSellMoney().equals(condition.getPrice())) {
                logger.error("商品加购异常{}  购物车商品价格有变动！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                throw new BusinessException(BusinessCode.CODE_402012);
            }
        }
    }

    private List<ShopCartProdVO> getShopCarProdVO(List<String> skuCodes, Long storeId){
        ResponseResult<List<ShopCartProdVO>> shopCarProds = storeServiceClient.findShopCarProd(skuCodes, storeId);
        if (null == shopCarProds) {
            logger.error("store-service服务调用异常{} -> StoreServiceClient");
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        if (CollectionUtils.isEmpty(shopCarProds.getData()) || skuCodes.size() != shopCarProds.getData().size()) {
            logger.error("获取ShopCarProdVO异常{} -> 商品信息不存在或获取商品数量不正确");
            throw new BusinessException(BusinessCode.CODE_402011);
        }
        return shopCarProds.getData();
    }

    private List<String> getSkuCodeListByOrderItem(List<OrderItemCondition> orderItemConditions){
        List<String> skuCodes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderItemConditions)) {
            orderItemConditions.stream().forEach(orderItemCondition -> {
                skuCodes.add(orderItemCondition.getSkuCode());
            });
        }
        return skuCodes;
    }

}
