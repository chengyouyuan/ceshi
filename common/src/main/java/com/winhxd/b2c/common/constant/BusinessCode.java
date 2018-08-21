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
    /** 未定义的API */
    public static final int CODE_1009 = 1009;
    /** 该账号已存在 */
    public static final int CODE_1013 = 1013;
    /**token参数为空*/
    public static final int CODE_1014 = 1014;
    /**上传图片格式不正确**/
    public static final int CODE_100016 = 100016;
    /**上传图片是失败**/
    public static final int CODE_100017 = 100017;
    /**图片大小超过限制**/
    public static final int CODE_100018= 100018;
    /**图片名称不能为空**/
    public static final int CODE_100019= 100019;
    /**图片不能为空**/
    public static final int CODE_100020= 100020;
    /**登录凭证无效，请重新登录！**/
    public static final int CODE_102701 = 102701;
    /**业务异常**/
    public static final int CODE_102702 = 102702;
    /** 测试门店区域:区域重复*/
    public static final int CODE_103901 = 103901;
    /** 测试门店区域:查询区域失败*/
    public static final int  CODE_103902 = 103902;
    /**  验证码错误 */
    public static final int  CODE_100808 = 100808;
    /** 该微信号已绑定过其它账号 */
    public static final int  CODE_100810 = 100810;
    /**您的账号或者密码错误*/
    public static final int  CODE_100821 = 100821;
    /**您还不是惠下单用户快去注册吧*/
    public static final int  CODE_100822 = 100822;
    /**您还没有绑定惠下单账号*/
    public static final int  CODE_100819 = 100819;
    /**请求超时*/
    public static final int  CODE_100815 = 100815;
    /**验证码请求时长没有超过一分钟*/
    public static final int  CODE_100912 = 100912;
    /**您还不是惠下单用户快去注册吧*/
    public static final int  CODE_100922 = 100922;
    /** 该微信号已绑定过账号 */
    public static final int  CODE_100910 = 100910;
    
    /**1012接口参数无效*/
    public static final int CODE_101201 = 101201;
    /**账号未启用*/
    public static final int CODE_101202 = 101202;
    
    /**1013接口参数无效*/
    public static final int CODE_101301 = 101301;
    /**门店商品skuCode无效*/
    public static final int CODE_101302 = 101302;
    /**价格无效*/
    public static final int CODE_101303 = 101303;
    
    /**1014接口参数无效*/
    public static final int CODE_101401 = 101401;
    
    /**1015接口参数无效*/
    public static final int CODE_101501 = 101501;
    
    /**1024接口参数无效*/
    public static final int CODE_102401 = 102401;
    
    /**1028接口参数无效*/
    public static final int CODE_102801 = 102801;
    
    /**1031接口参数无效*/
    public static final int CODE_103101 = 103101;
    /**门店商品skuCode无效*/
    public static final int CODE_103102 = 103102;
    
    /**1049接口参数无效*/
    public static final int CODE_104901 = 104901;

    /**1022接口参数无效*/
    public static final int CODE_102201 = 102201;
    
    /**验证码错误 */
    public static final int  CODE_202108 = 202108;
    /** 该微信号已绑定过其它账号 */
    public static final int  CODE_202110 = 202110;
    /**请求超时*/
    public static final int  CODE_202115 = 202115;
    /**验证码请求时长没有超过一分钟*/
    public static final int  CODE_202212 = 202212;
    /**验证码错误 */
    public static final int  CODE_202308 = 202308;
    
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
    /**门店小程序码url参数为空*/
    public static final int CODE_200017 = 200017;
    /**门店小程序码图片生成失败*/
    public static final int CODE_200018 = 200018;


    /** 原密码输入错误 */
    public static final int CODE_300021 = 302001;
    /** 不可删除有成员的权限组 */
    public static final int CODE_301000 = 301000;
    /** 新密码与原密码相同 */
    public static final int CODE_302002 = 302002;

    /** 购物车:门店ID为空*/
    public static final int CODE_402001 = 402001;
    /** 购物车:自提地址为空*/
    public static final int CODE_402002 = 402002;
    /** 购物车:商品信息为空*/
    public static final int CODE_402004 = 402004;
    /** 购物车:支付方式*/
    public static final int CODE_402006 = 402006;
    /** 购物车:参数错误*/
    public static final int CODE_402008 = 402008;
    /**订单id参数为空*/
    public static final int CODE_402013 = 402013;
    /** 购物车:商品下架或删除*/
    public static final int CODE_402010 = 402010;
    /** 购物车:商品信息不存在或获取商品数量不正确*/
    public static final int CODE_402011 = 402011;
    /** 购物车:购物车商品价格有变动*/
    public static final int CODE_402012 = 402012;
    /** 购物车:用户下单操作频繁*/
    public static final int CODE_402014 = 402014;
    /** 购物车:获取支付信息失败*/
    public static final int CODE_402015 = 402015;


    /** 查询订单参数异常*/
    public static final int CODE_4011001 = 4011001;

    /** 不存在符合的优惠券活动*/
    public static final int CODE_500001 = 500001;

    /** 该手机号已经享受过新用户福利*/
    public static final int CODE_500002 = 500002;

    /** 优惠券模板添加失败*/
    public static final int CODE_500003 = 500003;

    /** 适用对象添加失败*/
    public static final int CODE_500004 = 500004;

    /** 坎级规则新建失败*/
    public static final int CODE_500005 = 500005;

    /** 添加出资方规则失败*/
    public static final int CODE_500006 = 500006;

    /** 订单使用的优惠券信息不存在*/
    public static final int CODE_500007 = 500007;

    /** 模板不存在*/
    public static final int CODE_500008 = 500008;

    /** 出资方规则不存在*/
    public static final int CODE_500009 = 500009;

    /** 必填参数错误*/
    public static final int CODE_500010 = 500010;

    /** 出资方详情必填参数为空*/
    public static final int CODE_500011 = 500011;

    /** 出资方占比之和不等于等于100*/
    public static final int CODE_500012 = 500012;

    /** 出资方不能重复*/
    public static final int CODE_500013 = 500013;

    /** 用户不存在*/
    public static final int CODE_500014 = 500014;

    /** 优惠券活动添加失败*/
    public static final int CODE_503001 = 503001;

    /** 优惠券活动更新失败*/
    public static final int CODE_503201 = 503201;

    /** 删除活动信息（更新活动状态为无效）失败*/
    public static final int CODE_503301 = 503301;

    /** 停止活动失败*/
    public static final int CODE_503501 = 503501;

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
    
    @ApiModelProperty("订单正在支付中")
    public static final int ORDER_IS_BEING_PAID = 400013;
    
    @ApiModelProperty("订单为非线下计价订单")
    public static final int ORDER_IS_NOT_OFFLINE_VALUATION = 400014;
    
    @ApiModelProperty("订单金额没有变化")
    public static final int WRONG_ORDER_TOTAL_MONEY_NO_CHANGE = 400015;
    
    @ApiModelProperty("顾客id为空")
    public static final int CUSTOMER_ID_EMPTY = 400016;

    @ApiModelProperty("订单商品信息为空")
    public static final int ORDER_SKU_EMPTY = 400017;

    @ApiModelProperty("订单状态异常不能退款")
    public static final int ORDER_REFUND_STATUS_ERROR = 400018;

    @ApiModelProperty("订单退款失败")
    public static final int ORDER_REFUND_FAIL = 400019;
    @ApiModelProperty("订单退款已完成")
    public static final int ORDER_REFUND_FINISHED = 400020;
    @ApiModelProperty("订单退款失败")
    public static final int ORDER_REFUND_CLOSED = 400021;

    @ApiModelProperty("订单获取支付信息失败")
    public static final int ORDER_GET_PAY_INFO_ERROR = 400022;
    @ApiModelProperty("订单号和用户不匹配")
    public static final int ORDER_INFO_NOT_MATCH_ERROR = 400023;
    @ApiModelProperty("订单支付信息查询失败，无法修改价格")
    public static final int ORDER_PAY_INFO_ERROR = 400024;

    /** 参数异常*/
    public static final int CODE_4022001 = 4022001;
    /** 未支付的订单不允许退款*/
    public static final int CODE_422002 = 422002;
    /** 已完成的订单不允许退款*/
    public static final int CODE_422003 = 422003;
    /** 订单修改中*/
    public static final int CODE_422004 = 422004;

    /** 订单状态不允许退款*/
    public static final int CODE_4021002 = 4021002;
    /** 参数错误*/
    public static final int CODE_421001 = 421001;
    /** 订单号不能为空*/
    public static final int CODE_420001 = 420001;
    /** 订单已支付成功不能取消*/
    public static final int CODE_420002 = 420002;
    /** 用户不存在*/
    public static final int CODE_4010001 = 4010001;
    /** 订单号错误*/
    public static final int CODE_420003 = 420003;
    /** 取消订单状态更新失败*/
    public static final int CODE_420004 = 420004;
    /** 订单取消处理用户退款失败*/
    public static final int CODE_422005 = 422005;
    /** 订单取消退优惠券失败*/
    public static final int CODE_4022006 = 4022006;

    @ApiModelProperty("查询的地理区域不存在")
    public static final int CODE_3020001 = 3020001;
    
    @ApiModelProperty("当前用户没有银行卡信息")
    public static final int CODE_610001 = 610001;
    @ApiModelProperty("银行名称为空")
    public static final int CODE_610011 = 610011;
    @ApiModelProperty("银行卡卡号为空")
    public static final int CODE_610012 = 610012;
    @ApiModelProperty("开户人姓名为空")
    public static final int CODE_610013 = 610013;
    @ApiModelProperty("开户支行或分行为空")
    public static final int CODE_610014 = 610014;
    @ApiModelProperty("手机号为空")
    public static final int CODE_610015 = 610015;
    @ApiModelProperty("验证码为空")
    public static final int CODE_610016 = 610016;
    @ApiModelProperty("B端绑定银行卡失败")
    public static final int CODE_610017 = 610017;
    @ApiModelProperty("验证码已经生成，请勿重复操作")
    public static final int CODE_610018 = 610018;
    @ApiModelProperty("验证码输入不正确")
    public static final int CODE_610019 = 610019;
    @ApiModelProperty("请先获取验证码")
    public static final int CODE_610020 = 610020;
    @ApiModelProperty("查询结果有误，请联系管理员")
    public static final int CODE_610021 = 610021;
    @ApiModelProperty("请传入提现类型")
    public static final int CODE_610022 = 610022;
    @ApiModelProperty("当前没有提现类型")
    public static final int CODE_610023 = 610023;
    @ApiModelProperty("当前要绑定的银行卡已经存在")
    public static final int CODE_610024 = 610024;
    @ApiModelProperty("请先绑定银行卡")
    public static final int CODE_610025 = 610025;
    @ApiModelProperty("请先绑定微信")
    public static final int CODE_610026 = 610026;
    @ApiModelProperty("门店当前没有可提现的记录")
    public static final int CODE_610027 = 610027;
    @ApiModelProperty("请输入身份证号")
    public static final int CODE_610028 = 610028;
    @ApiModelProperty("请输入银行swiftcode")
    public static final int CODE_610029 = 610029;
    @ApiModelProperty("参数为空")
    public static final int CODE_610030 = 610030;
    @ApiModelProperty("请输入微信账号")
    public static final int CODE_610031 = 610031;
    @ApiModelProperty("请输入提现金额")
    public static final int CODE_610032 = 610032;
    @ApiModelProperty("请输入流向类型")
    public static final int CODE_610033 = 610033;
    @ApiModelProperty("请输入流向名称")
    public static final int CODE_610034 = 610034;
    @ApiModelProperty("提取限额不能大于实际账户余额")
    public static final int CODE_610035 = 610035;
    
    
    @ApiModelProperty("订单支付  参数为空")
    public static final int CODE_600101 = 600101;
     
    @ApiModelProperty("订单支付  订单号为空")
    public static final int CODE_600102 = 600102;
     
    @ApiModelProperty("订单支付  商品描述为空")
    public static final int CODE_600103 = 600103;
     
    @ApiModelProperty("订单支付  用户openid为空")
    public static final int CODE_600104 = 600104;
     
    @ApiModelProperty("订单支付  设备Ip为空")
    public static final int CODE_600105 = 600105;
    
    @ApiModelProperty("订单支付  支付方式为空")
    public static final int CODE_600106 = 600106;
    
    @ApiModelProperty("订单支付  支付金额为空")
    public static final int CODE_600107 = 600107;
    
    @ApiModelProperty("退款  参数为空")
    public static final int CODE_600201 = 600201;
   
    @ApiModelProperty("退款  订单号为空")
    public static final int CODE_600202 = 600202;
    
    @ApiModelProperty("退款  appid为空")
    public static final int CODE_600203 = 600203;
    
    @ApiModelProperty("退款  订单金额为空")
    public static final int CODE_600204 = 600204;
    
    @ApiModelProperty("退款  退款金额为空")
    public static final int CODE_600205 = 600205;
    
    @ApiModelProperty("退款  创建人为空")
    public static final int CODE_600206 = 600206;
    
    @ApiModelProperty("退款  创建人姓名为空")
    public static final int CODE_600207 = 600207;
    
    @ApiModelProperty("门店资金变化  参数为空")
    public static final int CODE_600001 = 600001;
    
    @ApiModelProperty("门店资金变化  门店id为空")
    public static final int CODE_600002 = 600002;
    
    @ApiModelProperty("门店资金变化  操作类型为空")
    public static final int CODE_600003 = 600003;
    
    @ApiModelProperty("门店资金变化  订单号为空")
    public static final int CODE_600004 = 600004;
    
    @ApiModelProperty("门店资金变化  提现单号为空")
    public static final int CODE_600005 = 600005;
    
    @ApiModelProperty("门店资金变化  订单闭环计算用户资金重复")
    public static final int CODE_600006 = 600006;
    
    @ApiModelProperty("门店资金变化  结算审核费用类型为空")
    public static final int CODE_600007 = 600007;
    
    @ApiModelProperty("门店资金变化  结算审核计算用户资金重复")
    public static final int CODE_600008 = 600008;
    
    @ApiModelProperty("门店资金变化 金额为空")
    public static final int CODE_600009 = 600009;

    @ApiModelProperty("门店资金变化 金额有误")
    public static final int CODE_600010 = 600010;
    
    @ApiModelProperty("门店资金变化  提现申请计算用户资金重复")
    public static final int CODE_600011 = 600011;
    
    @ApiModelProperty("门店资金变化  提现审核不通过计算用户资金重复")
    public static final int CODE_600012 = 600012;
    
    @ApiModelProperty("门店资金变化  提现失败计算用户资金重复")
    public static final int CODE_600013 = 600013;
    
    
    @ApiModelProperty("获取门店绑定钱包  未获取到门店信息")
    public static final int CODE_600601 = 600601;

    @ApiModelProperty("转账必填字段为空, 请检查属性")
    public static final int CODE_600301 = 600301;

    @ApiModelProperty("门店提现  未获取到提现信息")
    public static final int CODE_600310 = 600310;
    
    @ApiModelProperty("判断订单是否支付  订单号为空")
    public static final int CODE_601601 = 601601;

    @ApiModelProperty("云信账户异常")
    public static final int CODE_701101 = 701101;

    @ApiModelProperty("消息服务 创建云信用户 customerId为空")
    public static final int CODE_701301 = 701301;

    @ApiModelProperty("消息服务 修改云信用户 云信用户存在，更新token失败（需查询官方错误码）")
    public static final int CODE_701302 = 701302;

    @ApiModelProperty("消息服务 修改云信用户 云信用户不存在，创建新用户失败（需查询官方错误码）")
    public static final int CODE_701303 = 701303;

    @ApiModelProperty("消息服务 修改云信用户信息 云信用户不存在")
    public static final int CODE_701304 = 701304;

    @ApiModelProperty("消息服务 给B端用户发云信消息 customerId为空")
    public static final int CODE_701401 = 701401;

    @ApiModelProperty("消息服务 给B端用户发云信消息 接口参数getEaseMsg为空")
    public static final int CODE_701402 = 701402;

    @ApiModelProperty("消息服务 给B端用户发云信消息 云信用户不存在")
    public static final int CODE_701403 = 701403;

    @ApiModelProperty("消息服务 给B端用户发云信消息 发送消息失败（需查询官方错误码）")
    public static final int CODE_701404 = 701404;

    @ApiModelProperty("消息服务 根据code获取openid信息出错（需查询官方错误码）")
    public static final int CODE_702101 = 702101;

    @ApiModelProperty("消息服务 根据code获取openid信息 code为空")
    public static final int CODE_702102 = 702102;

    @ApiModelProperty("消息服务 给C端用户推送小程序模板消息 toUser为空")
    public static final int CODE_702201 = 702201;

    @ApiModelProperty("消息服务 给C端用户推送小程序模板消息 消息为空")
    public static final int CODE_702202 = 702202;

    @ApiModelProperty("消息服务 给C端用户推送小程序模板消息 模板信息不存在")
    public static final int CODE_702203 = 702203;

    @ApiModelProperty("消息服务 给C端用户推送小程序模板消息 用户没有可用的formid")
    public static final int CODE_702204 = 702204;

    @ApiModelProperty("消息服务 给C端用户推送小程序模板消息 发送消息后没有返回值")
    public static final int CODE_702205 = 702205;

    @ApiModelProperty("消息服务 给C端用户推送小程序模板消息 发送消息后返回的错误码为空")
    public static final int CODE_702206 = 702206;

    @ApiModelProperty("消息服务 给C端用户推送小程序模板消息 发送消息错误（需查询官方错误码）")
    public static final int CODE_702207 = 702207;

    @ApiModelProperty("消息服务 保存formid formid为空")
    public static final int CODE_702301 = 702301;

    @ApiModelProperty("后台消息管理 手动给门店推送消息 推送配置不存在")
    public static final int CODE_703501 = 703501;

    @ApiModelProperty("后台消息管理 手动给门店推送消息 云信门店记录不存在")
    public static final int CODE_703502 = 703502;

    @ApiModelProperty("后台消息管理 手动给门店推送消息 消息推送出错（需查询官方错误码）")
    public static final int CODE_703503 = 703503;
    
    @ApiModelProperty("支付中，请勿重复支付")
    public static final int CODE_3400900 = 3400900;
    @ApiModelProperty("生产签名失败")
    public static final int CODE_3400901 = 3400901;
    @ApiModelProperty("微信请求参数转换异常")
    public static final int CODE_3400902 = 3400902;
    @ApiModelProperty("微信响应参数解析异常")
    public static final int CODE_3400903 = 3400903;
    @ApiModelProperty("微信响应值错误")
    public static final int CODE_3400904 = 3400904;
    @ApiModelProperty("微信响应验签失败")
    public static final int CODE_3400905 = 3400905;
    @ApiModelProperty("Bean转换失败")
    public static final int CODE_3400906 = 3400906;
    @ApiModelProperty("下载对账单时，响应参数解析失败")
    public static final int CODE_3400907 = 3400907;
    @ApiModelProperty("支付完成，请勿重复支付")
    public static final int CODE_3400908 = 3400908;
    @ApiModelProperty("微信无证书请求失败")
    public static final int CODE_3400910 = 3400910;
    @ApiModelProperty("微信有证书请求失败")
    public static final int CODE_3400911 = 3400911;
}
