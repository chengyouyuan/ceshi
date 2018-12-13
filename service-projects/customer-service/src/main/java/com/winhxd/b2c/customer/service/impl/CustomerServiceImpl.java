package com.winhxd.b2c.customer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.customer.model.CustomerUserInfo;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.condition.BackStageCustomerInfoCondition;
import com.winhxd.b2c.customer.dao.CustomerUserInfoMapper;
import com.winhxd.b2c.customer.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chengyy
 * @Description: 小程序用户服务实现类
 * @date 2018/8/6 9:23
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerUserInfoMapper customerUserInfoMapper;


    @Autowired
    private Cache cache;

    @Override
    public PagedList<CustomerUserInfoVO> findCustomerPageInfo(BackStageCustomerInfoCondition condition) {
        PagedList<CustomerUserInfoVO> pagedList = new PagedList<>();
        if (!condition.getIsQueryAll()) {
            PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        }
        List<CustomerUserInfoVO> customers = customerUserInfoMapper.selectCustomer(condition);
        PageInfo<CustomerUserInfoVO> pageInfo = new PageInfo<>(customers);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }

    @Override
    public int modifyCustomerStatus(BackStageCustomerInfoCondition condition) {
        CustomerUserInfo record = new CustomerUserInfo();
        record.setCustomerId(condition.getCustomerId());
        record.setStatus(condition.getStatus());
        int line =  customerUserInfoMapper.updateByPrimaryKeySelective(record);
        //如果是添加黑名单则需要把token设置无效
        if(line == 1 && condition.getStatus() == 0){
            //如果加入黑名单成功直接将token值为无效
            CustomerUserInfo customerUserInfo = customerUserInfoMapper.selectByPrimaryKey(condition.getCustomerId());
            if(customerUserInfo != null){
                cache.del(CacheName.CUSTOMER_USER_INFO_TOKEN + customerUserInfo.getToken());
            }
        }
        return line;
    }

    @Override
    public List<CustomerUserInfoVO> findCustomerUserByIds(List<Long> ids) {
        return customerUserInfoMapper.selectCustomerUserByIds(ids);
    }

    @Override
    public List<CustomerUserInfoVO> findCustomerUserByPhones(List<String> phones) {
        if (phones == null || phones.size() == 0) {
            return null;
        }
        return customerUserInfoMapper.selectCustomerUserByPhones(phones);
    }

    @Override
    public CustomerUserInfoVO findCustomerByToken(String token) {
        CustomerUserInfo customerUserInfo = customerUserInfoMapper.selectCustomerByToken(token);
        if (customerUserInfo != null) {
            CustomerUserInfoVO customerUserInfoVO = new CustomerUserInfoVO();
            BeanUtils.copyProperties(customerUserInfo, customerUserInfoVO);
            return customerUserInfoVO;
        }
        return null;
    }

    @Override
    public PagedList<CustomerUserInfoVO> findAvabileCustomerPageInfo(BackStageCustomerInfoCondition condition) {
        PagedList<CustomerUserInfoVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        List<CustomerUserInfoVO> customers = customerUserInfoMapper.selectBindingCustomer(condition);
        PageInfo<CustomerUserInfoVO> pageInfo = new PageInfo<>(customers);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }


}
