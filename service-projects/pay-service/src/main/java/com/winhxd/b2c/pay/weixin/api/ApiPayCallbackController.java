package com.winhxd.b2c.pay.weixin.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.pay.service.PayService;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderCallbackDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundResponseDTO;
import com.winhxd.b2c.pay.weixin.constant.BillStatusEnum;
import com.winhxd.b2c.pay.weixin.dao.PayRefundMapper;
import com.winhxd.b2c.pay.weixin.model.PayBill;
import com.winhxd.b2c.pay.weixin.model.PayRefund;
import com.winhxd.b2c.pay.weixin.service.WXRefundService;
import com.winhxd.b2c.pay.weixin.service.WXUnifiedOrderService;
import com.winhxd.b2c.pay.weixin.util.DecipherUtil;
import com.winhxd.b2c.pay.weixin.util.XmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

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
	private static final Logger logger = LoggerFactory.getLogger(ApiPayCallbackController.class);
	private static final String SUCCESS_RESPONSE = "<xml>" + "<return_code><!--[CDATA[SUCCESS]]--></return_code>" + "<return_msg><!--[CDATA[OK]]--></return_msg>" + "</xml> ";
	private static final String FAIL_RESPONSE = "<xml>" + "<return_code><!--[CDATA[FAIL]]--></return_code>" + "<return_msg><!--[CDATA[报文为空]]--></return_msg>" + "</xml> ";
	
	@Autowired
	private WXUnifiedOrderService unifiedOrderService;
	@Autowired
	private PayService payService;

	@Autowired
	WXRefundService wxRefundService;
	@Autowired
	PayRefundMapper payRefundMapper;

	/**
	 * 密钥算法
	 */
	private static final String ALGORITHM = "AES";
	/**
	 * API 密钥
	 */
	@Value("${WX.KEY}")
	private String apiKey;

	@ApiOperation(value = "微信支付回调", notes = "微信支付回调")
	@PostMapping(value = "${WX.PAY_NOTIFY_URL}")
	private void unifiedOrderCallback(HttpServletRequest request,HttpServletResponse response){
		//微信回调解析
		String resqXml = this.wxCallbackParser(request);
		if(resqXml == null) {
			this.response(response, FAIL_RESPONSE);
			return;
		}
		logger.info("支付回调参数：{}", resqXml);
		try {
			PayPreOrderCallbackDTO payPreOrderCallbackDTO = XmlUtil.xml2Bean(resqXml, PayPreOrderCallbackDTO.class);
			logger.info("支付回调转换参数（PayPreOrderCallbackDTO）：{}", JsonUtil.toJSONString(payPreOrderCallbackDTO));
			if(PayPreOrderCallbackDTO.FAIL.equals(payPreOrderCallbackDTO.getReturnCode())) {
				this.response(response, FAIL_RESPONSE);
				return;
			}
			Short status = BillStatusEnum.FAIL.getCode();
			if(PayPreOrderCallbackDTO.SUCCESS.equals(payPreOrderCallbackDTO.getReturnCode())) {
				status = BillStatusEnum.PAID.getCode();
			}
			PayBill bill = unifiedOrderService.updatePayBillByOutTradeNo(payPreOrderCallbackDTO, status);
			Boolean result = payService.callbackOrderPay(bill);
			if(result) {
				this.response(response, SUCCESS_RESPONSE);
			} else {
				logger.error("支付回调成功，回调业务系统返回失败，订单号：{}，支付流水号：{}", bill.getOutOrderNo(), bill.getOutTradeNo());
				this.response(response, FAIL_RESPONSE);
			}
		} catch (Exception e) {
			logger.error("微信支付回调失败", e);
			this.response(response, FAIL_RESPONSE);
		}
	}
	
	@ApiOperation(value = "微信退款回调", notes = "微信退款回调")
	@ApiResponses({
		@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	@PostMapping(value = "${WX.REFUND_NOTIFY_URL}")
	private void refundCallback(HttpServletRequest request,HttpServletResponse response){
		//微信回调解析
		String resqXml = this.wxCallbackParser(request);
		if(resqXml == null) {
			this.response(response, FAIL_RESPONSE);
			return;
		}
		logger.info("退款回调参数：{}", resqXml);
		try {
			PayRefundResponseDTO refundCallbackDTO = XmlUtil.xml2Bean(resqXml, PayRefundResponseDTO.class);
			logger.info("退款回调转换参数（PayRefundResponseDTO）：{}", refundCallbackDTO);
			if(PayPreOrderCallbackDTO.SUCCESS.equals(refundCallbackDTO.getReturnCode())) {
				String reqInfo = refundCallbackDTO.getReqInfo();
				//对加密串进行解密
				SecretKeySpec key = new SecretKeySpec(DigestUtils.md5Hex(apiKey).toLowerCase().getBytes(), ALGORITHM);
				logger.info("注入的apiKey为："+apiKey);
				logger.info("apiKey进行MD5之后为"+ key);
				String decodeString = DecipherUtil.decodeReqInfo(reqInfo, key);

				PayRefundResponseDTO fillResponseDto = XmlUtil.xml2Bean(decodeString, PayRefundResponseDTO.class);
				fillResponseDto.setReqInfo(reqInfo);

				PayRefund payRefund = wxRefundService.updatePayRefundByOutTradeNo(fillResponseDto);
				Boolean result = payService.callbackOrderRefund(payRefund);
				if(result) {
					this.response(response, SUCCESS_RESPONSE);
				} else {
					this.response(response, FAIL_RESPONSE);
				}
			} else {
				this.response(response, FAIL_RESPONSE);
			}
		} catch (Exception e) {
			logger.error("微信退款回调转换失败", e);
			this.response(response, FAIL_RESPONSE);
		}
	}
	
	/**
	 * 微信回调解析
	 * @author mahongliang
	 * @date  2018年8月19日 下午3:23:32
	 * @Description 
	 * @param request
	 * @return
	 */
	private String wxCallbackParser(HttpServletRequest request) {
		String resqXml = null;
        try(InputStream is = request.getInputStream()){
    		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    		StringBuilder sb = new StringBuilder();
        	String line = null;
        	while ((line = reader.readLine()) != null) {
        		sb.append(line);
        	}
        	resqXml = sb.toString();
            //logger.info("微信回调参数：{}", resqXml);
        } catch (IOException e) {
        	logger.error("微信回调通知失败", e);
        }
		return resqXml;
	}
	
	/**
	 * 微信回调响应
	 * @author mahongliang
	 * @date  2018年8月19日 下午4:07:25
	 * @Description 
	 * @param response
	 * @param respXml
	 */
	private void response(HttpServletResponse response, String respXml) {
		try (BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());){
			out.write(respXml.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("微信回调响应失败", e);
		}
	}

}
