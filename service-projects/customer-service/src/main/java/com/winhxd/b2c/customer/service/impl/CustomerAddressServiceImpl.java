package com.winhxd.b2c.customer.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressLabelCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressQueryCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressSelectCondition;
import com.winhxd.b2c.common.domain.customer.enums.CustomerAddressEnum;
import com.winhxd.b2c.common.domain.customer.model.CustomerAddress;
import com.winhxd.b2c.common.domain.customer.model.CustomerAddressLabel;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressLabelVO;
import com.winhxd.b2c.common.domain.customer.vo.CustomerAddressVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.SecurityCheckUtil;
import com.winhxd.b2c.customer.dao.CustomerAddressLabelMapper;
import com.winhxd.b2c.customer.dao.CustomerAddressMapper;
import com.winhxd.b2c.customer.service.CustomerAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: sunwenwu
 * @Date: 2018/11/8 14：39
 * @Description: 小程序用户收货地址服务接口类
 */
@Service
public class CustomerAddressServiceImpl implements CustomerAddressService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerAddressServiceImpl.class);

    @Autowired
    CustomerAddressMapper customerAddressMapper;
    @Autowired
    CustomerAddressLabelMapper customerAddressLabelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCustomerAddress(CustomerAddressSelectCondition condition) {
        //判断必填参数
        if (null == condition || null == condition.getId()) {
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        return customerAddressMapper.deleteByPrimaryKey(condition.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveCustomerAddress(CustomerAddressCondition customerAddressCondition, CustomerUser customerUser) {
        CustomerAddress customerAddress = new CustomerAddress();
        BeanUtils.copyProperties(customerAddressCondition,customerAddress);
        customerAddress.setDefaultAddress(true);
        customerAddress.setCreated(new Date());
        customerAddress.setUpdated(new Date());
        customerAddress.setCustomerId(customerUser.getCustomerId());
        //修改其他地址不是默认地址
        customerAddressMapper.setDefaultCustomerAddressFalse(customerUser.getCustomerId());
        return customerAddressMapper.insertSelective(customerAddress);
    }


    @Override
    public CustomerAddressVO selectCustomerAddressById(Long customerAddressId) {
        return customerAddressMapper.selectByPrimaryKey(customerAddressId);
    }

    @Override
    public CustomerAddressVO selectDefaultCustomerAddress(CustomerUser customerUser) {
        CustomerAddressVO defaultAddress = null;
        CustomerAddressQueryCondition queryCondtion = new CustomerAddressQueryCondition();
        queryCondtion.setDefaultAddress(true);
        queryCondtion.setCustomerId(customerUser.getCustomerId());
        List<CustomerAddressVO> customerAddressVOS = customerAddressMapper.selectCustomerAddressByCondtion(queryCondtion);
        if (!CollectionUtils.isEmpty(customerAddressVOS)) {
            defaultAddress = customerAddressVOS.get(0);
        } else {
            List<CustomerAddressVO> customerAddressList= customerAddressMapper.selectCustomerAddressByUserId(customerUser.getCustomerId());
            if (!CollectionUtils.isEmpty(customerAddressList)) {
                defaultAddress = customerAddressList.get(0);
            }
        }
        return defaultAddress;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCustomerAddress(CustomerAddressCondition condition,CustomerUser customerUser) {
        CustomerAddress customerAddress = new CustomerAddress();
        BeanUtils.copyProperties(condition,customerAddress);
        customerAddress.setUpdated(new Date());
        customerAddress.setDefaultAddress(true);
        //修改其他地址不是默认地址
        customerAddressMapper.setDefaultCustomerAddressFalse(customerUser.getCustomerId());
        return customerAddressMapper.updateByPrimaryKeySelectiveSupportLableNull(customerAddress);
    }

    @Override
    public int updateDefaultCustomerAddress(Long customerAddressId,CustomerUser customerUser) {
        customerAddressMapper.setDefaultCustomerAddressFalse(customerUser.getCustomerId());
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setId(customerAddressId);
        customerAddress.setDefaultAddress(true);
        return customerAddressMapper.updateByPrimaryKeySelective(customerAddress);
    }

    @Override
    public List<CustomerAddressVO> selectCustomerAddressByUserId(Long userId) {
        return customerAddressMapper.selectCustomerAddressByUserId(userId);
    }

    @Override
    public List<CustomerAddressLabelVO> selectAddressLabelByUserId(Long customerId) {
        List<CustomerAddressLabelVO> customerAddressLabelVOS = customerAddressLabelMapper.selectCustomerAddressLabelByUserId(customerId);
        return customerAddressLabelVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveCustomerAddressLabel(CustomerAddressLabelCondition customerAddressLabelCondition) {
        CustomerAddressLabel customerAddressLabel = new CustomerAddressLabel();
        BeanUtils.copyProperties(customerAddressLabelCondition, customerAddressLabel);
        customerAddressLabel.setCreated(new Date());
        customerAddressLabel.setLabelType((short) 2);
        return customerAddressLabelMapper.insertSelective(customerAddressLabel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCustomerAddressLabel(CustomerAddressLabelCondition customerAddressLabelCondition) {
        Long customerId = customerAddressLabelCondition.getCustomerId();
        Long labelId = customerAddressLabelCondition.getId();
        //通过标签id和customer_id 查询地址列表
        CustomerAddress condition = new CustomerAddress();
        condition.setCustomerId(customerId);
        condition.setLabelId(labelId);
        customerAddressMapper.updateCustomerAddressByLabel(condition);
        //删除
        return customerAddressLabelMapper.deleteByPrimaryKey(labelId);
    }

}
