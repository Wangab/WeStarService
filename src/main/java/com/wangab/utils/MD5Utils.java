package com.wangab.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static String getMD5Code(String obj){
		try {
			char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}; 
			MessageDigest md5Utils = MessageDigest.getInstance("MD5");
			md5Utils.update(obj.getBytes());
			byte[] md = md5Utils.digest();
			System.out.println(new String());
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
			    byte byte0 = md[i];
			    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			    str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}