package com.tianyi.tool.data;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


public class DeviceInfo {
	
	private static String IMEI = null;
	private static String IMSI = null;
	/**
	 * Get Telephone IMEI
	 * 
	 * @param context
	 * @return String DeviceUtils.java 
	 * create time: 2010-11-24 
	 * last modify: 2010-11-24 
	 * author: liangxiaoshan
	 */
	public static String getTelImei(Context context) {
		if (IMEI != null) return IMEI;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if("000000000000000".equals(imei) || "sdk".equalsIgnoreCase(Build.MODEL)){
			imei= "000000";
		}
		return IMEI = (TextUtils.isEmpty(imei) ? "" : imei);
	}
	
	/**
	 * Get Telephone IMSI
	 */
	public static String getTelImsi(Context context) {
		if (IMSI != null) return IMSI;
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		if (TextUtils.isEmpty(imsi)) imsi = "";
		if (imsi.length() > 15) {
			imsi = imsi.substring(0, 15);
		}
		return IMSI = imsi;
	}
}
