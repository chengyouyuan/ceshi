package com.winhxd.b2c.pay.weixin.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CurrencyEnum;
import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundResponseDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayRequest;
import com.winhxd.b2c.pay.weixin.dao.PayRefundMapper;
import com.winhxd.b2c.pay.weixin.model.PayRefund;
import com.winhxd.b2c.pay.weixin.service.WXRefundService;
import com.winhxd.b2c.pay.weixin.util.DateUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 退款实现类
 * @author lizhonghua
 * @Description
 * @Date 2018年8月15日17:15:18
 */
@Service
public class WXRefundServiceImpl implements WXRefundService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WXRefundServiceImpl.class);

    @Autowired
    WXPayApi wxPayApi;

    @Autowired
    WXPayRequest wxPayRequest;
    
    @Autowired
    PayRefundMapper payRefundMapper;

    /**
     * 退款成功
     */
    private static final String SUCCESS = "SUCCESS";
    /**
     * 退款关闭
     */
    private static final String REFUNDCLOSE = "REFUNDCLOSE";
    /**
     * 退款处理中
     */
    private static final String PROCESSING = "PROCESSING";
    /**
     * 退款异常
     */
    private static final String CHANGE = "CHANGE";



    /**
     * 退款通知日期格式
     */
    private static final String WX_REFUND_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 分与元单位转换
     */
    private static final BigDecimal UNITS = new BigDecimal("100");

    @Override
    public PayRefundVO refundOrder(PayRefundCondition condition){
        //方法返参
        PayRefundVO payRefundVO = new PayRefundVO();
        try {
            //支付流水号
            String outTradeNo = condition.getOutTradeNo();
            PayRefund payRefund = payRefundMapper.selectByOutTradeNo(outTradeNo);
            Integer switchStatus = -1;
            if (payRefund != null) {
                switchStatus = new Integer(payRefund.getCallbackRefundStatus());
            }
            switch (switchStatus) {
                case 0:
                    payRefundVO = this.refunding(payRefund, condition);
                    break;
                case 1:
                    LOGGER.info("退款已完成(1)，交易流水号是："+outTradeNo);
                    this.refunded();
                    break;
                case 2:
                    LOGGER.info("退款已关闭(2)，交易流水号是："+outTradeNo);
                    this.refundAlreadyClosed();
                case 3:
                    payRefundVO = this.refundRetry(condition, payRefund);
                    break;
                case -1:
                    payRefundVO = this.refund(condition);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            LOGGER.error("退款时发生错误" + e.getMessage(), e);
            throw new BusinessException(BusinessCode.ORDER_REFUND_CLOSED);
        }

        return payRefundVO;
    }

    @Override
    public PayRefund updatePayRefundByOutTradeNo(PayRefundResponseDTO payRefundResponseDTO) throws Exception{

        String reqInfo = payRefundResponseDTO.getReqInfo();
        PayRefund payRefund = payRefundMapper.selectByOutTradeNo(payRefundResponseDTO.getOutTradeNo());
        if (payRefund.getCallbackRefundStatus() != 1){
            payRefund.setCallbackRefundId(payRefundResponseDTO.getRefundId());
            payRefund.setOutRefundNo(payRefundResponseDTO.getOutRefundNo());
            payRefund.setCallbackTotalFee(payRefundResponseDTO.getTotalFee());

            if(payRefundResponseDTO.getSettlementTotalFee() != null){
                payRefund.setCallbackSettlementTotalFee(payRefundResponseDTO.getSettlementTotalFee());
            }

            payRefund.setCallbackRefundFee(payRefundResponseDTO.getRefundFee());
            payRefund.setCallbackSettlementRefundFee(payRefundResponseDTO.getSettlementRefundFee());

            if(payRefundResponseDTO.getSuccessTime() != null){
                payRefund.setCallbackSuccessTime(DateUtil.toDate(payRefundResponseDTO.getSuccessTime(), WX_REFUND_DATE_FORMAT));
            }

            payRefund.setCallbackRefundRecvAccout(payRefundResponseDTO.getRefundRecvAccout());
            payRefund.setCallbackRefundAccount(payRefundResponseDTO.getRefundAccount());
            payRefund.setCallbackRefundRequestSource(payRefundResponseDTO.getRefundRequestSource());
            String refundStatus = payRefundResponseDTO.getRefundStatus();
            if(SUCCESS.equals(refundStatus)){
                payRefund.setCallbackRefundStatus((short)1);
            }else if(REFUNDCLOSE.equals(refundStatus)){
                payRefund.setCallbackRefundStatus((short)2);
            }else if(CHANGE.equals(refundStatus)){
                payRefund.setCallbackRefundStatus((short)3);
            }
            payRefund.setCallbackReqInfo(reqInfo);
            payRefund.setUpdated(new Date());
            payRefundMapper.updateByPrimaryKeySelective(payRefund);
        }
        
        return payRefund;
    }

    private PayRefundVO getResultVO(PayRefundResponseDTO responseDTO){
        PayRefundVO payRefundVO = new PayRefundVO();
        //返回失败
        if (PayRefundResponseDTO.FAIL.equals(responseDTO.getReturnCode())){
            payRefundVO.setStatus(false);
            payRefundVO.setErrorCodeDesc(responseDTO.getReturnMsg());
            return payRefundVO;
        }
        //返回成功，但是接受失败
        if (PayRefundResponseDTO.FAIL.equals(responseDTO.getResultCode())){
            payRefundVO.setStatus(false);
            payRefundVO.setErrorCode(responseDTO.getErrCode());
            payRefundVO.setErrorCodeDesc(responseDTO.getErrCodeDes());
            return payRefundVO;
        }
        //返回成功
        payRefundVO.setStatus(true);
        payRefundVO.setAppid(responseDTO.getAppid());
        payRefundVO.setTransactionId(responseDTO.getTransactionId());
        payRefundVO.setOutRefundNo(responseDTO.getOutRefundNo());
        payRefundVO.setRefundId(responseDTO.getRefundId());
        if(responseDTO.getSettlementRefundFee() != null){
            payRefundVO.setRefundAmount(new BigDecimal(responseDTO.getSettlementRefundFee()).divide(UNITS));
        }
        return payRefundVO;
    }

    /**
     * 组装持久对象，用于插入
     * @param condition 接口入参
     * @param payRefundDTO 微信入参
     * @param dto 微信返参
     */
    private void savePayRefund(PayRefundCondition condition,PayRefundDTO payRefundDTO, PayRefundResponseDTO dto){
        PayRefund model = new PayRefund();
        model.setAppid(payRefundDTO.getAppid());
        model.setMchId(payRefundDTO.getMchId());
        model.setNonceStr(payRefundDTO.getNonceStr());
        model.setSign(payRefundDTO.getSign());
        model.setSignType(payRefundDTO.getSignType());
        model.setTransactionId(dto.getTransactionId());
        model.setOutTradeNo(condition.getOutTradeNo());
        model.setOutRefundNo(dto.getOutRefundNo());
        model.setTotalFee(dto.getTotalFee());
        model.setTotalAmount(condition.getTotalAmount());
        model.setRefundFee(dto.getRefundFee());
        model.setRefundAmount(condition.getRefundAmount());
        model.setRefundFeeType(CurrencyEnum.CNY.getCode());
        model.setRefundDesc(condition.getRefundDesc());
        model.setNotifyUrl(payRefundDTO.getNotifyUrl());
        model.setOrderNo(condition.getOrderNo());
        model.setPayType((short)1);
        model.setCreated(new Date());
        model.setCreatedBy(condition.getCreatedBy());
        model.setCreatedByName(condition.getCreatedByName());
        model = this.fillReturnParam(dto,model);
        payRefundMapper.insert(model);
    }

    /**
     * 组装退款返回或主动查询返回的参数
     * @param responseDTO
     * @param refund
     */
    private PayRefund fillReturnParam(PayRefundResponseDTO responseDTO, PayRefund refund){
        if (PayRefundResponseDTO.SUCCESS.equals(responseDTO.getReturnCode()) && PayRefundResponseDTO.SUCCESS.equals(responseDTO.getResultCode())) {
            refund.setCallbackRefundId(responseDTO.getRefundId());
            refund.setCallbackRefundFee(responseDTO.getRefundFee());
            refund.setCallbackRefundAmount(new BigDecimal(responseDTO.getRefundFee()).divide(UNITS));
            refund.setCallbackSettlementRefundFee(responseDTO.getSettlementRefundFee());
            if (responseDTO.getSettlementRefundFee() != null){
                refund.setCallbackSettlementRefundAmount(new BigDecimal(responseDTO.getSettlementRefundFee()).divide(UNITS));
            }
            refund.setCallbackTotalFee(responseDTO.getTotalFee());
            refund.setCallbackSettlementTotalFee(responseDTO.getSettlementTotalFee());
            refund.setCallbackFeeType(responseDTO.getFeeType());
            refund.setCallbackCashFee(responseDTO.getCashFee());
            refund.setCallbackCashFeeType(responseDTO.getCashFeeType());
            refund.setCallbackCashRefundFee(responseDTO.getCashRefundFee());
            refund.setCallbackRefundStatus((short)0);
        }else {
            refund.setCallbackRefundStatus((short)3);
            if (PayRefundResponseDTO.FAIL.equals(responseDTO.getReturnCode())){
                refund.setErrorMessage(responseDTO.getReturnMsg());
            }else if (PayRefundResponseDTO.FAIL.equals(responseDTO.getResultCode())){
                refund.setErrorMessage(responseDTO.getErrCode());
                refund.setErrorCode(responseDTO.getErrCodeDes());
            }
        }
        return refund;
    }

    /**
     * 退款中
     * @param payRefund
     * @param condition
     * @return
     * @throws Exception
     */
    private PayRefundVO refunding(PayRefund payRefund, PayRefundCondition condition) throws Exception{
        PayRefundVO payRefundVO = new PayRefundVO();

        PayRefund model = new PayRefund();
        model.setId(payRefund.getId());

        //微信入参
        PayRefundDTO payRefundDTO = new PayRefundDTO();
        payRefundDTO.setOutRefundNo(payRefund.getOutRefundNo());

        Map<String, String> mapOut = wxPayApi.refundQuery(payRefundDTO);

        //获取查询到的信息
        String returnCode = mapOut.get("return_code");
        String resultCode = mapOut.get("result_code");
        //返回失败
        if (PayRefundResponseDTO.FAIL.equals(returnCode)){
            model.setErrorMessage(mapOut.get("return_msg"));

            payRefundVO.setStatus(false);
            payRefundVO.setErrorCodeDesc(mapOut.get("return_msg"));
        }else if (PayRefundResponseDTO.FAIL.equals(resultCode)){
            //返回成功，但是接收失败
            model.setErrorCode(mapOut.get("err_code"));
            model.setErrorMessage(mapOut.get("err_code_des"));

            payRefundVO.setStatus(false);
            payRefundVO.setErrorCode(mapOut.get("err_code"));
            payRefundVO.setErrorCodeDesc(mapOut.get("err_code_des"));
            return payRefundVO;
        }else {
            //返回成功
            payRefundVO.setStatus(true);
            payRefundVO.setAppid(mapOut.get("appid"));
            payRefundVO.setTransactionId(mapOut.get("mch_id"));
            payRefundVO.setOutRefundNo(mapOut.get("out_trade_no"));
            payRefundVO.setRefundId(mapOut.get("refund_id_0"));
            payRefundVO.setRefundAmount(new BigDecimal(mapOut.get("refund_fee_0")).divide(UNITS));

            String refundStatus = mapOut.get("refund_status_0");
            String refundRecvAccout0 = mapOut.get("refund_recv_accout_0");
            model.setCallbackRefundRecvAccout(refundRecvAccout0);
            if (PROCESSING.equals(refundStatus)){
                model.setCallbackRefundStatus((short)0);
            }else if(SUCCESS.equals(refundStatus)){
                model.setCallbackRefundStatus((short)1);
                String successTime0 = mapOut.get("refund_success_time_0");
                if(successTime0 != null){
                    model.setCallbackSuccessTime(DateUtil.toDate(successTime0, WX_REFUND_DATE_FORMAT));
                }
            }else if(REFUNDCLOSE.equals(refundStatus)){
                model.setCallbackRefundStatus((short)2);
            }else if(CHANGE.equals(refundStatus)){
                model.setCallbackRefundStatus((short)3);
            }

        }
        payRefundMapper.updateByPrimaryKeySelective(model);

        return payRefundVO;
    }

    /**
     * 退款完成
     * @return
     */
    private void refunded(){
        LOGGER.info("退款已完成");
        //throw new BusinessException(BusinessCode.ORDER_REFUND_FINISHED);
    }

    /**
     * 退款已关闭
     * @return
     */
    private void refundAlreadyClosed(){
        LOGGER.info("退款已关闭");
        //throw new BusinessException(BusinessCode.ORDER_REFUND_CLOSED);
    }

    /**
     * 退款
     * @param condition
     * @return
     * @throws Exception
     */
    private PayRefundVO refund(PayRefundCondition condition) throws Exception{
        //支付流水号
        String outTradeNo = condition.getOutTradeNo();
        //订单金额
        Integer totalFee = condition.getTotalAmount().multiply(UNITS).intValue();
        //退款金额
        Integer refundFee = condition.getRefundAmount().multiply(UNITS).intValue();
        //退款原因
        String refundDesc = condition.getRefundDesc();
        //微信入参
        PayRefundDTO payRefundDTO = new PayRefundDTO();
        //对微信入参进行赋值
        payRefundDTO.setOutTradeNo(outTradeNo);
        payRefundDTO.setOutRefundNo(outTradeNo);
        payRefundDTO.setTotalFee(totalFee);
        payRefundDTO.setRefundFee(refundFee);
        payRefundDTO.setRefundDesc(refundDesc);

        //调用
        PayRefundResponseDTO responseDTO = wxPayApi.refundOder(payRefundDTO);
        //向数据库持久
        this.savePayRefund(condition, payRefundDTO, responseDTO);
        PayRefundVO payRefundVO = this.getResultVO(responseDTO);
        return payRefundVO;
    }

    /**
     * 尝试再次退款
     * @param condition
     * @return
     * @throws Exception
     */
    private PayRefundVO refundRetry(PayRefundCondition condition, PayRefund payRefund) throws Exception{
        //支付流水号
        String outTradeNo = condition.getOutTradeNo();
        //订单金额
        Integer totalFee = condition.getTotalAmount().multiply(UNITS).intValue();
        //退款金额
        Integer refundFee = condition.getRefundAmount().multiply(UNITS).intValue();
        //退款原因
        String refundDesc = condition.getRefundDesc();
        //微信入参
        PayRefundDTO payRefundDTO = new PayRefundDTO();
        //对微信入参进行赋值
        payRefundDTO.setOutTradeNo(outTradeNo);
        payRefundDTO.setOutRefundNo(outTradeNo);
        payRefundDTO.setTotalFee(totalFee);
        payRefundDTO.setRefundFee(refundFee);
        payRefundDTO.setRefundDesc(refundDesc);
        //调用
        PayRefundResponseDTO responseDTO = wxPayApi.refundOder(payRefundDTO);
        //modifyPayRefund
        PayRefund refund = this.fillReturnParam(responseDTO, payRefund);
        payRefundMapper.updateByPrimaryKeySelective(refund);

        PayRefundVO payRefundVO = this.getResultVO(responseDTO);
        return payRefundVO;
    }

}
