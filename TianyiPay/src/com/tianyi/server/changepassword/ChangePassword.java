package com.tianyi.server.changepassword;

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

public class ChangePassword
{
	public static boolean changePassword(Context context, String username, String old_password, String new_password)
	{
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("username", username);
		parameterMap.put("old_password", old_password);
		parameterMap.put("new_password", new_password);
		
		String identification = Tool.getIdentification(context);
		if(identification==null)
			return false;
		
		parameterMap.put("s", DES.encryptDES(identification));
		String access_token =new UserDatabaseAdapater(context).getUserWhichIsLastestLogin().getAccesstoken();
		parameterMap.put("access_token", access_token );
		String body = MyHttpClient.httpGet(Constant.changePasswordURL, parameterMap);
		
		Response response = new Response(body);
		
		if(response.code!=null&&response.code.equals("1"))
		{
			String tablename = UserDatabaseParameter.TABLE_USER;
			String colname = UserDatabaseParameter.COLUMN_PASSWORD;
			String colvalue = new_password;
			new UserDatabaseAdapater(context).updateUserByUsername(tablename , colname , colvalue , username);
			return true;
		}
		else
			return false;
	}
}
