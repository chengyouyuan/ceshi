package com.winhxd.b2c.pay.weixin.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static sun.security.x509.CertificateAlgorithmId.ALGORITHM;

/**
 * DecipherUtil
 *
 * @Author 李中华
 * @Date 2018/8/19 16:36
 * @Description:
 */
public class DecipherUtil {

    private static String password = "aaa";
    private static SecretKeySpec key = new SecretKeySpec(byteArrayToHexString(DigestUtils.md5(password)).toLowerCase().getBytes(), ALGORITHM);

    public static String decodeReqInfo(String reqInfo) throws Exception{
        byte[] b = Base64.decodeBase64(reqInfo);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(b),"UTF-8");


    }

    public static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
}
