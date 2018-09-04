package com.winhxd.b2c.common.domain.pay.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("费用明细")
@Data
public class AccountingDetail {

    private Long id;

    @ApiModelProperty("订单号")
    private String orderNo;

    /**
     * 费用类型
     */
    public enum DetailTypeEnum {

        REAL_PAY(1, "订单货款"), FEE_OF_WX(2, "手续费"), DISCOUNT(3, "优惠抵扣");

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

    @ApiModelProperty("费用类型")
    private Integer detailType;

    @ApiModelProperty("费用金额")
    private BigDecimal detailMoney;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("订单是否完成：0-未完成；1-已完成；-1-取消")
    private Integer orderCompleteStatus;

    @ApiModelProperty("入账时间")
    private Date recordedTime;

    @ApiModelProperty("记录插入时间")
    private Date insertTime;

    /**
     * 支付平台与惠下单结算状态
     */
    public enum ThirdPartyVerifyStatusEnum {

        NOT_ACCEPT(-1, "对账异常"), NOT_VERIFIED(0, "未结算"), VERIFIED(1, "已结算");

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

    @ApiModelProperty("与支付平台结算状态")
    private Integer thirdPartyVerifyStatus;

    @ApiModelProperty("与支付平台结算时间")
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

    @ApiModelProperty("结算状态")
    private Integer verifyStatus;

    @ApiModelProperty("处理批次编码")
    private String verifyCode;

    @ApiModelProperty("结算时间")
    private Date verifyTime;

    @ApiModelProperty("操作时间")
    private Date operatedTime;

    @ApiModelProperty("操作人")
    private Long operatedBy;

    @ApiModelProperty("操作人")
    private String operatedByName;
}
