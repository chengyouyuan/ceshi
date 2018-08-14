package com.winhxd.b2c.common.feign.pay;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.VerifyDetailListCondition;
import com.winhxd.b2c.common.domain.pay.condition.VerifySummaryListCondition;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifySummaryVO;
import feign.hystrix.FallbackFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ServiceName.PAY_SERVICE, fallbackFactory = VerifyServiceClientFallback.class)
public interface VerifyServiceClient {

    /**
     * 结算列表查询
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/pay/6091/v1/verifyList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<PagedList<VerifySummaryVO>> verifyList(VerifySummaryListCondition condition);

    /**
     * 费用明细列表查询
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/pay/6093/v1/accountingDetailList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<PagedList<VerifyDetailVO>> accountingDetailList(VerifyDetailListCondition condition);
}

@Component
class VerifyServiceClientFallback implements VerifyServiceClient, FallbackFactory<VerifyServiceClient> {

    private final Logger log = LogManager.getLogger(this.getClass());

    private Throwable e;

    public VerifyServiceClientFallback() {
    }

    private VerifyServiceClientFallback(Throwable e) {
        this.e = e;
    }

    @Override
    public VerifyServiceClient create(Throwable throwable) {
        return new VerifyServiceClientFallback(throwable);
    }

    @Override
    public ResponseResult<PagedList<VerifySummaryVO>> verifyList(VerifySummaryListCondition condition) {
        log.warn(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<VerifyDetailVO>> accountingDetailList(VerifyDetailListCondition condition) {
        log.warn(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
