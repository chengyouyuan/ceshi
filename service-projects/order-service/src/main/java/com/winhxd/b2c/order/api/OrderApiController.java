package com.winhxd.b2c.order.api;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.domain.order.model.HxdEventHistory;
import com.winhxd.b2c.order.dao.HxdEventHistoryMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
public class OrderApiController {
    @Autowired
    private HxdEventHistoryMapper hxdEventHistoryMapper;

    @GetMapping("ss")
    @ApiOperation(value = "aaaa")
    public Object ss() {
        PageHelper.startPage(1, 2);
        Page<HxdEventHistory> all = hxdEventHistoryMapper.findAll();
        return all;
    }

    @GetMapping("api-order/ss")
    @ApiOperation(value = "aaaa")
    public Object tt() {
        return true;
    }
}
