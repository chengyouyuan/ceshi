package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

/**
 * 下载对账单、资金账单返回对象
 * @author yuluyuan
 *
 * 2018年8月19日
 */
@Data
public class PayBillDownloadResponseDTO extends ResponseBase{

    /**
     * 业务结果:SUCCESS/FAIL
     */
    private String resultCode;

    /**
     * 错误代码
     */
    private String errCode;

    /**
     * 错误代码描述
     */
    private String errCodeDes;
    
    /**
     * 返回数据
     */
    private String data;
    
}
