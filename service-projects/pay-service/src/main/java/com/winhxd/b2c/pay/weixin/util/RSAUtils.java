package com.winhxd.b2c.pay.weixin.util;

/**
 * RSAUtils
 *
 * @Author yindanqing
 * @Date 2018/8/16 18:06
 * @Description: rsa加密工具类
 */
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    /**
     * 编码格式
     */
    private static final String CHARSET = "UTF-8";
    /**
     * 默认填充模式
     */
    private static final String RSA_ALGORITHM = "RSA";
    /**
     * wx-rsa指定填充模式
     */
    private static final String WX_DEFAULT_PADDING = "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";

    /**
     * 构造公私钥
     * @param keySize 密钥长度
     * @return
     */
    public static Map<String, String> createKeys(int keySize){
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try{
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        }catch(NoSuchAlgorithmException e){
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }
        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        return keyPairMap;
    }

    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    private static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    private static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 通用公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    private static String publicEncrypt(String data, RSAPublicKey publicKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 通用公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return publicEncrypt(data, getPublicKey(publicKey));
    }

    /**
     * 通用公钥解密
     * @param data
     * @param publicKey
     * @return
     */
    private static String publicDecrypt(String data, RSAPublicKey publicKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 通用公钥解密
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicDecrypt(String data, String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return publicDecrypt(data, getPublicKey(publicKey));
    }

    /**
     * 通用私钥加密
     * @param data
     * @param privateKey
     * @return
     */
    private static String privateEncrypt(String data, RSAPrivateKey privateKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 通用私钥加密
     * @param data
     * @param privateKey
     * @return
     */
    public static String privateEncrypt(String data, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return privateEncrypt(data, getPrivateKey(privateKey));
    }

    /**
     * 通用私钥解密
     * @param data
     * @param privateKey
     * @return
     */
    private static String privateDecrypt(String data, RSAPrivateKey privateKey){
        try{
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 通用私钥解密
     * @param data
     * @param privateKey
     * @return
     */
    private static String privateDecrypt(String data, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return privateDecrypt(data, getPrivateKey(privateKey));
    }

    /**
     * RSA加解密核心
     * @param cipher
     * @param opmode
     * @param datas
     * @param keySize
     * @return
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
        int maxBlock = 0;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        }else{
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try{
            while(datas.length > offSet){
                if(datas.length-offSet > maxBlock){
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        }catch(Exception e){
            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

    /**
     * 微信公钥加密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @Description: 指定填充模式为 RSA_PKCS1_OAEP_PADDING
     * @return 密文
     */
    public static String wxPublicKeyEncrypt(String data, String publicKeyStr){
        try{
            RSAPublicKey publicKey = getPublicKey(publicKeyStr);
            Cipher cipher = Cipher.getInstance(WX_DEFAULT_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    private static final String PUBLIC_KEY_DEMO = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAInD_UPY5Uy32xT41LwbFlVroqXrzOjgBZXe6yKe3BTsyHwrGARZuyKJi6C9THiMeyqJxti0uIzL92M15YRsNSUCAwEAAQ";
    private static final String PRIVATE_KEY_DEMO = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAicP9Q9jlTLfbFPjUvBsWVWuipevM6OAFld7rIp7cFOzIfCsYBFm7IomLoL1MeIx7KonG2LS4jMv3YzXlhGw1JQIDAQABAkAcGum-P6932UJOovzzaytBPfYul050a8935cReib4oXl6Covp2saWzu0KA-CdaVK4HXBzK48cGPG26G0IEpSr5AiEA9qiPBP5WuiKnwAO1fDTMdoeymiiYX3td1tdPSZJu_3MCIQCO-6z6wDyXbtdzh_6AcwFNvWoR8GWH3SsctO8R5Q-jBwIhANP82DY9dUIyKKQhS-f85MD2LSzKuPJO776Ge9FKdfU7AiBqQz0BdlERsjzJDe7lA5OadQUZo_GxEXvy770lLXl7jQIhAKn0JSRUUmCYVmJ-Z6zrYUxecOsffnc4OFkJJjG1ru_n";

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String clearText = "123456";
        //获得公钥私钥
        RSAPublicKey publicKey = getPublicKey(PUBLIC_KEY_DEMO);
        RSAPrivateKey privateKey = getPrivateKey(PRIVATE_KEY_DEMO);
        //公钥加密, 私钥解密demo
        String encrypt = publicEncrypt(clearText, publicKey);
        System.out.println(encrypt);
        String decrypt = privateDecrypt(encrypt, privateKey);
        System.out.println(decrypt);
        //私钥加密, 公钥解密demo
        String encrypt2 = privateEncrypt(clearText, privateKey);
        System.out.println(encrypt2);
        String decrypt2 = publicDecrypt(encrypt2, publicKey);
        System.out.println(decrypt2);
    }

    /**
     * 微信RSA加密公钥(UAT环境)
     */
    private static final String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyNmqAxl/ggCjInh2SsnH\r2RiH7COLppUKZ4Lq6jDctQIgxykETFjcZ6azuiqDBvFJLD9NuHBlko1hfBDIMP78\r8EEkp0IPi+EwWWWutMr4lQOln05JVDyDYNTelA/lLPQa3sevD9/GBrf10lxiWCI6\rqCqstDliZ3BrCuZvKjjUFQ9WW3Lhk0wbs6IngbmUqnSgcb/Gn98XJBHHgH8TgsVb\rXbK2EuLHKVMuOkWp39ZqJ1yJi0rhhoyLQ38kk8GfI+mFUXXeh37kZhlzcp28+Equ\r0iV3lKX/L5kFOAAbQ62Ee+DNRIxw9i6nCkQSB+0BNwRpazNiXCeoRiUCr5gj74zQ\rhQIDAQAB";

}