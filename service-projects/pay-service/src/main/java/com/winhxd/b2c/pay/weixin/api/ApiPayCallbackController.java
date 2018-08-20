package com.winhxd.b2c.pay.weixin.api;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.pay.service.PayService;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderCallbackDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundResponseDTO;
import com.winhxd.b2c.pay.weixin.dao.PayRefundMapper;
import com.winhxd.b2c.pay.weixin.model.PayBill;
import com.winhxd.b2c.pay.weixin.model.PayRefund;
import com.winhxd.b2c.pay.weixin.service.WXRefundService;
import com.winhxd.b2c.pay.weixin.service.WXUnifiedOrderService;
import com.winhxd.b2c.pay.weixin.util.BeanAndXmlUtil;
import com.winhxd.b2c.pay.weixin.util.DecipherUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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

	@ApiOperation(value = "微信支付回调", notes = "微信支付回调")
	@PostMapping(value = "${WX.PAY_NOTIFY_URL}")
	private void unifiedOrderCallback(HttpServletRequest request,HttpServletResponse response){
		//微信回调解析
		String resqXml = this.wxCallbackParser(request);
		if(resqXml == null) {
			this.response(response, FAIL_RESPONSE);
			return;
		}
		try {
			//Map<String, String> map = WXPayUtil.xmlToMap(respXml);
			PayPreOrderCallbackDTO payPreOrderCallbackDTO = BeanAndXmlUtil.xml2Bean(resqXml, PayPreOrderCallbackDTO.class);
			if(PayPreOrderCallbackDTO.SUCCESS.equals(payPreOrderCallbackDTO.getReturnCode())) {
				PayBill bill = unifiedOrderService.updatePayBillByOutTradeNo(payPreOrderCallbackDTO);
				int success = payService.callbackOrderPay(bill);
				if(success == 0) {
					this.response(response, SUCCESS_RESPONSE);
				} else {
					this.response(response, FAIL_RESPONSE);
				}
			} else {
				this.response(response, FAIL_RESPONSE);
			}
		} catch (Exception e) {
			logger.error("微信支付回调转换失败", e);
			this.response(response, FAIL_RESPONSE);
		}
	}
	
	@ApiOperation(value = "微信退款回调", notes = "微信退款回调")
	@ApiResponses({
		@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	@PostMapping(value = "${WX.REFUND_NOTIFY_URL}")
	private void refundCallback(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//微信回调解析
		String resqXml = this.wxCallbackParser(request);
		if(resqXml == null) {
			this.response(response, FAIL_RESPONSE);
			return;
		}
		try {
			PayRefundResponseDTO refundCallbackDTO = BeanAndXmlUtil.xml2Bean(resqXml, PayRefundResponseDTO.class);
			if(PayPreOrderCallbackDTO.SUCCESS.equals(refundCallbackDTO.getReturnCode())) {
				String reqInfo = refundCallbackDTO.getReqInfo();
				//对加密串进行解密
				String decodeString = DecipherUtil.decodeReqInfo(reqInfo);
				refundCallbackDTO = BeanAndXmlUtil.xml2Bean(decodeString, PayRefundResponseDTO.class);

				PayRefund payRefund = wxRefundService.updatePayRefundByOutTradeNo(refundCallbackDTO);
				int success = payService.callbackOrderRefund(payRefund);
				if(success == 0) {
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
        try(InputStream is = request.getInputStream();){
    		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    		StringBuilder sb = new StringBuilder();
        	String line = null;
        	while ((line = reader.readLine()) != null) {
        		sb.append(line);
        	}
        	resqXml = sb.toString();
            logger.info("微信支付回调参数：{}", resqXml);
        } catch (IOException e) {
        	logger.error("微信支付回调通知失败", e);
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
			logger.error("微信支付回调响应失败", e);
		}
	}

}
