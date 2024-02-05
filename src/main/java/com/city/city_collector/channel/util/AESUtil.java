package com.city.city_collector.channel.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Description:
 *
 * @author nb
 * @version 1.0
 * @since 2018-3-3
 */
public class AESUtil {
//    public static final String DEFAULT_KEY = "1400009129000000";
//
//    public static String decrypt(String paramString)throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
//    {
//      return decrypt(paramString, "1400009129000000");
//    }

    public static String decrypt(String paramString1, String paramString2) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//      try
//      {
        byte[] arrayOfByte1 = B4.decode(paramString1, 0);
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramString2.getBytes("UTF-8"), "AES");
        Cipher localCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        localCipher.init(2, localSecretKeySpec);
        byte[] arrayOfByte2 = localCipher.doFinal(arrayOfByte1);
        String localObject = null;
        if (arrayOfByte2 != null) {
            String str = new String(arrayOfByte2, "UTF-8");
            localObject = str;
        }
        return localObject;
//      }
//      catch (Exception localException)
//      {
//        localException.printStackTrace();
//      }
//      return null;
    }

//    public static String encrypt(String paramString)
//    {
//      return encrypt(paramString, "1400009129000000");
//    }

    public static String encrypt(String paramString1, String paramString2) {
        try {
            byte[] arrayOfByte2 = paramString1.getBytes("UTF-8");
            SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramString2.getBytes("UTF-8"), "RSA");
//        Cipher localCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            Cipher localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            localCipher.init(1, localSecretKeySpec);
            byte[] arrayOfByte3 = localCipher.doFinal(arrayOfByte2);
//        arrayOfByte1 = arrayOfByte3;
            String str = null;
            if (arrayOfByte3 != null)
                str = B4.encodeToString(arrayOfByte3, 0);
            return str;
        } catch (Exception localException) {
            return null;
        }
    }
}
