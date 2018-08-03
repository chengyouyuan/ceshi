package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.store.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Description: 用于获取门店信息
 * @author chengyy
 * @date 2018/8/3 16:03
 */
@RestController
@RequestMapping("/api/store")
public class ApiStoreController {
    private Logger logger = LoggerFactory.getLogger(ApiStoreController.class);
    @Autowired
    private StoreService storeService;
    /**
     * @author chengyy
     * @date 2018/8/3 16:04
     * @Description 获取门店信息
     * @param  storeUserId 门店id
     * @return StoreUserInfoVO 返回当前门店信息数据
     */
    @RequestMapping(value = "/2002/v1/findStoreUserInfo/{storeUserId}",method = RequestMethod.GET)
    public ResponseResult<StoreUserInfoVO> findStoreUserInfo(@PathVariable("storeUserId")Long storeUserId){
        ResponseResult<StoreUserInfoVO> result = new ResponseResult<>();
        if(storeUserId == null){
            logger.error("ApiStoreController -> findStoreUserInfo获取的参数storeUserId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        StoreUserInfoVO data = storeService.findStoreUserInfo(storeUserId);
        if(data == null){
            result.setCode(BusinessCode.CODE_200004);
        }
        result.setData(data);
        return result;
    }


}
