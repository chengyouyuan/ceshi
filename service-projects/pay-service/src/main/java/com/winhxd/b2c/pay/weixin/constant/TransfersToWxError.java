package com.winhxd.b2c.pay.weixin.constant;

/**
 * TransfersToWxError
 *
 * @Author yindanqing
 * @Date 2018/8/17 11:15
 * @Description: 转账至微信错误码枚举
 */
public enum TransfersToWxError {

    INSTANCE,

    /**
     * 自定义错误类型
     */
    FAILTOREACH("FAIL","通信失败, 请求未抵达微信侧, 请重试."),
    /**
     * 转账至微信错误码类型枚举
     */
    INVALID_REQUEST("INVALID_REQUEST","无效的请求, 商户系统异常导致, 商户权限异常,证书错误,频率限制等"),
    SYSTEMERROR("SYSTEMERROR","业务错误导致交易失败"),
    PARAM_ERROR("PARAM_ERROR","参数错误, 商户系统异常导致"),
    SIGNERROR("SIGNERROR","签名错误"),
    AMOUNT_LIMIT("AMOUNT_LIMIT","超额;已达到今日付款金额上限或已达到今日银行卡收款金额上限"),
    ORDERPAID("ORDERPAID","超过付款重入有效期"),
    FATAL_ERROR("FATAL_ERROR","已存在该单, 并且订单信息不一致;或订单太老"),
    NOTENOUGH("NOTENOUGH","账号余额不足"),
    FREQUENCY_LIMITED("FREQUENCY_LIMITED","超过每分钟600次的频率限制"),
    SUCCESS("SUCCESS","Wx侧受理成功");

    private String code;

    private String text;

    private TransfersToWxError(String code, String text){
        this.code = code;
        this.text = text;
    }

    private TransfersToWxError(){
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

}
