package com.winhxd.b2c.admin.module.backStage.controller;

import com.winhxd.b2c.admin.module.system.controller.LoginController;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by caiyulong on 2018/8/4.
 */
@Api(value = "后台门店管理")
@RestController
@RequestMapping("/")
public class StoreController {

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    private static final String MODULE_NAME = "后台-门店管理";


}
