package com.winhxd.b2c.order.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.order.vo.ShopCarVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.order.service.ShopCarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: wangbaokuo
 * @date: 2018/8/2 19:32
 */
@Api(tags = "ApiShopCarController")
@RestController
public class ApiShopCarController {

    private static final Logger logger = LoggerFactory.getLogger(ApiShopCarController.class);

    @Resource
    private ShopCarService shopCarService;

    /**
     * 商品加购
     * @author: wangbaokuo
     * @date: 2018/8/2 19:43
     * @param: [shopCar]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<java.lang.Long>
     */
    @ApiOperation(value = "商品加购", notes = "商品加购")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_402008, message = "参数错误"),
            @ApiResponse(code = BusinessCode.CODE_402001, message = "门店ID为空"),
            @ApiResponse(code = BusinessCode.CODE_402002, message = "自提地址为空"),
            @ApiResponse(code = BusinessCode.CODE_402003, message = "自提为空"),
            @ApiResponse(code = BusinessCode.CODE_402004, message = "商品信息为空"),
            @ApiResponse(code = BusinessCode.CODE_402006, message = "支付类型为空"),
            @ApiResponse(code = BusinessCode.CODE_402012, message = "购物车商品价格有变动")
    })
    @RequestMapping(value = "/api-order/order/430/v1/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Long> saveShopCar(@RequestBody ShopCarCondition condition){
        ResponseResult<Long> result = new ResponseResult<>();
        try {
            shopCarParam(condition);
        	shopCarService.saveShopCar(condition);
        } catch (Exception e){
            logger.error("ShopCarController -> saveShopCar接口异常, 异常信息{}" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    /**
     * 查询购物车
     * @author: wangbaokuo
     * @date: 2018/8/3 11:29
     * @param: [condition]
     * @return: ShopCarVO
     */
    @ApiOperation(value = "查询购物车", notes = "查询购物车")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_402001, message = "参数storeId为空")
    })
    @RequestMapping(value = "/api-order/order/431/v1/find", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<ShopCarVO> findShopCar(@RequestBody ShopCarCondition condition){
        ResponseResult<ShopCarVO> result = new ResponseResult<>();
        try {
            if (null == condition || null == condition.getStoreId()) {
                logger.error("查询购物车异常{}  参数storeId为空");
                throw new BusinessException(BusinessCode.CODE_402001);
            }
            ShopCarVO shopCarVO = shopCarService.findShopCar(condition);
            result.setData(shopCarVO);
        } catch (Exception e){
            logger.error("ShopCarController -> findShopCar接口异常, 异常信息{}" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    /**
     * 预订单
     * @author: wangbaokuo
     * @date: 2018/8/3 15:24
     * @param: [condition]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<java.lang.Long>
     */
    @ApiOperation(value = "预订单", notes = "预订单")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_402008, message = "参数错误"),
            @ApiResponse(code = BusinessCode.CODE_402001, message = "门店ID为空"),
            @ApiResponse(code = BusinessCode.CODE_402002, message = "自提地址为空"),
            @ApiResponse(code = BusinessCode.CODE_402003, message = "自提为空"),
            @ApiResponse(code = BusinessCode.CODE_402004, message = "商品信息为空"),
            @ApiResponse(code = BusinessCode.CODE_402006, message = "支付类型为空"),
            @ApiResponse(code = BusinessCode.CODE_402012, message = "购物车商品价格有变动")
    })
    @RequestMapping(value = "/api-order/order/432/v1/readyOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Long> readyOrder(@RequestBody ShopCarCondition condition){
        ResponseResult<Long> result = new ResponseResult<>();
        try {
            shopCarParam(condition);
            shopCarService.readyOrder(condition);
        } catch (Exception e){
            logger.error("ShopCarController -> readyOrder接口异常, 异常信息{}" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
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
        if (CollectionUtils.isEmpty(condition.getOrderItemConditions())){
            logger.error("商品加购异常{}  商品信息为空");
            throw new BusinessException(BusinessCode.CODE_402004);
        }
        if (null == condition.getPayType()){
            logger.error("商品加购异常{}  参数payType为空");
            throw new BusinessException(BusinessCode.CODE_402006);
        }
        /*if (null == condition.getCouponIds() || condition.getCouponIds().length == 0){
            logger.error("商品加购异常{}  参数couponIds为空");
            throw new BusinessException(BusinessCode.CODE_402007);
        }
        if (null == condition.getOrderTotalMoney()){
            logger.error("商品加购异常{}  参数orderTotalMoney为空");
            throw new BusinessException(BusinessCode.CODE_402008);
        }*/
    }
}
