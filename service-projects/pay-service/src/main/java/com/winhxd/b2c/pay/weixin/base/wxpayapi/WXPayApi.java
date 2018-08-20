package com.winhxd.b2c.pay.weixin.base.wxpayapi;

import java.util.Map;

import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
import com.winhxd.b2c.pay.weixin.base.dto.PayBillDownloadResponseDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayFinancialBillDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayOrderQueryDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderCallbackDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayPreOrderResponseDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayRefundResponseDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayStatementDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants.SignType;

public interface WXPayApi {
	
    /**
     * 作用：统一下单
     * 场景：公共号支付、扫码支付、APP支付
     * @author mahongliang
     * @date  2018年8月18日 下午1:35:43
     * @Description 
     * @param payPreOrderDTO
     * @return
     */
    PayPreOrderResponseDTO unifiedOrder(PayPreOrderDTO payPreOrderDTO);
    
    /**
     * 微信统一签名方法
     * @author mahongliang
     * @date  2018年8月18日 下午6:26:47
     * @Description 
     * @param obj
     * @return
     */
    String generateSign(Object obj);
    
    /**
     * 微信统一签名方法
     * @author mahongliang
     * @date  2018年8月19日 下午12:24:05
     * @Description 
     * @param obj
     * @param signType
     * @return
     */
    String generateSign(Object obj, SignType signType);


    /**
     * 作用：申请退款<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     * @param payRefundDTO 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    PayRefundResponseDTO refundOder(PayRefundDTO payRefundDTO);

    /**
     * 作用：退款查询<br>
     * 场景：退款查询
     * @param payRefundDTO
     * @return
     */
    Map<String, String> refundQuery(PayRefundDTO payRefundDTO);

    /**
     * 作用：对账单下载<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付<br>
     * 其他：无论是否成功都返回Map。若成功，返回的Map中含有return_code、return_msg、data，
     *      其中return_code为`SUCCESS`，data为对账单数据。
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs 读超时时间，单位是毫秒
     * @return 经过封装的API返回数据
     * @throws Exception
     */
	PayBillDownloadResponseDTO downloadBill(PayStatementDTO payStatementDTO);

    /**
     * 作用：资金账单下载<br>
     * 场景：资金账单中的数据反映的是商户微信账户资金变动情况 <br>
     * 其他：无论是否成功都返回Map。若成功，返回的Map中含有return_code、return_msg、data，
     *      其中return_code为`SUCCESS`，data为资金账单数据。
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs 读超时时间，单位是毫秒
     * @return 经过封装的API返回数据
     * @throws Exception
     */
	PayBillDownloadResponseDTO downloadFundFlow(PayFinancialBillDTO payFinancialBillDTO);

    /**
     * 作用：企业付款<br>
     * 场景：企业付款到微信零钱<br>
     * 其他：需要证书
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    String transferToChange(Map<String, String> reqData) throws Exception;

    /**
     * 作用：查询企业付款<br>
     * 场景：查询企业付款到微信零钱<br>
     * 其他：需要证书
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    String queryTransferToChange(Map<String, String> reqData) throws Exception;

    /**
     * 作用：企业付款<br>
     * 场景：企业付款到微信银行卡<br>
     * 其他：需要证书
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    String transferToBank(Map<String, String> reqData) throws Exception;

    /**
     * 作用：查询企业付款<br>
     * 场景：查询企业付款到微信银行卡<br>
     * 其他：需要证书
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    String queryTransferToBank(Map<String, String> reqData) throws Exception;

    /**
     * 小程序返回值签名
     * @author mahongliang
     * @date  2018年8月21日 上午2:01:54
     * @Description 
     * @param payPreOrderVO
     * @return
     */
	String payPreOrderSign(PayPreOrderVO payPreOrderVO);

    /**
     * 作用：获取证书<br>
     * 场景：获取证书<br>
     * 其他：需要证书
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    public String publicKey(Map<String, String> reqData) throws Exception;
    
    /**
	 * 主动查询订单
	 * @author mahongliang
	 * @date  2018年8月21日 上午2:35:39
	 * @Description 
	 * @param payOrderQueryDTO
	 * @return
	 * @throws Exception
	 */
	PayPreOrderCallbackDTO orderQuery(PayOrderQueryDTO payOrderQueryDTO);

}
