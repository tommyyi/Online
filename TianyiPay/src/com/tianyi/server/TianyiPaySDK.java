package com.tianyi.server;

import android.app.Activity;

public class TianyiPaySDK
{
	public static void initiateSDK(Activity activity,TianyiLoginCallback tianyiLoginCallback)
	{
		TianyiPay.tianyiPay = new TianyiPay(activity, tianyiLoginCallback);
	}

	public static void loginByActivity()
	{
		TianyiPay.tianyiPay.loginByActivity();
	}
	
	public static void registerByActivity()
	{
		TianyiPay.tianyiPay.registerByActivity();
	}
	
	public static boolean autoLogin()
	{
		return TianyiPay.tianyiPay.autoLogin();
	}

	public static void autoLoginWithSelect()
	{
		TianyiPay.tianyiPay.autoLoginWithSelect();
	}
	
	public static void pay(final String amount, final String notify_url, final String extend,
			final String product_name, final String server_name, final String player_name,
			final String cpOrderID, final String remark, TianyiPayCallback tianyiPayCallback)
	{
		TianyiPay.tianyiPay.pay(amount, notify_url, extend, product_name, server_name, player_name, cpOrderID, remark, tianyiPayCallback);
	}
	
	public static void startUsercenter()
	{
		TianyiPay.tianyiPay.startUsercenter();
	}
}