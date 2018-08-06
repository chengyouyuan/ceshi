package com.winhxd.b2c.common.feign.message;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.NeteaseAccountCondition;
import com.winhxd.b2c.common.domain.message.model.MiniOpenId;
import com.winhxd.b2c.common.domain.message.vo.NeteaseAccountVO;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping(value = "/message/702/v1/getNeteaseAccountInfo",method = RequestMethod.POST)
    ResponseResult<NeteaseAccountVO> getNeteaseAccountInfo(@RequestBody NeteaseAccountCondition neteaseAccountCondition);

    /**
     * @Description: 给手机号发短信
     * @param mobile 手机号
     * @param content 短信内容
     * @return
     */
    @RequestMapping(value = "/message/711/v1/sendSMS",method = RequestMethod.POST)
    ResponseResult<Void> sendSMS(@RequestParam("mobile")String mobile,@RequestParam("content")String content);

    /**
     * @Description: 小程序登录相关，根据code返回openid和
     * @param code
     * @return
     */
    @RequestMapping(value = "/message/721/v1/getMiniOpenId",method = RequestMethod.POST)
    ResponseResult<MiniOpenId> getMiniOpenId(@RequestParam("code")String code);
}

/**
 * @Description: 熔断回调
 * @author jujinbiao
 */
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
    public ResponseResult<Void> sendSMS(String mobile,String content) {
        logger.error("MessageServiceClientFallBack -> sendSMS，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<MiniOpenId> getMiniOpenId(String code) {
        logger.error("MessageServiceClientFallBack -> getMiniOpenId，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

}
