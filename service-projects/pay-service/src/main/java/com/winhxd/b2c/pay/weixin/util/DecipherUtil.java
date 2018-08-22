package com.winhxd.b2c.pay.weixin.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * DecipherUtil
 *
 * @Author 李中华
 * @Date 2018/8/19 16:36
 * @Description:
 */
public class DecipherUtil {


    /**
     * 密钥算法
     */
//    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";

    /**
     * API 密钥
     */
    /*@Value("${WX.KEY}")
    private static String password;

    private static SecretKeySpec key = new SecretKeySpec(DigestUtils.md5Hex(password).toLowerCase().getBytes(), ALGORITHM);*/

    /**
     * AES解密
     *
     * @param reqInfo 加密串
     * @return
     * @throws Exception
     */
    public static String decodeReqInfo(String reqInfo, SecretKeySpec key) throws Exception{
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.decodeBase64(reqInfo)),"UTF-8");
    }
}
