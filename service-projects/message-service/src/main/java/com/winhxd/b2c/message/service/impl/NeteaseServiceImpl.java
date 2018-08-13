package com.winhxd.b2c.message.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsg;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgCondition;
import com.winhxd.b2c.common.domain.message.model.MessageNeteaseAccount;
import com.winhxd.b2c.common.domain.message.model.MessageNeteaseHistory;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import com.winhxd.b2c.message.dao.MessageNeteaseAccountMapper;
import com.winhxd.b2c.message.dao.MessageNeteaseHistoryMapper;
import com.winhxd.b2c.message.service.NeteaseService;
import com.winhxd.b2c.message.utils.GeneratePwd;
import com.winhxd.b2c.message.utils.NeteaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author jujinbiao
 * @className NeteaseServiceImpl
 * @description
 */
@Service
public class NeteaseServiceImpl implements NeteaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NeteaseServiceImpl.class);
    private static final String SUCCESS_CODE = "200";
    private static final String ERROR_CODE = "414";
    private static final String ERROR_MSG = "not register";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_DESC = "desc";
    private static final String PARAM_MSGID = "msgid";

    @Value("${netease.accidSuffix}")
    private String accidSuffix;

    @Autowired
    MessageNeteaseAccountMapper neteaseAccountMapper;
    @Autowired
    MessageNeteaseHistoryMapper neteaseHistoryMapper;
    @Autowired
    NeteaseUtils neteaseUtils;

    @Override
    public NeteaseAccountVO getNeteaseAccountInfo(NeteaseAccountCondition neteaseAccountCondition) {
        NeteaseAccountVO result = new NeteaseAccountVO();
        Long customerId = neteaseAccountCondition.getCustomerId();
        if (customerId == null){
            return null;
        }
        //查询云信账户信息
        MessageNeteaseAccount neteaseAccount = neteaseAccountMapper.getNeteaseAccountByCustomerId(customerId);
        if(neteaseAccount != null){
            BeanUtils.copyProperties(neteaseAccount,result);
        }
        return result;
    }

    @Override
    public NeteaseAccountVO createNeteaseAccount(NeteaseAccountCondition neteaseAccountCondition) {
        NeteaseAccountVO result = new NeteaseAccountVO();
        Long customerId = neteaseAccountCondition.getCustomerId();
        if (customerId == null){
            return null;
        }
        //创建云信用户
        //从云信获取该用户的信息，判断该用户是否在云信已经存在
        String accidUinfo = "[\"customerId + accidSuffix\"]";
        Map<String, Object> userInfo = neteaseUtils.getUserInfo(accidUinfo);
        String codeMes = String.valueOf(userInfo.get(PARAM_CODE));
        String desc = String.valueOf(userInfo.get(PARAM_DESC));
        if (SUCCESS_CODE.equals(codeMes)){
            //如果云信账户存在,则更新token
            String token = GeneratePwd.generatePwd();
            String accid = customerId + accidSuffix;
            Map<String, Object> tokenMap = neteaseUtils.updateUserInfo(accid, token);
            if(SUCCESS_CODE.equals(String.valueOf(tokenMap.get(PARAM_CODE)))){
                MessageNeteaseAccount account = saveAccount(neteaseAccountCondition, customerId, token, accid);
                BeanUtils.copyProperties(account,result);
            }else{
                LOGGER.error("NeteaseServiceImpl ->createNeteaseAccount,云信账号更新失败，customerId={}",customerId);
                return null;
            }
        }else if(ERROR_CODE.equals(codeMes) && desc.indexOf(ERROR_MSG)>0){
            // 如果云信账号不存在，则创建
            String token = GeneratePwd.generatePwd();
            String accid = customerId + accidSuffix;
            Map<String, Object> createMap = neteaseUtils.createAccount(accid, token, neteaseAccountCondition.getName(), neteaseAccountCondition.getIcon());
            if (SUCCESS_CODE.equals(String.valueOf(createMap.get(PARAM_CODE)))){
                MessageNeteaseAccount account = saveAccount(neteaseAccountCondition, customerId, token, accid);
                BeanUtils.copyProperties(account,result);
            }else{
                LOGGER.error("NeteaseServiceImpl ->createNeteaseAccount,创建网易云信账号失败 customerId={}",customerId);
                return null;
            }
        }
        return result;
    }

    @Override
    public ResponseResult<Void> sendNeteaseMsg(NeteaseMsgCondition neteaseMsgCondition) {
        ResponseResult<Void> result = new ResponseResult<Void>();
        //校验参数
        int errorCode = verifyParamSend(neteaseMsgCondition);
        if (BusinessCode.CODE_OK != errorCode){
            result.setCode(errorCode);
            return result;
        }
        MessageNeteaseAccount account = neteaseAccountMapper.getNeteaseAccountByCustomerId(neteaseMsgCondition.getCustomerId());
        if(account == null){
            //云信用户不存在
            result.setCode(BusinessCode.CODE_1001);
            return result;
        }
        Map<String, Object> msgMap = neteaseUtils.sendTxtMessage2Person(account.getAccid(), neteaseMsgCondition);
        if (SUCCESS_CODE.equals(String.valueOf(msgMap.get(PARAM_CODE)))){
            //云信消息发送成功
            saveNeteaseMsgHistory(account.getAccid(),neteaseMsgCondition.getNeteaseMsg(),String.valueOf(msgMap.get(PARAM_MSGID)));
        }else{
            LOGGER.error("NeteaseServiceImpl ->sendNeteaseMsg,给B端用户发云信消息出错 neteaseMsgCondition={}",neteaseMsgCondition.getCustomerId()+","+neteaseMsgCondition.getNeteaseMsg().getMsgContent());
        }
        return result;
    }

    /**
     * 保存云信消息发送记录
     * @param accid
     * @param neteaseMsg
     */
    private void saveNeteaseMsgHistory(String accid, NeteaseMsg neteaseMsg, String msgIdServer) {
        MessageNeteaseHistory history = new MessageNeteaseHistory();
        history.setFromAccid("admmin");
        history.setToAccid(accid);
        history.setMsgType(Short.valueOf("0"));
        history.setMsgBody(neteaseMsg.getMsgContent());
        history.setExtJson(NeteaseUtils.buildExtJsonMsg(neteaseMsg));
        history.setMsgIdServer(msgIdServer);
        history.setMsgTimeStamp(new Date());
        neteaseHistoryMapper.insert(history);
    }

    /**
     * 保存云信账户信息
     * @param neteaseAccountCondition
     * @param customerId
     * @param token
     * @param accid
     * @return
     */
    private MessageNeteaseAccount saveAccount(NeteaseAccountCondition neteaseAccountCondition, Long customerId, String token, String accid) {
        MessageNeteaseAccount account = new MessageNeteaseAccount();
        account.setCustomerId(customerId);
        account.setAccid(accid);
        account.setToken(token);
        account.setIcon(neteaseAccountCondition.getIcon());
        account.setMobile(neteaseAccountCondition.getMobile());
        account.setName(neteaseAccountCondition.getName());
        account.setCreated(new Date());
        neteaseAccountMapper.insert(account);
        return account;
    }

    /**
     * 校验发送云信消息参数
     * @param netEaseCondition
     */
    private int verifyParamSend(NeteaseMsgCondition netEaseCondition) {
        if (netEaseCondition.getCustomerId() == null) {
            LOGGER.info("给B端用户发送云信消息，接口参数getCustomerId为空");
            return BusinessCode.CODE_1001;
        }
        if (netEaseCondition.getNeteaseMsg() == null) {
            LOGGER.info("给B端用户发送云信消息，,接口参数getEaseMsg为空");
            return BusinessCode.CODE_1001;
        }
        return BusinessCode.CODE_OK;
    }
}
