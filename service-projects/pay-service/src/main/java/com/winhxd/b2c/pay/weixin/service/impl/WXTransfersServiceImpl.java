package com.winhxd.b2c.pay.weixin.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.TransfersChannelCodeType;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxBankVO;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxChangeVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.base.dto.*;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPay;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConfig;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayUtil;
import com.winhxd.b2c.pay.weixin.constant.PayTransfersStatus;
import com.winhxd.b2c.pay.weixin.constant.TransfersChannelType;
import com.winhxd.b2c.pay.weixin.constant.TransfersToWxError;
import com.winhxd.b2c.pay.weixin.dao.PayTransfersMapper;
import com.winhxd.b2c.pay.weixin.model.PayTransfers;
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
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

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
     * 分与元单位转换
     */
    private static final BigDecimal DEFAULT_CMMS = new BigDecimal("0.00");

    /**
     * wx返回标准时间格式转换
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private WXPay wxPay;

    @Autowired
    private WXPayConfig wxPayConfig;

    @Autowired
    private PayTransfersMapper payTransfersMapper;

    @Override
    public PayTransfersToWxChangeVO transfersToChange(PayTransfersToWxChangeCondition toWxBalanceCondition) {
        PayTransfersToWxChangeVO toWxChangeVO = null;
        try {
            //必填验证
            checkNecessaryFieldForChange(toWxBalanceCondition);
            //准备wx侧请求参数
            PayTransfersForWxChangeDTO wxChangeDTO = getReqParamForChange(toWxBalanceCondition);
            //处理微信请求及结果
            String respxml = wxPay.transferToChange(BeanAndXmlUtil.beanToSortedMap(wxChangeDTO));
            PayTransfersToWxChangeResponseDTO responseDTO = BeanAndXmlUtil.xml2Bean(respxml, PayTransfersToWxChangeResponseDTO.class);
            toWxChangeVO = praseResultForChange(responseDTO);
            //错误情况下调用查询确认转账情况
            if(!toWxChangeVO.isTransfersResult()){
                //调用查询接口确认转账详情(由于处理中状态存在, 该方法最多获得3次查询结果)
                toWxChangeVO = confirmTransfersResult(toWxChangeVO);
            }
            //保存请求流水
            savePayTransfersToWxChangeRecord(toWxBalanceCondition, wxChangeDTO, responseDTO);
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
        /**
         * DeviceInfo&NonceStr, 如果不是第一次进行请求,则须和前一次相同
         */
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
                toWxChangeVO.setAbleContinue(error.getAbleContinue());
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

    private PayTransfersToWxChangeVO confirmTransfersResult(PayTransfersToWxChangeVO toWxChangeVO)  throws Exception{
        PayTransfersQueryForWxChangeResponseDTO queryForWxChangeResponseDTO = getExactResultByQuery(toWxChangeVO, 3);
        if (null == queryForWxChangeResponseDTO) {
            logger.error("Transfers result query failed, partnerTradeNo : " + toWxChangeVO.getPartnerTradeNo());
            return toWxChangeVO;
        }
        //获得查询结果, 开始处理返参
        String transfersStatus = queryForWxChangeResponseDTO.getStatus();
        if (PayTransfersStatus.SUCCESS.getCode().equals(transfersStatus)) {
            toWxChangeVO.setTransfersResult(true);
            toWxChangeVO.setErrorDesc(null);
        } else if (PayTransfersStatus.FAILED.getCode().equals(transfersStatus)) {
            toWxChangeVO.setErrorDesc(queryForWxChangeResponseDTO.getReason());
        } else if (PayTransfersStatus.PROCESSING.getCode().equals(transfersStatus)) {
            toWxChangeVO.setErrorDesc(PayTransfersStatus.FAILED.getText());
        } else {
            logger.error("Transfers query result return UNKNOW STATUS, partnerTradeNo : " + toWxChangeVO.getPartnerTradeNo());
        }
        return toWxChangeVO;
    }

    /**
     * 转账结果失败时重新查询, 确认结果
     * 茜
     * @param toWxChangeVO
     * @throws Exception
     */
    private PayTransfersQueryForWxChangeResponseDTO getExactResultByQuery(PayTransfersToWxChangeVO toWxChangeVO, int queryTimes) throws Exception {
        if(queryTimes <= 0){
            return new PayTransfersQueryForWxChangeResponseDTO();
        }
        PayTransfersQueryForWxChangeResponseDTO queryForWxChangeResponseDTO = new PayTransfersQueryForWxChangeResponseDTO();
        //请求查询接口参数
        PayTransfersQueryForWxChangeDTO queryForWxChangeDTO = new PayTransfersQueryForWxChangeDTO();
        queryForWxChangeDTO.setMchId(wxPayConfig.getMchID());
        queryForWxChangeDTO.setAppid(wxPayConfig.getAppID());
        queryForWxChangeDTO.setPartnerTradeNo(toWxChangeVO.getPartnerTradeNo());
        queryForWxChangeDTO.setNonceStr(WXPayUtil.generateNonceStr());
        //处理签名
        queryForWxChangeDTO.setSign(WXPayUtil.generateSignature(BeanAndXmlUtil.beanToSortedMap(queryForWxChangeDTO), wxPayConfig.getKey()));
        //返参
        String resultXml = wxPay.queryTransferToChange(BeanAndXmlUtil.beanToSortedMap(queryForWxChangeDTO));
        if(StringUtils.isNotBlank(resultXml)){
            queryForWxChangeResponseDTO = BeanAndXmlUtil.xml2Bean(resultXml, PayTransfersQueryForWxChangeResponseDTO.class);
        }
        if(PayTransfersStatus.PROCESSING.getCode().equals(queryForWxChangeResponseDTO.getStatus())){
            Thread.sleep(1 * 1000);
            queryForWxChangeResponseDTO = getExactResultByQuery(toWxChangeVO, --queryTimes);
        }
        return queryForWxChangeResponseDTO;
    }

    /**
     * 保存提现记录流水表
     * @param wxChangeDTO 向wx请求入参
     * @param responseDTO wx请求出参
     */
    private void savePayTransfersToWxChangeRecord(PayTransfersToWxChangeCondition toWxBalanceCondition,
                                                  PayTransfersForWxChangeDTO wxChangeDTO,
                                                  PayTransfersToWxChangeResponseDTO responseDTO) throws ParseException {
        PayTransfers record = new PayTransfers();
        //设置商户,设备等基本信息
        record.setMchAppid(wxChangeDTO.getMchAppid());
        record.setMchid(wxChangeDTO.getMchid());
        record.setDeviceInfo(wxChangeDTO.getDeviceInfo());
        record.setNonceStr(wxChangeDTO.getNonceStr());
        record.setSign(wxChangeDTO.getSign());
        //设置流水记录信息
        record.setPartnerTradeNo(wxChangeDTO.getPartnerTradeNo());
        record.setTransactionId(responseDTO.getPaymentNo());
        record.setAccount(wxChangeDTO.getOpenid());
        record.setCheckName(wxChangeDTO.getCheckName());
        record.setAccountName(wxChangeDTO.getReUserName());
        //设置渠道&金额信息
        record.setChannel(TransfersChannelType.WXBALANCE.getCode());
        record.setChannelCode(String.valueOf(TransfersChannelCodeType.WXBALANCE.getCode()));
        record.setTotalFee(wxChangeDTO.getAmount());
        record.setTotalAmount(new BigDecimal(wxChangeDTO.getAmount()).divide(UNITS).setScale(2,RoundingMode.HALF_UP));
        record.setCmmsFee(0);
        record.setCmmsAmount(DEFAULT_CMMS);
        record.setRealFee(record.getTotalFee());
        record.setRealAmount(record.getTotalAmount());
        //设置其他
        record.setDesc(wxChangeDTO.getDesc());
        record.setSpbillCreateIp(wxChangeDTO.getSpbillCreateIp());
        record.setTimeEnd(DATE_FORMAT.parse(responseDTO.getPaymentTime()));
        record.setStatus((short)(StringUtils.equals(responseDTO.getResultCode(),TransfersToWxError.SUCCESS.getCode()) ? 1 : 0));
        if(0 == record.getStatus()) {
            record.setErrorCode(responseDTO.getErrCode());
            record.setErrorMsg(responseDTO.getErrCodeDes());
        }
        //设置创建人&创建时间
        record.setCreatedBy(toWxBalanceCondition.getOperaterID());
        record.setCreated(new Date(System.currentTimeMillis()));
        payTransfersMapper.insertSelective(record);
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
