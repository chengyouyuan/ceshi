package com.winhxd.b2c.pay.weixin.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxBankVO;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxChangeVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersToWxBankResponseDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersToWxChangeResponseDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPay;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConfig;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayUtil;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersForWxBankDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayTransfersForWxChangeDTO;
import com.winhxd.b2c.pay.weixin.constant.TransfersToWxError;
import com.winhxd.b2c.pay.weixin.service.WXTransfersService;
import com.winhxd.b2c.pay.weixin.util.BeanAndXmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.winhxd.b2c.pay.weixin.constant.TransfersToWxError.INVALID_REQUEST;

/**
 * WXTransfersServiceImpl
 *
 * @Author yindanqing
 * @Date 2018/8/13 12:47
 * @Description: 微信转账实现
 */
@Service(value = "wXTransfersService")
public class WXTransfersServiceImpl implements WXTransfersService {

    private static final Logger logger = LoggerFactory.getLogger(WXTransfersServiceImpl.class);

    /**
     * 默认设置姓名强校验
     */
    private static final String FORCE_CHECK = "FORCE_CHECK";

    /**
     * 分与元单位转换
     */
    private static final BigDecimal UNITS = new BigDecimal("100");

    /**
     * wx返回标准时间格式转换
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private WXPay wxPay;

    @Autowired
    private WXPayConfig wxPayConfig;

    @Override
    public PayTransfersToWxChangeVO transfersToChange(PayTransfersToWxChangeCondition toWxBalanceCondition) {
        PayTransfersToWxChangeVO toWxChangeVO = null;
        try {
            checkNecessaryFieldForChange(toWxBalanceCondition);
            PayTransfersForWxChangeDTO wxChangeDTO = getReqParamForChange(toWxBalanceCondition);
            String respxml = wxPay.transferToChange(BeanAndXmlUtil.beanToSortedMap(wxChangeDTO));
            PayTransfersToWxChangeResponseDTO responseDTO = BeanAndXmlUtil.xml2Bean(respxml, PayTransfersToWxChangeResponseDTO.class);
            toWxChangeVO = praseResultForChange(responseDTO);
            if(!toWxChangeVO.isTransfersResult()){
                //调用查询接口确认转账详情

            }
            //saveRecord();
        } catch (Exception ex) {
            logger.error("TransfersToChange error.");
        }
        return toWxChangeVO;
    }

    /**
     * 检查必须字段
     * @Author yindanqing
     * @Date 2018-8-15 10:24:48
     */
    private void checkNecessaryFieldForChange(PayTransfersToWxChangeCondition toWxBalanceCondition){
        boolean res = //StringUtils.isBlank(toWxBalanceCondition.getMchAppid()) ||
                StringUtils.isBlank(toWxBalanceCondition.getPartnerTradeNo())
                        || StringUtils.isBlank(toWxBalanceCondition.getAccountId())
                        || StringUtils.isBlank(toWxBalanceCondition.getAccountName())
                        || null == toWxBalanceCondition.getTotalAmount()
                        && toWxBalanceCondition.getTotalAmount().doubleValue() <= 0d
                        || StringUtils.isBlank(toWxBalanceCondition.getDesc())
                        || StringUtils.isBlank(toWxBalanceCondition.getSpbillCreateIp());
        if (res) {
            logger.warn("转账必填字段为空");
            throw new BusinessException(BusinessCode.CODE_600201);
        }
    }

    /**
     * 获得请求参数
     * @return
     */
    private PayTransfersForWxChangeDTO getReqParamForChange(PayTransfersToWxChangeCondition toWxBalanceCondition) throws Exception {
        PayTransfersForWxChangeDTO forWxChangeDTO = new PayTransfersForWxChangeDTO();
        forWxChangeDTO.setMchAppid(wxPayConfig.getMchAppID());
        forWxChangeDTO.setMchid(wxPayConfig.getMchID());
        forWxChangeDTO.setDeviceInfo(toWxBalanceCondition.getDeviceInfo());
        forWxChangeDTO.setNonceStr(WXPayUtil.generateNonceStr());
        forWxChangeDTO.setPartnerTradeNo(toWxBalanceCondition.getPartnerTradeNo());
        forWxChangeDTO.setOpenid(toWxBalanceCondition.getAccountId());
        forWxChangeDTO.setCheckName(FORCE_CHECK);
        forWxChangeDTO.setReUserName(toWxBalanceCondition.getAccountName());
        forWxChangeDTO.setAmount(toWxBalanceCondition.getTotalAmount().multiply(UNITS).intValue());
        forWxChangeDTO.setDesc(toWxBalanceCondition.getDesc());
        forWxChangeDTO.setSpbillCreateIp(toWxBalanceCondition.getSpbillCreateIp());
        //处理签名
        forWxChangeDTO.setSign(WXPayUtil.generateSignature(BeanAndXmlUtil.beanToSortedMap(forWxChangeDTO), wxPayConfig.getKey()));
        return forWxChangeDTO;
    }

    /**
     * 准备处理返参
     * @param responseDTO 接口返回值
     */
    private PayTransfersToWxChangeVO praseResultForChange(PayTransfersToWxChangeResponseDTO responseDTO) throws ParseException {
        PayTransfersToWxChangeVO toWxChangeVO = new PayTransfersToWxChangeVO();
        //未通信情况
        if(StringUtils.isBlank(responseDTO.getReturnCode()) ||
                StringUtils.equals(responseDTO.getReturnCode(), TransfersToWxError.FAILTOREACH.getCode())){
            toWxChangeVO.setErrorDesc(TransfersToWxError.FAILTOREACH.getText());
            return toWxChangeVO;
        }
        //设置返参的业务处理信息
        String resultCode = responseDTO.getResultCode();
        for (TransfersToWxError error : TransfersToWxError.values()){
            if(resultCode.equals(error.getCode())){
                toWxChangeVO.setTransfersResult(error.getCode().equals(TransfersToWxError.SUCCESS.getCode()));
                toWxChangeVO.setErrorDesc(error.getText());
                break;
            }
        }
        //设置其他信息(同名属性)
        BeanUtils.copyProperties(responseDTO, toWxChangeVO);
        //设置日期
        toWxChangeVO.setPaymentTime(DATE_FORMAT.parse(responseDTO.getPaymentTime()));
        return toWxChangeVO;
    }

    @Override
    public PayTransfersToWxBankVO transfersToBank(PayTransfersToWxBankCondition toWxBankCondition) {
        PayTransfersToWxBankVO toWxBankVO = null;
        try {
            checkNecessaryFieldForBank(toWxBankCondition);
            PayTransfersForWxBankDTO wxBankDTO = getReqParamForBank(toWxBankCondition);
            String respxml = wxPay.transferToBank(BeanAndXmlUtil.beanToSortedMap(wxBankDTO));
            PayTransfersToWxBankResponseDTO responseDTO = BeanAndXmlUtil.xml2Bean(respxml, PayTransfersToWxBankResponseDTO.class);
            toWxBankVO = praseResultForBank(responseDTO);
        } catch (Exception ex) {
            logger.error("TransfersToBank error.");
        }
        return toWxBankVO;
    }

    /**
     * 检查必须字段
     * @Author yindanqing
     * @Date 2018-8-15 10:24:48
     */
    private void checkNecessaryFieldForBank(PayTransfersToWxBankCondition toWxBankCondition){
        boolean res = StringUtils.isBlank(toWxBankCondition.getPartnerTradeNo())
                || StringUtils.isBlank(toWxBankCondition.getAccount())
                || StringUtils.isBlank(toWxBankCondition.getAccountName())
                || null == toWxBankCondition.getChannelCode()
                || null == toWxBankCondition.getTotalAmount()
                || toWxBankCondition.getTotalAmount().doubleValue() <= 0d
                || StringUtils.isBlank(toWxBankCondition.getDesc());
        if (res) {
            logger.warn("转账必填字段为空");
            throw new BusinessException(BusinessCode.CODE_600201);
        }
    }

    /**
     * 获得请求参数
     * @return
     */
    private PayTransfersForWxBankDTO getReqParamForBank(PayTransfersToWxBankCondition toWxBankCondition) throws Exception {
        PayTransfersForWxBankDTO forWxBankDTO = new PayTransfersForWxBankDTO();
        forWxBankDTO.setMchid(wxPayConfig.getMchID());
        forWxBankDTO.setPartnerTradeNo(toWxBankCondition.getPartnerTradeNo());
        forWxBankDTO.setNonceStr(WXPayUtil.generateNonceStr());
        //处理卡号姓名加密rsa
        forWxBankDTO.setEncBankNo(toWxBankCondition.getAccount());
        forWxBankDTO.setEncTrueName(toWxBankCondition.getAccountName());
        forWxBankDTO.setBankCode(String.valueOf(toWxBankCondition.getChannelCode().getCode()));
        forWxBankDTO.setAmount(toWxBankCondition.getTotalAmount().multiply(UNITS).intValue());
        forWxBankDTO.setDesc(toWxBankCondition.getDesc());
        //处理签名
        forWxBankDTO.setSign(WXPayUtil.generateSignature(BeanAndXmlUtil.beanToSortedMap(toWxBankCondition), wxPayConfig.getKey()));
        return forWxBankDTO;
    }

    public static void main(String[] args) throws Exception {
        WXPay wxPay = new WXPay();
        SortedMap<String,String> sortedMap = new TreeMap<String,String>();
        sortedMap.put("mch_id", "1467361502");
        sortedMap.put("nonce_str", WXPayUtil.generateNonceStr());
        sortedMap.put("sign_type", "MD5");
        sortedMap.put("sign", WXPayUtil.generateSignature(sortedMap, "u29K8cDc48zhF0hKlQ3pd1tiamkZpRoS"));
        System.out.println(wxPay.publicKey(sortedMap));

        //System.out.println(new BigDecimal("1001").divide(UNITS).setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    /**
     * 准备处理返参
     * @param responseDTO 接口返回值
     */
    private PayTransfersToWxBankVO praseResultForBank(PayTransfersToWxBankResponseDTO responseDTO){
        PayTransfersToWxBankVO toWxBankVO = new PayTransfersToWxBankVO();
        //未通信情况
        if (StringUtils.isBlank(responseDTO.getReturnCode()) ||
                StringUtils.equals(responseDTO.getReturnCode(), TransfersToWxError.FAILTOREACH.getCode())) {
            toWxBankVO.setTransfersResult(false);
            toWxBankVO.setErrorDesc(TransfersToWxError.FAILTOREACH.getText());
            return toWxBankVO;
        }
        //设置返参的业务处理信息
        String resultCode = responseDTO.getResultCode();
        for (TransfersToWxError error : TransfersToWxError.values()) {
            if (resultCode.equals(error.getCode())) {
                toWxBankVO.setTransfersResult(error.getCode().equals(TransfersToWxError.SUCCESS.getCode()));
                toWxBankVO.setErrorDesc(error.getText());
                break;
            }
        }
        //设置其他信息(同名属性)
        BeanUtils.copyProperties(responseDTO, toWxBankVO);
        //设置金额及手续费信息
        toWxBankVO.setAmount(new BigDecimal(responseDTO.getAmount()).divide(UNITS, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
        toWxBankVO.setCmmsAmt(new BigDecimal(responseDTO.getCmmsAmt()).divide(UNITS, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
        return toWxBankVO;
    }

}
