package com.winhxd.b2c.customer.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressCondition;
import com.winhxd.b2c.common.domain.customer.condition.CustomerAddressLabelCondition;
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
    @Transactional
    public int deleteByPrimaryKey(CustomerAddressSelectCondition condition) {
        //判断必填参数
        if (null == condition || null == condition.getId()) {
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        return customerAddressMapper.deleteByPrimaryKey(condition.getId());
    }

    @Override
    @Transactional
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
    @Transactional
    public int updateByPrimaryKey(CustomerAddressCondition condition) {
        //参数校验
        addOrUpdateVerifyParam(condition,CustomerAddressEnum.UPDATE);

        CustomerAddressVO customerAddress = customerAddressMapper.selectByPrimaryKey(condition.getId());
        if(null == customerAddress){
            logger.error("--customerAddressService.updateByPrimaryKey C端用户收货地址不存在");
            throw new BusinessException(BusinessCode.CODE_503704);
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
    public List<String> findCustomerAddressLabelByUserId(Long customerId) {
        List<CustomerAddressLabelVO> customerAddressLabelVOS = customerAddressLabelMapper.selectCustomerAddressLabelByUserId(customerId);
        List<String> labelList = customerAddressLabelVOS.stream().map(customerAddressLabelVO -> customerAddressLabelVO.getLabelName()).collect(Collectors.toList());
        return labelList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveCustomerAddressLabel(CustomerAddressLabelCondition customerAddressLabelCondition) {
        CustomerAddressLabel customerAddressLabel = new CustomerAddressLabel();
        BeanUtils.copyProperties(customerAddressLabelCondition, customerAddressLabel);
        customerAddressLabel.setCreated(new Date());
        customerAddressLabel.setLabelType(2);
        return customerAddressLabelMapper.insertSelective(customerAddressLabel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCustomerAddressLabel(CustomerAddressLabelCondition customerAddressLabelCondition) {
        Long customerId = customerAddressLabelCondition.getCustomerId();
        Long labelId = customerAddressLabelCondition.getId();
        //通过标签id和customer_id 查询地址列表
        List<CustomerAddressVO> customerAddressVOS = customerAddressMapper.selectCustomerAddressByLabelId(labelId, customerId);

//        if (customerAddressVOS.size() > 0) {
//            customerAddressVOS
//        }
        //删除
        return customerAddressMapper.deleteByPrimaryKey(customerAddressLabelCondition.getId());

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
