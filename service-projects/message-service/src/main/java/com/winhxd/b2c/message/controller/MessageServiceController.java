package com.winhxd.b2c.message.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.MessageBatchPushCondition;
import com.winhxd.b2c.common.domain.message.condition.MiniFormIdCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.model.MessageBatchPush;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.domain.message.vo.MessageBatchPushVO;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import com.winhxd.b2c.message.service.MessageBatchPushService;
import com.winhxd.b2c.message.service.MiniProgramService;
import com.winhxd.b2c.message.service.NeteaseService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        } catch (BusinessException be) {
            LOGGER.error("/message/7012/v1/getNeteaseAccountInfo,获取云信用户信息出错，异常信息为={}", be);
            result.setCode(be.getErrorCode());
        } catch (Exception e){
            LOGGER.error("/message/7012/v1/getNeteaseAccountInfo,获取云信用户信息出错，异常信息为={}",e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

	@Override
	public ResponseResult<Void> updateNeteaseAccount(@RequestBody NeteaseAccountCondition neteaseAccountCondition) {
		ResponseResult<Void> result = new ResponseResult<>();
		try {
			neteaseService.updateNeteaseAccount(neteaseAccountCondition);
		} catch (BusinessException be) {
            LOGGER.error("/message/7013/v1/updateNeteaseAccount,更新云信用户出错，异常信息为={}", be);
            result.setCode(be.getErrorCode());
        } catch (Exception e) {
			LOGGER.error("/message/7013/v1/updateNeteaseAccount,更新云信用户出错，异常信息为={}", e);
			result.setCode(BusinessCode.CODE_1001);
		}
		return result;
	}

	@Override
	public ResponseResult<MiniOpenId> getMiniOpenId(@RequestParam("code") String code) {
		ResponseResult<MiniOpenId> result = new ResponseResult<>();
		try {
			result = miniProgramService.getMiniOpenId(code);
		} catch (BusinessException be) {
            LOGGER.error("/message/7021/v1/getMiniOpenId,根据code获取openid出错，异常信息为={}", be);
            result.setCode(be.getErrorCode());
        } catch (Exception e) {
			LOGGER.error("/message/7021/v1/getMiniOpenId,根据code获取openid出错，异常信息为={}", e);
			result.setCode(BusinessCode.CODE_1001);
		}
		return result;
	}

    @Override
    public ResponseResult<Void> saveFormIds(@RequestBody MiniFormIdCondition miniFormIdCondition) {
        ResponseResult<Void> result = new ResponseResult<>();
        try {
            miniProgramService.saveFormIds(miniFormIdCondition);
        } catch (BusinessException be) {
            LOGGER.error("/message/7023/v1/saveFormIds,保存用户formid出错，异常信息为={}", be);
            result.setCode(be.getErrorCode());
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
        } catch (BusinessException be) {
            LOGGER.error("/message/7030/v1/queryMessageBatchPushPageInfo,后台查询手动推送消息列表出错，异常信息为={}", be);
            result.setCode(be.getErrorCode());
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
            Long aLong = messageBatchPushService.addBatchPush(messageBatchPush);
            result.setData(aLong);
        } catch (BusinessException be) {
            LOGGER.error("/message/7031/v1/addBatchPush,后台新增手动推送消息出错，异常信息为={}", be);
            result.setCode(be.getErrorCode());
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
        } catch (BusinessException be) {
            LOGGER.error("/message/7032/v1/modifyBatchPush,后台修改手动推送消息出错，异常信息为={}", be);
            result.setCode(be.getErrorCode());
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
        } catch (BusinessException be) {
            LOGGER.error("/message/7033/v1/getBatchPush,后台获取手动推送消息出错，异常信息为={}", be);
            result.setCode(be.getErrorCode());
        } catch (Exception e) {
            LOGGER.error("/message/7033/v1/getBatchPush,后台获取手动推送消息出错，异常信息为={}", e);
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
        } catch (BusinessException be) {
            LOGGER.error("/message/7034/v1/removeBatchPush,后台删除手动推送消息出错，异常信息为={}", be);
            result.setCode(be.getErrorCode());
        } catch (Exception e) {
            LOGGER.error("/message/7034/v1/removeBatchPush,后台删除手动推送消息出错，异常信息为={}", e);
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
        } catch (BusinessException be) {
            LOGGER.error("/message/7035/v1/batchPushMessage,后台手动推送消息出错，异常信息为={}", be);
            result.setCode(be.getErrorCode());
        } catch (Exception e) {
            LOGGER.error("/message/7035/v1/batchPushMessage,后台手动推送消息出错，异常信息为={}", e);
            result.setCode(BusinessCode.CODE_1001);
        }
        return result;
    }

}
