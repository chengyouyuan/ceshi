package com.winhxd.b2c.store.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.store.service.StoreLoginService;

import io.swagger.annotations.Api;

/**
 * @author wufuyun
 * @date  2018年8月3日 下午3:06:09
 * @Description B端用户登录
 * @version
 */
@Api(value = "StoreLoginController Controller", tags = "B-Login")
@RestController
public class ApiStoreLoginController {
	private static final Logger logger = LoggerFactory.getLogger(ApiStoreLoginController.class);

	@Autowired
	private StoreLoginService storeLoginService;
}
