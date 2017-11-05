package com.tianyi.server.resetkey;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.tianyi.server.Constant;
import com.tianyi.server.Response;
import com.tianyi.server.login.LoginResult;
import com.tianyi.tool.data.DES;
import com.tianyi.tool.data.Tool;
import com.tianyi.tool.net.MyHttpClient;
import com.tianyi.userdb.UserDatabaseAdapater;

public class ResetKey
{
	public static String getResetkey(Context context, String phoneNumber)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("value", phoneNumber);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return null;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String access_token=new UserDatabaseAdapater(context).getUserWhichIsLastestLogin().getAccesstoken();
		parameterMap.put("access_token", access_token);
		String body = MyHttpClient.httpGet(Constant.resetkeyURL, parameterMap);
		
		Response response = new Response(body);
		
		if(response.code!=null&&response.code.equals("1"))
		{
			try
			{
				JSONObject jsonObject_register = new JSONObject(response.result);
				boolean send = jsonObject_register.getBoolean(ResetKeyResult.SEND);
				String content = jsonObject_register.getString(ResetKeyResult.CONTENT);
				return content;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return null;
		}
		else
			return null;
	}
	
	public static boolean requestResetkeyForLoginStatus(Context context, String phoneNumber)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("type", "PHONE");
		parameterMap.put("value", phoneNumber);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String access_token=new UserDatabaseAdapater(context).getUserWhichIsLastestLogin().getAccesstoken();
		parameterMap.put("access_token", access_token);
		String body = MyHttpClient.httpGet(Constant.resetkeyURL, parameterMap);
		
		Response response = new Response(body);
		
		if(response.code!=null&&response.code.equals("1"))
		{
			return true;
		}
		else
			return false;
	}
	
	public static boolean requestResetkeyForUnloginStatus(Context context, String username)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("type", "USERNAME");
		parameterMap.put("value", username);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		parameterMap.put("access_token", ""/*Tool.getFromSharedpreference(context,Constant.access_token)*/);
		String body = MyHttpClient.httpGet(Constant.resetkeyURL, parameterMap);
		
		Response response = new Response(body);
		
		if(response.code!=null&&response.code.equals("1"))
		{
			return true;
		}
		else
			return false;
	}
}
