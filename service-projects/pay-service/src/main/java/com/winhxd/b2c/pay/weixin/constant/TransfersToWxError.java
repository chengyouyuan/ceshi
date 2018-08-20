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
    FAILTOREACH("FAIL", "通信失败, 请求未抵达微信侧, 请重试.", true),
    /**
     * 转账至微信错误码类型枚举
     */
    NO_AUTH("NO_AUTH", "没有该接口权限",true),
    INVALID_REQUEST("INVALID_REQUEST", "无效的请求, 商户系统异常导致, 商户权限异常,证书错误,频率限制等", true),
    SYSTEMERROR("SYSTEMERROR", "业务错误导致交易失败", true),
    PARAM_ERROR("PARAM_ERROR", "参数错误, 商户系统异常导致", false),
    OPENID_ERROR("OPENID_ERROR", "Openid错误", false),
    SEND_FAILED("SEND_FAILED", "付款错误",false),
    NAME_MISMATCH("NAME_MISMATCH", "姓名校验出错", false),
    SIGNERROR("SIGNERROR", "签名错误", false),
    XML_ERROR("XML_ERROR", "Post内容出错", false),
    AMOUNT_LIMIT("AMOUNT_LIMIT", "超额;已达到今日付款金额上限或已达到今日银行卡收款金额上限", true),
    MONEY_LIMIT("MONEY_LIMIT", "已经达到今日付款总额上限/已达到付款给此用户额度上限",true),
    ORDERPAID("ORDERPAID", "超过付款重入有效期", false),
    FATAL_ERROR("FATAL_ERROR", "已存在该单, 并且订单信息不一致;或订单太老", false),
    NOTENOUGH("NOTENOUGH", "账号余额不足", true),
    FREQUENCY_LIMITED("FREQUENCY_LIMITED", "超过每分钟600次的频率限制", true),
    CA_ERROR("CA_ERROR", "商户API证书校验出错", true),
    V2_ACCOUNT_SIMPLE_BAN("V2_ACCOUNT_SIMPLE_BAN", "无法给非实名用户付款", false),
    PARAM_IS_NOT_UTF8("PARAM_IS_NOT_UTF8", "请求参数中包含非utf8编码字符", false),
    SENDNUM_LIMIT("SENDNUM_LIMIT", "该用户今日付款次数超过限制,如有需要请登录微信支付商户平台更改API安全配置", true),
    SUCCESS("SUCCESS", "Wx侧受理成功", false);

    private String code;

    private String text;

    /**
     * @see com.winhxd.b2c.common.domain.pay.vo.PayTransfersToWxChangeVO.ableContinue
     */
    private boolean ableContinue;

    private TransfersToWxError(String code, String text, boolean ableContinue){
        this.code = code;
        this.text = text;
        this.ableContinue = ableContinue;
    }

    private TransfersToWxError(){
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public boolean getAbleContinue() {
        return ableContinue;
    }

}
