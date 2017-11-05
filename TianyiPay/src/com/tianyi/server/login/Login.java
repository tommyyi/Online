package com.tianyi.server.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.tianyi.server.Constant;
import com.tianyi.server.Response;
import com.tianyi.server.TianyiPay;
import com.tianyi.tool.data.DES;
import com.tianyi.tool.data.Tool;
import com.tianyi.tool.net.MyHttpClient;
import com.tianyi.userdb.UserDatabaseAdapater;
import com.tianyi.userdb.UserDatabaseParameter;
import com.tianyi.userdb.UserInfo;

public class Login
{
	public static boolean login(TianyiPay tianyiPay, Context context)
	{
		UserInfo userInfo=new UserDatabaseAdapater(context).getUserWhichIsLastestLogin();
		if(userInfo==null)
		{
			List<UserInfo> userList = new UserDatabaseAdapater(context).getUserList();
			if(userList==null||userList.size()==0)
				return false;
			else
			{
				String username =userList.get(0).getUsername();
				String  password = userList.get(0).getPassword();
				return login(tianyiPay, context, username, password);
			}
		}
		else 
		{
			String username=userInfo.getUsername();
			String password=userInfo.getPassword();
			return login(tianyiPay, context, username, password);
		}
		
		
		/*Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("username", username);
		parameterMap.put("password", password);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String body = MyHttpClient.httpGet(Constant.loginURL, parameterMap);
		
		Response response = new Response(body);
		
		try
		{
			if(response.code!=null&&response.code.equals("1"))
			{
				JSONObject jsonObject_register = new JSONObject(response.result);
				String access_token = jsonObject_register.getString(LoginResult.ACCESS_TOKEN);
				
				Tool.save2sharedpreference(context, Constant.access_token, access_token);
				tianyiPay.setAccessToken(access_token);
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
	
	public static boolean login(TianyiPay tianyiPay, Context context, String username, String password)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("username", username);
		parameterMap.put("password", password);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String body = MyHttpClient.httpGet(Constant.loginURL, parameterMap);
		
		Response response = new Response(body);
		
		try
		{
			if(response.code!=null&&response.code.equals("1"))
			{
				JSONObject jsonObject_register = new JSONObject(response.result);
				String access_token = jsonObject_register.getString(LoginResult.ACCESS_TOKEN);
				String bindedphone = jsonObject_register.getString(LoginResult.PHONE);
				if(bindedphone==null||bindedphone.equals("null"))
					bindedphone="";
				
				UserInfo userInfo2=new UserDatabaseAdapater(context).getUserWhichIsLastestLogin();
				if(userInfo2!=null)
				{
					String tablename = UserDatabaseParameter.TABLE_USER;
					String colname = UserDatabaseParameter.COLUMN_IS_LATEST_LOGIN_ACCOUNT;
					String colvalue="0";
					new UserDatabaseAdapater(context).updateUserByUsername(tablename , colname , colvalue, userInfo2.getUsername());
				}
				
				UserInfo userInfo1 = new UserDatabaseAdapater(context).getUserByusername(username);
				if(userInfo1==null)
				{
					userInfo1 = new UserInfo();
					userInfo1.setUsername(username);
					userInfo1.setPassword(password);
					userInfo1.setAccesstoken(access_token);
					userInfo1.setIslatestlogin("1");
					new UserDatabaseAdapater(context).insertRow(userInfo1);
				}
				else 
				{
					String tablename = UserDatabaseParameter.TABLE_USER;
					String colname=UserDatabaseParameter.COLUMN_IS_LATEST_LOGIN_ACCOUNT;
					String colvalue = "1";
					new UserDatabaseAdapater(context).updateUserByUsername(tablename , colname, colvalue , username);
					
					colname=UserDatabaseParameter.COLUMN_ACCESSTOKEN;
					colvalue=access_token;
					new UserDatabaseAdapater(context).updateUserByUsername(tablename , colname, colvalue , username);
					
					colname=UserDatabaseParameter.COLUMN_BINDEDNUM;
					colvalue=bindedphone;
					new UserDatabaseAdapater(context).updateUserByUsername(tablename , colname, colvalue , username);
				}
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
}
