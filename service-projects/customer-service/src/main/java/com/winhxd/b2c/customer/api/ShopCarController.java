package com.winhxd.b2c.customer.api;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.shopcar.model.ShopCar;
import com.winhxd.b2c.customer.service.ShopCarService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @auther: wangbaokuo
 * @date: 2018/8/2 19:32
 * @description:
 */
@Api(value = "购物车Controller", tags = "api_shop_car")
@RestController
public class ShopCarController {

    private static final Logger logger = LoggerFactory.getLogger(ShopCarController.class);

    @Resource
    private ShopCarService shopCarService;

    /**
     * @auther: wangbaokuo
     * @date: 2018/8/2 19:43
     * @deprecated:
     * @param: [shopCar]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<java.lang.Long>
     */
    @ApiOperation(value = "商品加购")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_500, message = "服务器内部异常")
    })
    @RequestMapping(value = "/api/user/2010/v1/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Long> saveShopCar(@RequestBody ShopCar ShopCar){
        ResponseResult<Long> result = new ResponseResult<>();
        try {
        	shopCarService.saveShopCar(ShopCar);
            return result;
        } catch (Exception e){
            logger.error("ShopCarController -> saveShopCar异常, 异常信息{}" + e.getMessage(), e);
            throw e;
        }
    }
}