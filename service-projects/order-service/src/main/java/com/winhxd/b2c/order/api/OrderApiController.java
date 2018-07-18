package com.winhxd.b2c.order.api;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.order.dao.HxdEventHistoryMapper;
import com.winhxd.b2c.order.model.HxdEventHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApiController {
    @Autowired
    private HxdEventHistoryMapper hxdEventHistoryMapper;

    @GetMapping("ss")
    public Object ss() {
        PageHelper.startPage(2, 2);
        Page<HxdEventHistory> all = hxdEventHistoryMapper.findAll();
        return all.toString();
    }
}
