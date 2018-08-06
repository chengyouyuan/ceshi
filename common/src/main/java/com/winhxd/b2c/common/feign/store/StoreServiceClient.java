package com.winhxd.b2c.common.feign.store;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.vo.LoginCheckSellMoneyVO;
import com.winhxd.b2c.common.domain.store.vo.ShopCarProdVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author chengyy
 * @Description: 门店服务接口
 * @date 2018/8/3 10:18
 */
@FeignClient(value = ServiceName.STORE_SERVICE, fallbackFactory = StoreServiceClientFallBack.class)
public interface StoreServiceClient {
    /**
     * @param customerId 用户id主键
     * @return 无
     * @author chengyy
     * @date 2018/8/3 10:32
     * @Description 门店绑定用户
     * @Param storeUserId  门店id主键
     */
    @RequestMapping(value = "/store/2001/v1/bindCustomer", method = RequestMethod.GET)
    ResponseResult<Void> bindCustomer(@RequestParam("customerId") Long customerId, @RequestParam("storeUserId") Long storeUserId);

    /**
     * 获取购物车内商品信息
     *
     * @param skus
     * @param storeId
     * @return ResponseResult<List                               <                               ShopCarProdVO>>
     * @Title: findShopCarProd
     * @Description: TODO
     * @author wuyuanbao
     * @date 2018年8月6日上午9:23:34
     */
    @RequestMapping(value = "/store/2003/v1/findShopCarProd", method = RequestMethod.GET)
    ResponseResult<List<ShopCarProdVO>> findShopCarProd(@RequestParam("skuCodes")List<String> skuCodes, @RequestParam("storeId")Long storeId);

    /**
     * B端登入时校验改门店下上架商品未设置价格信息
     *
     * @param storeId
     * @return ResponseResult<LoginCheckSellMoneyVO>
     * @Title: loginCheckSellMoney
     * @Description: TODO
     * @author wuyuanbao
     * @date 2018年8月6日下午1:40:49
     */
    @RequestMapping(value = "/store/2004/v1/findShopCarProd", method = RequestMethod.GET)
    ResponseResult<LoginCheckSellMoneyVO> loginCheckSellMoney(@RequestParam("storeId") Long storeId);


}

/**
 * @author chengyy
 * @Description: 熔断回调
 * @date 2018/8/3 10:43
 */
class StoreServiceClientFallBack implements StoreServiceClient, FallbackFactory<StoreServiceClient> {

    Throwable throwable;

    Logger logger = LoggerFactory.getLogger(StoreServiceClientFallBack.class);

    @Override
    public StoreServiceClient create(Throwable throwable) {
        this.throwable = throwable;
        return new StoreServiceClientFallBack();
    }

    @Override
    public ResponseResult<Void> bindCustomer(Long customerId, Long storeUserId) {
        logger.error("StoreServiceClientFallBack -> bindCustomer报错，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<List<ShopCarProdVO>> findShopCarProd(List<String> skus, Long storeId) {
        logger.error("StoreServiceClientFallBack -> findShopCarProd报错，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<LoginCheckSellMoneyVO> loginCheckSellMoney(Long storeId) {
        logger.error("StoreServiceClientFallBack -> loginCheckSellMoney报错，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

}
