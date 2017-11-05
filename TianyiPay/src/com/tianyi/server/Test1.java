package com.tianyi.server;

import android.app.Activity;

import com.tianyi.tool.CallbackGetter;

public class Test1
{
	public static void initiate(Activity activity)
	{
		TianyiLoginCallback tianyiLoginCallback = CallbackGetter.getTianyiLoginCallback();
		new TianyiPay(activity, tianyiLoginCallback);
	}
	
	public static void pay(int index)
	{
		String amount = Test1.getamount(index);
		String notify_url = "";
		String extend = "extend";
		String product_name = Test1.getProductname(index);
		String server_name = "server_name";
		String player_name = "player_name";
		String cpOrderID = "cpOrderID";
		String remark = "remark";
		TianyiPayCallback tianyiPayCallback = CallbackGetter.getTianyiPayCallback();
		TianyiPay.tianyiPay.pay(amount, notify_url, extend, product_name, server_name, player_name, cpOrderID, remark, tianyiPayCallback);
	}
	
	public static String getProductname(int index)
	{
		if(index==10)
			return "10个金币";
		if(index==25)
			return "25个金币";
		if(index==60)
			return "60个金币";
		if(index==100)
			return "100个金币";
		return null;
	}
	
	public static String getamount(int index)
	{
		if(index==10)
			return "2.0";
		if(index==25)
			return "4.0";
		if(index==60)
			return "8.0";
		if(index==100)
			return "10.0";
		return null;
	}
	
	public static void autoLogin()
	{
		new Thread(){
			@Override
			public void run()
			{
				TianyiPay.tianyiPay.autoLogin();
				super.run();
			}
		}.start();
	}
	
	public static void testpay(int index)
	{
		pay(index);
	}
	
	public static void testautologin()
	{
		autoLogin();
	}
	
	public static void testinitiate(Activity activity)
	{
		initiate(activity);
	}
}