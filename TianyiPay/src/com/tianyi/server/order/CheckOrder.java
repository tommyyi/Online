package com.tianyi.server.order;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.tianyi.server.Constant;
import com.tianyi.server.Response;
import com.tianyi.tool.data.DES;
import com.tianyi.tool.data.Tool;
import com.tianyi.tool.net.MyHttpClient;
import com.tianyi.userdb.UserDatabaseAdapater;

public class CheckOrder
{
	public class CheckStatus
	{
		public static final int SUCCESS = 0;
		public static final int FAIL = 1;
		public static final int NOTCONFIRM = 2;		
	}
	
	private static final int TIMES=3;
	
	public static int isOrderPayOK(Context context, String orderid)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("code", orderid);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return CheckStatus.NOTCONFIRM;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String access_token=new UserDatabaseAdapater(context).getUserWhichIsLastestLogin().getAccesstoken();
		parameterMap.put("access_token", access_token);
		
		int count = 0;
		do
		{
			String body = MyHttpClient.httpGet(Constant.checkOrderURL,	parameterMap);
			if(body==null)
			{
				Tool.sleep(1000+count*2000);
				count++;
				if(count==TIMES)
					return CheckStatus.NOTCONFIRM;
				else
					continue;
			}
			
			Response response = new Response(body);
			if (response.code != null)
			{
				if(response.code.equals("1"))
				{
					try
					{
						JSONObject resultJsonObject = new JSONObject(response.result);
						String payment_status = resultJsonObject.getString("payment_status");
						if(payment_status!=null&&payment_status.equals("1"))
							return CheckStatus.SUCCESS;
						else {
							Tool.sleep(1000+count*2000);
							count++;
							if(count==TIMES)
								return CheckStatus.NOTCONFIRM;
							else
								continue;
						}
					}
					catch (JSONException e)
					{
						e.printStackTrace();
						Tool.sleep(1000+count*2000);
						count++;
						if(count==TIMES)
							return CheckStatus.NOTCONFIRM;
						else
							continue;
					}
				}
				/*else if(response.code.equals("0"))
					return CheckStatus.FAIL;*/
				else
				{
					Tool.sleep(1000+count*2000);
					count++;
					if(count==TIMES)
						return CheckStatus.NOTCONFIRM;
					else
						continue;
				}
			}
			else
			{
				Tool.sleep(1000+count*2000);
				count++;
				if(count==TIMES)
					return CheckStatus.NOTCONFIRM;
				else
					continue;
			}
		} while (true);
	}
}