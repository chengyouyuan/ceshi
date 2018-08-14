package com.winhxd.b2c.admin.module.store.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreInfoSimpleCondition;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chengyy
 * @Description: 后台门店信息管理
 * @date 2018/8/13 19:57
 */
@Api(value = "后台门店信息管理", tags = "后台门店信息管理")
@RestController
@RequestMapping(value = "store/user")
public class StoreUserInfoController {

    @Autowired
    private StoreServiceClient storeServiceClient;

    @ApiOperation(value = "根据条件查询门店的分页数据信息", notes = "根据条件查询门店的分页数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询用户列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @GetMapping(value = "/findStorePageInfo")
    public ResponseResult<PagedList<StoreUserInfoVO>> findStorePageInfo(BackStageStoreInfoSimpleCondition condition) {
        ResponseResult<PagedList<StoreUserInfoVO>> responseResult = storeServiceClient.queryStorePageInfo(condition);
        return responseResult;
    }

}
