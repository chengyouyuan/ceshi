package com.winhxd.b2c.order.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.order.vo.ShopCarVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.order.service.OrderService;
import com.winhxd.b2c.order.service.ShopCarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: wangbaokuo
 * @date: 2018/8/2 19:32
 * @description:
 */
@Api(tags = "ApiShopCarController")
@RestController
public class ApiShopCarController {

    private static final Logger logger = LoggerFactory.getLogger(ApiShopCarController.class);

    @Resource
    private ShopCarService shopCarService;

    @Resource
    private OrderService orderService;

    /**
     * 商品加购
     * @author: wangbaokuo
     * @date: 2018/8/2 19:43
     * @param: [shopCar]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<java.lang.Long>
     */
    @ApiOperation(value = "商品加购", response = Long.class, notes = "商品加购")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Long.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @RequestMapping(value = "/api-order/order/430/v1/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Long> saveShopCar(@RequestBody ShopCarCondition condition){
        ResponseResult<Long> result = new ResponseResult<>();
        try {
            shopCarParam(condition);
        	shopCarService.saveShopCar(condition);
            return result;
        } catch (Exception e){
            logger.error("ShopCarController -> saveShopCar接口异常, 异常信息{}" + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 查询购物车
     * @author: wangbaokuo
     * @date: 2018/8/3 11:29
     * @param: [condition]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<java.util.List<com.winhxd.b2c.common.domain.shopcar.vo.ShopCarVO>>
     */
    @ApiOperation(value = "查询购物车", response = ShopCarVO.class, notes = "查询购物车")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ShopCarVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @RequestMapping(value = "/api-order/order/431/v1/find", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<ShopCarVO>> findShopCar(@RequestBody ShopCarCondition condition){
        ResponseResult<List<ShopCarVO>> result = new ResponseResult<>();
        try {
            if (null == condition || null == condition.getStoreId()) {
                logger.error("查询购物车异常{}  参数storeId为空");
                throw new BusinessException(BusinessCode.CODE_402001);
            }
            List<ShopCarVO> resultList = shopCarService.findShopCar(condition);
            result.setData(resultList);
            return result;
        } catch (Exception e){
            logger.error("ShopCarController -> findShopCar接口异常, 异常信息{}" + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 预订单
     * @author: wangbaokuo
     * @date: 2018/8/3 15:24
     * @param: [condition]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<java.lang.Long>
     */
    @ApiOperation(value = "预订单", response = Long.class, notes = "预订单")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Long.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @RequestMapping(value = "/api-order/order/432/v1/readyOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Long> readyOrder(@RequestBody ShopCarCondition condition){
        ResponseResult<Long> result = new ResponseResult<>();
        try {
            shopCarParam(condition);
            // 校验门店上下架，库存
            if (!shopCarService.checkShelves(condition.getSkuCode())) {
                logger.error("商品加购异常{}  商品下架");
                throw new BusinessException(BusinessCode.CODE_402010);
            }
            // 保存订单
            OrderCreateCondition orderCreateCondition = new OrderCreateCondition();
            BeanUtils.copyProperties(condition, orderCreateCondition);
            orderService.submitOrder(orderCreateCondition);

            // 保存成功删除此用户门店的购物车
            shopCarService.removeShopCar(condition);
            return result;
        } catch (Exception e){
            logger.error("ShopCarController -> readyOrder接口异常, 异常信息{}" + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 验参
     * @author: wangbaokuo
     * @date: 2018/8/3 13:25
     * @param: [condition]
     * @return: void
     */
    private void shopCarParam(ShopCarCondition condition){
        if (null == condition) {
            logger.error("商品加购异常{}  参数错误");
            throw new BusinessException(BusinessCode.CODE_402008);
        }
        if (null == condition.getStoreId()){
            logger.error("商品加购异常{}  参数storeId为空");
            throw new BusinessException(BusinessCode.CODE_402001);
        }
        if (null == condition.getExtractAddress()){
            logger.error("商品加购异常{}  参数extractAddress为空");
            throw new BusinessException(BusinessCode.CODE_402002);
        }
        if (null == condition.getPickupDateTime()){
            logger.error("商品加购异常{}  参数pickupDateTime为空");
            throw new BusinessException(BusinessCode.CODE_402003);
        }
        if (null == condition.getSkuCode()){
            logger.error("商品加购异常{}  参数skuCode为空");
            throw new BusinessException(BusinessCode.CODE_402004);
        }
        if (null == condition.getProdNum()){
            logger.error("商品加购异常{}  参数prodNum为空");
            throw new BusinessException(BusinessCode.CODE_402005);
        }
        if (null == condition.getPayType()){
            logger.error("商品加购异常{}  参数payType为空");
            throw new BusinessException(BusinessCode.CODE_402006);
        }
        if (null == condition.getCouponIds() || condition.getCouponIds().length == 0){
            logger.error("商品加购异常{}  参数couponIds为空");
            throw new BusinessException(BusinessCode.CODE_402007);
        }
        if (null == condition.getOrderTotalMoney()){
            logger.error("商品加购异常{}  参数orderTotalMoney为空");
            throw new BusinessException(BusinessCode.CODE_402008);
        }
    }
}
