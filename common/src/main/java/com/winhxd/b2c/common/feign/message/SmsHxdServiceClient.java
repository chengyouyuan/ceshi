package com.winhxd.b2c.common.feign.message;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.SMSCondition;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: Louis
 * @Date: 2018/10/11 17:53
 * @Description: 调用惠下单短信服务
 */
@FeignClient(value = ServiceName.SMS_SERVICE, fallbackFactory = SmsHxdServiceClientFallBack.class)
public interface SmsHxdServiceClient {

    /**
     * 功能描述:惠下单发送短信服务接口
     * @param smsCondition
     * @return
     * @auther Louis
     * @date 2018/10/11 17:55
     */
    @RequestMapping(value = "sendSmsAsyncByCondition", method = RequestMethod.POST)
    ResponseResult sendSmsAsyncByCondition(@RequestBody SMSCondition smsCondition);

}

@Component
class SmsHxdServiceClientFallBack implements SmsHxdServiceClient, FallbackFactory<SmsHxdServiceClient> {

    Throwable throwable;

    Logger logger = LoggerFactory.getLogger(SmsHxdServiceClientFallBack.class);

    @Override
    public SmsHxdServiceClient create(Throwable throwable) {
        this.throwable = throwable;
        return new SmsHxdServiceClientFallBack();
    }

    @Override
    public ResponseResult sendSmsAsyncByCondition(SMSCondition smsCondition) {
        logger.error("惠下单发送短信服务 SmsHxdServiceClientFallBack -> sendSmsAsyncByCondition，错误信息为{}", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }
}