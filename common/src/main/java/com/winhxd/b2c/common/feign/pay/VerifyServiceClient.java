package com.winhxd.b2c.common.feign.pay;

import com.winhxd.b2c.common.constant.ServiceName;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(value = ServiceName.PAY_SERVICE, fallbackFactory = VerifyServiceClientFallback.class)
public interface VerifyServiceClient {
}

@Component
class VerifyServiceClientFallback implements VerifyServiceClient, FallbackFactory<VerifyServiceClient> {

    private Throwable throwable;

    private VerifyServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public VerifyServiceClient create(Throwable throwable) {
        return new VerifyServiceClientFallback(throwable);
    }
}
