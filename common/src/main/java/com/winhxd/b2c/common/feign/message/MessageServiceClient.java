package com.winhxd.b2c.common.feign.message;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.*;
import com.winhxd.b2c.common.domain.message.model.MessageBatchPush;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.domain.message.vo.MessageBatchPushVO;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @Description 消息服务接口
 * @author jujinbiao
 */
@FeignClient(value = ServiceName.MESSAGE_SERVICE,fallbackFactory = MessageServiceClientFallBack.class)
public interface MessageServiceClient {

    /**
     * @Description 获取云信用户信息
     * @param neteaseAccountCondition
     * @return
     */
    @RequestMapping(value = "/message/7012/v1/getNeteaseAccountInfo",method = RequestMethod.POST)
    ResponseResult<NeteaseAccountVO> getNeteaseAccountInfo(@RequestBody NeteaseAccountCondition neteaseAccountCondition);

    /**
     * @Description 创建云信用户
     * @param neteaseAccountCondition
     * @return
     */
    @RequestMapping(value = "/message/7013/v1/createNeteaseAccount",method = RequestMethod.POST)
    ResponseResult<NeteaseAccountVO> createNeteaseAccount(@RequestBody NeteaseAccountCondition neteaseAccountCondition);

    /**
     * @Description 给B端用户发云信消息
     * @param neteaseMsgCondition
     * @return
     */
    @RequestMapping(value = "/message/7014/v1/createNeteaseAccount",method = RequestMethod.POST)
    ResponseResult<Void> sendNeteaseMsg(@RequestBody NeteaseMsgCondition neteaseMsgCondition);

    /**
     * @Description: 给手机号发短信
     * @param mobile 手机号
     * @param content 短信内容
     * @return
     */
    @RequestMapping(value = "/message/7020/v1/sendSMS",method = RequestMethod.POST)
    ResponseResult<Void> sendSMS(@RequestParam("mobile")String mobile,@RequestParam("content")String content);

    /**
     * @Description: 小程序登录相关，根据code返回openid和sessionKey
     * @param code
     * @return
     */
    @RequestMapping(value = "/message/7021/v1/getMiniOpenId",method = RequestMethod.POST)
    ResponseResult<MiniOpenId> getMiniOpenId(@RequestParam("code")String code);

    /**
     * @Description: 给C端用户发小程序模板消息
     * @param miniMsgCondition
     * @return
     */
    @RequestMapping(value = "/message/7022/v1/sendMiniMsg",method = RequestMethod.POST)
    ResponseResult<Void> sendMiniMsg(@RequestBody MiniMsgCondition miniMsgCondition);

    /**
     * 保存用户formid
     * @param miniFormIdCondition
     * @return
     */
    @RequestMapping(value = "/message/7023/v1/saveFormIds",method = RequestMethod.POST)
    ResponseResult<Void> saveFormIds(@RequestBody MiniFormIdCondition miniFormIdCondition);

    /**
     * 后台消息管理，查询手动推送消息列表
     * @param condition
     * @return
     */
    @RequestMapping(value = "/message/7030/v1/findMessageBatchPushPageInfo",method = RequestMethod.POST)
    ResponseResult<PagedList<MessageBatchPushVO>> findMessageBatchPushPageInfo(@RequestBody MessageBatchPushCondition condition);

    /**
     * 后台消息管理，新增手动推送消息列表
     * @param messageBatchPush
     * @return
     */
    @RequestMapping(value = "/message/7031/v1/addBatchPush",method = RequestMethod.POST)
    ResponseResult<Long> addBatchPush(@RequestBody MessageBatchPush messageBatchPush);
    /**
     * 后台消息管理，修改手动推送消息
     * @param messageBatchPush
     * @return
     */
    @RequestMapping(value = "/message/7032/v1/modifyBatchPush",method = RequestMethod.POST)
    ResponseResult<Long> modifyBatchPush(@RequestBody MessageBatchPush messageBatchPush);
    /**
     * 后台消息管理，获取手动推送消息
     * @param id
     * @return
     */
    @RequestMapping(value = "/message/7033/v1/getBatchPush/{id}",method = RequestMethod.GET)
    ResponseResult<MessageBatchPush> getBatchPush(@PathVariable("id") Long id);
    /**
     * 后台消息管理，删除手动推送消息
     * @param id
     * @return
     */
    @RequestMapping(value = "/message/7034/v1/removeBatchPush/{id}",method = RequestMethod.GET)
    ResponseResult<Long> removeBatchPush(@PathVariable("id") Long id);

    /**
     * 后台消息管理，手动推送消息
     * @param id
     * @return
     */
    @RequestMapping(value = "/message/7035/v1/batchPushMessage/{id}",method = RequestMethod.GET)
    ResponseResult<Long> batchPushMessage(@PathVariable("id") Long id);
}

/**
 * @Description: 熔断回调
 * @author jujinbiao
 */
@Component
class MessageServiceClientFallBack implements MessageServiceClient, FallbackFactory<MessageServiceClient> {

    Throwable throwable;

    Logger logger = LoggerFactory.getLogger(MessageServiceClientFallBack.class);

    @Override
    public MessageServiceClient create(Throwable throwable) {
        this.throwable = throwable;
        return new com.winhxd.b2c.common.feign.message.MessageServiceClientFallBack();
    }

    @Override
    public ResponseResult<NeteaseAccountVO> getNeteaseAccountInfo(NeteaseAccountCondition neteaseAccountCondition) {
        logger.error("MessageServiceClientFallBack -> getNeteaseAccountInfo，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<NeteaseAccountVO> createNeteaseAccount(NeteaseAccountCondition neteaseAccountCondition) {
        logger.error("MessageServiceClientFallBack -> createNeteaseAccount，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Void> sendNeteaseMsg(NeteaseMsgCondition neteaseMsgCondition) {
        logger.error("MessageServiceClientFallBack -> sendNeteaseMsg，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Void> sendSMS(String mobile,String content) {
        logger.error("MessageServiceClientFallBack -> sendSMS，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<MiniOpenId> getMiniOpenId(String code) {
        logger.error("MessageServiceClientFallBack -> getMiniOpenId，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Void> sendMiniMsg(MiniMsgCondition miniMsgCondition) {
        logger.error("MessageServiceClientFallBack -> sendMiniMsg，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Void> saveFormIds(MiniFormIdCondition miniFormIdCondition) {
        logger.error("MessageServiceClientFallBack -> saveFormIds，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<MessageBatchPushVO>> findMessageBatchPushPageInfo(MessageBatchPushCondition condition) {
        logger.error("MessageServiceClientFallBack -> queryMessageBatchPushPageInfo，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Long> addBatchPush(MessageBatchPush messageBatchPush) {
        logger.error("MessageServiceClientFallBack -> addBatchPush，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Long> modifyBatchPush(MessageBatchPush messageBatchPush) {
        logger.error("MessageServiceClientFallBack -> modifyBatchPush，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<MessageBatchPush> getBatchPush(Long id) {
        logger.error("MessageServiceClientFallBack -> getBatchPush，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Long> removeBatchPush(Long id) {
        logger.error("MessageServiceClientFallBack -> removeBatchPush，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Long> batchPushMessage(Long id) {
        logger.error("MessageServiceClientFallBack -> batchPushMessage，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

}
