
package com.city.city_collector.channel.util;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 *
 */
public class MD5withRSA {

    private static final char[] bcdLookup = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static final String bytesToHexStr(byte[] bcd) {
        StringBuffer s = new StringBuffer(bcd.length * 2);

        for (int i = 0; i < bcd.length; i++) {
            s.append(bcdLookup[(bcd[i] >>> 4 & 0xF)]);
            s.append(bcdLookup[(bcd[i] & 0xF)]);
        }

        return s.toString();
    }

    public static final byte[] hexStrToBytes(String s) {
        byte[] bytes = new byte[s.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = ((byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16));
        }

        return bytes;
    }

    public static byte[] sign(byte[] priKeyText, String plainText) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(priKeyText);
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey prikey = keyf.generatePrivate(priPKCS8);
            Signature signet = java.security.Signature.getInstance("MD5withRSA");
            signet.initSign(prikey);
            signet.update(plainText.getBytes());
            byte[] signed = Base64.encodeBase64(signet.sign());
            return signed;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    
//    public static String getEncryptDataByPrivateKey(String data,String privateKey) {
//        
//        
//    }

//    /**
//     * 根据字符串获取私钥
//     * @param privateKey
//     * @return
//     * @throws UnsupportedEncodingException
//     * @throws NoSuchAlgorithmException
//     * @throws InvalidKeySpecException
//     */
//    public static PrivateKey getPrivateKey(String privateKey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException {
////        byte[] encoded=privateKey.getBytes("UTF-8");
//        
//        byte[] encoded=Base64.getDecoder().decode(privateKey);
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
//        KeyFactory factory = KeyFactory.getInstance("RSA");
//        return factory.generatePrivate(keySpec);
//    }
//    
////    public static KeyPair getKeyPair() throws Exception {
////        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
////        keyGen.initialize(512); //这是生成的秘钥长度，此处的值也可以是1024或2048。秘钥越大加密后的密文长度也越大，加密解密越慢。加密的原文要比秘钥小些。一般1024足够用了。
////        KeyPair keyPair = keyGen.generateKeyPair();
////        return keyPair;
////    }
// 
//    // 用md5生成内容摘要，再用RSA的私钥加密，进而生成数字签名
//    public static String getMd5Sign(String content , PrivateKey privateKey) throws Exception {
//        byte[] contentBytes = content.getBytes("utf-8");
//        // 返回MD5withRSA签名算法的 Signature对象
//        Signature signature = Signature.getInstance("MD5withRSA");
//        signature.initSign(privateKey);
//        signature.update(contentBytes);
//        byte[] signs = signature.sign();
//        return B4.encodeToString(signs, 0);
//    }
// 
//    // 对用md5和RSA私钥生成的数字签名进行验证
//    public static boolean verifyWhenMd5Sign(String content, String sign, PublicKey publicKey) throws Exception {
//        byte[] contentBytes = content.getBytes("utf-8");
//        Signature signature = Signature.getInstance("MD5withRSA");
//        signature.initVerify(publicKey);
//        signature.update(contentBytes);
//        return signature.verify(B4.decode(sign, 0));
//    }

}
