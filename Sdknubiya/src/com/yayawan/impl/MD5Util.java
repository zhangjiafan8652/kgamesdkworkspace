package com.yayawan.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 计算MD5哈希值
 *
 */
public class MD5Util {

    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * MD5计算哈希值
     *
     * @param data
     * @return 返回字节数组
     */
    public static byte[] getMD5Code(byte[] data) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(data, 0, data.length);
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5计算哈希值
     *
     * @param data
     * @return 返回16进制字符串
     */
    public static String getMD5CodeHex(byte[] data) {
        byte[] codeByte = getMD5Code(data);
        if (codeByte != null) {
            return Hex.byte2hex(codeByte);
        } else {
            return null;
        }
    }

    /**
     * 对byte类型的数组进行MD5加密
     *
     * @author
     */
    public static String getMD5String(byte[] bytes) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(bytes);
        return bufferToHex(md5.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            char c0 = hexDigits[(bytes[l] & 0xf0) >> 4];
            char c1 = hexDigits[bytes[l] & 0xf];
            stringbuffer.append(c0);
            stringbuffer.append(c1);
        }
        return stringbuffer.toString();
    }
}
