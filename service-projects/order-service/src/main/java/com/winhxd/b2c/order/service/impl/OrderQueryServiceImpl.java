package com.winhxd.b2c.order.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.order.condition.OrderListCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.order.dao.OrderInfoMapper;
import com.winhxd.b2c.order.service.OrderQueryService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pangjianhua
 * @date 2018/8/3 15:02
 */
public class OrderQueryServiceImpl implements OrderQueryService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    /**
     * 根据用户ID查询所有订单
     *
     * @param condition
     * @return
     */
    @Override
    public PagedList<OrderInfoDetailVO> findOrderByCustomerId(OrderListCondition condition) {
        Long customerId = 1L;
        Page page = PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        PagedList<OrderInfoDetailVO> pagedList = new PagedList();
        pagedList.setData(this.orderInfoMapper.selectOrderInfoListByCustomerId(customerId));
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }
}
