package com.tianyi.server.bindphone;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.tianyi.server.Constant;
import com.tianyi.server.Response;
import com.tianyi.tool.data.DES;
import com.tianyi.tool.data.Tool;
import com.tianyi.tool.net.MyHttpClient;
import com.tianyi.userdb.UserDatabaseAdapater;
import com.tianyi.userdb.UserDatabaseParameter;
import com.tianyi.userdb.UserInfo;

public class BindPhone
{
	/*public static boolean bindPhone(Context context, String phoneNumber)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("value", phoneNumber);
		String resetKey = ResetKey.getResetkey(context, phoneNumber);
		if(resetKey==null)
			return false;
		resetKey = Tool.getNumberStr(resetKey);
		parameterMap.put("resetkey", resetKey);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		parameterMap.put("access_token", Tool.getFromSharedpreference(context,Constant.access_token));
		String body = MyHttpClient.httpGet(Constant.bindPhoneURL, parameterMap);
		
		Response response = new Response(body);
		
		if(response.code!=null&&response.code.equals("1"))
		{
			String username = Tool.getFromSharedpreference(context, Constant.username_fieldname);
			String password = Tool.getFromSharedpreference(context, Constant.password_fieldname);
		}
		else
			return false;
	}*/
	
	public static boolean requestPhone(Context context, String phoneNumber, String resetKey)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("value", phoneNumber);
		parameterMap.put("resetkey", resetKey);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String accessToken = new UserDatabaseAdapater(context).getUserWhichIsLastestLogin().getAccesstoken();
		parameterMap.put("access_token", accessToken);
		String body = MyHttpClient.httpGet(Constant.bindPhoneURL, parameterMap);
		
		Response response = new Response(body);
		
		if(response.code!=null&&response.code.equals("1"))
		{
			UserInfo userInfo = new UserDatabaseAdapater(context).getUserWhichIsLastestLogin();
			new UserDatabaseAdapater(context).updateUserByUsername(
					UserDatabaseParameter.TABLE_USER, 
					UserDatabaseParameter.COLUMN_BINDEDNUM, phoneNumber, userInfo.getUsername());
			return true;
		}
		else
			return false;
	}
}
