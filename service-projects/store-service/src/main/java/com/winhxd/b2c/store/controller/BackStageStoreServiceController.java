package com.winhxd.b2c.store.controller;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.backstage.store.condition.BackStageModifyStoreCondition;
import com.winhxd.b2c.common.domain.backstage.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.backstage.store.vo.BackStageStoreVO;

import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.feign.store.backstage.BackStageStoreServiceClient;
import com.winhxd.b2c.store.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by caiyulong on 2018/8/6.
 */
@RestController
public class BackStageStoreServiceController implements BackStageStoreServiceClient {

    @Autowired
    private StoreService storeService;

    @Override
    public ResponseResult<PagedList<BackStageStoreVO>> findStoreList(BackStageStoreInfoCondition storeCondition) {
        ResponseResult<PagedList<BackStageStoreVO>> responseResult = new ResponseResult<>();
        PagedList<BackStageStoreVO> storeVOPagedList = storeService.findStoreUserInfo(storeCondition);
        responseResult.setData(storeVOPagedList);
        return responseResult;
    }

    @Override
    public ResponseResult<BackStageStoreVO> getStoreInfoById(Long id) {
        ResponseResult<BackStageStoreVO> responseResult = new ResponseResult<>();
        BackStageStoreVO backStageStoreVO = storeService.findByIdForBackStage(id);
        responseResult.setData(backStageStoreVO);
        return responseResult;
    }

    @Override
    public ResponseResult<Integer> modifyStoreInfo(@RequestBody BackStageModifyStoreCondition condition) {
        StoreUserInfo storeUserInfo = new StoreUserInfo();
        BeanUtils.copyProperties(condition, storeUserInfo);
        storeService.updateByPrimaryKeySelective(storeUserInfo);
        return new ResponseResult<>();
    }

}
