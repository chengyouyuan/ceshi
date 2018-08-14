package com.winhxd.b2c.common.domain.pay.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccountingDetail {

    private Long id;
    private String orderNo;

    /**
     * 费用类型
     */
    public enum DetailTypeEnum {

        REAL_PAY(1, "实付款"), FEE_OF_WX(2, "交易手续费"), DISCOUNT(3, "促销费");

        private int code;
        private String memo;

        DetailTypeEnum(int code, String memo) {
            this.code = code;
            this.memo = memo;
        }

        public int getCode() {
            return code;
        }

        public String getMemo() {
            return memo;
        }

        public static String getMemoOfCode(int code) {
            for (DetailTypeEnum value : values()) {
                if (value.code == code) {
                    return value.memo;
                }
            }
            return null;
        }
    }

    private Integer detailType;
    /**
     * 费用金额
     */
    private BigDecimal detailMoney;
    private Long storeId;
    /**
     * 订单是否完成：0-未完成；1-已完成；
     */
    private Integer orderCompleteStatus;
    /**
     * 入账时间
     */
    private Date recordedTime;
    /**
     * 记录插入时间
     */
    private Date insertTime;

    /**
     * 支付平台与惠下单结算状态
     */
    public enum ThirdPartyVerifyStatusEnum {

        NOT_VERIFIED(0, "未结算"), VERIFIED(1, "已结算");

        private int code;
        private String memo;

        public int getCode() {
            return code;
        }

        public String getMemo() {
            return memo;
        }

        ThirdPartyVerifyStatusEnum(int code, String memo) {
            this.code = code;
            this.memo = memo;
        }

        public static String getMemoOfCode(int code) {
            for (ThirdPartyVerifyStatusEnum value : values()) {
                if (value.code == code) {
                    return value.memo;
                }
            }
            return null;
        }
    }

    private Integer thirdPartyVerifyStatus;
    private Date thirdPartyVerifyTime;

    /**
     * 结算状态
     */
    public enum VerifyStatusEnum {

        NOT_VERIFIED(0, "未结算"), VERIFIED(1, "已结算"), PAUSED(2, "暂缓"), STOPPED(-1, "终止");

        private int code;
        private String memo;

        public int getCode() {
            return code;
        }

        public String getMemo() {
            return memo;
        }

        VerifyStatusEnum(int code, String memo) {
            this.code = code;
            this.memo = memo;
        }

        public static String getMemoOfCode(int code) {
            for (VerifyStatusEnum value : values()) {
                if (value.code == code) {
                    return value.memo;
                }
            }
            return null;
        }
    }

    private Integer verifyStatus;
    private String verifyCode;
    private Date verifyTime;
    private Date operatedTime;
    private Long operatedBy;
    private String operatedByName;
}
