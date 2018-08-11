package com.winhxd.b2c.order.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.ReadyShopCarCondition;
import com.winhxd.b2c.common.domain.order.condition.ShopCarCondition;
import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
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
            if (null == condition || null == condition.getStoreId() || null == condition.getSkuCode()
                    || null == condition.getSkuNum()) {
                logger.error("商品加购异常{}  参数错误");
                throw new BusinessException(BusinessCode.CODE_402008);
            }
        	shopCarService.saveShopCar(condition, getCurrentCustomerId());
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
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_402001, message = "参数storeId为空")
    })
    @RequestMapping(value = "/api-order/order/431/v1/find", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<ShopCarProdInfoVO>> findShopCar(@RequestParam("storeId") Long storeId){
        ResponseResult<List<ShopCarProdInfoVO>> result = new ResponseResult<>();
        try {
            if (null == storeId || 0 == storeId) {
                logger.error("查询购物车异常{}  参数storeId为空");
                throw new BusinessException(BusinessCode.CODE_402001);
            }
            List<ShopCarProdInfoVO> data = shopCarService.findShopCar(storeId, getCurrentCustomerId());
            result.setData(data);
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
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_402008, message = "参数错误"),
            @ApiResponse(code = BusinessCode.CODE_402001, message = "门店ID为空"),
            @ApiResponse(code = BusinessCode.CODE_402002, message = "自提地址为空"),
            @ApiResponse(code = BusinessCode.CODE_402003, message = "自提为空"),
            @ApiResponse(code = BusinessCode.CODE_402004, message = "商品信息为空"),
            @ApiResponse(code = BusinessCode.CODE_402006, message = "支付类型为空"),
            @ApiResponse(code = BusinessCode.CODE_402012, message = "购物车商品价格有变动")
    })
    @RequestMapping(value = "/api-order/order/432/v1/readyOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Long> readyOrder(@RequestBody ReadyShopCarCondition condition){
        ResponseResult<Long> result = new ResponseResult<>();
        try {
            shopCarParam(condition);
            shopCarService.readyOrder(condition, getCurrentCustomerId());
        } catch (Exception e){
            logger.error("ShopCarController -> readyOrder接口异常, 异常信息{}" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
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
    @RequestMapping(value = "/api-order/order/433/v1/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Long> removeShopCar(){
        ResponseResult<Long> result = new ResponseResult<>();
        try{
            shopCarService.removeShopCar(getCurrentCustomerId());
        }catch (Exception e){
            logger.error("ShopCarController -> removeShopCar接口异常, 异常信息{}" + e.getMessage(), e);
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
