package com.winhxd.b2c.pay.controller;

import com.winhxd.b2c.pay.weixin.base.dto.PayRefundResponseDTO;
import com.winhxd.b2c.pay.weixin.dao.PayRefundMapper;
import com.winhxd.b2c.pay.weixin.model.PayRefund;
import com.winhxd.b2c.pay.weixin.util.BeanAndXmlUtil;
import com.winhxd.b2c.pay.weixin.util.MD5Util;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static sun.security.x509.CertificateAlgorithmId.ALGORITHM;


/**
 * CallBackController
 *
 * @Author 李中华
 * @Date 2018/8/18 11:51
 * @Description:
 */
@Controller
@Api(tags = "Api CallBack")
@RequestMapping(value = "/callback")
public class CallBackController {

    private static String password = "aaa";

    private static SecretKeySpec key = new SecretKeySpec(MD5Util.MD5Encode(password, "UTF-8").toLowerCase().getBytes(), ALGORITHM);

    @Autowired
    PayRefundMapper payRefundMapper;

    /**
     * 支付成功的回调函数
     * @param request
     * @param response
     * @throws Exception
     */
    public void weixinpay_notify(HttpServletRequest request,HttpServletResponse response) throws Exception{

        PayRefund model = new PayRefund();

        InputStream inputStream ;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s ;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null){
            sb.append(s);
        }
        in.close();
        inputStream.close();
        PayRefundResponseDTO callbackDto = BeanAndXmlUtil.xml2Bean(sb.toString(), PayRefundResponseDTO.class);

        String resXml = "";
        if("SUCCESS".equals(callbackDto.getReturnCode())){
            //得到返回的参数
            String appid = callbackDto.getAppid();
            String mchId = callbackDto.getMchId();
            String nonceStr = callbackDto.getNonceStr();
            String reqInfo = callbackDto.getReqInfo();

            //对加密串进行解密
            String decodeString = this.decodeReqInfo(reqInfo);
            callbackDto = BeanAndXmlUtil.xml2Bean(decodeString, PayRefundResponseDTO.class);

            PayRefund payRefund = payRefundMapper.selectByOutRefundNo(callbackDto.getOutTradeNo());
            if (payRefund.getCallbackRefundStatus() != 1){
                model.setId(payRefund.getId());
                model.setAppid(appid);
                model.setMchId(mchId);
                model.setNonceStr(nonceStr);
                model.setTransactionId(callbackDto.getTransactionId());
                model.setOutTradeNo(callbackDto.getOutTradeNo());
                model.setCallbackRefundId(callbackDto.getRefundId());
                model.setOutRefundNo(callbackDto.getOutRefundNo());
                model.setCallbackTotalFee(callbackDto.getTotalFee());
                model.setCallbackSettlementTotalFee(callbackDto.getSettlementTotalFee());
                model.setCallbackRefundFee(callbackDto.getRefundFee());
                model.setCallbackSettlementRefundFee(callbackDto.getSettlementRefundFee());
                DateFormat bf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                model.setCallbackSuccessTime(bf.parse(callbackDto.getSuccessTime()));
                model.setCallbackRefundRecvAccout(callbackDto.getRefundRecvAccout());
                model.setCallbackRefundAccount(callbackDto.getRefundAccount());
                model.setCallbackRefundRequestSource(callbackDto.getRefundRequestSource());
                String refundStatus = callbackDto.getRefundStatus();
                if("SUCCESS".equals(refundStatus)){
                    model.setCallbackRefundStatus((short)1);
                }else if("REFUNDCLOSE".equals(refundStatus)){
                    model.setCallbackRefundStatus((short)2);
                }else if("CHANGE".equals(refundStatus)){
                    model.setCallbackRefundStatus((short)3);
                }
                payRefundMapper.updateByPrimaryKeySelective(model);
            }
            //这里可以写你需要的业务

            resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(resXml.getBytes());
            out.flush();
            out.close();
        } else {
            System.out.println("回调失败");
            /*resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";*/
        }

    }

    private String decodeReqInfo(String reqInfo) throws Exception{
        byte[] b = new BASE64Decoder().decodeBuffer(reqInfo);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(b),"UTF-8");
    }
}
