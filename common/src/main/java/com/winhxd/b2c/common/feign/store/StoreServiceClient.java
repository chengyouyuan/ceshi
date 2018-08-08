package com.winhxd.b2c.common.feign.store;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.vo.LoginCheckSellMoneyVO;
import com.winhxd.b2c.common.domain.store.vo.ShopCarProdVO;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;

import feign.hystrix.FallbackFactory;

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
    @RequestMapping(value = "/store/1016/v1/bindCustomer", method = RequestMethod.GET)
    ResponseResult<Void> bindCustomer(@RequestParam("customerId") Long customerId, @RequestParam("storeUserId") Long storeUserId);

    /**
     * 获取购物车内商品信息
     *
     * @param skuCodes
     * @param storeId
     * @return ResponseResult<List   <   ShopCarProdVO>>
     * @Title: findShopCarProd
     * @Description: TODO
     * @author wuyuanbao
     * @date 2018年8月6日上午9:23:34
     */
    @RequestMapping(value = "/store/1017/v1/findShopCarProd", method = RequestMethod.GET)
    ResponseResult<List<ShopCarProdVO>> findShopCarProd(@RequestParam("skuCodes") List<String> skuCodes, @RequestParam("storeId") Long storeId);

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
    @RequestMapping(value = "/store/1018/v1/loginCheckSellMoney", method = RequestMethod.GET)
    ResponseResult<LoginCheckSellMoneyVO> loginCheckSellMoney(@RequestParam("storeId") Long storeId);

    /**
     * 功能描述: 统计门店商品信息
     *
     * @param: storeProdCondition
     * @return: ResponseResult
     * @auther: lvsen
     * @date: 2018/8/6 15:10
     */
    @RequestMapping(value = "/store/1019/v1/statisticsStoreProdInfo/", method = RequestMethod.GET)
    void statisticsStoreProdInfo(@RequestBody StoreProductManageCondition condition);

    /**
     * @param customerUserId 用户id
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/7 13:57
     * @Description 通过用户id查询绑定的门店信息
     */
    @RequestMapping(value = "/store/1020/v1/findStoreUserInfoByCustomerId/", method = RequestMethod.GET)
    ResponseResult<StoreUserInfo> findStoreUserInfoByCustomerId(@RequestParam("customerUserId") Long customerUserId);

    /**
     * @param id 门店id
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/7 17:58
     * @Description 根据门店id(主键)查询门店信息
     */
    @RequestMapping(value = "/store/1021/v1/findStoreUserInfo/{id}", method = RequestMethod.POST)
    ResponseResult<StoreUserInfoVO> findStoreUserInfo(@PathVariable("id") Long id);
}

/**
 * /**
 *
 * @author chengyy
 * @Description: 熔断回调
 * @date 2018/8/3 10:43
 */
@Component
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

    @Override
    public void statisticsStoreProdInfo(StoreProductManageCondition condition) {
        logger.error("StoreServiceClientFallBack -> statisticsStoreProdInfo，错误信息为{}", throwable);
    }

    @Override
    public ResponseResult<StoreUserInfo> findStoreUserInfoByCustomerId(Long customerUserId) {
        logger.error("StoreServiceClientFallBack -> findStoreUserInfoByCustomerId，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<StoreUserInfoVO> findStoreUserInfo(Long id) {
        logger.error("StoreServiceClientFallBack -> findStoreUserInfo，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

}
