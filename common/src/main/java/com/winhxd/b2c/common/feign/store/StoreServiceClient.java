package com.winhxd.b2c.common.feign.store;

import java.util.List;
import java.util.Set;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.store.condition.StoreRegionCondition;
import com.winhxd.b2c.common.domain.store.vo.StoreRegionVO;
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
import com.winhxd.b2c.common.domain.store.condition.StoreProductStatisticsCondition;
import com.winhxd.b2c.common.domain.store.vo.ShopCartProdVO;
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
     * @return 无  (根据状态码进行判断绑定状态 1001绑定失败, 0绑定成功, 200011用户已经和当前门店存在绑定关系 ， 200003用户已经和其他门店存在绑定关系)
     * @author chengyy
     * @date 2018/8/3 10:32
     * @Description 门店绑定用户
     * @Param storeUserId  门店id主键
     */
    @RequestMapping(value = "/store/1030/v1/bindCustomer", method = RequestMethod.GET)
    ResponseResult<Void> bindCustomer(@RequestParam("customerId") Long customerId, @RequestParam("storeUserId") Long storeUserId);

    /**
     * 获取购物车内商品信息
     *
     * @param skuCodes
     * @param storeId
     * @return ResponseResult<List       <       ShopCarProdVO>>
     * @Title: findShopCarProd
     * @Description: TODO
     * @author wuyuanbao
     * @date 2018年8月6日上午9:23:34
     */
    @RequestMapping(value = "/store/1031/v1/findShopCarProd", method = RequestMethod.GET)
    ResponseResult<List<ShopCartProdVO>> findShopCarProd(@RequestParam("skuCodes") List<String> skuCodes, @RequestParam("storeId") Long storeId);

    /**
     * @param customerUserId 用户id
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/7 13:57
     * @Description 通过用户id查询绑定的门店信息
     */
    @RequestMapping(value = "/store/1033/v1/findStoreUserInfoByCustomerId/", method = RequestMethod.GET)
    ResponseResult<StoreUserInfoVO> findStoreUserInfoByCustomerId(@RequestParam("customerUserId") Long customerUserId);

    /**
     * @param id 门店id
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/7 17:58
     * @Description 根据门店id(主键)查询门店信息
     */
    @RequestMapping(value = "/store/1032/v1/findStoreUserInfo/{id}", method = RequestMethod.POST)
    ResponseResult<StoreUserInfoVO> findStoreUserInfo(@PathVariable("id") Long id);

    /**
     * @param ids 门店id
     * @return 门店信息集合
     * @author chengyy
     * @date 2018/8/8 10:10
     * @Description 根据id批量查询门店信息
     */
    @RequestMapping(value = "/store/1034/v1/findStoreUserInfoList", method = RequestMethod.POST)
    ResponseResult<List<StoreUserInfoVO>> findStoreUserInfoList(@RequestBody Set<Long> ids);

    /**
     * 更新门店商品统计信息
     * @Title: updateStoreProductStatistics
     * @Description: TODO
     * @param conditions
     * @return ResponseResult<Void>
     * @author wuyuanbao
     * @date 2018年8月9日下午4:16:00
     */
    @RequestMapping(value = "/store/1024/v1/findStoreUserInfoList", method = RequestMethod.POST)
    ResponseResult<Void> saveStoreProductStatistics(@RequestBody List<StoreProductStatisticsCondition> conditions);

    /**
     * 查询测试区域配置
     * @author: wangbaokuo
     * @date: 2018/8/10 15:26
     * @param: [conditions]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<com.winhxd.b2c.common.domain.PagedList<com.winhxd.b2c.common.domain.store.vo.StoreRegionVO>>
     */
    @RequestMapping(value = "/store/1037/v1/findStoreRegions", method = RequestMethod.POST)
    ResponseResult<PagedList<StoreRegionVO>> findStoreRegions(@RequestBody StoreRegionCondition conditions);

    /**
     * 删除测试区域配置
     * @author: wangbaokuo
     * @date: 2018/8/10 15:27
     * @param: [id]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<java.lang.Void>
     */
    @RequestMapping(value = "/store/1038/v1/removeStoreRegion", method = RequestMethod.GET)
    ResponseResult<Void> removeStoreRegion(@RequestParam("id") Long id);

    /**
     * 保存测试区域配置
     * @author: wangbaokuo
     * @date: 2018/8/10 15:27
     * @param: [conditions]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<java.lang.Void>
     */
    @RequestMapping(value = "/store/1039/v1/saveStoreRegion", method = RequestMethod.POST)
    ResponseResult<Void> saveStoreRegion(@RequestBody StoreRegionCondition conditions);

}

/**
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
    public ResponseResult<List<ShopCartProdVO>> findShopCarProd(List<String> skus, Long storeId) {
        logger.error("StoreServiceClientFallBack -> findShopCarProd报错，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<StoreUserInfoVO> findStoreUserInfoByCustomerId(Long customerUserId) {
        logger.error("StoreServiceClientFallBack -> findStoreUserInfoByCustomerId，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<StoreUserInfoVO> findStoreUserInfo(Long id) {
        logger.error("StoreServiceClientFallBack -> findStoreUserInfo，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<List<StoreUserInfoVO>> findStoreUserInfoList(Set<Long> ids) {
        logger.error("StoreServiceClientFallBack -> findStoreUserInfoList，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Void> saveStoreProductStatistics(List<StoreProductStatisticsCondition> conditions) {
        logger.error("StoreServiceClientFallBack -> saveStoreProductStatistics，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<StoreRegionVO>> findStoreRegions(StoreRegionCondition conditions) {
        logger.error("StoreServiceClientFallBack -> findStoreRegions，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Void> removeStoreRegion(Long id) {
        logger.error("StoreServiceClientFallBack -> removeStoreRegion，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Void> saveStoreRegion(StoreRegionCondition conditions) {
        logger.error("StoreServiceClientFallBack -> saveStoreRegion，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }


}
