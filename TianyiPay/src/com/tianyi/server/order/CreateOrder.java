package com.tianyi.server.order;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.Toast;

import com.tianyi.server.Constant;
import com.tianyi.server.Response;
import com.tianyi.tool.data.DES;
import com.tianyi.tool.data.Tool;
import com.tianyi.tool.net.MyHttpClient;
import com.tianyi.userdb.UserDatabaseAdapater;

public class CreateOrder
{
	public static CreateOrderResult createOrder(Context context, String amount, String notify_url, String extend, String product_name, String server_name, String player_name, String cpOrderID, String remark, String payment_type)
	{
		try
		{
			CreateOrderResult createOrderResult = null;
			
			Map<String, String> parameterMap = new HashMap<String, String>();
			parameterMap.put("amount", amount);
			parameterMap.put("notify_url", notify_url);
			parameterMap.put("extend", extend);
			parameterMap.put("product_name", product_name);
			parameterMap.put("server_name", server_name);
			parameterMap.put("player_name", player_name);
			parameterMap.put("code", cpOrderID);
			parameterMap.put("remark", remark);
			parameterMap.put("payment_type", payment_type);
			
			String identification = Tool.getIdentification(context);
			if(identification==null)
				return null;
			
			parameterMap.put("s", DES.encryptDES(identification));
			String access_token=new UserDatabaseAdapater(context).getUserWhichIsLastestLogin().getAccesstoken();
			parameterMap.put("access_token", access_token);
			String body = MyHttpClient.httpGet(Constant.createOrderURL, parameterMap);
			
			Response response = new Response(body);
			
			if(response.code!=null&&response.code.equals("1"))
			{
				createOrderResult = new CreateOrderResult(response.result);
				if(createOrderResult.orderid==null)
					return null;
				else
					return createOrderResult;
			}
			else
				return null;
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(context, "无网络连接或未登陆，支付失败", Toast.LENGTH_LONG).show();
		}
		return null;
	}
}
