package com.winhxd.b2c.order.controller;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.ShopCartProductCondition;
import com.winhxd.b2c.common.domain.order.model.ShopCar;
import com.winhxd.b2c.common.domain.order.vo.ShopCartProductVO;
import com.winhxd.b2c.common.feign.order.ShopCartServiceClient;
import com.winhxd.b2c.order.service.ShopCarService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ShopCartController implements ShopCartServiceClient {

    @Autowired
    private ShopCarService shopCarService;
    @Override
    public ResponseResult<List<ShopCartProductVO>> queryShopCartBySelective(ShopCartProductCondition condition) {
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
}
