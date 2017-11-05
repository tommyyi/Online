package com.tianyi.server;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.tianyi.tool.data.Tool;
import com.tianyi.ui.AutomaticLoginningDialog;
import com.tianyi.ui.LoadingDialog;
import com.tianyi.ui.TianyiLoginActivity;
import com.tianyi.ui.TianyiPaymentCenterActivity;
import com.tianyi.ui.TianyiRegisterActivity;
import com.tianyi.ui.TianyiUserCenter;
import com.tianyi.ui.UIResource;
import com.tianyi.userdb.UserDatabaseAdapater;
import com.tianyi.userdb.UserInfo;

public class TianyiPay
{
	private Handler handler;
	public boolean islogin;
	public Handler getHandler()
	{
		return handler;
	}

	private TianyiPayCallback	tianyiPayCallback;
	private TianyiLoginCallback	tianyiLoginCallback;

	public static TianyiPay	tianyiPay;

	private Activity activity;
	private LoadingDialog	loadingDialog;
	private AutomaticLoginningDialog	automaticLoginningDialog;
	
	public TianyiPay(Activity activity,TianyiLoginCallback tianyiLoginCallback)
	{
		super();
		tianyiPay = this;
		
		this.handler = new Handler();
		this.tianyiLoginCallback = tianyiLoginCallback;
		
		this.activity = activity;
		this.loadingDialog = new LoadingDialog(activity,	UIResource.getStyleResIDByName(activity,"tianyi_loading_dialog"), handler);
		this.automaticLoginningDialog = new AutomaticLoginningDialog(activity,	UIResource.getStyleResIDByName(activity,"tianyi_loading_dialog"), handler);
	}
	
	public TianyiLoginCallback getTianyiLoginCallback()
	{
		return tianyiLoginCallback;
	}
	
	public TianyiPayCallback getTianyiPayCallback()
	{
		return tianyiPayCallback;
	}

	public void loginByActivity()
	{
		handler.post(new Runnable() {
			@Override
			public void run()
			{
				Intent intent = new Intent(activity,TianyiLoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				activity.startActivity(intent);
			}
		});
	}
	
	public void registerByActivity()
	{
		handler.post(new Runnable() {
			@Override
			public void run()
			{
				Intent intent = new Intent(activity,TianyiRegisterActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				activity.startActivity(intent);
			}
		});
	}
	
	public boolean autoLogin()
	{
		UserInfo userInfo = new UserDatabaseAdapater(activity).getUserWhichIsLastestLogin();
		if(userInfo==null)
		{
			if(Tool.autoregister(loadingDialog,activity)==true)
			{
				return Tool.doLogin(loadingDialog, activity);
			}
			else
			{
				return false;
			}
		}
		else
		{
			return Tool.doLogin(loadingDialog, activity);
		}
	}
	
	public void autoLoginWithSelect()
	{
		final UserInfo userInfo = new UserDatabaseAdapater(activity).getUserWhichIsLastestLogin();
		if(userInfo==null)
		{
			loginByActivity();
		}
		else
		{
			handler.post(new Runnable() {
				@Override
				public void run()
				{
					String username = userInfo.getUsername();
					String message = String.format(automaticLoginningDialog.getMessage(),username);
					automaticLoginningDialog.show(message);
				}
			});
		}
	}
	
	public void pay(final String amount, final String notify_url, final String extend,
			final String product_name, final String server_name, final String player_name,
			final String cpOrderID, final String remark, TianyiPayCallback tianyiPayCallback)
	{
		TianyiPay.tianyiPay.tianyiPayCallback = tianyiPayCallback;
		handler.post(new Runnable() {
			@Override
			public void run()
			{
				Intent intent = new Intent(activity,
						TianyiPaymentCenterActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("amount", amount);
				intent.putExtra("notify_url", notify_url);
				intent.putExtra("extend", extend);
				intent.putExtra("product_name", product_name);
				intent.putExtra("server_name", server_name);
				intent.putExtra("player_name", player_name);
				intent.putExtra("cpOrderID", cpOrderID);
				intent.putExtra("remark", remark);
				activity.startActivity(intent);
			}
		});
	}

	public void startUsercenter()
	{
		handler.post(new Runnable() {
			
			@Override
			public void run()
			{
				Intent intent = new Intent(activity,TianyiUserCenter.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				activity.startActivity(intent);
			}
		});
	}
}