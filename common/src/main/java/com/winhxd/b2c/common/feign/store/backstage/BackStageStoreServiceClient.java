package com.winhxd.b2c.common.feign.store.backstage;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.backstage.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.backstage.store.vo.BackStageStoreVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author caiyulong
 * @date 2018/8/6
 */
@FeignClient(value = ServiceName.STORE_SERVICE,fallbackFactory = BackStageStoreServiceClientFallBack.class)
public interface BackStageStoreServiceClient {

    /**
     * 获取门店列表信息
     * @auther caiyulong
     * @param storeCondition
     * @return
     */
    @RequestMapping(value = "/store/2003/v1/storeList",method = RequestMethod.GET)
    ResponseResult<PagedList<BackStageStoreVO>> storeList(@RequestBody BackStageStoreInfoCondition storeCondition);

    /**
     * 获取门店详细信息
     * @auther liutong
     * @param id
     * @return
     */
    @RequestMapping(value = "/store/2003/v1/getStoreInfoById",method = RequestMethod.GET)
    ResponseResult<BackStageStoreVO> getStoreInfoById(@RequestParam("id") Long id);

}
/**
 * @Description: 熔断回调
 * @author caiyulong
 * @date 2018/8/6 10:43
 */
@Component
class BackStageStoreServiceClientFallBack implements BackStageStoreServiceClient, FallbackFactory<BackStageStoreServiceClient> {

    Throwable throwable;

    Logger logger = LoggerFactory.getLogger(BackStageStoreServiceClientFallBack.class);

    @Override
    public BackStageStoreServiceClient create(Throwable throwable) {
        this.throwable = throwable;
        return new BackStageStoreServiceClientFallBack();
    }

    @Override
    public ResponseResult<PagedList<BackStageStoreVO>> storeList(BackStageStoreInfoCondition storeCondition) {
        logger.error("StoreServiceClientFallBack -> storeList报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<BackStageStoreVO> getStoreInfoById(Long id) {
        logger.error("StoreServiceClientFallBack -> getStoreInfoById 报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
