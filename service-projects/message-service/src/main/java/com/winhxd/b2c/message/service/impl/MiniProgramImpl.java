package com.winhxd.b2c.message.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.MiniFormIdCondition;
import com.winhxd.b2c.common.domain.message.model.MessageCustomerFormIds;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.message.dao.MessageCustomerFormIdsMapper;
import com.winhxd.b2c.message.dao.MessageWechatHistoryMapper;
import com.winhxd.b2c.message.service.MiniProgramService;
import com.winhxd.b2c.message.utils.MiniProgramUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jujinbiao
 * @className MiniProgramImpl
 * @description
 */
@Service
public class MiniProgramImpl implements MiniProgramService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MiniProgramImpl.class);
    private static final String RETURN_NULL = "null";
    /**过滤模拟器工具获取到的formid*/
    private static final String IGNORE_FORMID = "the formId is a mock one";

    @Autowired
    MiniProgramUtils miniProgramUtils;
    @Autowired
    MessageCustomerFormIdsMapper customerFormIdsMapper;
    @Autowired
    MessageWechatHistoryMapper messageWechatHistoryMapper;

    @Override
    public ResponseResult<MiniOpenId> getMiniOpenId(String code) {
        ResponseResult<MiniOpenId> result = new ResponseResult<>();
        if(StringUtils.isNotEmpty(code)){
            //根据code获取openid信息
            MiniOpenId miniOpenId = miniProgramUtils.oauth2GetOpenid(code);
            if (miniOpenId == null || StringUtils.isEmpty(miniOpenId.getOpenid()) || RETURN_NULL.equals(miniOpenId.getOpenid())){
                result.setData(null);
                result.setCode(BusinessCode.CODE_702101);
            }else{
                result.setData(miniOpenId);
            }
        }else{
            LOGGER.error("MiniProgramImpl -> getMiniOpenId,根据code获取小程序的openid和session_key出错，code is null，code={}",code);
            result.setData(null);
            result.setCode(BusinessCode.CODE_702102);
        }
        return result;
    }


    @Override
    public void saveFormIds(MiniFormIdCondition miniFormIdCondition) {
        CustomerUser user = UserContext.getCurrentCustomerUser();
        if (user == null || user.getCustomerId() == null || StringUtils.isEmpty(user.getOpenid())){
            LOGGER.error("MiniProgramImpl ->saveFormIds,保存formid出错，用户凭证无效");
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        if (CollectionUtils.isEmpty(miniFormIdCondition.getFormIds())){
            LOGGER.error("MiniProgramImpl ->saveFormIds,保存formid出错，formids为空");
            throw new BusinessException(BusinessCode.CODE_702401);
        }
        //批量保存C端用户formid
        List<MessageCustomerFormIds> list = new ArrayList<>();
        for (String formid:miniFormIdCondition.getFormIds()) {
            MessageCustomerFormIds customerFormIds = new MessageCustomerFormIds();
            if(!IGNORE_FORMID.equals(formid)){
                customerFormIds.setOpenid(user.getOpenid());
                customerFormIds.setFormid(formid);
                customerFormIds.setCreated(new Date());
                list.add(customerFormIds);
            }
        }
        if (CollectionUtils.isNotEmpty(list)){
            customerFormIdsMapper.insertFormIds(list);
        }
    }

}
