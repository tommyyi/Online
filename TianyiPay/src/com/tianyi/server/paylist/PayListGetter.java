package com.tianyi.server.paylist;

import java.util.HashMap;
import java.util.List;
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

public class PayListGetter
{
	public static List<PayRecord> getPaylist(Context context, String offset)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("offset", offset);
		parameterMap.put("limit", "10");
		parameterMap.put("status", "1");
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return null;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String access_token=new UserDatabaseAdapater(context).getUserWhichIsLastestLogin().getAccesstoken();
		parameterMap.put("access_token", access_token);
		String body = MyHttpClient.httpGet(Constant.payListURL, parameterMap);
		
		Response response = new Response(body);
		
		PayRecordList payRecordList = null;
		if(response.code!=null&&response.code.equals("1"))
		{
			try
			{
				JSONObject jsonObject_result = new JSONObject(response.result);
				payRecordList = new PayRecordList(jsonObject_result.getString("lists"));
				return payRecordList.payRecordList;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		else
			return null;
	}	
}
