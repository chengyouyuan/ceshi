package com.winhxd.b2c.common.constant;

/**
 * 客户端相关常量
 */
public class AppConstant {

    /**
     * C端用户grp
     */
    public static final String GRP_CUSTOMER = "winretailrc";

    /**
     * 门店用户grp
     */
    public static final String GRP_STORE = "winretailrb";
    
    /**
     * 发送短信有效时间五分钟
     */
    
    public static final int  SEND_SMS_EXPIRE_SECOND = 5 * 60;
    /**
     * 已发送短信,60秒以后调用短信服务
     */
    
    public static final int  REQUEST_SEND_SMS_EXPIRE_SECOND =  60;
    
    /**
     * C端,B端登录token有效时长时间30天
     */
    public static final int  LOGIN_APP_TOKEN_EXPIRE_SECOND =  30 * 24 * 60 * 60;
}
