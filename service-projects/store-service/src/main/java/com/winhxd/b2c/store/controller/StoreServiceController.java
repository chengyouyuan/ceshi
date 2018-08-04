package com.winhxd.b2c.store.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.store.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 门店服务控制器
 * @author chengyy
 * @date 2018/8/3 10:46
 */
@RestController
public class StoreServiceController implements StoreServiceClient {
    @Autowired
    private StoreService storeService;
    private Logger logger = LoggerFactory.getLogger(StoreService.class);
    @Override
    public ResponseResult<Void> bindCustomer(Long customerId,Long storeUserId) {
        ResponseResult<Void> result = new ResponseResult<>();
        if(customerId == null) {
            logger.error("StoreServiceController ->bindCustomer获取的用户id参数为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (storeUserId == null) {
            logger.error("StoreServiceController -> bindCustomer获取的门店id参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        int status = storeService.bindCustomer(customerId,storeUserId);
        result.setCode(status == 1 ? BusinessCode.CODE_OK : BusinessCode.CODE_200003);
        return result;
    }
}
