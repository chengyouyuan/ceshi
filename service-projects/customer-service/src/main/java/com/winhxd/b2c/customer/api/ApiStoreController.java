package com.winhxd.b2c.customer.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "门店信息对外api接口",tags = "门店接口")
@RestController
public class ApiStoreController {
    private Logger logger = LoggerFactory.getLogger(ApiStoreController.class);
    @Autowired
    private StoreServiceClient storeServiceClient;
    /**
     * @author chengyy
     * @date 2018/8/3 16:04
     * @Description 获取门店信息
     * @param  storeUserId 门店id
     * @return StoreUserInfoVO 返回当前门店信息数据
     */
    @ApiOperation(value = "通过门店id查询门店信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_200002,message = "请求缺少参数门店id"),@ApiResponse(code = BusinessCode.CODE_OK,message = "操作成功")})
    @RequestMapping(value = "/api-store/store/2002/v1/findStoreUserInfo/{storeUserId}",method = RequestMethod.GET)
    public ResponseResult<StoreUserInfoVO> findStoreUserInfo(@PathVariable("storeUserId")Long storeUserId){
        ResponseResult<StoreUserInfoVO> result = new ResponseResult<>();
        if(storeUserId == null){
            logger.error("ApiStoreController -> findStoreUserInfo获取的参数storeUserId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        result = storeServiceClient.findStoreUserInfo(storeUserId);
        return result;
    }


}
