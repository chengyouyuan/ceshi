package com.winhxd.b2c.order.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.order.condition.ReadyShopCarCondition;
import com.winhxd.b2c.common.domain.order.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.order.condition.ShopCarQueryCondition;
import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.order.service.ShopCarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: wangbaokuo
 * @date: 2018/8/2 19:32
 */
@Api(tags = "ApiShopCarController")
@RestController
public class ApiShopCarController {

    private static final Logger logger = LoggerFactory.getLogger(ApiShopCarController.class);

    @Autowired
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
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_402008, message = "参数错误"),
            @ApiResponse(code = BusinessCode.CODE_402001, message = "门店ID为空"),
            @ApiResponse(code = BusinessCode.CODE_402002, message = "自提地址为空"),
            @ApiResponse(code = BusinessCode.CODE_402003, message = "自提为空"),
            @ApiResponse(code = BusinessCode.CODE_402004, message = "商品信息为空"),
            @ApiResponse(code = BusinessCode.CODE_402006, message = "支付类型为空"),
            @ApiResponse(code = BusinessCode.CODE_402012, message = "购物车商品价格有变动")
    })
    @RequestMapping(value = "/api-order/order/4030/v1/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Long> saveShopCar(@RequestBody ShopCarCondition condition){
        if (null == condition || null == condition.getStoreId() || null == condition.getSkuCode()
                || null == condition.getAmount()) {
            logger.error("商品加购异常{}  参数错误");
            throw new BusinessException(BusinessCode.CODE_402008);
        }
        shopCarService.saveShopCar(condition, getCurrentCustomerId());
        return new ResponseResult<>();
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
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_402001, message = "参数storeId为空")
    })
    @RequestMapping(value = "/api-order/order/4031/v1/find", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<ShopCarProdInfoVO>> findShopCar(@RequestBody ShopCarQueryCondition condition){
        ResponseResult<List<ShopCarProdInfoVO>> result = new ResponseResult<>();
        if (null == condition || null == condition.getStoreId()) {
            logger.error("查询购物车异常{}  参数storeId为空");
            throw new BusinessException(BusinessCode.CODE_402001);
        }
        List<ShopCarProdInfoVO> data = shopCarService.findShopCar(condition.getStoreId(), getCurrentCustomerId());
        result.setData(data);
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
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_402008, message = "参数错误"),
            @ApiResponse(code = BusinessCode.CODE_402001, message = "门店ID为空"),
            @ApiResponse(code = BusinessCode.CODE_402002, message = "自提地址为空"),
            @ApiResponse(code = BusinessCode.CODE_402003, message = "自提为空"),
            @ApiResponse(code = BusinessCode.CODE_402004, message = "商品信息为空"),
            @ApiResponse(code = BusinessCode.CODE_402006, message = "支付类型为空")
    })
    @RequestMapping(value = "/api-order/order/4032/v1/readyOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OrderPayVO> readyOrder(@RequestBody ReadyShopCarCondition condition){
        ResponseResult result = new ResponseResult<>();
        shopCarParam(condition);
        OrderPayVO orderPayVO = shopCarService.readyOrder(condition, getCurrentCustomerId());
        result.setData(orderPayVO);
        return result;
    }

    /**
     *  清空购物车接口
     * @author: wangbaokuo
     * @date: 2018/8/9 19:09
     * @param: [condition]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<java.lang.Long>
     */
    @ApiOperation(value = "清空购物车", notes = "清空购物车")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_402001, message = "参数storeId为空")
    })
    @RequestMapping(value = "/api-order/order/4033/v1/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Long> removeShopCar(@RequestBody ApiCondition apiCondition){
        shopCarService.removeShopCar(getCurrentCustomerId());
        return new ResponseResult<>();
    }

    /**
     * 验参
     * @author: wangbaokuo
     * @date: 2018/8/3 13:25
     * @param: [condition]
     * @return: void
     */
    private void shopCarParam(ReadyShopCarCondition condition){
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
        if (null == condition.getPayType()){
            logger.error("商品加购异常{}  参数payType为空");
            throw new BusinessException(BusinessCode.CODE_402006);
        }
    }
    /**
     *
     * @author: wangbaokuo
     * @date: 2018/8/10 10:31
     * @return: 获取用户ID
     */
    private Long getCurrentCustomerId(){
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (null == customerUser || null == customerUser.getCustomerId() || 0 == customerUser.getCustomerId()) {
            logger.error("获取当前用户信息异常{} UserContext.getCurrentCustomerUser():" + UserContext.getCurrentCustomerUser());
            throw new BusinessException(BusinessCode.CODE_1004);
        }
        return customerUser.getCustomerId();
    }
}
