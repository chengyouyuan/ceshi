package com.winhxd.b2c.order.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderItemCondition;
import com.winhxd.b2c.common.domain.order.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.order.model.ShopCar;
import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
import com.winhxd.b2c.common.domain.order.vo.ShopCarVO;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.vo.ShopCarProdVO;
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
import java.util.Date;
import java.util.List;

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

    @Transactional(rollbackFor= {Exception.class})
    @Override
    public void saveShopCar(ShopCarCondition condition){
        List<OrderItemCondition> orderItemConditions = condition.getOrderItemConditions();
        checkShopCarProdInfo(orderItemConditions, condition.getStoreId());
        // 加购：1、存在，则删除再保存，2、不存在，直接保存
        ShopCar shopCar = new ShopCar();
        // 获取当前用户信息
        Long customerId = getCurrentCustomerId();
        shopCar.setCustomerId(customerId);
        shopCar.setStoreId(condition.getStoreId());
        List<ShopCar> shopCars = shopCarMapper.selectShopCars(shopCar);
        if (CollectionUtils.isNotEmpty(shopCars)) {
            shopCarMapper.deleteShopCars(shopCar);
        }
        Date current = new Date();
        shopCar.setCreated(current);
        shopCar.setCreatedby(customerId);
        shopCar.setUpdated(current);
        shopCar.setUpdatedby(customerId);
        List<ShopCar> insertList = new ArrayList<>(orderItemConditions.size());
        for(OrderItemCondition item : orderItemConditions){
            shopCar.setSkuCode(item.getSkuCode());
            shopCar.setProdNum(item.getAmount());
            insertList.add(shopCar);
        }
        shopCarMapper.insertByBatch(insertList);
    }

    @Override
    public ShopCarVO findShopCar(ShopCarCondition condition) {
        ShopCar shopCar = new ShopCar();
        // 获取当前用户信息
        Long customerId = getCurrentCustomerId();
        shopCar.setCustomerId(customerId);
        shopCar.setStoreId(condition.getStoreId());
        List<ShopCar> shopCars = shopCarMapper.selectShopCars(shopCar);
        ShopCarVO shopCarVO = new ShopCarVO();
        if (CollectionUtils.isNotEmpty(shopCars)) {
            BeanUtils.copyProperties(shopCars.get(0), shopCarVO);
            List<ShopCarProdVO> shopCarProdVOs = getShopCarProdVO(getSkuCodeListByShopCar(shopCars), shopCars.get(0).getStoreId());
            List<ShopCarProdInfoVO> prodInfos = new ArrayList<>(shopCarProdVOs.size());
            ShopCarProdInfoVO shopCarProdInfoVO;
            for (ShopCar shopCar2 : shopCars){
                for (ShopCarProdVO shopCarProdVO : shopCarProdVOs){
                    if (shopCar2.getSkuCode().equals(shopCarProdVO.getSkuCode())) {
                        shopCarProdInfoVO = new ShopCarProdInfoVO();
                        shopCarProdInfoVO.setSkuCode(shopCar2.getSkuCode());
                        shopCarProdInfoVO.setAmount(shopCar2.getProdNum());
                        shopCarProdInfoVO.setPrice(shopCarProdVO.getSellMoney());
                        shopCarProdInfoVO.setProdImg(shopCarProdVO.getProdImage());
                        // TODO 商品名称
                        shopCarProdInfoVO.setProdName("");
                        shopCarProdInfoVO.setProdStatus(shopCarProdVO.getProdStatus());
                        prodInfos.add(shopCarProdInfoVO);
                    }
                }
            }
            shopCarVO.setShopCarProdInfoVOs(prodInfos);
        }
        return shopCarVO;
    }

    @Transactional(rollbackFor= {Exception.class})
    @Override
    public int removeShopCar(ShopCarCondition condition) {
        ShopCar shopCar = new ShopCar();
        // TODO TOCKEN获取当前用户信息
        Long customerId = getCurrentCustomerId();
        shopCar.setCustomerId(customerId);
        shopCar.setStoreId(condition.getStoreId());
        return shopCarMapper.deleteShopCars(shopCar);
    }

    @Override
    public void readyOrder(ShopCarCondition condition) {
        List<OrderItemCondition> orderItemConditions = condition.getOrderItemConditions();
        checkShopCarProdInfo(orderItemConditions, condition.getStoreId());
        // 保存订单
        OrderCreateCondition orderCreateCondition = new OrderCreateCondition();
        BeanUtils.copyProperties(condition, orderCreateCondition);
        orderService.submitOrder(orderCreateCondition);
        // 保存成功删除此用户门店的购物车
        removeShopCar(condition);
    }

    private List<ShopCarProdVO> getShopCarProdVO(List<String> skuCodes, Long storeId){
        ResponseResult<List<ShopCarProdVO>> shopCarProds = storeServiceClient.findShopCarProd(skuCodes, storeId);
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
        List<ShopCarProdVO> list = getShopCarProdVO(getSkuCodeListByOrderItem(orderItemConditions), storeId);
        for (ShopCarProdVO shopCarProdVO : list) {
            if (StoreProductStatusEnum.PUTAWAY.getStatusCode() != shopCarProdVO.getProdStatus()) {
                logger.error("商品加购异常{}  购物车商品下架或已被删除！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                throw new BusinessException(BusinessCode.CODE_402010);
            }
            for(OrderItemCondition orderItem : orderItemConditions) {
                if (shopCarProdVO.getSkuCode().equals(orderItem.getSkuCode())
                        && null != shopCarProdVO.getSellMoney() && !shopCarProdVO.getSellMoney().equals(orderItem.getPrice())) {
                    logger.error("商品加购异常{}  购物车商品价格有变动！skuCode:" + shopCarProdVO.getSkuCode() + "sellMoney:" + shopCarProdVO.getSellMoney());
                    throw new BusinessException(BusinessCode.CODE_402012);
                }
            }
        }
    }

    private Long getCurrentCustomerId(){
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (null == customerUser) {
            logger.error("获取当前用户信息异常{} UserContext.getCurrentCustomerUser():" + UserContext.getCurrentCustomerUser());
            throw new BusinessException(BusinessCode.CODE_1004);
        }
        return customerUser.getCustomerId();
    }

}
