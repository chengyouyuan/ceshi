package com.winhxd.b2c.pay.weixin.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.TransfersChannelCodeTypeEnum;
import com.winhxd.b2c.common.constant.TransfersToWxError;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxBankCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayTransfersToWxChangeCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersQueryToWxBankVO;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxBankVO;
import com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxChangeVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.DateUtil;
import com.winhxd.b2c.pay.weixin.base.config.PayConfig;
import com.winhxd.b2c.pay.weixin.base.dto.*;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayUtil;
import com.winhxd.b2c.pay.weixin.constant.PayTransfersStatus;
import com.winhxd.b2c.pay.weixin.constant.TransfersChannelType;
import com.winhxd.b2c.pay.weixin.dao.PayTransfersMapper;
import com.winhxd.b2c.pay.weixin.model.PayTransfers;
import com.winhxd.b2c.pay.weixin.service.WXTransfersService;
import com.winhxd.b2c.pay.weixin.util.XmlUtil;
import com.winhxd.b2c.pay.weixin.util.rsa.GetRSA;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Date;

/**
 * WXTransfersServiceImpl
 *
 * @Author yindanqing
 * @Date 2018/8/13 12:47
 * @Description: 微信转账实现
 */
@Service(value = "wxTransfersService")
public class WXTransfersServiceImpl implements WXTransfersService {

    private static final Logger logger = LoggerFactory.getLogger(WXTransfersServiceImpl.class);

    /**
     * 设置姓名强校验
     */
    private static final String FORCE_CHECK = "FORCE_CHECK";
    /**
     * 默认设置不校验用户真实姓名
     */
    private static final String NO_CHECK = "NO_CHECK";

    /**
     * 分与元单位转换
     */
    private static final BigDecimal UNITS = new BigDecimal("100");

    /**
     * 默认手续费
     */
    private static final BigDecimal DEFAULT_CMMS = new BigDecimal("0.00");

    /**
     * wx返回标准时间格式转换
     */
    private static final String DATE_FORMAT ="yyyy-MM-dd HH:mm:ss";

    @Autowired
    private WXPayApi wxPayApi;

    @Autowired
    private PayConfig payConfig;

    @Autowired
    private PayTransfersMapper payTransfersMapper;

    @Override
    public PayTransfersToWxChangeVO transfersToChange(PayTransfersToWxChangeCondition toWxBalanceCondition) {
        PayTransfersToWxChangeVO toWxChangeVO = new PayTransfersToWxChangeVO();
        try {
            //必填验证
            checkNecessaryFieldForChange(toWxBalanceCondition);
            //准备wx侧请求参数
            PayTransfersForWxChangeDTO wxChangeDTO = getReqParamForChange(toWxBalanceCondition);
            //处理微信请求及结果
            String respxml = wxPayApi.transferToChange(XmlUtil.bean2MapUnderline2Hump(wxChangeDTO));
            logger.info("微信转账结果11:"+respxml);
            PayTransfersToWxChangeResponseDTO responseDTO = XmlUtil.xml2Bean(respxml, PayTransfersToWxChangeResponseDTO.class);
            //准备返参
            toWxChangeVO = praseResultForChange(responseDTO);
            //错误情况下调用查询确认转账情况
            if(!toWxChangeVO.isTransfersResult()){
                //调用查询接口确认转账详情
                toWxChangeVO = confirmTransfersToWxChangeResult(toWxChangeVO, wxChangeDTO);
            }
            //保存请求流水
            savePayTransfersToWxChangeRecord(toWxBalanceCondition, wxChangeDTO, responseDTO, toWxChangeVO);
        } catch (Exception ex) {
            logger.error("TransfersToChange error.", ex);
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
//                        || StringUtils.isBlank(toWxBalanceCondition.getAccountName())
                        || null == toWxBalanceCondition.getTotalAmount()
                        || toWxBalanceCondition.getTotalAmount().doubleValue() <= 0d
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
        //这里好像不对, 但又只能这样写
        forWxChangeDTO.setMchAppid(payConfig.getStoreAppID());
        forWxChangeDTO.setMchid(payConfig.getMchID());
        /**
         * DeviceInfo&NonceStr, 如果不是第一次进行请求,则须和前一次相同
         */
        PayTransfers records =
                payTransfersMapper.selectTOP1TransfersByPartnerTradeNoAndPaymentNo(toWxBalanceCondition.getPartnerTradeNo(), "");
        if(null == records){
            forWxChangeDTO.setDeviceInfo(toWxBalanceCondition.getDeviceInfo());
            forWxChangeDTO.setNonceStr(WXPayUtil.generateNonceStr());
        }else{
            forWxChangeDTO.setDeviceInfo(records.getDeviceInfo());
            forWxChangeDTO.setNonceStr(records.getNonceStr());
        }
        forWxChangeDTO.setPartnerTradeNo(toWxBalanceCondition.getPartnerTradeNo());
        //openID在这里不需要主动获取
        forWxChangeDTO.setOpenid(toWxBalanceCondition.getAccountId());
        forWxChangeDTO.setCheckName(NO_CHECK);
        forWxChangeDTO.setReUserName(toWxBalanceCondition.getAccountName());
        forWxChangeDTO.setAmount(toWxBalanceCondition.getTotalAmount().multiply(UNITS).intValue());
        forWxChangeDTO.setDesc(toWxBalanceCondition.getDesc());
        forWxChangeDTO.setSpbillCreateIp(toWxBalanceCondition.getSpbillCreateIp());
        //处理签名
        forWxChangeDTO.setSign(WXPayUtil.generateSignature(XmlUtil.bean2MapUnderline2Hump(forWxChangeDTO), payConfig.getKey()));
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
        if(TransfersToWxError.SUCCESS.getCode().equals(resultCode)){
            toWxChangeVO.setTransfersResult(true);
        } else {
            String errCode = responseDTO.getErrCode();
            for (TransfersToWxError error : TransfersToWxError.values()){
                if(error.getCode().equals(errCode)){
                    toWxChangeVO.setTransfersResult(false);
                    toWxChangeVO.setAbleContinue(error.getAbleContinue());
                    if(StringUtils.isNotBlank(responseDTO.getErrCodeDes())){
                        toWxChangeVO.setErrorDesc(responseDTO.getErrCodeDes());
                    } else {
                        toWxChangeVO.setErrorDesc(error.getText());
                    }
                    break;
                }
            }
        }
        toWxChangeVO.setErrorCode(responseDTO.getErrCode());
        //设置其他信息(同名属性)
        BeanUtils.copyProperties(responseDTO, toWxChangeVO);
        //设置日期
        if(StringUtils.isNotBlank(responseDTO.getPaymentTime())){
            toWxChangeVO.setPaymentTime(DateUtil.toDate(responseDTO.getPaymentTime(), DATE_FORMAT));
        }
        return toWxChangeVO;
    }

    /**
     * 调用查询wx转账到余额接口, 得到准确转账信息
     * @param toWxChangeVO 接口返参
     * @return 更正后接口返参
     * @throws Exception
     */
    private PayTransfersToWxChangeVO confirmTransfersToWxChangeResult(PayTransfersToWxChangeVO toWxChangeVO, PayTransfersForWxChangeDTO wxChangeDTO)  throws Exception{
        //(由于处理中状态存在, 该方法最多尝试3次查询)
        PayTransfersQueryForWxChangeResponseDTO queryForWxChangeResponseDTO = getExactResultForWxChange(wxChangeDTO.getPartnerTradeNo(), 3);
        if (null == queryForWxChangeResponseDTO) {
            logger.error("Transfers result query failed, partnerTradeNo : " + toWxChangeVO.getPartnerTradeNo());
            return toWxChangeVO;
        }
        //以查询结果为准
        toWxChangeVO.setErrorCode(queryForWxChangeResponseDTO.getErrCode());
        //获得查询结果, 开始处理返参
        String transfersStatus = queryForWxChangeResponseDTO.getStatus();
        if (PayTransfersStatus.SUCCESS.getCode().equals(transfersStatus)) {
            toWxChangeVO.setTransfersResult(true);
            toWxChangeVO.setErrorDesc(null);
        } else if (PayTransfersStatus.FAILED.getCode().equals(transfersStatus)) {
            //以接口返参为准
            if(StringUtils.isBlank(toWxChangeVO.getErrorDesc())){
                toWxChangeVO.setErrorDesc(queryForWxChangeResponseDTO.getReason());
            }
        } else if (PayTransfersStatus.PROCESSING.getCode().equals(transfersStatus)) {
            toWxChangeVO.setTransfersResult(true);
            toWxChangeVO.setAbleContinue(false);
            toWxChangeVO.setErrorDesc(PayTransfersStatus.PROCESSING.getText());
        } else {
            logger.error("Transfers query result return UNKNOW STATUS, partnerTradeNo : " + toWxChangeVO.getPartnerTradeNo());
        }
        return toWxChangeVO;
    }

    /**
     * 转账结果失败时重新查询, 确认结果
     * @param
     * @throws Exception
     */
    private PayTransfersQueryForWxChangeResponseDTO getExactResultForWxChange(String partnerTradeNo, int queryTimes) throws Exception {
        PayTransfersQueryForWxChangeResponseDTO queryForWxChangeResponseDTO = new PayTransfersQueryForWxChangeResponseDTO();
        //请求查询接口参数
        PayTransfersQueryForWxChangeDTO queryForWxChangeDTO = new PayTransfersQueryForWxChangeDTO();
        queryForWxChangeDTO.setMchId(payConfig.getMchID());
        queryForWxChangeDTO.setAppid(payConfig.getAppID());
        queryForWxChangeDTO.setPartnerTradeNo(partnerTradeNo);
        queryForWxChangeDTO.setNonceStr(WXPayUtil.generateNonceStr());
        //处理签名
        queryForWxChangeDTO.setSign(WXPayUtil.generateSignature(XmlUtil.bean2Map(queryForWxChangeDTO), payConfig.getKey()));
        //返参
        String resultXml = wxPayApi.queryTransferToChange(XmlUtil.bean2Map(queryForWxChangeDTO));
        System.out.println(resultXml);
        if(StringUtils.isNotBlank(resultXml)){
            queryForWxChangeResponseDTO = XmlUtil.xml2Bean(resultXml, PayTransfersQueryForWxChangeResponseDTO.class);
        }
        if(queryTimes > 1 && StringUtils.isNotBlank(queryForWxChangeResponseDTO.getStatus()) &&
                PayTransfersStatus.PROCESSING.getCode().equals(queryForWxChangeResponseDTO.getStatus())){
            logger.info("Pay transfers is PROCESSING, sleep for 1 second.");
            Thread.sleep(1 * 1000);
            queryForWxChangeResponseDTO = getExactResultForWxChange(partnerTradeNo, --queryTimes);
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
                                                  PayTransfersToWxChangeResponseDTO responseDTO,
                                                  PayTransfersToWxChangeVO toWxChangeVO) throws ParseException {
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
        record.setAccountId(wxChangeDTO.getOpenid());
        record.setCheckName(wxChangeDTO.getCheckName());
        record.setAccountName(wxChangeDTO.getReUserName());
        //设置渠道&金额信息
        record.setChannel(TransfersChannelType.WXBALANCE.getCode());
        record.setChannelCode(String.valueOf(TransfersChannelCodeTypeEnum.WXBALANCE.getCode()));
        //转账至零钱接口不返回金额信息, 从入参中获取
        record.setTotalFee(wxChangeDTO.getAmount());
        record.setTotalAmount(new BigDecimal(wxChangeDTO.getAmount()).divide(UNITS).setScale(2,RoundingMode.HALF_UP));

        record.setCmmsFee(0);
        record.setCmmsAmount(DEFAULT_CMMS);

        record.setRealFee(record.getTotalFee());
        record.setRealAmount(record.getTotalAmount());
        //设置其他
        record.setDesc(wxChangeDTO.getDesc());
        record.setSpbillCreateIp(wxChangeDTO.getSpbillCreateIp());
        if(StringUtils.isNotBlank(responseDTO.getPaymentTime())){
            record.setTimeEnd(DateUtil.toDate(responseDTO.getPaymentTime(), DATE_FORMAT));
        }
        record.setStatus((short)(toWxChangeVO.isTransfersResult() ? 1 : 0));
        if(0 == record.getStatus()) {
            record.setErrorCode(responseDTO.getErrCode());
            record.setErrorMsg(toWxChangeVO.getErrorDesc());
        }
        //设置创建人&创建时间
        record.setCreatedBy(toWxBalanceCondition.getOperaterID());
        record.setCreated(new Date(System.currentTimeMillis()));
        payTransfersMapper.insertSelective(record);
    }

    @Override
    public PayTransfersToWxBankVO transfersToBank(PayTransfersToWxBankCondition toWxBankCondition) {
        PayTransfersToWxBankVO toWxBankVO = new PayTransfersToWxBankVO();
        try {
            //必填验证
            checkNecessaryFieldForBank(toWxBankCondition);
            //准备wx侧请求参数
            PayTransfersForWxBankDTO wxBankDTO = getReqParamForBank(toWxBankCondition);
            //处理微信请求及结果
            String respxml = wxPayApi.transferToBank(XmlUtil.bean2MapUnderline2Hump(wxBankDTO));
            System.out.println(respxml);
            PayTransfersToWxBankResponseDTO responseDTO = XmlUtil.xml2Bean(respxml, PayTransfersToWxBankResponseDTO.class);
            //准备返参
            toWxBankVO = praseResultForBank(responseDTO);
            //错误情况下调用查询确认转账情况
            if(!toWxBankVO.isTransfersResult()){
                //调用查询接口确认转账详情(由于处理中状态存在, 该方法最多尝试3次查询)
                toWxBankVO = confirmTransfersToWxBankResult(toWxBankVO, wxBankDTO);
            }
            //保存请求流水
            savePayTransfersToWxBankRecord(toWxBankCondition, wxBankDTO, responseDTO, toWxBankVO);
        } catch (Exception ex) {
            logger.error("TransfersToBank error.", ex);
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
        forWxBankDTO.setMchId(payConfig.getMchID());
        forWxBankDTO.setPartnerTradeNo(toWxBankCondition.getPartnerTradeNo());
        /**
         * NonceStr, 如果不是第一次进行请求,则须和前一次相同
         */
        PayTransfers records =
                payTransfersMapper.selectTOP1TransfersByPartnerTradeNoAndPaymentNo(toWxBankCondition.getPartnerTradeNo(), "");
        if(null == records){
            forWxBankDTO.setNonceStr(WXPayUtil.generateNonceStr());
        } else {
            forWxBankDTO.setNonceStr(records.getNonceStr());
        }
        //处理卡号姓名加密rsa
        forWxBankDTO.setEncBankNo(GetRSA.getRSA(toWxBankCondition.getAccount(), StringEscapeUtils.unescapeJava(payConfig.getRSAPublicKey())));
        forWxBankDTO.setEncTrueName(GetRSA.getRSA(toWxBankCondition.getAccountName(), StringEscapeUtils.unescapeJava(payConfig.getRSAPublicKey())));
        forWxBankDTO.setBankCode(String.valueOf(toWxBankCondition.getChannelCode().getCode()));
        forWxBankDTO.setAmount(toWxBankCondition.getTotalAmount().multiply(UNITS).intValue());
        forWxBankDTO.setDesc(toWxBankCondition.getDesc());
        //处理签名
        forWxBankDTO.setSign(WXPayUtil.generateSignature(XmlUtil.bean2MapUnderline2Hump(forWxBankDTO), payConfig.getKey()));
        return forWxBankDTO;
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
        if(TransfersToWxError.SUCCESS.getCode().equals(resultCode)){
            toWxBankVO.setTransfersResult(true);
            toWxBankVO.setErrorDesc(TransfersToWxError.SUCCESS.getText());
            toWxBankVO.setAbleContinue(false);
        } else {
            String errCode = responseDTO.getErrCode();
            for (TransfersToWxError error : TransfersToWxError.values()) {
                if (error.getCode().equals(errCode)) {
                    toWxBankVO.setTransfersResult(error.getCode().equals(TransfersToWxError.SUCCESS.getCode()));
                    if(StringUtils.isNotBlank(responseDTO.getErrCodeDes())){
                        toWxBankVO.setErrorDesc(responseDTO.getErrCodeDes());
                    } else {
                        toWxBankVO.setErrorDesc(error.getText());
                    }
                    toWxBankVO.setAbleContinue(error.getAbleContinue());
                    break;
                }
            }
        }
        toWxBankVO.setErrorCode(responseDTO.getErrCode());
        //设置其他信息(同名属性)
        BeanUtils.copyProperties(responseDTO, toWxBankVO);
        //设置金额及手续费信息
        toWxBankVO.setAmount(new BigDecimal(responseDTO.getAmount() == null ? 0 : responseDTO.getAmount()).
                divide(UNITS, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
        toWxBankVO.setCmmsAmt(new BigDecimal(responseDTO.getCmmsAmt() == null ? 0 : responseDTO.getCmmsAmt()).
                divide(UNITS, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
        return toWxBankVO;
    }

    /**
     * 调用查询wx转账到银行卡接口, 得到准确转账信息
     * @param toWxChangeVO 接口返参
     * @return 更正后接口返参
     * @throws Exception
     */
    private PayTransfersToWxBankVO confirmTransfersToWxBankResult(PayTransfersToWxBankVO toWxBankVO, PayTransfersForWxBankDTO wxBankDTO)  throws Exception{
        PayTransfersQueryToWxBankVO queryToWxBankVO = getExactResultForWxBank(wxBankDTO.getPartnerTradeNo());
        /*if (null == queryToWxBankVO) {
            logger.error("Transfers result query failed, partnerTradeNo : " + toWxBankVO.getPartnerTradeNo());
            return toWxBankVO;
        }*/
        //获得查询结果, 开始处理返参
        if(StringUtils.isNotBlank(queryToWxBankVO.getResultCode()) &&
                PayTransfersStatus.FAIL.getCode().equals(queryToWxBankVO.getResultCode())){
            return toWxBankVO;
        }
        //以查询结果返回的错误码为准
        toWxBankVO.setErrorCode(queryToWxBankVO.getErrCode());
        String transfersStatus = queryToWxBankVO.getStatus();
        if (PayTransfersStatus.SUCCESS.getCode().equals(transfersStatus)) {
            toWxBankVO.setTransfersResult(true);
            toWxBankVO.setErrorDesc(null);
        } else if (PayTransfersStatus.FAILED.getCode().equals(transfersStatus)) {
            //以接口返参为准
            if(StringUtils.isBlank(toWxBankVO.getErrorDesc())){
                toWxBankVO.setErrorDesc(queryToWxBankVO.getReason());
            }
        } else if (PayTransfersStatus.PROCESSING.getCode().equals(transfersStatus)) {
            toWxBankVO.setTransfersResult(true);
            toWxBankVO.setAbleContinue(false);
            toWxBankVO.setErrorDesc(PayTransfersStatus.PROCESSING.getText());
        } else {
            logger.error("Transfers query result return UNKNOW STATUS, partnerTradeNo : " + toWxBankVO.getPartnerTradeNo());
        }

        return toWxBankVO;
    }

    /**
     * 重新查询转账结果, 确认结果
     *
     * @param toWxBankVO wx转账至银行卡接口返参
     * @throws Exception
     */
    @Override
    public PayTransfersQueryToWxBankVO getExactResultForWxBank(String partnerTradeNo) throws Exception {
        PayTransfersQueryToWxBankVO queryToWxBankVO = new PayTransfersQueryToWxBankVO();
        //请求查询接口参数
        PayTransfersQueryForWxBankDTO queryForWxBankDTO = new PayTransfersQueryForWxBankDTO();
        queryForWxBankDTO.setMchId(payConfig.getMchID());
        queryForWxBankDTO.setPartnerTradeNo(partnerTradeNo);
        queryForWxBankDTO.setNonceStr(WXPayUtil.generateNonceStr());
        //处理签名
        queryForWxBankDTO.setSign(WXPayUtil.generateSignature(XmlUtil.bean2MapUnderline2Hump(queryForWxBankDTO), payConfig.getKey()));
        //返参
        String resultXml = wxPayApi.queryTransferToBank(XmlUtil.bean2MapUnderline2Hump(queryForWxBankDTO));
        System.out.println(resultXml);
        PayTransfersQueryForWxBankResponseDTO queryForWxBankResponseDTO = new PayTransfersQueryForWxBankResponseDTO();
        if(StringUtils.isNotBlank(resultXml)){
            queryForWxBankResponseDTO = XmlUtil.xml2Bean(resultXml, PayTransfersQueryForWxBankResponseDTO.class);
        }
        BeanUtils.copyProperties(queryForWxBankResponseDTO, queryToWxBankVO);
        queryToWxBankVO.setAmount(new BigDecimal(queryForWxBankResponseDTO.getAmount()).
                divide(UNITS,2,RoundingMode.HALF_UP));
        queryToWxBankVO.setCmmsAmt(new BigDecimal(queryForWxBankResponseDTO.getCmmsAmt()).
                divide(UNITS,2,RoundingMode.HALF_UP));
/*      //舍弃递归, 转账至银行卡过程缓慢, 再次查询没有意义
        if(queryTimes > 1 && StringUtils.isNotBlank(queryForWxBankResponseDTO.getStatus()) &&
                PayTransfersStatus.PROCESSING.getCode().equals(queryForWxBankResponseDTO.getStatus())){
            logger.info("Pay transfers is PROCESSING, sleep for 1 second.");
            Thread.sleep(1 * 1000);
            queryForWxBankResponseDTO = getExactResultForWxBank(partnerTradeNo, --queryTimes);
        }*/
        return queryToWxBankVO;
    }

    private void savePayTransfersToWxBankRecord(PayTransfersToWxBankCondition toWxBankCondition,
                                                PayTransfersForWxBankDTO wxBankDTO,
                                                PayTransfersToWxBankResponseDTO responseDTO,
                                                PayTransfersToWxBankVO toWxBankVO){
        PayTransfers record = new PayTransfers();
        //设置商户,设备等基本信息
        record.setMchid(wxBankDTO.getMchId());
        record.setNonceStr(wxBankDTO.getNonceStr());
        record.setSign(wxBankDTO.getSign());
        //设置流水记录信息
        record.setPartnerTradeNo(wxBankDTO.getPartnerTradeNo());
        record.setTransactionId(responseDTO.getPaymentNo());
        record.setAccount(toWxBankCondition.getAccount());
        record.setAccountName(toWxBankCondition.getAccountName());
        //设置渠道&金额信息
        record.setChannel(TransfersChannelType.WXBBANK.getCode());
        record.setChannelCode(wxBankDTO.getBankCode());
        //转账至wx银行卡是返参中包含金额信息, 以返参中金额信息为准
        record.setTotalFee(responseDTO.getAmount() == null ? Integer.valueOf(0) : responseDTO.getAmount());
        record.setTotalAmount(new BigDecimal(responseDTO.getAmount() == null ? 0 : responseDTO.getAmount()).
                divide(UNITS).setScale(2,RoundingMode.HALF_UP));

        record.setCmmsFee(responseDTO.getCmmsAmt() == null ? Integer.valueOf(0) : responseDTO.getCmmsAmt());
        record.setCmmsAmount(new BigDecimal(responseDTO.getCmmsAmt() == null ? 0 : responseDTO.getCmmsAmt()).
                divide(UNITS).setScale(2,RoundingMode.HALF_UP));

        record.setRealFee(record.getTotalFee()-record.getCmmsFee());
        record.setRealAmount(record.getTotalAmount().subtract(record.getCmmsAmount()));
        //设置其他
        record.setDesc(wxBankDTO.getDesc());
        //转账到银行卡默认置位处理中
        record.setStatus((short)(toWxBankVO.isTransfersResult() ? 2 : 0));
        if(0 == record.getStatus()) {
            record.setErrorCode(responseDTO.getErrCode());
            record.setErrorMsg(toWxBankVO.getErrorDesc());
        }
        //设置创建人&创建时间
        record.setCreatedBy(toWxBankCondition.getOperaterID());
        record.setCreated(new Date(System.currentTimeMillis()));
        payTransfersMapper.insertSelective(record);
    }

    @Override
    public void modifyTransfersToBankStatus(PayTransfersQueryToWxBankVO resultForWxBank) {
        PayTransfers payTransfers = payTransfersMapper.selectTOP1TransfersByPartnerTradeNoAndPaymentNo(
                resultForWxBank.getPartnerTradeNo(), resultForWxBank.getPaymentNo());
        try {
            payTransfers.setTimeEnd(DateUtil.toDate(resultForWxBank.getPaySuccTime(), DATE_FORMAT));
        } catch (ParseException e) {
            logger.error("时间解析错误", e);
        }
        String transfersStatus = resultForWxBank.getStatus();
        if(PayTransfersStatus.SUCCESS.getCode().equals(transfersStatus)){
            payTransfers.setStatus((short) 1);
        } else if (PayTransfersStatus.FAILED.getCode().equals(transfersStatus)) {
            payTransfers.setStatus((short) 0);
            payTransfers.setErrorMsg(resultForWxBank.getReason());
        } else if (PayTransfersStatus.BANK_FAIL.getCode().equals(transfersStatus)) {
            payTransfers.setStatus((short) 0);
            payTransfers.setErrorCode(TransfersToWxError.BANK_FAIL.getCode());
            payTransfers.setErrorMsg(resultForWxBank.getReason());
        }
        payTransfersMapper.updateByPrimaryKeySelective(payTransfers);
    }

    public static void main(String[] args) throws Exception {
        /*WXPay wxPay = new WXPay();
        SortedMap<String,String> sortedMap = new TreeMap<String,String>();
        sortedMap.put("mch_id", "1467361502");
        sortedMap.put("nonce_str", WXPayUtil.generateNonceStr());
        sortedMap.put("sign_type", "MD5");
        sortedMap.put("sign", WXPayUtil.generateSignature(sortedMap, "u29K8cDc48zhF0hKlQ3pd1tiamkZpRoS"));
        System.out.println(wxPay.publicKey(sortedMap));*/

        //System.out.println(new BigDecimal("1001").divide(UNITS).setScale(2, RoundingMode.HALF_UP).doubleValue());
        /*RequestBase preOrderDTO = new PayPreOrderDTO();
        Class<? extends RequestBase> clazz = preOrderDTO.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Field[] fields2 = clazz.getFields();
        System.out.println(fields.length);*/
    }

}
