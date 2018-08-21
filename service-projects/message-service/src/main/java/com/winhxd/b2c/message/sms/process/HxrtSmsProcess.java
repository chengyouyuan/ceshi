/**
 *
 */
package com.winhxd.b2c.message.sms.process;

import com.winhxd.b2c.common.domain.message.model.MessageSmsHistory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yaoshuai
 */
public class HxrtSmsProcess extends BaseSmsProcess {

    private static final String STR_REG_URL = "http://www.stongnet.com/sdkhttp/reg.aspx";
    private static final String STR_BALANCE_URL = "http://www.stongnet.com/sdkhttp/getbalance.aspx";
    private static final String STR_SMS_URL = "http://www.stongnet.com/sdkhttp/sendsms.aspx";
    private static final String STR_SCH_SMS_URL = "http://www.stongnet.com/sdkhttp/sendschsms.aspx";
    private static final String STR_STATUS_URL = "http://www.stongnet.com/sdkhttp/getmtreport.aspx";
    private static final String STR_UP_PWD_URL = "http://www.stongnet.com/sdkhttp/uptpwd.aspx";

    private static final String STR_REG = "101100-WEB-HUAX-810224";
    private static final String STR_PWD = "VXCQMPIN";

    @Override
    public int sendMessage(MessageSmsHistory smsSend) {
        HxrtSmsProcess tt = new HxrtSmsProcess();
        try {
            String ret = tt.sendMsg("", smsSend.getTelephone(), smsSend.getContent());
            String[] results = ret.split("&");
            if (results != null) {
                for (int i = 0; i < results.length; i++) {
                    String result = results[i];
                    String[] res = result.split("=");
                    if (i == 0) {
                        if (res != null && res.length == 2) {
                            return Integer.valueOf(res[1]);
                        }
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 注册
     *
     * @param strUname
     * @param strMobile
     * @param strRegPhone
     * @param strFax
     * @param strEmail
     * @param strPostcode
     * @param strCompany
     * @param strAddress
     *
     * @return
     */
    public String reg(String strUname, String strMobile, String strRegPhone,
                      String strFax, String strEmail, String strPostcode,
                      String strCompany, String strAddress)
        throws UnsupportedEncodingException {
        String strRes = new String();
        strUname = HxrtHttpSend.paraTo16(strUname);
        strCompany = HxrtHttpSend.paraTo16(strCompany);
        strAddress = HxrtHttpSend.paraTo16(strAddress);

        String strRegParam = "reg=" + STR_REG + "&pwd=" + STR_PWD + "&uname="
            + strUname + "&mobile=" + strMobile + "&phone=" + strRegPhone
            + "&fax=" + strFax + "&email=" + strEmail + "&postcode="
            + strPostcode + "&company=" + strCompany + "&address="
            + strAddress;

        strRes = HxrtHttpSend.postSend(STR_REG_URL, strRegParam);
        return strRes;
    }

    /**
     * 查询余额
     * @return
     */

    public String getBalance() {
        String strRes = new String();
        String strBalanceParam = "reg=" + STR_REG + "&pwd=" + STR_PWD;

        strRes = HxrtHttpSend.postSend(STR_BALANCE_URL, strBalanceParam);
        return strRes;
    }

    /**
     * 发送短信
     * @param strSourceAdd
     * @param strPhone
     * @param strContent
     *
     * @return
     */
    public String sendMsg(String strSourceAdd, String strPhone,
                          String strContent) throws UnsupportedEncodingException {
        strContent = HxrtHttpSend.paraTo16(strContent);

        String strSmsParam = "reg=" + STR_REG + "&pwd=" + STR_PWD + "&sourceadd="
            + strSourceAdd + "&phone=" + strPhone + "&content="
            + strContent;
        String strRes = new String();
        // 发送短信
        strRes = HxrtHttpSend.postSend(STR_SMS_URL, strSmsParam);
        return strRes;
    }

    /**
     * 定时短信
     * @param strSourceAdd
     * @param strPhone
     * @param strContent
     * @param date
     *
     * @return
     */

    public String sendMsgByTime(String strSourceAdd, String strPhone,
                                String strContent, Date date) throws UnsupportedEncodingException {
        String strRes = new String();
        strContent = HxrtHttpSend.paraTo16(strContent);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String strTim = HxrtHttpSend.paraTo16(df.format(date)); // 定时发送时间

        String strSchSmsParam = "reg=" + STR_REG + "&pwd=" + STR_PWD
            + "&sourceadd=" + strSourceAdd + "&tim=" + strTim + "&phone="
            + strPhone + "&content=" + strContent;

        strRes = HxrtHttpSend.postSend(STR_SCH_SMS_URL, strSchSmsParam);
        return strRes;
    }

    /**
     * 状态报告
     * @return
     */

    public String statusReport() {
        String strRes = new String();
        String strStatusParam = "reg=" + STR_REG + "&pwd=" + STR_PWD;

        strRes = HxrtHttpSend.postSend(STR_STATUS_URL, strStatusParam);
        return strRes;
    }

    /**
     * 修改密码
     * @param strNewPwd
     *
     * @return
     */

    public String changePwd(String strNewPwd) {
        String strRes = new String();

        String strUpPwdParam = "reg=" + STR_REG + "&pwd=" + STR_PWD + "&newpwd="
            + strNewPwd;

        strRes = HxrtHttpSend.postSend(STR_UP_PWD_URL, strUpPwdParam);
        return strRes;
    }

    public static void main(String[] args) {
        HxrtSmsProcess tt = new HxrtSmsProcess();
        try {
            String ret = tt.sendMsg("", "18701243491",
                "测试短信发送" + "【惠赢天下】");
            String[] results = ret.split("&");
            if (results != null) {
                for (int i = 0; i < results.length; i++) {
//					String result = results[i];
//					String[] res = result.split("=");

                    System.out.println(results[i]);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
