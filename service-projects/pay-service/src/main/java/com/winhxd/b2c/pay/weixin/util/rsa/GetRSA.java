package com.winhxd.b2c.pay.weixin.util.rsa;
/**
 * rsa加密
 * @author 王士忠
 * @date 2017年11月27日
 * */
public class GetRSA {  
 /**
  * @param publicKeyPKCS8  为pkcs8格式的公钥
  * */
   public static String getRSA(String str,String publicKeyPKCS8) throws Exception {
       byte[] cipherData=RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(publicKeyPKCS8),str.getBytes());
       String cipher=Base64.encode(cipherData);  
       return cipher;
   }

    public static void main(String[] args) throws Exception {
        System.out.println(getRSA("6217000210004907167",publicKeyStr));
    }

    /**
     * uat-rsa公钥pks8格式
     */
    private static final String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyNmqAxl/ggCjInh2SsnH\r2RiH7COLppUKZ4Lq6jDctQIgxykETFjcZ6azuiqDBvFJLD9NuHBlko1hfBDIMP78\r8EEkp0IPi+EwWWWutMr4lQOln05JVDyDYNTelA/lLPQa3sevD9/GBrf10lxiWCI6\rqCqstDliZ3BrCuZvKjjUFQ9WW3Lhk0wbs6IngbmUqnSgcb/Gn98XJBHHgH8TgsVb\rXbK2EuLHKVMuOkWp39ZqJ1yJi0rhhoyLQ38kk8GfI+mFUXXeh37kZhlzcp28+Equ\r0iV3lKX/L5kFOAAbQ62Ee+DNRIxw9i6nCkQSB+0BNwRpazNiXCeoRiUCr5gj74zQ\rhQIDAQAB";

}  