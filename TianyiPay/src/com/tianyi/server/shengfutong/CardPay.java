package com.tianyi.server.shengfutong;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.tianyi.server.Constant;
import com.tianyi.server.Response;
import com.tianyi.tool.data.DES;
import com.tianyi.tool.data.Tool;
import com.tianyi.tool.net.MyHttpClient;
import com.tianyi.userdb.UserDatabaseAdapater;

public class CardPay
{
	public static boolean pay(Context context, String order_code, String card_code, String card_password, String card_type)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("order_code", order_code);
		parameterMap.put("card_code", card_code);
		parameterMap.put("card_password", card_password);
		parameterMap.put("payment_type", "shengpay");
		parameterMap.put("card_type", card_type);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String access_token=new UserDatabaseAdapater(context).getUserWhichIsLastestLogin().getAccesstoken();
		parameterMap.put("access_token", access_token);
		String body = MyHttpClient.httpGet(Constant.cardPayURL, parameterMap);
		
		Response response = new Response(body);
		
		if(response.code!=null&&response.code.equals("1"))
		{
			return true;
		}
		else
			return false;
	}
	
	public static boolean pay_then_you_need_check(Context context, String order_code, String card_code, String card_password, String card_type)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("order_code", order_code);
		parameterMap.put("card_code", card_code);
		parameterMap.put("card_password", card_password);
		parameterMap.put("payment_type", "shengpay");
		parameterMap.put("card_type", card_type);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String access_token=new UserDatabaseAdapater(context).getUserWhichIsLastestLogin().getAccesstoken();
		parameterMap.put("access_token", access_token);
		String body = MyHttpClient.httpGet(Constant.cardPayURL, parameterMap);
		
		Response response = new Response(body);
		
		if(response.code!=null&&response.code.equals("1"))
		{
			return true;
		}
		else
			return false;
	}
}
