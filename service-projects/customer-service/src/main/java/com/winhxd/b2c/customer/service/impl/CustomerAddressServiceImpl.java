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
    public int deleteByPrimaryKey(CustomerAddressSelectCondition condition) {
        //判断必填参数
        if (null == condition || null == condition.getId()) {
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        return customerAddressMapper.deleteByPrimaryKey(condition.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(CustomerAddressCondition customerAddressCondition, CustomerUser customerUser) {

        //参数校验
        addOrUpdateVerifyParam(customerAddressCondition,CustomerAddressEnum.INSERT);

        CustomerAddress customerAddress = new CustomerAddress();
        BeanUtils.copyProperties(customerAddressCondition,customerAddress);
        customerAddress.setDefaultAddress(false);
        customerAddress.setCreated(new Date());
        customerAddress.setUpdated(new Date());

        customerAddress.setCustomerId(customerUser.getCustomerId());
        return customerAddressMapper.insertSelective(customerAddress);
    }


    @Override
    public CustomerAddressVO selectByPrimaryKey(CustomerAddressSelectCondition condition) {
        //判断必填参数
        if (null == condition || null == condition.getId()) {
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        return customerAddressMapper.selectByPrimaryKey(condition.getId());
    }

    @Override
    public CustomerAddressVO selectCustomerDefaultAddress(CustomerUser customerUser) {
        CustomerAddressVO defaultAddress = null;
        CustomerAddressQueryCondition queryCondtion = new CustomerAddressQueryCondition();
        queryCondtion.setDefaultAddress(true);
        queryCondtion.setCustomerId(customerUser.getCustomerId());
        List<CustomerAddressVO> customerAddressVOS = customerAddressMapper.selectCustomerAddressByCondtion(queryCondtion);
        if (!CollectionUtils.isEmpty(customerAddressVOS)) {
            defaultAddress = customerAddressVOS.get(0);
            return defaultAddress;
        }
        List<CustomerAddressVO> customerAddressList= customerAddressMapper.selectCustomerAddressByUserId(customerUser.getCustomerId());
        if (!CollectionUtils.isEmpty(customerAddressList)) {
            defaultAddress = customerAddressList.get(0);
            for (CustomerAddressVO address :customerAddressList){
                if (address.getDefaultAddress()) {
                    defaultAddress = address;
                    break;
                }
            }
        }
        return defaultAddress;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(CustomerAddressCondition condition,CustomerUser customerUser) {
        //参数校验
        addOrUpdateVerifyParam(condition,CustomerAddressEnum.UPDATE);

        CustomerAddressVO customerAddress = customerAddressMapper.selectByPrimaryKey(condition.getId());
        if (null == customerAddress) {
            logger.error("--customerAddressService.updateByPrimaryKey C端用户收货地址不存在");
            throw new BusinessException(BusinessCode.CODE_503704);
        }
        if (null != condition.getDefaultAddress() && condition.getDefaultAddress() == true) {
            CustomerAddressQueryCondition queryCondtion = new CustomerAddressQueryCondition();
            queryCondtion.setDefaultAddress(true);
            queryCondtion.setCustomerId(customerUser.getCustomerId());
            List<CustomerAddressVO> customerAddressVOS = customerAddressMapper.selectCustomerAddressByCondtion(queryCondtion);
            if (!CollectionUtils.isEmpty(customerAddressVOS)) {
                CustomerAddressVO addressVO = customerAddressVOS.get(0);
                if (addressVO.getId().longValue() !=  condition.getId().longValue()) {
                    //将之前默认的地址改为 非默认的
                    addressVO.setDefaultAddress(false);
                    addressVO.setUpdated(new Date());
                    customerAddressMapper.updateByPrimaryKeySelective(addressVO);
                }
            }
        }
        BeanUtils.copyProperties(condition,customerAddress);
        customerAddress.setUpdated(new Date());

        return customerAddressMapper.updateByPrimaryKeySelective(customerAddress);
    }

    @Override
    public List<CustomerAddressVO> getCustomerAddressByUserId(Long userId) {
        return customerAddressMapper.selectCustomerAddressByUserId(userId);
    }

    @Override
    public List<CustomerAddressLabelVO> findCustomerAddressLabelByUserId(Long customerId) {
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
        List<CustomerAddress> customerAddressVOS = customerAddressMapper.selectCustomerAddressByLabelId(labelId, customerId);
        List<Long> list = new ArrayList<>(5);
        if (customerAddressVOS.size() > 0) {
            list = customerAddressVOS.stream().map(customerAddressVO -> customerAddressVO.getId()).collect(Collectors.toList());
        }
        customerAddressMapper.updateCustomerAddressById(list);
        //删除
        return customerAddressLabelMapper.deleteByPrimaryKey(customerAddressLabelCondition.getId());
    }



    public void addOrUpdateVerifyParam (CustomerAddressCondition condition,CustomerAddressEnum opt) {
        //判断必填参数
        if (null == condition) {
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if (CustomerAddressEnum.INSERT.equals(opt)) {
            if (null == condition.getContacterDetailAddress() ||
                    null == condition.getContacterMobile() ||
                    null == condition.getContacterName() ||
                    null == condition.getLabelId() ||
                    null == condition.getContacterRegion()) {
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if (!SecurityCheckUtil.validateMobile(condition.getContacterMobile())) {
                throw new BusinessException(BusinessCode.CODE_611109);
            }
        } else if (CustomerAddressEnum.UPDATE.equals(opt)) {
            if (null == condition.getId()) {
                throw new BusinessException(BusinessCode.CODE_1007);
            }
            if (null != condition.getContacterMobile() && !SecurityCheckUtil.validateMobile(condition.getContacterMobile())) {
                throw new BusinessException(BusinessCode.CODE_611109);
            }
        }
    }
}
