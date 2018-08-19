package com.winhxd.b2c.pay.weixin.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundResponseDTO;
import com.winhxd.b2c.pay.weixin.dao.PayRefundMapper;
import com.winhxd.b2c.pay.weixin.model.PayRefund;
import com.winhxd.b2c.pay.weixin.util.BeanAndXmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

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
 * 微信支付回调
 * @author mahongliang
 * @date  2018年8月18日 下午8:43:21
 * @Description 
 * @version
 */
@Controller
@Api(tags = "Api CallBack")
public class ApiPayCallbackController {

	private static String password = "aaa";

	private static SecretKeySpec key = new SecretKeySpec(byteArrayToHexString(DigestUtils.md5(password)).toLowerCase().getBytes(), ALGORITHM);

	@Autowired
	PayRefundMapper payRefundMapper;

	@ApiOperation(value = "微信支付回调", notes = "微信支付回调")
	@PostMapping(value = "${WX.PAY_NOTIFY_URL}")
	private void unifiedOrderCallback(HttpServletRequest request,HttpServletResponse response){
		return ;
	}
	
	@ApiOperation(value = "微信退款回调", notes = "微信退款回调")
	@ApiResponses({
		@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	@PostMapping(value = "${WX.REFUND_NOTIFY_URL}")
	private void refundCallback(HttpServletRequest request,HttpServletResponse response) throws Exception{
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
		byte[] b = Base64.decodeBase64(reqInfo);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(b),"UTF-8");


	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

}
