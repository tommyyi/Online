package com.tianyi.server.resetpassword;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.tianyi.server.Constant;
import com.tianyi.server.Response;
import com.tianyi.server.resetkey.ResetKey;
import com.tianyi.tool.data.DES;
import com.tianyi.tool.data.Tool;
import com.tianyi.tool.net.MyHttpClient;
import com.tianyi.userdb.UserDatabaseAdapater;
import com.tianyi.userdb.UserDatabaseParameter;
import com.tianyi.userdb.UserInfo;

public class ResetPassword
{
	public static boolean resetPassword(Context context, String phoneNumber, String new_password)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("value", phoneNumber);
		String resetKey = ResetKey.getResetkey(context, phoneNumber);
		if(resetKey==null)
			return false;
		resetKey = Tool.getNumberStr(resetKey);
		parameterMap.put("type", "PHONE");
		parameterMap.put("reset_key", resetKey);
		parameterMap.put("new_password", new_password);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String access_token = new UserDatabaseAdapater(context).getUserWhichIsLastestLogin().getAccesstoken();
		parameterMap.put("access_token", access_token );
		String body = MyHttpClient.httpGet(Constant.resetPasswordURL, parameterMap);
		
		Response response = new Response(body);
		
		if(response.code!=null&&response.code.equals("1"))
		{
			UserInfo userInfo=new UserDatabaseAdapater(context).getUserWhichIsLastestLogin();
			String username = userInfo.getUsername();
			String tablename = UserDatabaseParameter.TABLE_USER;
			String colname = UserDatabaseParameter.COLUMN_PASSWORD;
			String colvalue = new_password;
			new UserDatabaseAdapater(context).updateUserByUsername(tablename , colname , colvalue , username );
			return true;
		}
		else
			return false;
	}
	
	public static boolean resetPassword(Context context, String username, String new_password, String resetKey)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("type", "USERNAME");
		parameterMap.put("value", username);
		parameterMap.put("reset_key", resetKey);
		parameterMap.put("new_password", new_password);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		parameterMap.put("access_token", ""/*Tool.getFromSharedpreference(context,Constant.access_token)*/);
		String body = MyHttpClient.httpGet(Constant.resetPasswordURL, parameterMap);
		
		Response response = new Response(body);
		
		if(response.code!=null&&response.code.equals("1"))
		{
			String tablename = UserDatabaseParameter.TABLE_USER;
			String colname = UserDatabaseParameter.COLUMN_PASSWORD;
			String colvalue = new_password;
			new UserDatabaseAdapater(context).updateUserByUsername(tablename , colname , colvalue , username );
			return true;
		}
		else
			return false;
	}
}