package com.city.city_collector.channel.util.bmw;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAHelper {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 245;

    private static final String SIGNATURE_ALGORITHM = "MD5withRSA"; // 签名算法 MD5
    private static final String ALGORITHM = "RSA";// 加密算法RSA
    private static final String CIPHER_EN = "RSA";//解密算法RSA

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws InvalidKeySpecException
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        // 得到公钥
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        Key key = keyFactory.generatePublic(x509EncodedKeySpec);
        // 加密数据，分段加密
        Cipher cipher = Cipher.getInstance(CIPHER_EN);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int inputLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        while (inputLength - offset > 0) {
            if (inputLength - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputLength - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 签名字符串
     *
     * @param text       需要签名的字符串
     * @param privateKey 私钥(BASE64编码)
     * @return 签名结果(BASE64编码)
     */
    public static String sign(String text, String privateKey) {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(text.getBytes());
            byte[] result = signature.sign();
            return Base64.encode(result);
        } catch (Exception e) {
            System.out.println("签名失败 " + e);
        }
        return null;
    }

}
