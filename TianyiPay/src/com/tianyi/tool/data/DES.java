package com.tianyi.tool.data;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.text.TextUtils;
/**
 ***************************************************************************** 
 ** Module :Android DES 加密和java DES加密结果不一样， 调用DES加密算法包最精要的就是下面两句话�?
 * 
 * Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
 * 
 * cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
 * 
 * CBC是工作模式，DES�?共有电子密码本模式（ECB）�?�加密分组链接模式（CBC）�?�加密反馈模式（CFB）和输出反馈模式（OFB）四种模式，
 * 
 * PKCS5Padding是填充模式，还有其它的填充模式：
 * 
 * 然后，cipher.init�?)�?共有三个参数：Cipher.ENCRYPT_MODE, key,
 * zeroIv，zeroIv就是初始化向量，�?�?8为字符数组�??
 * 
 * 工作模式、填充模式�?�初始化向量这三种因素一个都不能少�?�否则，如果你不指定的话，那么就要程序就要调用默认实现�?�问题就来了，这就与平台有关了�?�（
 * 之前我并没有指定IV，导致结果不�?致）
 ** 
 ** Date: 2011-11-6 Time: 上午11:22:59
 ** 
 ** Author: 田志�? (seara)
 ** 
 ** Email: seara@qq.com
 ** 
 ** (C) Copyright 2011 广州赢典信息科技有限公司
 ***************************************************************************** 
 */
public class DES {
	public static final String DECIPHERINGAES_KEY = "consummateadvert";
	public static final String DECIPHERING_CONTENT = "CC9Q2fnHFm0TrX7APPHEkid7PqIdkTlgNTWLphP7nK/odLAUwOjcztTXKLl2 gOIC";
	public static final String DECIPHERING_KEY = "tianyihuyu123654@@@@####****!!!!";
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	public static String encryptDES(String encryptString/*, String encryptKey*/) {
		String encryptKey = DECIPHERING_KEY;
		if (! TextUtils.isEmpty(encryptKey)) {
			encryptKey = getKey(encryptKey);

			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
			try {
				Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
				byte[] encryptedData = cipher.doFinal(encryptString.getBytes("utf-8"));
				return Base64.encode(encryptedData);
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		return "";
	}

	public static String getKey(String decryptKey) {
		String key = null;
		if (! TextUtils.isEmpty(decryptKey)) {
			int length = decryptKey.length();
			if (length > 8) {
				key = decryptKey.substring(0, 8);
			} else {
				int s = 8 - length;// 相差多少个字�?
				StringBuilder sb = new StringBuilder(decryptKey);
				for (int i = 0; i < s; i++) {
					sb.append("*");
				}
				key = sb.toString();
			}
		}
		return key;
	}
	
	public static String decryptDES(String decryptString/*, String decryptKey*/) {
		String decryptKey = DECIPHERING_KEY;
		decryptKey = getKey(decryptKey);
		byte[] byteMi = Base64.decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte decryptedData[] = cipher.doFinal(byteMi);
			return new String(decryptedData,"utf-8");
		} catch (Exception e) {
		}
		return "";
	}
	public static byte[] encryptDESBytes(String encryptString/*, String encryptKey*/) {
		String encryptKey = DECIPHERING_KEY;
		encryptKey = getKey(encryptKey);
		if (! TextUtils.isEmpty(encryptKey)) {
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
			try {
				Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
				byte[] encryptedData = cipher.doFinal(encryptString.getBytes("utf-8"));
				return encryptedData;
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}
		return null;
	}

	public static String decryptDESBytes(byte[] byteMi/*, String decryptKey*/) {
		String decryptKey = DECIPHERING_KEY;
		decryptKey = getKey(decryptKey);
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte decryptedData[] = cipher.doFinal(byteMi);
			return new String(decryptedData, "utf-8");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return "";
	}
}