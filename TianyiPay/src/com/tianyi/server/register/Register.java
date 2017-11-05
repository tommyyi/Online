package com.tianyi.server.register;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.tianyi.server.Constant;
import com.tianyi.server.Response;
import com.tianyi.server.TianyiPay;
import com.tianyi.tool.data.DES;
import com.tianyi.tool.data.Tool;
import com.tianyi.tool.net.MyHttpClient;
import com.tianyi.userdb.UserDatabaseAdapater;
import com.tianyi.userdb.UserInfo;

public class Register
{
	public static boolean register(Context context, String username, String password)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("username", username);
		parameterMap.put("password", password);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String body = MyHttpClient.httpGet(Constant.registerURL, parameterMap);
		
		Response response = new Response(body);
		
		try
		{
			if(response.code!=null&&response.code.equals("1"))
			{
				JSONObject jsonObject_register = new JSONObject(response.result);
				String name = jsonObject_register.getString(RegisterResult.NAME);
				
				UserInfo userInfo = new UserInfo();
				userInfo.setUsername(name);
				userInfo.setPassword(password);
				
				new UserDatabaseAdapater(context).insertRow(userInfo);
			}
			else
				return false;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean autoRegister(Context context)
	{
		String username = Tool.getCurrentTimeString();
		String password = "88888888";
		
		return register(context, username, password);
		/*Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("username", username);
		parameterMap.put("password", password);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String body = MyHttpClient.httpGet(Constant.registerURL, parameterMap);
		
		Response response = new Response(body);
		
		try
		{
			if(response.code!=null&&response.code.equals("1"))
			{
				JSONObject jsonObject_register = new JSONObject(response.result);
				
				String name = jsonObject_register.getString(RegisterResult.NAME);
				String uid = jsonObject_register.getString(RegisterResult.UID);
				
				Tool.save2sharedpreference(context, Constant.username_fieldname, name);
				Tool.save2sharedpreference(context, Constant.password_fieldname, password);
				Tool.save2sharedpreference(context, Constant.uid_fieldname, uid);
			}
			else
				return false;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;*/
	}
}
