package com.winhxd.b2c.common.feign.store;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.*;
import com.winhxd.b2c.common.domain.store.vo.ShopCartProdVO;
import com.winhxd.b2c.common.domain.store.vo.StoreRegionVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerBindingStatusCondition;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserInfoCondition;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * @author chengyy
 * @Description: 门店服务接口
 * @date 2018/8/3 10:18
 */
@FeignClient(value = ServiceName.STORE_SERVICE, fallbackFactory = StoreServiceClientFallBack.class)
public interface StoreServiceClient {
    /**
     * @param customerId 用户id主键
     * @return 0绑定失败, 1绑定成功, - 1用户已经和当前门店存在绑定关系 ， - 2用户已经和其他门店存在绑定关系
     * @author chengyy
     * @date 2018/8/3 10:32
     * @Description 门店绑定用户
     * @Param storeUserId  门店id主键
     */
    @RequestMapping(value = "/store/1030/v1/bindCustomer", method = RequestMethod.GET)
    ResponseResult<Integer> bindCustomer(@RequestParam("customerId") Long customerId, @RequestParam("storeUserId") Long storeUserId);

    /**
     * 获取购物车内商品信息
     *
     * @param skuCodes
     * @param storeId
     * @return ResponseResult<List<ShopCarProdVO>>
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
     * @param
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/7 17:58
     * @Description 根据门店id(主键)查询门店信息
     */
    @RequestMapping(value = "/store/1032/v1/findStoreUserInfo", method = RequestMethod.GET)
    ResponseResult<StoreUserInfoVO> findStoreUserInfo(@RequestParam("id") Long id);

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
     *
     * @param conditions
     * @return ResponseResult<Void>
     * @Title: updateStoreProductStatistics
     * @Description: TODO
     * @author wuyuanbao
     * @date 2018年8月9日下午4:16:00
     */
    @RequestMapping(value = "/store/1024/v1/findStoreUserInfoList", method = RequestMethod.POST)
    ResponseResult<Void> saveStoreProductStatistics(@RequestBody List<StoreProductStatisticsCondition> conditions);

    /**
     * 查询测试区域配置
     *
     * @author: wangbaokuo
     * @date: 2018/8/10 15:26
     * @param: [conditions]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<com.winhxd.b2c.common.domain.PagedList                               <                               com.winhxd.b2c.common.domain.store.vo.StoreRegionVO>>
     */
    @RequestMapping(value = "/store/1037/v1/findStoreRegions", method = RequestMethod.POST)
    ResponseResult<PagedList<StoreRegionVO>> findStoreRegions(@RequestBody StoreRegionCondition conditions);

    /**
     * 删除测试区域配置
     *
     * @author: wangbaokuo
     * @date: 2018/8/10 15:27
     * @param: [id]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<java.lang.Void>
     */
    @RequestMapping(value = "/store/1038/v1/removeStoreRegion", method = RequestMethod.GET)
    ResponseResult<Void> removeStoreRegion(@RequestParam("id") Long id);

    /**
     * 保存测试区域配置
     *
     * @author: wangbaokuo
     * @date: 2018/8/10 15:27
     * @param: [conditions]
     * @return: com.winhxd.b2c.common.domain.ResponseResult<java.lang.Void>
     */
    @RequestMapping(value = "/store/1039/v1/saveStoreRegion", method = RequestMethod.POST)
    ResponseResult<Void> saveStoreRegion(@RequestBody StoreRegionCondition conditions);

    /**
     * @param condition 分页条件
     * @return 分页数据
     * @author chengyy
     * @date 2018/8/13 19:17
     * @Description 根据条件查询门店分页列表
     */
    @RequestMapping(value = "/store/1050/v1/queryStorePageInfo", method = RequestMethod.POST)
    ResponseResult<PagedList<StoreUserInfoVO>> queryStorePageInfo(@RequestBody BackStageStoreInfoSimpleCondition condition);

    /**
     * @param condition 条件
     * @return true 保存成功  flase 保存失败
     * @author chengyy
     * @date 2018/8/15 19:17
     * @Description 保存更新门店小程序二维码url
     */
    @RequestMapping(value = "/store/1057/v1/saveStoreCodeUrl", method = RequestMethod.POST)
    ResponseResult<Boolean> saveStoreCodeUrl(@RequestBody StoreUserInfoCondition condition);
   
    /**
     * @author liuhanning
     * @date  2018年8月19日 下午3:00:58
     * @Description 根据条件批量查询门店信息
     * @param condition
     * @return
     */
    @RequestMapping(value = "/store/1066/v1/getStoreListByKeywords", method = RequestMethod.POST)
    public ResponseResult<List<StoreUserInfoVO>> getStoreListByKeywords(@RequestBody StoreListByKeywordsCondition condition);

    /**
     * 根据门店id集合，门店状态查询绑定的用户ID
     * @param conditions
     * @return
     */
    @RequestMapping(value = "/store/1069/v1/findStoreRegions", method = RequestMethod.POST)
    ResponseResult<List<Long>> findStoreCustomerRegions(@RequestBody StoreCustomerRegionCondition conditions);

    /**
     * @Author: zhoufenglong
     * @Description: 通过用户id查询绑定的门店信息(内部服务接口)
     * @param: [customerUserId] 用户id
     * @return： com.winhxd.b2c.common.domain.ResponseResult<com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO>
     * @Date: 2018/10/12 13:18
     */
    @RequestMapping(value = "/store/1070/v1/queryStoreUserInfoByCustomerId", method = RequestMethod.GET)
    ResponseResult<StoreUserInfoVO> queryStoreUserInfoByCustomerId(@RequestParam("customerUserId") Long customerUserId);

    /**
     * 解绑
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/store/1071/v1/unBundling", method = RequestMethod.POST)
    ResponseResult<Boolean> unBundling(@RequestBody List<CustomerBindingStatusCondition> condition);

    /**
     * 换绑
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/store/1072/v1/changeBind", method = RequestMethod.POST)
    ResponseResult<Boolean> changeBind(List<CustomerBindingStatusCondition> condition);
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
    public ResponseResult<Integer> bindCustomer(Long customerId, Long storeUserId) {
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

    @Override
    public ResponseResult<PagedList<StoreUserInfoVO>> queryStorePageInfo(BackStageStoreInfoSimpleCondition condition) {
        logger.error("StoreServiceClientFallBack -> queryStorePageInfo，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Boolean> saveStoreCodeUrl(StoreUserInfoCondition condition) {
        logger.error("StoreServiceClientFallBack -> saveStoreCodeUrl，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

	@Override
	public ResponseResult<List<StoreUserInfoVO>> getStoreListByKeywords(StoreListByKeywordsCondition condition) {
		 logger.error("StoreServiceClientFallBack -> getStoreListByKeywords，错误信息为{}", throwable);
	        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

    @Override
    public ResponseResult<List<Long>> findStoreCustomerRegions(StoreCustomerRegionCondition conditions) {
        logger.error("StoreServiceClientFallBack -> findStoreCustomerRegions，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<StoreUserInfoVO> queryStoreUserInfoByCustomerId(Long customerUserId) {
        logger.error("StoreServiceClientFallBack -> queryStoreUserInfoByCustomerId，错误信息为{}", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Boolean> unBundling(List<CustomerBindingStatusCondition> condition) {
        logger.error("StoreServiceClientFallBack -> unBundling{}", throwable.getMessage());
        return new ResponseResult<Boolean>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Boolean> changeBind(List<CustomerBindingStatusCondition> condition) {
        logger.error("StoreServiceClientFallBack -> batchChangeBind{}", throwable.getMessage());
        return new ResponseResult<Boolean>(BusinessCode.CODE_1001);
    }
}
