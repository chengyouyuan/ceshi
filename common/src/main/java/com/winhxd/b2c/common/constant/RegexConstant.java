package com.winhxd.b2c.common.constant;

import java.util.regex.Pattern;

/**
 * 正则常量
 *
 * @author liutong
 * @date 2018-08-21 16:37:26
 */
public class RegexConstant {
    /**
     * 店铺名称
     */
    public static final Pattern STORE_NAME_PATTERN = Pattern.compile("^[\\w-\u4e00-\u9fa5]{1,15}$");
    /**
     * 提货地址
     */
    public static final Pattern STORE_ADDRESS_PATTERN = Pattern.compile("^[\\w-\u4e00-\u9fa5]{1,30}$");
    /**
     * 联系人
     */
    public static final Pattern SHOPKEEPER_PATTERN = Pattern.compile("^[\\w-\u4e00-\u9fa5]{1,10}$");
    /**
     * 联系方式
     */
    public static final Pattern CONTACT_MOBILE_PATTERN = Pattern.compile("^1[3-9][0-9]\\d{8}$");
}
