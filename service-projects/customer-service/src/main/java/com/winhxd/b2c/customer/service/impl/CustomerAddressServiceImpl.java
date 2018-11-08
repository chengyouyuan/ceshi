package com.winhxd.b2c.customer.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressCondition;
import com.winhxd.b2c.common.domain.customer.enums.CustomerAddressEnum;
import com.winhxd.b2c.common.domain.customer.model.CustomerAddress;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.SecurityCheckUtil;
import com.winhxd.b2c.customer.dao.CustomerAddressMapper;
import com.winhxd.b2c.customer.service.CustomerAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    @Override
    public int deleteByPrimaryKey(Long id) {
        return customerAddressMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int insert(CustomerAddressCondition customerAddressCondition, CustomerUser customerUser) {
        //参数校验
        addOrUpdateVerifyParam(customerAddressCondition,"insert");

        CustomerAddress customerAddress = new CustomerAddress();
        BeanUtils.copyProperties(customerAddressCondition,customerAddress);
        customerAddress.setDefaultAddress(CustomerAddressEnum.NO_DDFAULT_ADDRESS.getCode());
        customerAddress.setCreated(new Date());
        customerAddress.setUpdated(new Date());

        customerAddress.setCustomerId(customerUser.getCustomerId());
        return customerAddressMapper.insertSelective(customerAddress);
    }


    @Override
    public CustomerAddress selectByPrimaryKey(Long id) {
        return customerAddressMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int updateByPrimaryKey(CustomerAddressCondition customerAddressCondition, CustomerUser customerUser) {
        //参数校验
        addOrUpdateVerifyParam(customerAddressCondition,"update");

        CustomerAddress customerAddress = customerAddressMapper.selectByPrimaryKey(customerAddressCondition.getId());
        if(null == customerAddress){
            logger.error("--customerAddressService.updateByPrimaryKey C端用户收货地址不存在");
            throw new BusinessException(BusinessCode.CODE_503704);
        }
        BeanUtils.copyProperties(customerAddressCondition,customerAddress);
        customerAddress.setUpdated(new Date());

        return customerAddressMapper.updateByPrimaryKeySelective(customerAddress);
    }


    public void addOrUpdateVerifyParam (CustomerAddressCondition condition,String opt) {
        //判断必填参数
        if (null == condition) {
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(null == condition.getContacterDetailAddress() ||
            null == condition.getContacterMobile() ||
            null == condition.getContacterName() ||
            null == condition.getLabelId() ||
            null == condition.getContacterRegion()){
                throw new BusinessException(BusinessCode.CODE_1007);
        }
        if(!SecurityCheckUtil.validateMobile(condition.getContacterMobile())){
            throw new BusinessException(BusinessCode.CODE_611109);
        }
        if("update".equals(opt) && null == condition.getId()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
    }
}
