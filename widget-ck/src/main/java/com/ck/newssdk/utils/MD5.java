package com.ck.newssdk.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/7/14.
 */

public class MD5 {
    public MD5() {
    }

    public static final String MD5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] str = null;
        byte[] strTemp = s.getBytes();

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] e = mdTemp.digest();
            int j = e.length;
            str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte b = e[i];
                str[k++] = hexDigits[b >> 4 & 15];
                str[k++] = hexDigits[b & 15];
            }
        } catch (NoSuchAlgorithmException var10) {
            var10.printStackTrace();
        }

        return new String(str);
    }

    public static final String MD516(String s) {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(s.getBytes());
            byte[] b = e.digest();
            StringBuffer buf = new StringBuffer("");

            for(int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];
                if(i < 0) {
                    i += 256;
                }

                if(i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            return new String(buf.toString().substring(8, 24));
        } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(MD516("!gmwqaz123"));
        System.out.println(MD5("SDK-BBX-010-19736+1-]fc0-4").toUpperCase());
    }

}
