package com.winhxd.b2c.store.controller.backStage;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.backStage.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.backStage.store.vo.StoreVO;
import com.winhxd.b2c.common.feign.store.backStage.BackStageStoreServiceClient;
import com.winhxd.b2c.store.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by caiyulong on 2018/8/6.
 */
@RestController
public class BackStageStoreServiceController implements BackStageStoreServiceClient{

    private Logger logger = LoggerFactory.getLogger(BackStageStoreServiceController.class);

    @Autowired
    private StoreService storeService;

    @Override
    public ResponseResult<PagedList<StoreVO>> storeList(BackStageStoreInfoCondition storeCondition) {
        ResponseResult<PagedList<StoreVO>> responseResult = new ResponseResult<>();
        PagedList<StoreVO> storeVOPagedList = storeService.findStoreUserInfo(storeCondition);
        responseResult.setData(storeVOPagedList);
        return responseResult;
    }

}