package com.winhxd.b2c.common.constant;

/**
 * Currency
 *
 * @Author yindanqing
 * @Date 2018/8/14 15:54
 * @Description: 货币种类枚举
 */
public enum CurrencyEnum {

    /**
     * 英镑：GBP、港币：HKD、美元：USD、新加坡元：SGD、日元：JPY、加拿大元：CAD、
     * 澳元：AUD、欧元：EUR、新西兰元：NZD、韩元：KRW、泰铢：THB、瑞士法郎：CHF、
     * 瑞典克朗：SEK、丹麦克朗：DKK、挪威克朗：NOK、马来西亚林吉特：MYR、
     * 印尼卢比：IDR、菲律宾比索：PHP、毛里求斯卢比：MUR、以色列新谢克尔：ILS、
     * 斯里兰卡卢比：LKR、俄罗斯卢布：RUB、阿联酋迪拉姆：AED、捷克克朗：CZK、
     * 南非兰特：ZAR、人民币：CNY
     * */
    GBP("GBP","英镑"),
    HKD("HKD","港币"),
    USD("USD","美元"),
    SGD("SGD","新加坡元"),
    JPY("JPY","日元"),
    CAD("CAD","加拿大元"),

    AUD("AUD","澳元"),
    EUR("EUR","欧元"),
    NZD("NZD","新西兰元"),
    KRW("KRW","韩元"),
    THB("THB","泰铢"),
    CHF("CHF","瑞士法郎"),

    SEK("SEK","瑞典克朗"),
    DKK("DKK","丹麦克朗"),
    NOK("NOK","挪威克朗"),
    MYR("MYR","马来西亚林吉特"),

    IDR("IDR","印尼卢比"),
    PHP("PHP","菲律宾比索"),
    MUR("MUR","毛里求斯卢比"),
    ILS("ILS","以色列新谢克尔"),

    LKR("LKR","斯里兰卡卢比"),
    RUB("RUB","俄罗斯卢布"),
    AED("AED","阿联酋迪拉姆"),
    CZK("CZK","捷克克朗"),

    ZAR("ZAR","南非兰特"),
    CNY("CNY","人民币");

    private CurrencyEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getText() {
        return name;
    }

}
