package com.wangab;

import org.apache.commons.lang.RandomStringUtils;

import java.util.UUID;

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
    public static String createId()
    {
        String id = UUID.randomUUID().toString();

        id = DEKHash(id) + "";

        int diff = 11 - id.length();
        String randStr = RandomStringUtils.randomAlphabetic(11);
        for (int i = 0; i < diff; i++)
        {
            int randIndex = (int) (Math.random() * randStr.length());
            int index = (int) (Math.random() * id.length());
            id = id.substring(0, index) + randStr.charAt(randIndex) + id.substring(index, id.length());
        }
        return id;
    }

    private static int DEKHash(String str)
    {
        int hash = str.length();

        for (int i = 0; i < str.length(); i++)
        {
            hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
        }

        return (hash & 0x7FFFFFFF);
    }

}
