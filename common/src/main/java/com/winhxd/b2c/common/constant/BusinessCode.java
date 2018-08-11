package com.winhxd.b2c.common.constant;

import io.swagger.annotations.ApiModelProperty;

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
    /** 账号未启用 */
    public static final int CODE_1006 = 1006;
    /** 参数无效 */
    public static final int CODE_1007 = 1007;
    /** 验证码错误 */
    public static final int CODE_1008 = 1008;
    /** 未定义的API */
    public static final int CODE_1009 = 1009;
    /** 该微信号已绑定过账号 */
    public static final int CODE_1010 = 1010;
    /** 微信快捷登录绑定账号无效 */
    public static final int CODE_1011 = 1011;
    /**
     * 验证码请求时长没有超过一分钟
     */
    public static final int CODE_1012 = 1012;
    /** 该账号已存在 */
    public static final int CODE_1013 = 1013;
    /**token参数为空*/
    public static final int CODE_1014 = 1014;
    /**请求超时*/
    public static final int CODE_1015 = 1015;

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
    /** 购物车:参数错误*/
    public static final int CODE_402008 = 402008;
    /**订单id参数为空*/
    public static final int CODE_402013 = 402013;
    /**用户id参数为空*/
    public static final int CODE_200001 = 200001;
    /**门店id参数为空*/
    public static final int CODE_200002 = 200002;
    /**用户已经和其他门店绑定*/
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
    /**用户绑定的门店不存在*/
    public static final int CODE_200009 = 200009;
    /**用户信息不存在*/
    public static final int CODE_200010 = 200010;
    /**当前用户和门店已经存在绑定关系*/
    public static final int CODE_200011 = 200011;
    /**门店商品skuCode无效*/
    public static final int CODE_200012 = 200012;
    /**sku已上架**/
    public static final int CODE_200013 = 200013;
    /**上传图片格式不正确**/
    public static final int CODE_200014 = 200014;



    /** 购物车:商品下架或删除*/
    public static final int CODE_402010 = 402010;
    /** 购物车:商品信息不存在或获取商品数量不正确*/
    public static final int CODE_402011 = 402011;
    /** 购物车:购物车商品价格有变动*/
    public static final int CODE_402012 = 402012;
    /** 购物车:用户下单操作频繁*/
    public static final int CODE_402014 = 402014;


    /** 查询订单参数异常*/
    public static final int CODE_411001 = 411001;

    /** 不存在符合的优惠券活动*/
    public static final int CODE_500001 = 500001;

    /** 该手机号已经享受过新用户福利*/
    public static final int CODE_500002 = 500002;
    

    @ApiModelProperty("订单创建客户id为空")
    public static final int CODE_401001 = 401001;
    
    @ApiModelProperty("订单创建支付类型为空或错误")
    public static final int CODE_401003 = 401003;
    
    @ApiModelProperty("订单创建自提时间为空")
    public static final int CODE_401004 = 401004;
    
    @ApiModelProperty("订单创建商品信息为空")
    public static final int CODE_401005 = 401005;
    
    @ApiModelProperty("订单创建商品数量信息错误")
    public static final int CODE_401006 = 401006;
    
    @ApiModelProperty("订单创建商品sku为空")
    public static final int CODE_401007 = 401007;
    
    @ApiModelProperty("订单创建不支持订单类型")
    public static final int CODE_401008 = 401008;
    
    @ApiModelProperty("订单创建优惠券使用失败")
    public static final int CODE_401009 = 401009;

    @ApiModelProperty("订单号错误")
    public static final int WRONG_ORDERNO = 400000;
    
    @ApiModelProperty("订单已支付")
    public static final int ORDER_ALREADY_PAID = 400001;
    
    @ApiModelProperty("订单状态修改失败")
    public static final int ORDER_STATUS_CHANGE_FAILURE = 400002;
    
    @ApiModelProperty("订单号为空")
    public static final int ORDER_NO_EMPTY = 400003;
    
    @ApiModelProperty("提货码为空")
    public static final int ORDER_PICK_UP_CODE_WRONG = 400004;

    @ApiModelProperty("门店id为空")
    public static final int STORE_ID_EMPTY = 400005;
    
    @ApiModelProperty("订单状态错误")
    public static final int WRONG_ORDER_STATUS = 400006;
    
    @ApiModelProperty("订单金额错误")
    public static final int WRONG_ORDER_TOTAL_MONEY = 400007;
    
    @ApiModelProperty("订单提货码错误")
    public static final int WRONG_ORDER_PICKUP_CODE = 400008;

    @ApiModelProperty("门店id错误")
    public static final int WRONG_STORE_ID = 400009;

    @ApiModelProperty("顾客id错误")
    public static final int WRONG_CUSTOMER_ID = 400010;

    @ApiModelProperty("订单不存在")
    public static final int ORDER_DOES_NOT_EXIST = 400011;

    @ApiModelProperty("订单正在修改中")
    public static final int ORDER_IS_BEING_MODIFIED = 400012;

    /** 参数异常*/
    public static final int CODE_422001 = 422001;
    /** 未支付的订单不允许退款*/
    public static final int CODE_422002 = 422002;
    /** 已完成的订单不允许退款*/
    public static final int CODE_422003 = 422003;
    /** 订单修改中*/
    public static final int CODE_422004 = 422004;

    /** 订单状态不允许退款*/
    public static final int CODE_421002 = 421002;
    /** 参数错误*/
    public static final int CODE_421001 = 421001;
    /** 订单号不能为空*/
    public static final int CODE_420001 = 420001;
    /** 订单已支付成功不能取消*/
    public static final int CODE_420002 = 420002;
    /** 用户不存在*/
    public static final int CODE_410001 = 410001;
    /** 订单号错误*/
    public static final int CODE_420003 = 420003;
    /** 取消订单状态更新失败*/
    public static final int CODE_420004 = 420004;
    /** 订单取消处理用户退款失败*/
    public static final int CODE_422005 = 422005;
    /** 订单取消退优惠券失败*/
    public static final int CODE_422006 = 422006;

    @ApiModelProperty("查询的地理区域不存在")
    public static final int CODE_320001 = 320001;
    
    @ApiModelProperty("当前用户没有银行卡信息")
    public static final int CODE_6101 = 6101;
    @ApiModelProperty("银行名称为空")
    public static final int CODE_6111 = 6111;
    @ApiModelProperty("银行卡卡号为空")
    public static final int CODE_6112 = 6112;
    @ApiModelProperty("开户人姓名为空")
    public static final int CODE_6113 = 6113;
    @ApiModelProperty("开户支行或分行为空")
    public static final int CODE_6114 = 6114;
    @ApiModelProperty("手机号为空")
    public static final int CODE_6115 = 6115;
    @ApiModelProperty("验证码为空")
    public static final int CODE_6116 = 6116;

    /** 测试门店区域:区域重复*/
    public static final int CODE_103901 = 103901;
}
