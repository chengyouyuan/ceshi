package com.winhxd.b2c.common.domain.promotion.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.winhxd.b2c.common.domain.promotion.util.BaseExcelDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author sunwenwu
 * @date 2018/9/26
 */
@ApiModel("优惠券活动导入用戶信息")
@Data
public class CouponActivityImportCustomerVO extends BaseExcelDomain {
	protected String errorMsg;

	@Override
	public String getErrorMsg() {
		return errorMsg;
	}

	@Override
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

    @ApiModelProperty(value = "手机号")
    @Excel(name = "手机号", orderNum = "1")
    private String phone;

    @ApiModelProperty(value = "用户名")
    @Excel(name = "用户名", orderNum = "2")
    private String userName;
}
