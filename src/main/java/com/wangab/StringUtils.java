package com.wangab;

/**
 * Created by wanganbang on 9/1/16.
 */
public class StringUtils {

    public static String toHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
