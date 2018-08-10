package com.winhxd.b2c.pay.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.feign.pay.FinancialManagerServiceClient;
import com.winhxd.b2c.pay.service.impl.PayFinancialManagerServiceImpl;

import io.swagger.annotations.Api;

@RestController
@Api(tags = "PayFinancialManager")
public class PayFinancialManagerController implements FinancialManagerServiceClient{
	 private static final Logger logger = LoggerFactory.getLogger(PayFinancialManagerServiceImpl.class);
	 
	 /**出入帐汇总查询*/

}
