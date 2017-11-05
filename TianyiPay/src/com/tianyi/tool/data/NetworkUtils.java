package com.tianyi.tool.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络APN处理(创建与关闭APN)
 * 
 * @author Windale
 * @created 2011-3-11
 * @last 2011-3-11
 */
public class NetworkUtils {
	
	private NetworkUtils() {
	}

	/**
	 * 创建网络连接管理对象
	 * @param context 
	 * 
	 * @param mContext
	 */
	private static ConnectivityManager createConnectivityManager(Context context) {
		return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 得到网络连接类型
	 * @param context 
	 * @param context
	 * @return ConnectType
	 */
	public static String getConnectionType(Context context) {
		if (null != context) {
			ConnectivityManager manager = createConnectivityManager(context);
			if (null != manager) {
				NetworkInfo info = manager.getActiveNetworkInfo();
				if (null != info) {
					if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
						return info.getExtraInfo().toLowerCase(); 
					} else if (info.getType() == ConnectivityManager.TYPE_WIFI){
						return "wifi";
					}
				}
			}
		}
		return "";
	}

	/**
	 * 得到网络连接类型
	 * @param context 
	 * @param context
	 * @return ConnectType
	 */
	private static NetworkType getWapConnectionType(Context context) {
		if (null != context) {
			ConnectivityManager manager = createConnectivityManager(context);
			if (null != manager) {
				NetworkInfo info = manager.getActiveNetworkInfo();
				if (null != info) {
					if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
						String typename = info.getExtraInfo().toLowerCase(); 
						if (NetworkType.CMWAP.equals(typename) || NetworkType.UNIWAP.equals(typename)) {
							// cmwap and uniwap are use same proxy
							return NetworkType.CMWAP;
						} else if (NetworkType.CTWAP.equals(typename)) {
							return NetworkType.CTWAP;
						}
					} else if (info.getType() == ConnectivityManager.TYPE_WIFI){
						return NetworkType.WIFI;
					}
				}
			}
		}
		return NetworkType.NORMAL;
	}

	/**
	 * Get application Internet state or not
	 * Return true if the application can access the Internet
	 * @param context 
	 */
	public static boolean haveInternet(Context context) {
		NetworkInfo info = createConnectivityManager(context).getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to
			// disable internet while roaming, just return false
			return true;
		}
		return true;
	}
}
