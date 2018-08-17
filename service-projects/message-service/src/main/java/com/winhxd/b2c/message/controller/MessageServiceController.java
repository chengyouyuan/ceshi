package com.winhxd.b2c.message.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.*;
import com.winhxd.b2c.common.domain.message.model.MessageBatchPush;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.domain.message.vo.MessageBatchPushVO;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.message.service.MessageBatchPushService;
import com.winhxd.b2c.message.service.MiniProgramService;
import com.winhxd.b2c.message.service.NeteaseService;
import com.winhxd.b2c.message.service.SMSService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jujinbiao
 * @className MessageServiceController
 * @description
 */
@RestController
@Api(value = "消息服务内部接口", tags = "消息服务内部接口")
public class MessageServiceController implements MessageServiceClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceController.class);

	@Autowired
	NeteaseService neteaseService;

	@Autowired
	SMSService smsService;

	@Autowired
	MiniProgramService miniProgramService;

	@Autowired
	MessageBatchPushService messageBatchPushService;

    @Override
    public ResponseResult<NeteaseAccountVO> getNeteaseAccountInfo(@RequestBody NeteaseAccountCondition neteaseAccountCondition) {
        ResponseResult<NeteaseAccountVO> result = new ResponseResult<>();
        try{
            NeteaseAccountVO neteaseAccountInfo = neteaseService.getNeteaseAccountInfo(neteaseAccountCondition);
            if (neteaseAccountInfo == null) {
                result.setCode(BusinessCode.CODE_1001);
            } else {
                result.setData(neteaseAccountInfo);
            }
        }catch (Exception e){
            LOGGER.error("/message/7012/v1/getNeteaseAccountInfo,获取云信用户信息出错，异常信息为={}",e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

	@Override
	public ResponseResult<NeteaseAccountVO> createNeteaseAccount(@RequestBody NeteaseAccountCondition neteaseAccountCondition) {
		ResponseResult<NeteaseAccountVO> result = new ResponseResult<>();
		try {
			NeteaseAccountVO neteaseAccount = neteaseService.createNeteaseAccount(neteaseAccountCondition);
			if (neteaseAccount == null) {
				result.setCode(BusinessCode.CODE_1001);
			} else {
				result.setData(neteaseAccount);
			}
		} catch (Exception e) {
			LOGGER.error("/message/7013/v1/createNeteaseAccount,创建云信用户出错，异常信息为={}", e);
			result.setCode(BusinessCode.CODE_1001);
		}
		return result;
	}

	@Override
	public ResponseResult<Void> sendNeteaseMsg(@RequestBody NeteaseMsgCondition neteaseMsgCondition) {
		ResponseResult<Void> result = new ResponseResult<>();
		try {
			neteaseService.sendNeteaseMsg(neteaseMsgCondition);
		} catch (Exception e) {
			LOGGER.error("/message/7014/v1/sendNeteaseMsg,给B端用户发云信消息出错，异常信息为={}", e);
		}
		return result;
	}

	@Override
	@ApiOperation(value = "内部接口发送短信", notes = "内部接口发送短信")
	public ResponseResult<Void> sendSMS(@RequestParam("mobile") String mobile, @RequestParam("content") String content) {
		ResponseResult<Void> result = new ResponseResult<>();
		smsService.sendSMS(mobile, content);
		return result;
	}

	@Override
	public ResponseResult<MiniOpenId> getMiniOpenId(@RequestParam("code") String code) {
		ResponseResult<MiniOpenId> result = new ResponseResult<>();
		try {
			result = miniProgramService.getMiniOpenId(code);
		} catch (Exception e) {
			LOGGER.error("/message/7021/v1/getMiniOpenId,根据code获取openid出错，异常信息为={}", e);
			result.setCode(BusinessCode.CODE_1001);
		}
		return result;
	}

    @Override
    public ResponseResult<Void> sendMiniMsg(@RequestBody MiniMsgCondition miniMsgCondition) {
        ResponseResult<Void> result = new ResponseResult<>();
        try {
            result = miniProgramService.sendMiniMsg(miniMsgCondition);
        } catch (Exception e) {
            LOGGER.error("/message/7022/v1/sendMiniMsg,给C端用户推送小程序模板消息出错，异常信息为={}", e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<Void> saveFormIds(@RequestBody MiniFormIdCondition miniFormIdCondition) {
        ResponseResult<Void> result = new ResponseResult<>();
        try {
            miniProgramService.saveFormIds(miniFormIdCondition);
        } catch (Exception e) {
            LOGGER.error("/message/7023/v1/saveFormIds,保存用户formid出错，异常信息为={}", e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<PagedList<MessageBatchPushVO>> findMessageBatchPushPageInfo(@RequestBody MessageBatchPushCondition condition) {
        ResponseResult<PagedList<MessageBatchPushVO>> result = new ResponseResult<>();
        try {
            PagedList<MessageBatchPushVO> page = messageBatchPushService.findMessageBatchPushPageInfo(condition);
            result.setData(page);
        } catch (Exception e) {
            LOGGER.error("/message/7030/v1/queryMessageBatchPushPageInfo,后台查询手动推送消息列表出错，异常信息为={}", e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<Long> addBatchPush(@RequestBody MessageBatchPush messageBatchPush) {
        ResponseResult<Long> result = new ResponseResult<>(BusinessCode.CODE_OK);
        try {
            messageBatchPushService.addBatchPush(messageBatchPush);
            result.setData(messageBatchPush.getId());
        } catch (Exception e) {
            LOGGER.error("/message/7031/v1/addBatchPush,后台新增手动推送消息出错，异常信息为={}", e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<Long> modifyBatchPush(@RequestBody MessageBatchPush messageBatchPush) {
        ResponseResult<Long> result = new ResponseResult<>(BusinessCode.CODE_OK);
        try {
            messageBatchPushService.modifyBatchPush(messageBatchPush);
            result.setData(messageBatchPush.getId());
        } catch (Exception e) {
            LOGGER.error("/message/7032/v1/modifyBatchPush,后台修改手动推送消息出错，异常信息为={}", e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<MessageBatchPush> getBatchPush(@PathVariable("id") Long id) {
        ResponseResult<MessageBatchPush> result = new ResponseResult<>(BusinessCode.CODE_OK);
        try {
            MessageBatchPush batchPush = messageBatchPushService.getBatchPush(id);
            result.setData(batchPush);
        } catch (Exception e) {
            LOGGER.error("/message/7033/v1/modifyBatchPush,后台获取手动推送消息出错，异常信息为={}", e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<Long> removeBatchPush(@PathVariable("id") Long id) {
        ResponseResult<Long> result = new ResponseResult<>(BusinessCode.CODE_OK);
        try {
            messageBatchPushService.removeBatchPush(id);
            result.setData(id);
        } catch (Exception e) {
            LOGGER.error("/message/7034/v1/modifyBatchPush,后台删除手动推送消息出错，异常信息为={}", e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    public ResponseResult<Long> batchPushMessage(@PathVariable("id") Long id) {
        ResponseResult<Long> result = new ResponseResult<>(BusinessCode.CODE_OK);
        try {
            messageBatchPushService.batchPushMessage(id);
            result.setData(id);
        } catch (Exception e) {
            LOGGER.error("/message/7035/v1/batchPushMessage,后台手动推送消息出错，异常信息为={}", e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

}
