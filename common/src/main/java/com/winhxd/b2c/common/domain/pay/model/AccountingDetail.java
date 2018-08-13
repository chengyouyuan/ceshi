package com.winhxd.b2c.common.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

public class AccountingDetail {

    private Long id;
    private String orderNo;

    /**
     * 费用类型
     */
    public enum DetailTypeEnum {

        REAL_PAY(1, "实付款"), FEE_OF_WX(2, "交易手续费"),
        COUPON_OF_HXD(3, "惠下单出资优惠券"), COUPON_OF_BRAND(4, "品牌出资优惠券");

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
    private String detailTypeName;
    /**
     * 费用金额
     */
    private BigDecimal detailMoney;
    private Long storeId;
    private String storeName;
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
    private String thirdPartyVerifyStatusName;
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
    private String verifyStatusName;
    private String verifyCode;
    private Date verifyTime;
    private Date operatedTime;
    private Long operatedBy;
    private String operatedByName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getDetailType() {
        return detailType;
    }

    public void setDetailType(Integer detailType) {
        this.detailType = detailType;
    }

    public String getDetailTypeName() {
        return DetailTypeEnum.getMemoOfCode(detailType);
    }

    public BigDecimal getDetailMoney() {
        return detailMoney;
    }

    public void setDetailMoney(BigDecimal detailMoney) {
        this.detailMoney = detailMoney;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Date getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(Date recordedTime) {
        this.recordedTime = recordedTime;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Integer getThirdPartyVerifyStatus() {
        return thirdPartyVerifyStatus;
    }

    public void setThirdPartyVerifyStatus(Integer thirdPartyVerifyStatus) {
        this.thirdPartyVerifyStatus = thirdPartyVerifyStatus;
    }

    public String getThirdPartyVerifyStatusName() {
        return ThirdPartyVerifyStatusEnum.getMemoOfCode(thirdPartyVerifyStatus);
    }

    public Date getThirdPartyVerifyTime() {
        return thirdPartyVerifyTime;
    }

    public void setThirdPartyVerifyTime(Date thirdPartyVerifyTime) {
        this.thirdPartyVerifyTime = thirdPartyVerifyTime;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getVerifyStatusName() {
        return VerifyStatusEnum.getMemoOfCode(verifyStatus);
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Date getOperatedTime() {
        return operatedTime;
    }

    public void setOperatedTime(Date operatedTime) {
        this.operatedTime = operatedTime;
    }

    public Long getOperatedBy() {
        return operatedBy;
    }

    public void setOperatedBy(Long operatedBy) {
        this.operatedBy = operatedBy;
    }

    public String getOperatedByName() {
        return operatedByName;
    }

    public void setOperatedByName(String operatedByName) {
        this.operatedByName = operatedByName;
    }
}
