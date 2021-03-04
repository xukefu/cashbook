package com.xkf.cashbook.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * AES 加密工具类
 * @author xukf01
 */
@Slf4j
public class AesEncryptUtils {

    private static final String KEY_ALGORITHM = "AES";

    private static final String SIGN_ALGORITHMS = "SHA1PRNG";

    /**
     * PAD模式, 参数分别代表 算法名称/加密模式/数据填充方式
     * NoPadding
     * PKCS7Padding
     * PKCS5Padding
     * PKCS1Padding
     *
     */
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    /**
     * AES加密KEY参数值，16个字符
     */
    private static final String KEY = "xxxxxxxxxxxxxxxx";
    /**
     * 字符编码
     */
    private static final String CHARSET_UTF8 ="utf-8";

    /**
     * 生成密钥
     * @param seed
     * @return
     * @throws Exception
     */
    public static SecretKey geneKey(String seed) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance(SIGN_ALGORITHMS);
        random.setSeed(seed.getBytes());
        keyGenerator.init(random);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }
    public static SecretKey geneKey() throws Exception {
        //获取一个密钥生成器实例
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    /**
     * 加密
     * @param content 加密的字符串
     * @param encryptKey key值
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM));

        byte[] b = cipher.doFinal(content.getBytes(CHARSET_UTF8));
        // 采用base64算法进行转码,避免出现中文乱码

        return Base64.encodeBase64String(b);
    }
    public static String encrypt(String content, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] b = cipher.doFinal(content.getBytes(CHARSET_UTF8));
        // 采用base64算法进行转码,避免出现中文乱码

        return Base64.encodeBase64String(b);
    }

    /**
     * 解密
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM));
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }
    public static String decrypt(String encryptStr, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }


    public static void main(String[] args) throws Exception {
        Map map=new HashMap<String,String>();
        map.put("key","value");
        map.put("中文","汉字");
        String content = JacksonUtils.mapToJson(map);
        System.out.println("加密前：" + content);

        SecretKey secretKey = geneKey("hC5rAvaemoThvCpq4hbqt5RXdvQNLzfp");

        String encrypt = encrypt(content, secretKey);
        System.out.println("加密后：" + encrypt);

        String decrypt = decrypt(encrypt, secretKey);
        System.out.println("解密后：" + decrypt);


    }

}
