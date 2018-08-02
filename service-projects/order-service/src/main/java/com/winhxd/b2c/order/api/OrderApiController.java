package com.winhxd.b2c.order.api;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.model.AsyncEvent;
import com.winhxd.b2c.order.dao.AsyncEventMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api
public class OrderApiController {
    @Autowired
    private AsyncEventMapper asyncEventMapper;

    @Autowired
    private Cache cache;

    @GetMapping("/api-order/order/663/v1/getAsyncEvents")
    @ApiOperation(value = "aaaa")
    public ResponseResult<List<AsyncEvent>> getAsyncEvents() {
        PageHelper.startPage(1, 2);
        Page<AsyncEvent> all = asyncEventMapper.findAll();
        return new ResponseResult<>(all);
    }
}
