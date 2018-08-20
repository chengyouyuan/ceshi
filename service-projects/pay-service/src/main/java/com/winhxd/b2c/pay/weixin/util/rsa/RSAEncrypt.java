package com.winhxd.b2c.pay.weixin.util.rsa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

public class RSAEncrypt {
    private static final Logger logger = LoggerFactory.getLogger(RSAEncrypt.class);

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception ex) {
            logger.error("填充加密串失败.", ex);
            throw ex;
        }
    }

    /**
     * @param publicKey     公钥
     * @param plainTextData 待加密byte数组
     * @return
     * @throws Exception 异常
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception();
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plainTextData);
        } catch (Exception ex) {
            logger.error("填充加密串失败.", ex);
            throw ex;
        }
    }
}