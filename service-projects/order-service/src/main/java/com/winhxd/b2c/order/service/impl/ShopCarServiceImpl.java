package com.winhxd.b2c.order.service.impl;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.*;
import com.winhxd.b2c.common.domain.order.model.ShopCar;
import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.vo.ShopCartProdVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
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
                    shopCarProdInfoVO.setAmount(shopCar2.getAmount());
                    shopCarProdInfoVO.setPrice(shopCarProdVO.getSellMoney());
                    shopCarProdInfoVO.setSkuImage(shopCarProdVO.getSkuImage());
                    shopCarProdInfoVO.setProdName(shopCarProdVO.getProdName());
                    shopCarProdInfoVO.setBrandCode(shopCarProdVO.getBrandCode());
                    shopCarProdInfoVO.setSkuAttributeOption(shopCarProdVO.getSkuAttributeOption());
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
    public String readyOrder(ReadyShopCarCondition condition, Long customerId) {
        String lockKey = CacheName.CACHE_KEY_CUSTOMER_ORDER_REPEAT + customerId;
        Lock lock = new RedisLock(cache, lockKey, 1000);
        try {
            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                // 后台去获取购物车信息
                ShopCar shopCar = new ShopCar();
                shopCar.setCustomerId(customerId);
                shopCar.setStoreId(condition.getStoreId());
                logger.info("预订单接口readyOrder -> 调用查询购物车接口{findShopCar}执行...");
                List<ShopCar> shopCars = shopCarMapper.selectShopCars(shopCar);
                logger.info("预订单接口readyOrder -> 调用查询购物车接口{findShopCar}执行结束..." + JsonUtil.toJSONString(shopCars));

                logger.info("预订单接口readyOrder -> 校验购物车商品状态执行...");
                checkShopCarProdInfo(shopCars);
                logger.info("预订单接口readyOrder -> 校验购物车商品状态执行结束...");

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
                logger.info("预订单接口readyOrder -> 调用订单接口{submitOrder}执行...");
                String orderNo  = orderService.submitOrder(orderCreateCondition).getOrderNo();
                logger.info("预订单接口readyOrder -> 调用订单接口{submitOrder}执行结束...");

                // TODO 金额不为空  调用pay-service返回签名字段:appid，partnerid，prepayid，noncestr，timestamp，package。注意：package的值格式为Sign=WXPay
                if (null != condition.getOrderTotalMoney()) {

                }
                // 保存成功删除此用户门店的购物车
                shopCarMapper.deleteShopCarsByStoreId(shopCar);
                this.removeShopCar(customerId);
                return orderNo;
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
    public List<ShopCar> queryShopCartBySelective(ShopCartProductCondition condition) {
        return shopCarMapper.queryShopCartBySelective(condition);
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

    private void checkShopCarProdInfo(List<ShopCar> shopCars){
        List<ShopCartProdVO> list = getShopCarProdVO(getSkuCodeListByShopCar(shopCars), shopCars.get(0).getStoreId());
        for (ShopCartProdVO shopCarProdVO : list) {
            if (StoreProductStatusEnum.PUTAWAY.getStatusCode().equals(shopCarProdVO.getProdStatus())) {
                logger.error("商品加购异常{}  购物车商品下架或已被删除！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                throw new BusinessException(BusinessCode.CODE_402010);
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

}
