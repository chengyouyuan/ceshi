package com.winhxd.b2c.common.feign.pay;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.*;
import com.winhxd.b2c.common.domain.pay.vo.VerifyDetailVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifyResultVO;
import com.winhxd.b2c.common.domain.pay.vo.VerifySummaryVO;
import feign.hystrix.FallbackFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceName.PAY_SERVICE, fallbackFactory = VerifyServiceClientFallback.class)
public interface VerifyServiceClient {

    /**
     * 订单费用记账
     *
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/pay/6081/v1/recordAccounting", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Integer> recordAccounting(@RequestParam("orderNo") String orderNo);

    /**
     * 订单费用标记入账
     *
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/pay/6082/v1/completeAccounting", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Integer> completeAccounting(@RequestParam("orderNo") String orderNo);

    /**
     * 订单费用与支付平台结算
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/pay/6083/v1/thirdPartyVerifyAccounting", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Integer> thirdPartyVerifyAccounting(ThirdPartyVerifyAccountingCondition condition);

    /**
     * 结算列表查询
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/pay/6091/v1/verifyList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<PagedList<VerifySummaryVO>> verifyList(VerifySummaryListCondition condition);

    /**
     * 结算-按汇总结算
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/pay/6092/v1/verifyBySummary", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Integer> verifyBySummary(VerifySummaryCondition condition);

    /**
     * 费用明细列表查询
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/pay/6093/v1/accountingDetailList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<PagedList<VerifyDetailVO>> accountingDetailList(VerifyDetailListCondition condition);

    /**
     * 结算-按明细结算
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/pay/6094/v1/verifyByDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Integer> verifyByDetail(VerifyDetailCondition condition);

    /**
     * 费用明细暂缓
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/pay/6095/v1/accountingDetailPause", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Integer> accountingDetailPause(VerifyDetailCondition condition);

    /**
     * 费用明细暂缓恢复
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/pay/6096/v1/accountingDetailRestore", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Integer> accountingDetailRestore(VerifyDetailCondition condition);

    /**
     * 门店提现申请列表查询
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/pay/6097/v1/storeWithdrawalsList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<VerifyResultVO> storeWithdrawalsList(PayWithdrawalsListCondition condition);

    /**
     * 批准门店提现申请
     *
     * @param condition
     * @return
     */
    @RequestMapping(value = "/pay/6098/v1/approveStoreWithdrawals", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<VerifyResultVO> approveStoreWithdrawals(ApproveStoreWithdrawalsCondition condition);
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
    public ResponseResult<Integer> recordAccounting(String orderNo) {
        log.error(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> completeAccounting(String orderNo) {
        log.error(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> thirdPartyVerifyAccounting(ThirdPartyVerifyAccountingCondition condition) {
        log.error(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<VerifySummaryVO>> verifyList(VerifySummaryListCondition condition) {
        log.error(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> verifyBySummary(VerifySummaryCondition condition) {
        log.error(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<VerifyDetailVO>> accountingDetailList(VerifyDetailListCondition condition) {
        log.error(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> verifyByDetail(VerifyDetailCondition condition) {
        log.error(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> accountingDetailPause(VerifyDetailCondition condition) {
        log.error(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> accountingDetailRestore(VerifyDetailCondition condition) {
        log.error(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<VerifyResultVO> storeWithdrawalsList(PayWithdrawalsListCondition condition) {
        log.error(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<VerifyResultVO> approveStoreWithdrawals(ApproveStoreWithdrawalsCondition condition) {
        log.error(e.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
