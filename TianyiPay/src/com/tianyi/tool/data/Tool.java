package com.tianyi.tool.data;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tianyi.server.Constant;
import com.tianyi.server.TianyiPay;
import com.tianyi.server.login.Login;
import com.tianyi.server.register.Register;
import com.tianyi.ui.LoadingDialog;
import com.tianyi.ui.UIResource;
import com.tianyi.userdb.UserDatabaseAdapater;

public class Tool
{
	public static String buildIdentification(Context context)
	{
		JSONObject s_jsonObject = new JSONObject();

		try
		{
			s_jsonObject.put("imei", DeviceInfo.getTelImei(context));
			s_jsonObject.put("imsi", DeviceInfo.getTelImsi(context));
			s_jsonObject.put("os", "android");
			s_jsonObject.put("net_type", NetworkUtils.getConnectionType(context));
			s_jsonObject.put("appid", Tool.getStringMetaData(context, "TianyiAppId"));
			s_jsonObject.put("channelid", Tool.getStringMetaData(context, "TianyiChannelId"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return s_jsonObject.toString();
	}

	public static String getFromSharedpreference(Context context, String key)
	{
		/*defaultValue = DES.encryptDES(defaultValue);*/
		String value = context.getSharedPreferences(Constant.SharedpreferenceFilename, Context.MODE_PRIVATE).getString(key,	null);
		if(value==null)
			return null;
		else
			return DES.decryptDES(value);
	}

	public static void save2sharedpreference(Context context, String key, String value)
	{
		if (Tool.isEmpty(key) || Tool.isEmpty(value))
			return;
	
		value = DES.encryptDES(value);
	
		context.getSharedPreferences(Constant.SharedpreferenceFilename, Context.MODE_PRIVATE)
				.edit().putString(key, value).commit();
	}

	public static String getIdentification(Context context)
	{
		String identification = Tool.getFromSharedpreference(context, Constant.identification_fieldname);
		if(identification==null)
		{
			identification = Tool.buildIdentification(context);
			if(identification==null)
				return null;
			else
			{
				Tool.save2sharedpreference(context, Constant.identification_fieldname, identification);
				return identification;
			}
		}
		else
			return identification;
	}

	/**
	 * Determines whether the String is null or of length 0.
	 * 
	 * @param string
	 *            the string to check
	 * @return true if its null or length of 0, false otherwise.
	 */
	public static boolean isEmpty(final String string) {
		return string == null || string.length() == 0;
	}

	public static String getNumberStr(String originalStr)
	{
		originalStr=originalStr.trim();
		String str="";
		if(originalStr != null && !"".equals(originalStr))
		for(int i=0;i<originalStr.length();i++)
		{
			if(originalStr.charAt(i)>=48 && originalStr.charAt(i)<=57)
				str+=originalStr.charAt(i);
		}
		return str;
	}

	public static void sleep(int i)
	{
		try
		{
			Thread.sleep(i);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getStringMetaData(Context context, String key)
	{
		Object r = null;
		try
		{
			PackageManager manager = context.getPackageManager();
			ApplicationInfo info = manager.getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			if (info != null)
			{
				r = info.metaData.get(key);
			}
		}
		catch (Exception e)
		{
		}
		return r == null ? "" : r.toString();
	}

	public static String getCurrentTimeString()
	{
		Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);

        int month=cal.get(Calendar.MONTH);

        int day=cal.get(Calendar.DATE);

        int hour=cal.get(Calendar.HOUR);

        int minute=cal.get(Calendar.MINUTE);  

        int second=cal.get(Calendar.SECOND);
        
        int minisecond=cal.get(Calendar.MILLISECOND);

        return "t"+year%10+month+day+hour+minute+second+minisecond;
	}
	
	public static void showToast(Handler handler, final Activity activity, String message)
	{
		LayoutInflater inflater = activity.getLayoutInflater();
		final View layout = inflater.inflate(UIResource.getLayoutResIDByName(activity,"tianyi_toast"), null);
		TextView text = (TextView) layout.findViewById(UIResource.getIdResIDByName(activity,"tianyi_toast"));
		text.setText(message);
		handler.post(new Runnable() {

			@Override
			public void run()
			{
				Toast toast = new Toast(activity);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(layout);
				toast.show();
			}
		});
	}
	
	public static void showToastLONG(Handler handler, final Activity activity, String message)
	{
		LayoutInflater inflater = activity.getLayoutInflater();
		final View layout = inflater.inflate(UIResource.getLayoutResIDByName(activity,"tianyi_toast"), null);
		TextView text = (TextView) layout.findViewById(UIResource.getIdResIDByName(activity,"tianyi_toast"));
		text.setText(message);
		handler.post(new Runnable() {

			@Override
			public void run()
			{
				Toast toast = new Toast(activity);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
			}
		});
	}
	
	public static boolean autoregister(LoadingDialog loadingDialog, Activity activity)
	{
		loadingDialog.show(activity.getString(UIResource.getStringResIDByName(activity, "tianyi_registering")));
		if(Register.autoRegister(activity)==true)
		{
			loadingDialog.dismiss();
			//Tool.showToast(loadingDialog.getHandler(), activity, activity.getString(UIResource.getStringResIDByName(activity, "tianyi_register_fail")));
			return true;
		}
		else
		{
			loadingDialog.dismiss();
			Tool.showToast(loadingDialog.getHandler(), activity, activity.getString(UIResource.getStringResIDByName(activity, "tianyi_register_fail")));
			return false;
		}
	}

	public static boolean doLogin(LoadingDialog loadingDialog,Activity activity)
	{
		loadingDialog.show(activity.getString(UIResource.getStringResIDByName(activity, "tianyi_logining")));
		if(Login.login(TianyiPay.tianyiPay, activity)==true)
		{
			String accessToken=new UserDatabaseAdapater(activity).getUserWhichIsLastestLogin().getAccesstoken();
			TianyiPay.tianyiPay.islogin=true;
			TianyiPay.tianyiPay.getTianyiLoginCallback().loginSuccess(accessToken);
			loadingDialog.dismiss();
			showToast(loadingDialog.getHandler(), activity, activity.getString(UIResource.getStringResIDByName(activity, "tianyi_login_success")));
			return true;
		}
		else
		{
			TianyiPay.tianyiPay.islogin=false;
			TianyiPay.tianyiPay.getTianyiLoginCallback().loginFail(null);
			loadingDialog.dismiss();
			showToast(loadingDialog.getHandler(), activity, activity.getString(UIResource.getStringResIDByName(activity, "tianyi_login_fail")));
			return false;
		}
	}
}
