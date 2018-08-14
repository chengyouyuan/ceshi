package com.winhxd.b2c.common.domain.pay.vo;

import com.winhxd.b2c.common.domain.pay.model.AccountingDetail;
import lombok.Data;

@Data
public class VerifyDetailVO extends AccountingDetail {

    private String detailTypeName;
    private String storeName;
    private String thirdPartyVerifyStatusName;
    private String verifyStatusName;

    public String getDetailTypeName() {
        return DetailTypeEnum.getMemoOfCode(getDetailType());
    }

    public String getThirdPartyVerifyStatusName() {
        return ThirdPartyVerifyStatusEnum.getMemoOfCode(getThirdPartyVerifyStatus());
    }

    public String getVerifyStatusName() {
        return VerifyStatusEnum.getMemoOfCode(getVerifyStatus());
    }
}
