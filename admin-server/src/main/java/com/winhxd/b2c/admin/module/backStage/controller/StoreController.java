package com.winhxd.b2c.admin.module.backStage.controller;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.module.system.constant.Constant;
import com.winhxd.b2c.admin.module.system.controller.LoginController;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.backStage.store.condition.StoreCondition;
import com.winhxd.b2c.common.domain.backStage.store.vo.StoreVO;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by caiyulong on 2018/8/4.
 */
@Api(value = "后台门店账户管理")
@RestController
@RequestMapping("/")
public class StoreController {

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    private static final String MODULE_NAME = "后台-门店管理";

    @ApiOperation("门店账户列表")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @GetMapping(value = "/storeList")
    public ResponseResult<PagedList<StoreVO>> login(@RequestBody StoreCondition storeCondition) {
        ResponseResult<PagedList<StoreVO>> responseResult = new ResponseResult<>();

        logger.info("{} - #后台-门店##门店账户列表, 参数：storeCondition={}", MODULE_NAME, storeCondition);
        return responseResult;
    }


}
