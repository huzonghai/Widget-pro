package com.ck.newssdk.utils;

import com.ck.newssdk.utils.encoder.BASE64Decoder;
import com.ck.newssdk.utils.encoder.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * Created by Administrator on 2017/7/14.
 */

public class DES {
    private static String password = "!Q#E%T&U(O";

    public DES() {
    }

    public static String getPwd() {
        return password;
    }

    public static String base64Encode(byte[] bytes) {
        return (new BASE64Encoder()).encode(bytes);
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static byte[] base64Decode(String base64Code) throws Exception {
        return isEmpty(base64Code)?null:(new BASE64Decoder()).decodeBuffer(base64Code);
    }

    public static String desCrypto(String content, String password) {
        try {
            return base64Encode(desCrypto(content.getBytes("utf-8"), password));
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static byte[] desCrypto(byte[] datasource, String password) {
        try {
            SecureRandom e = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, securekey, e);
            return cipher.doFinal(datasource);
        } catch (Throwable var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String content, String password) throws Exception {
        return isEmpty(content)?null:new String(decrypt(base64Decode(content), password), "utf-8");
    }

    public static byte[] decrypt(byte[] src, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, securekey, random);
        return cipher.doFinal(src);
    }

    public static void main(String[] args) {
    }

}
