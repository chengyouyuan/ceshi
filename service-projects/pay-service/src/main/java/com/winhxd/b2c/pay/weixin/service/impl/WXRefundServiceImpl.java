package com.winhxd.b2c.pay.weixin.service.impl;

import com.winhxd.b2c.common.constant.Currency;
import com.winhxd.b2c.common.domain.pay.condition.PayRefundCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayRefundVO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayRequest;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayUtil;
import com.winhxd.b2c.pay.weixin.dao.PayRefundMapper;
import com.winhxd.b2c.pay.weixin.model.PayRefund;
import com.winhxd.b2c.pay.weixin.service.WXRefundService;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class WXRefundServiceImpl implements WXRefundService {

//    @Value("${pay.cert.path}")
    private String certPath;

    @Autowired
    WXPayRequest wxPayRequest;
    
    @Autowired
    PayRefundMapper payRefundMapper;



    @Override
    public PayRefundVO refundOrder(PayRefundCondition payRefund) {
        try{
            /*KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File(certPath));
            try {
                keyStore.load(instream, payRefund.getMchId().toCharArray());
            }finally {
                instream.close();
            }
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, payRefund.getMchId().toCharArray()).build();
            // Allow TLSv1 protocol only
            *//*SSLConnectionSocketFactory sslsf1 = new SSLConnectionSocketFactory(
                    sslcontext, new String[] { "TLSv1" }, null,SSLConnectionSocketFactory.getDefaultHostnameVerifier());*//*
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext, new String[] { "TLSv1" }, null,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf).build();
            HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");*/


            String domain = WXPayConstants.DOMAIN_API;
            String urlSuffix = WXPayConstants.REFUND_URL_SUFFIX;
            SortedMap<String,String> parameters = new TreeMap<String,String>();
            parameters.put("appid", payRefund.getAppid());
            //parameters.put("mch_id", payRefund.getMchId());
            parameters.put("nonce_str", WXPayUtil.generateNonceStr());
            //以下二选一
            //parameters.put("out_trade_no", out_trade_no);
            parameters.put("transaction_id", "");
            //退款单号使用流水单号
            parameters.put("out_refund_no", payRefund.getOutTradeNo());
            parameters.put("fee_type", "CNY");
            parameters.put("total_fee",(payRefund.getTotalAmount().multiply(new BigDecimal(100))).toString());
            //parameters.put("refund_fee", payRefund.getTotalFee().toString());
            String xml = WXPayUtil.generateSignedXml(parameters,"");
            //参数：商户订单号 out_trade_no，微信订单号 transaction_id ，订单金额 total_fee
            //String xml = WXPayUtil2.wxPayRefund(payRefund.getOutTradeNo(),payRefund.getTransactionId(),String.valueOf(payRefund.getTotalFee()));

            /*try {
                StringEntity se = new StringEntity("");
                httppost.setEntity(se);
                System.out.println("executing request" + httppost.getRequestLine());
                CloseableHttpResponse responseEntry = httpclient.execute(httppost);
                try {
                    HttpEntity entity = responseEntry.getEntity();
                    System.out.println(responseEntry.getStatusLine());
                    if (entity != null) {
                        System.out.println("Response content length: "
                                + entity.getContentLength());
                        SAXReader saxReader = new SAXReader();
                        Document document = saxReader.read(entity.getContent());
                        Element rootElt = document.getRootElement();
                        System.out.println("根节点：" + rootElt.getName());
                        System.out.println("==="+rootElt.elementText("result_code"));
                        System.out.println("==="+rootElt.elementText("return_msg"));
                        String resultCode = rootElt.elementText("result_code");
                        //TODO 定义返回值
                        JSONObject result = new JSONObject();

                        Document documentXml = DocumentHelper.parseText("");
                        Element rootEltXml = documentXml.getRootElement();
                        if(resultCode.equals("SUCCESS")){
                            System.out.println("=================prepay_id===================="+ rootElt.elementText("prepay_id"));
                            System.out.println("=================sign===================="+ rootEltXml.elementText("sign"));
                            result.put("weixinPayUrl", rootElt.elementText("code_url"));
                            result.put("prepayId", rootElt.elementText("prepay_id"));
                            result.put("status","success");
                            result.put("msg","success");
                        }else{
                            result.put("status","false");
                            result.put("msg",rootElt.elementText("err_code_des"));
                        }
                        return result;
                    }
                    EntityUtils.consume(entity);
                }
                finally {
                    responseEntry.close();
                }
            }
            finally {
                httpclient.close();
            }*/
            return null;
        }catch(Exception e){
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("status","error");
            result.put("msg",e.getMessage());
            return null;
        }
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
            // TODO 以下二选一
            //parameters.put("out_trade_no", out_trade_no);
            //parameters.put("transaction_id", payRefundDTO.getTransactionId());
            //TODO 退款单号
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
            parameters.put("appid", payRefund.getAppid());
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
