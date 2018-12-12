package com.winhxd.b2c.common.feign.store.backstage;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.BackStageModifyStoreCondition;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreProdCondition;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreSubmitProdCondition;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreProdVO;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreSubmitProdVO;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreVO;
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
    @RequestMapping(value = "/store/1020/v1/findStoreList",method = RequestMethod.POST)
    ResponseResult<PagedList<BackStageStoreVO>> findStoreList(@RequestBody BackStageStoreInfoCondition storeCondition);

    /**
     * 获取门店详细信息
     * @auther liutong
     * @param id
     * @return
     */
    @RequestMapping(value = "/store/1035/v1/getStoreInfoById",method = RequestMethod.GET)
    ResponseResult<BackStageStoreVO> getStoreInfoById(@RequestParam("id") Long id);

    /**
     * 编辑门店信息的保存方法
     * @auther liutong
     * @param condition
     * @return
     */
    @RequestMapping(value = "/store/1036/v1/modifyStoreInfo",method = RequestMethod.POST)
    ResponseResult<Integer> modifyStoreInfo(@RequestBody BackStageModifyStoreCondition condition);

    /**
     * 更改门店regioncode
     * @param condition
     * @return
     */
    @RequestMapping(value = "/store/1041/v1/updateRegionCode",method = RequestMethod.POST)
    ResponseResult<Integer> modifyStoreInfoRegionCode(@RequestBody BackStageModifyStoreCondition condition);
    /**
     * 分页查询门店商品信息（带搜索）
    * @Title: findStoreProdManageList
    * @Description: TODO
    * @param condition
    * @return ResponseResult<PagedList<BackStageStoreProdVO>>
    * @author wuyuanbao
    * @date 2018年8月13日下午1:38:46
     */
    @RequestMapping(value = "/store/1045/v1/findStoreProdManageList",method = RequestMethod.POST)
    ResponseResult<PagedList<BackStageStoreProdVO>> findStoreProdManageList(@RequestBody BackStageStoreProdCondition condition);
    /**
     * 获取门店商品信息详情
    * @Title: findStoreProdManage
    * @Description: TODO
    * @param condition
    * @return ResponseResult<BackStageStoreProdVO>
    * @author wuyuanbao
    * @date 2018年8月13日下午1:39:35
     */
    @RequestMapping(value = "/store/1046/v1/findStoreProdManage",method = RequestMethod.POST)
    ResponseResult<BackStageStoreProdVO> findStoreProdManage(@RequestBody BackStageStoreProdCondition condition);
    /**
     * 门店商品操作（上下架删除）
    * @Title: operateStoreProdManage
    * @Description: TODO
    * @param condition
    * @return ResponseResult<Void>
    * @author wuyuanbao
    * @date 2018年8月13日下午1:40:28
     */
    @RequestMapping(value = "/store/1047/v1/operateStoreProdManage",method = RequestMethod.POST)
    ResponseResult<Void> operateStoreProdManage(@RequestBody BackStageStoreProdCondition condition);

    /**
     * 根据regionCode集合查询门店
     * @param condition
     * @return
     */
    @RequestMapping(value = "/store/1051/v1/findStoreIdList",method = RequestMethod.POST)
    ResponseResult<List<String>> findStoreIdListByRegionCodes(@RequestBody BackStageStoreInfoCondition condition);
    
    /**
     * 后台分页获取门店提报商品
    * @Title: findStoreSubmitProdList 
    * @Description: TODO 
    * @param condition
    * @return ResponseResult<PagedList<BackStageStoreSubmitProdVO>>
    * @author wuyuanbao
    * @date 2018年8月14日下午4:06:29
     */
    @RequestMapping(value = "/store/1054/v1/findStoreSubmitProdList",method = RequestMethod.POST)
    ResponseResult<PagedList<BackStageStoreSubmitProdVO>> findStoreSubmitProdList(@RequestBody BackStageStoreSubmitProdCondition condition);
    
    /**
     * 后台获取门店提报商品（单个）
    * @Title: findStoreSubmitProd 
    * @Description: TODO 
    * @param condition
    * @return ResponseResult<BackStageStoreSubmitProdVO>
    * @author wuyuanbao
    * @date 2018年8月14日下午4:07:40
     */
    @RequestMapping(value = "/store/1055/v1/findStoreSubmitProd",method = RequestMethod.POST)
    ResponseResult<BackStageStoreSubmitProdVO> findStoreSubmitProd(@RequestBody BackStageStoreSubmitProdCondition condition);
    
    /**
     * 更新门店提报商品（审核和添加商品）
    * @Title: modifyStoreSubmitProd 
    * @Description: TODO 
    * @param condition
    * @return ResponseResult<Void>
    * @author wuyuanbao
    * @date 2018年8月23日下午2:49:58
     */
    @RequestMapping(value = "/store/1068/v1/modifyStoreSubmitProd",method = RequestMethod.POST)
    ResponseResult<Void> modifyStoreSubmitProd(@RequestBody BackStageStoreSubmitProdCondition condition);
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
    public ResponseResult<PagedList<BackStageStoreVO>> findStoreList(BackStageStoreInfoCondition storeCondition) {
        logger.error("StoreServiceClientFallBack -> storeList报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<BackStageStoreVO> getStoreInfoById(Long id) {
        logger.error("StoreServiceClientFallBack -> getStoreInfoById 报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> modifyStoreInfo(BackStageModifyStoreCondition condition) {
        logger.error("StoreServiceClientFallBack -> modifyStoreInfo 报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> modifyStoreInfoRegionCode(BackStageModifyStoreCondition condition) {
        logger.error("StoreServiceClientFallBack -> modifyStoreInfoRegionCode 报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

	@Override
	public ResponseResult<PagedList<BackStageStoreProdVO>> findStoreProdManageList(
			BackStageStoreProdCondition condition) {
		logger.error("StoreServiceClientFallBack -> findStoreProdManageList 报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

    @Override
	public ResponseResult<BackStageStoreProdVO> findStoreProdManage(BackStageStoreProdCondition condition) {
		logger.error("StoreServiceClientFallBack -> findStoreProdManage 报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<Void> operateStoreProdManage(BackStageStoreProdCondition condition) {
		logger.error("StoreServiceClientFallBack -> operateStoreProdManage 报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

    @Override
    public ResponseResult<List<String>> findStoreIdListByRegionCodes(BackStageStoreInfoCondition condition) {
        logger.error("StoreServiceClientFallBack -> findStoreIdListByRegionCodes 报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

	@Override
	public ResponseResult<PagedList<BackStageStoreSubmitProdVO>> findStoreSubmitProdList(
			BackStageStoreSubmitProdCondition condition) {
		logger.error("StoreServiceClientFallBack -> findStoreSubmitProdList 报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<BackStageStoreSubmitProdVO> findStoreSubmitProd(BackStageStoreSubmitProdCondition condition) {
		logger.error("StoreServiceClientFallBack -> findStoreSubmitProd 报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

    @Override
    public ResponseResult<Void> modifyStoreSubmitProd(BackStageStoreSubmitProdCondition condition) {
        logger.error("StoreServiceClientFallBack -> modifyStoreSubmitProd 报错，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
