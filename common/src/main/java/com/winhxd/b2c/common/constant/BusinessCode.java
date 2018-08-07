package com.winhxd.b2c.common.constant;

/**
 * @author lixiaodong
 */
public final class BusinessCode {
    /** 成功 */
    public static final int CODE_OK = 0;
    /** 服务器内部错误 */
    public static final int CODE_1001 = 1001;
    /** 登录凭证无效 */
    public static final int CODE_1002 = 1002;

    /** 没有权限 */
    public static final int CODE_1003 = 1003;
    /** 账号无效 */
    public static final int CODE_1004 = 1004;
    /** 密码错误 */
    public static final int CODE_1005 = 1005;
    /**  账号未启用 */
    public static final int CODE_1006 = 1006;
    /** 参数无效 */
    public static final int CODE_1007 = 1007;
    /** 验证码错误 */
    public static final int CODE_1008 = 1008;


    /** 原密码输入错误 */
    public static final int CODE_300021 = 302001;
    /** 新密码与原密码相同 */
    public static final int CODE_302002 = 302002;

    /** 购物车:门店ID为空*/
    public static final int CODE_402001 = 402001;
    /** 购物车:自提地址为空*/
    public static final int CODE_402002 = 402002;
    /** 购物车:自提时间为空*/
    public static final int CODE_402003 = 402003;
    /** 购物车:商品信息为空*/
    public static final int CODE_402004 = 402004;
    /** 购物车:支付方式*/
    public static final int CODE_402006 = 402006;
    /** 购物车:订单金额*/
    public static final int CODE_402007 = 402007;
    /** 购物车:参数错误*/
    public static final int CODE_402008 = 402008;
    /**用户id参数为空*/
    public static final int CODE_200001 = 200001;
    /**门店id参数为空*/
    public static final int CODE_200002 = 200002;
    /**门店用户绑定失败*/
    public static final int CODE_200003 = 200003;
    /**门店信息不存在*/
    public  static final int CODE_200004 = 200004;
    /**门店基础信息保存参数错误*/
    public  static final int CODE_200005 = 200005;
    /**店铺营业信息保存参数错误*/
    public  static final int CODE_200006 = 200006;
    /**缺少用户状态status参数*/
    public static final int CODE_200007 = 200007;
    /**更新用户状态失败*/
    public static final int CODE_200008 = 200008;


    /** 购物车:商品下架或删除*/
    public static final int CODE_402010 = 402010;
    /** 购物车:商品信息不存在或获取商品数量不正确*/
    public static final int CODE_402011 = 402011;


    /** 查询订单参数异常*/
    public static final int CODE_411001 = 411001;

    /** 不存在符合的优惠券活动*/
    public static final int CODE_500001 = 500001;

    /** 该手机号已经享受过新用户福利*/
    public static final int CODE_500002 = 500002;


}
