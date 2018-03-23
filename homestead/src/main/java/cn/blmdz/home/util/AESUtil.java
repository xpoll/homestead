package cn.blmdz.home.util;

import java.security.AlgorithmParameters;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AESUtil {
    public static final AESUtil instance = new AESUtil();
    
    public static final String AES = "AES";
    
    public static final String CIPHER_AES_CBC_PKCS7PADDING = "AES/CBC/PKCS7Padding";
    
    public static final String CIPHER_AES_EBC_PKCS7PADDING = "AES/ECB/PKCS5Padding";

    public static boolean initialized = false;

    /**
     * AES解密
     * 
     * @param content 密文
     * @param keyByte key
     * @param ivByte 偏移
     * @param cipherType 加密类型
     * @return
     */
    public byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte, String cipherType) {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance(cipherType);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyByte, AES), generateIV(ivByte));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成iv
     * @param iv
     * @return
     * @throws Exception
     */
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }
    /**
     * AES解密
     * @param content Hex.decodeHex("content".toCharArray())
     * @param key Hex.decodeHex("key".toCharArray())
     * @param cipherType
     * @return
     */
    public byte[] decrypt(byte[] content, byte[] key, String cipherType) {
        initialize();
        try {
            Cipher cipher = Cipher.getInstance(cipherType);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, AES));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密
     * @param content "".getBytes()
     * @param key hex
     * @param cipherType
     * @return
     */
    public String encrypt(byte[] content, byte[] key, String cipherType) {
        initialize();
        key = new SecretKeySpec(key, AES).getEncoded();
        try {
            Cipher cipher = Cipher.getInstance(cipherType);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, AES));
            return Hex.encodeHexString(cipher.doFinal(content));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void initialize() {
        if (initialized)
            return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }
}