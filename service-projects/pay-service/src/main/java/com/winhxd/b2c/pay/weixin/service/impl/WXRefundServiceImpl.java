package com.winhxd.b2c.pay.weixin.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.Currency;
import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundResponseDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.*;
import com.winhxd.b2c.pay.weixin.dao.PayRefundMapper;
import com.winhxd.b2c.pay.weixin.model.PayRefund;
import com.winhxd.b2c.pay.weixin.service.WXRefundService;
import com.winhxd.b2c.pay.weixin.util.BeanAndXmlUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author lizhonghua
 * @Description
 * @Date 2018年8月15日17:15:18
 */
@Service
public class WXRefundServiceImpl implements WXRefundService {

    @Autowired
    WXPay wxPay;
    @Autowired
    WXPayApi wxPayApi;

    @Autowired
    WXPayRequest wxPayRequest;
    
    @Autowired
    PayRefundMapper payRefundMapper;

    /**
     * 分与元单位转换
     */
    private static final BigDecimal UNITS = new BigDecimal("100");
    /**
     * 10分钟毫秒数=10*60*1000
     */
    private static final int TEN_MILLS = 600000;

    /**
     * 0退款中 1退款成功 2退款关闭 3退款异常
     */
    private static final String REFUND_STATUS_0 = "0";
    private static final String REFUND_STATUS_1 = "1";
    private static final String REFUND_STATUS_2 = "2";
    private static final String REFUND_STATUS_3 = "3";

    @Override
    public PayRefundVO refundOrder(PayRefundCondition condition){
        //方法返参
        PayRefundVO payRefundVO = new PayRefundVO();
        try {
            //支付流水号
            String outTradeNo = condition.getOutTradeNo();
            PayRefund payRefund = payRefundMapper.selectByOutRefundNo(outTradeNo);
            Integer switchStatus = -1;
            if (payRefund != null) {
                switchStatus = new Integer(payRefund.getCallbackRefundStatus());
            }
            switch (switchStatus) {
                case 0:
                    payRefundVO = this.refunding(payRefund, condition);
                    break;
                case 1:
                    this.refunded();
                    break;
                case 2:
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
            throw new BusinessException(BusinessCode.ORDER_REFUND_CLOSED);
        }

        return payRefundVO;
    }

    @Override
    public PayRefund updatePayRefundByOutTradeNo(PayRefundResponseDTO payRefundResponseDTO) throws Exception{

        String reqInfo = payRefundResponseDTO.getReqInfo();
        PayRefund payRefund = payRefundMapper.selectByOutRefundNo(payRefundResponseDTO.getOutTradeNo());
        if (payRefund.getCallbackRefundStatus() != 1){
            payRefund.setCallbackRefundId(payRefundResponseDTO.getRefundId());
            payRefund.setOutRefundNo(payRefundResponseDTO.getOutRefundNo());
            payRefund.setCallbackTotalFee(payRefundResponseDTO.getTotalFee());
            payRefund.setCallbackSettlementTotalFee(payRefundResponseDTO.getSettlementTotalFee());
            payRefund.setCallbackRefundFee(payRefundResponseDTO.getRefundFee());
            payRefund.setCallbackSettlementRefundFee(payRefundResponseDTO.getSettlementRefundFee());

            DateFormat bf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            payRefund.setCallbackSuccessTime(bf.parse(payRefundResponseDTO.getSuccessTime()));
            payRefund.setCallbackRefundRecvAccout(payRefundResponseDTO.getRefundRecvAccout());
            payRefund.setCallbackRefundAccount(payRefundResponseDTO.getRefundAccount());
            payRefund.setCallbackRefundRequestSource(payRefundResponseDTO.getRefundRequestSource());
            String refundStatus = payRefundResponseDTO.getRefundStatus();
            if("SUCCESS".equals(refundStatus)){
                payRefund.setCallbackRefundStatus((short)1);
            }else if("REFUNDCLOSE".equals(refundStatus)){
                payRefund.setCallbackRefundStatus((short)2);
            }else if("CHANGE".equals(refundStatus)){
                payRefund.setCallbackRefundStatus((short)3);
            }
            payRefund.setCallbackReqInfo(reqInfo);
            payRefundMapper.updateByPrimaryKeySelective(payRefund);
        }
        
        return payRefund;
    }

    /**
     * 调用微信退款API接口
     * @param condition
     * @return
     * @throws Exception
     */
    private PayRefundResponseDTO callRefund(PayRefundCondition condition) throws Exception{
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
        return responseDTO;
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
        payRefundVO.setRefundAmount(new BigDecimal(responseDTO.getSettlementRefundFee()).divide(UNITS));
        return payRefundVO;
    }

    /**
     * 组装持久对象，用于插入
     * @param condition
     * @param dto
     */
    private void savePayRefund(PayRefundCondition condition, PayRefundResponseDTO dto){
        PayRefund model = new PayRefund();
        model.setAppid(dto.getAppid());
        model.setMchId(dto.getMchId());
        model.setNonceStr(dto.getNonceStr());
        model.setSign(dto.getSign());
        model.setSignType(WXPayConstants.MD5);
        model.setTransactionId(dto.getTransactionId());
        model.setOutTradeNo(dto.getOutTradeNo());
        model.setOutRefundNo(dto.getOutRefundNo());
        model.setTotalFee(dto.getTotalFee());
        model.setTotalAmount(condition.getTotalAmount());
        model.setRefundFee(dto.getRefundFee());
        model.setRefundAmount(condition.getRefundAmount());
        model.setRefundFeeType(Currency.CNY.getCode());
        model.setRefundDesc(condition.getRefundDesc());
        //model.setRefundAccount();
        //model.setNotifyUrl();
        model.setOrderNo(condition.getOrderNo());
        model.setPayType((short)1);
        model.setCreated(new Date());
        model.setCreatedBy(condition.getCreatedBy());
        model.setCreatedByName(condition.getCreatedByName());
        if (PayRefundResponseDTO.SUCCESS.equals(dto.getReturnCode()) && PayRefundResponseDTO.SUCCESS.equals(dto.getResultCode())) {
            model.setCallbackRefundId(dto.getRefundId());
            model.setCallbackRefundFee(dto.getRefundFee());
            model.setCallbackRefundAmount(new BigDecimal(dto.getRefundFee()).divide(UNITS));
            model.setCallbackSettlementRefundFee(dto.getSettlementRefundFee());
            model.setCallbackSettlementRefundAmount(new BigDecimal(dto.getSettlementRefundFee()).divide(UNITS));
            model.setCallbackTotalFee(dto.getTotalFee());
            model.setCallbackSettlementTotalFee(dto.getSettlementTotalFee());
            model.setCallbackFeeType(dto.getFeeType());
            model.setCallbackCashFee(dto.getCashFee());
            model.setCallbackCashFeeType(dto.getCashFeeType());
            model.setCallbackCashRefundFee(dto.getCashRefundFee());
            model.setCallbackRefundStatus((short)0);
        }else {
            model.setCallbackRefundStatus((short)3);
            if (PayRefundResponseDTO.FAIL.equals(dto.getReturnCode())){
                model.setErrorMessage(dto.getReturnMsg());
            }else if (PayRefundResponseDTO.FAIL.equals(dto.getResultCode())){
                model.setErrorMessage(dto.getErrCode());
                model.setErrorCode(dto.getErrCodeDes());
            }
        }
        payRefundMapper.insert(model);
    }

    /**
     * 组装持久对象，用于更新
     * @param dto
     * @param model
     */
    private void modifyPayRefund(PayRefundResponseDTO dto, PayRefund model){
        if (PayRefundResponseDTO.SUCCESS.equals(dto.getReturnCode()) && PayRefundResponseDTO.SUCCESS.equals(dto.getResultCode())) {
            model.setCallbackRefundId(dto.getRefundId());
            model.setCallbackRefundFee(dto.getRefundFee());
            model.setCallbackRefundAmount(new BigDecimal(dto.getRefundFee()).divide(UNITS));
            model.setCallbackSettlementRefundFee(dto.getSettlementRefundFee());
            model.setCallbackSettlementRefundAmount(new BigDecimal(dto.getSettlementRefundFee()).divide(UNITS));
            model.setCallbackTotalFee(dto.getTotalFee());
            model.setCallbackSettlementTotalFee(dto.getSettlementTotalFee());
            model.setCallbackFeeType(dto.getFeeType());
            model.setCallbackCashFee(dto.getCashFee());
            model.setCallbackCashFeeType(dto.getCashFeeType());
            model.setCallbackCashRefundFee(dto.getCashRefundFee());
            model.setCallbackRefundStatus((short)0);
        }else {
            model.setCallbackRefundStatus((short)3);
            if (PayRefundResponseDTO.FAIL.equals(dto.getReturnCode())){
                model.setErrorMessage(dto.getReturnMsg());
            }else if (PayRefundResponseDTO.FAIL.equals(dto.getResultCode())){
                model.setErrorMessage(dto.getErrCode());
                model.setErrorCode(dto.getErrCodeDes());
            }
        }
        payRefundMapper.updateByPrimaryKeySelective(model);
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

        //bean转map
        Map<String, String> mapIn = BeanAndXmlUtil.beanToSortedMap(payRefundDTO);
        Map<String, String> mapOut = wxPay.refundQuery(mapIn);

        //获取查询到的信息
        String returnCode = mapOut.get("return_code");
        //返回失败
        if (PayRefundResponseDTO.FAIL.equals(returnCode)){
            model.setErrorMessage(mapOut.get("return_msg"));

            payRefundVO.setStatus(false);
            payRefundVO.setErrorCodeDesc(mapOut.get("return_msg"));
        }else if (PayRefundResponseDTO.FAIL.equals(mapOut.get("result_code"))){
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
            if ("PROCESSING".equals(refundStatus)){
                model.setCallbackRefundStatus((short)0);
            }else if("SUCCESS".equals(refundStatus)){
                model.setCallbackRefundStatus((short)1);
                DateFormat bf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                model.setCallbackSuccessTime(bf.parse(mapOut.get("refund_success_time_0")));
            }else if("REFUNDCLOSE".equals(refundStatus)){
                model.setCallbackRefundStatus((short)2);
            }else if("CHANGE".equals(refundStatus)){
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
        throw new BusinessException(BusinessCode.ORDER_REFUND_FINISHED);
    }

    /**
     * 退款已关闭
     * @return
     */
    private void refundAlreadyClosed(){
        throw new BusinessException(BusinessCode.ORDER_REFUND_CLOSED);
    }

    /**
     * 退款
     * @param condition
     * @return
     * @throws Exception
     */
    private PayRefundVO refund(PayRefundCondition condition) throws Exception{
        PayRefundResponseDTO responseDTO = this.callRefund(condition);
        //向数据库持久
        this.savePayRefund(condition, responseDTO);
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
        PayRefundResponseDTO responseDTO = this.callRefund(condition);
        //modifyPayRefund
        this.modifyPayRefund(responseDTO, payRefund);
        PayRefundVO payRefundVO = this.getResultVO(responseDTO);
        return payRefundVO;
    }

    private String test(PayRefundDTO payRefundDTO){
        try {
            //String domain = WXPayConstants.DOMAIN_API;
            String refundUrlSuffix = WXPayConstants.REFUND_URL_SUFFIX;
            //根据订单流水号查询相关信息
            String outTradeNo = payRefundDTO.getOutTradeNo();

            SortedMap<String,String> parameters = new TreeMap<String,String>();
            parameters.put("appid", payRefundDTO.getAppid());
            parameters.put("mch_id", payRefundDTO.getMchId());
            String noncestr = WXPayUtil.generateNonceStr();
            parameters.put("nonce_str", noncestr);
            //  以下二选一
            //parameters.put("out_trade_no", out_trade_no);
            //parameters.put("transaction_id", payRefundDTO.getTransactionId());
            // 退款单号
            parameters.put("out_refund_no", payRefundDTO.getOutTradeNo());
            parameters.put("fee_type", "CNY");
            parameters.put("total_fee", payRefundDTO.getTotalFee().toString());
            parameters.put("refund_fee", payRefundDTO.getTotalFee().toString());
            parameters.put("notify_url", "");

            String xml = WXPayUtil.generateSignedXml(parameters,"");

            //组装微信退款数据
            PayRefund payRefund = new PayRefund();
            payRefund.setAppid(payRefundDTO.getAppid());
            payRefund.setMchId(payRefundDTO.getMchId());
            payRefund.setNonceStr(noncestr);
            payRefund.setSign(parameters.get("sign"));
            payRefund.setSignType(WXPayConstants.SignType.MD5.toString());
            //payRefund.setTransactionId(payRefundDTO.getTransactionId());
            payRefund.setOutTradeNo(payRefundDTO.getOutTradeNo());
            payRefund.setOutRefundNo(payRefundDTO.getOutTradeNo());
            payRefund.setTotalFee(payRefundDTO.getTotalFee());
            payRefund.setTotalAmount(new BigDecimal(payRefundDTO.getTotalFee()/100));
            payRefund.setRefundFee(payRefundDTO.getRefundFee());
            payRefund.setRefundAmount(new BigDecimal(payRefundDTO.getRefundFee()/100));
            payRefund.setRefundFeeType(Currency.CNY.getCode());
            payRefund.setRefundDesc("");
            payRefund.setRefundAccount("");
            payRefund.setNotifyUrl("");
            payRefund.setOrderNo("");
            payRefund.setPayType((short)1);
            payRefund.setCreated(new Date());
            payRefund.setCreatedBy(null);
            payRefund.setCreatedByName("");
            payRefundMapper.insertSelective(payRefund);
            //============================
            PayRefund payRefundCallback = new PayRefund();
            //ID
            payRefundCallback.setId(payRefund.getId());
            //微信退款单号
            payRefundCallback.setCallbackRefundId("");
            //退款金额：分
            payRefundCallback.setCallbackRefundFee(0);
            //退款金额：元
            payRefundCallback.setCallbackRefundAmount(new BigDecimal(0));
            //应结退款总金额：分
            payRefundCallback.setCallbackSettlementRefundFee(0);
            //应结退款总金额：元
            payRefundCallback.setCallbackSettlementRefundAmount(new BigDecimal(0));
            //订单总金额：分
            payRefundCallback.setCallbackTotalFee(0);
            //应结订单总金额
            payRefundCallback.setCallbackSettlementTotalFee(0);
            //退款货币种类
            payRefundCallback.setCallbackFeeType("");
            //现金支付金额
            payRefundCallback.setCallbackCashFee(0);
            //现金支付类型
            payRefundCallback.setCallbackCashFeeType("");
            //现金退款金额
            payRefundCallback.setCallbackCashRefundFee(0);
            //退款状态
            payRefundCallback.setCallbackRefundStatus((short) 0);
            //退款状态描述
            payRefundCallback.setErrorCode("");
            payRefundCallback.setErrorMessage("");
            payRefundCallback.setCallbackSuccessTime(new Date());
            payRefundCallback.setCallbackRefundRecvAccout("");
            payRefundCallback.setCallbackRefundAccount("");
            payRefundCallback.setCallbackRefundRequestSource("");
            //修改
            payRefundCallback.setUpdated(new Date());
            

            String entityString = wxPayRequest.requestWithCert(refundUrlSuffix,"",xml,true);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(entityString);
            Element rootElt = document.getRootElement();
            System.out.println("根节点：" + rootElt.getName());
            System.out.println("==="+rootElt.elementText("result_code"));
            System.out.println("==="+rootElt.elementText("return_msg"));
            String resultCode = rootElt.elementText("result_code");

        }catch(Exception e){
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("status","error");
            result.put("msg",e.getMessage());
        }
        return null;
    }

    @Override
    public PayRefundDTO refundQuery(PayRefundCondition payRefund) {
        try {
            String refundqueryUrlSuffix = WXPayConstants.REFUNDQUERY_URL_SUFFIX;
            SortedMap<String,String> parameters = new TreeMap<String,String>();
            //parameters.put("mch_id", payRefund.getMchId());
            parameters.put("nonce_str", WXPayUtil.generateNonceStr());
            //以下二选一
            parameters.put("out_trade_no", payRefund.getOutTradeNo());
            //parameters.put("transaction_id", payRefund.getTransactionId());
            //TODO 退款单号
            parameters.put("out_refund_no", payRefund.getOutTradeNo());
            parameters.put("fee_type", "CNY");
            //parameters.put("total_fee", payRefund.getTotalFee().toString());
            //parameters.put("refund_fee", payRefund.getTotalFee().toString());

            String xml = WXPayUtil.generateSignedXml(parameters,"");
            wxPayRequest.requestWithoutCert(refundqueryUrlSuffix,"",xml,true);
        }catch(Exception e){
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("status","error");
            result.put("msg",e.getMessage());
        }
        return null;

    }
}
