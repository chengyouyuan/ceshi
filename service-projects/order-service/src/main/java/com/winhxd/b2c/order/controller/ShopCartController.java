package com.winhxd.b2c.order.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.ShopCartProductCondition;
import com.winhxd.b2c.common.domain.order.condition.ShopCartQueryCondition;
import com.winhxd.b2c.common.domain.order.model.ShopCar;
import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
import com.winhxd.b2c.common.domain.order.vo.ShopCartProductVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.ShopCartServiceClient;
import com.winhxd.b2c.order.service.ShopCarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ShopCartController implements ShopCartServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ShopCartController.class);

    @Autowired
    private ShopCarService shopCarService;
    @Override
    public ResponseResult<List<ShopCartProductVO>> queryShopCartBySelective(@RequestBody ShopCartProductCondition condition) {
        List<ShopCartProductVO> shopCartProductVOS = new ArrayList<>();
        List<ShopCar> shopCars = shopCarService.queryShopCartBySelective(condition);
        ShopCartProductVO shopCartProductVO;
        for (ShopCar shopCar : shopCars) {
            shopCartProductVO = new ShopCartProductVO();
            BeanUtils.copyProperties(shopCar,shopCartProductVO);
            shopCartProductVOS.add(shopCartProductVO);
        }
        return new ResponseResult<>(shopCartProductVOS);
    }

    @Override
    public ResponseResult<List<ShopCarProdInfoVO>> findShopCar(@RequestBody ShopCartQueryCondition condition) {
        if (null == condition || null == condition.getStoreId()) {
            throw new BusinessException(BusinessCode.CODE_402001);
        }
        if (null == condition.getCustomerId()) {
            throw new BusinessException(BusinessCode.CODE_1004);
        }
        List<ShopCarProdInfoVO> data = shopCarService.findShopCar(condition.getStoreId(), condition.getCustomerId()).getShopCarts();
        return new ResponseResult<>(data);
    }

}
