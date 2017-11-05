package com.tianyi.uppay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public class UPpayPlugin implements ActivityProxyCallback2, Callback
{

	/**
	 * 支付成功
	 */
	public static final String	SUCCESS			= "9000";
	/**
	 * 需要安装
	 */
	public static final String	INSTALL			= "10000";
	/**
	 * 需要安装，点击了确定
	 */
	public static final String	INSTALL_OK		= "10001";
	/**
	 * 需要安装，点击了取消
	 */
	public static final String	INSTALL_CANCEL	= "10002";
	/**
	 * 进了APK后，不支付了
	 */
	public static final String	CANCEL			= "10003";
	/**
	 * 支付APK出现异常
	 */
	public static final String	FAIL			= "10004";
	/**
	 * 异常退出
	 */
	public static final String	ERROR			= "4444";

	private Activity			activity;
	private Handler				mHandler;
	private Activity			mActivity;
	private TianyiUppayCallback	tianyiUppayCallback;
	private String	orderid;

	public void setOrderid(String orderid)
	{
		this.orderid = orderid;
	}

	public UPpayPlugin(Activity activity, TianyiUppayCallback tianyiUppayCallback)
	{
		this.mActivity = activity;
		this.tianyiUppayCallback = tianyiUppayCallback;
	}

	@Override
	public void onActivityStart(Activity activity)
	{
		this.activity = activity;
		this.mHandler = new Handler(this);
		String tn = activity.getIntent().getStringExtra("tn");
		Message msg = mHandler.obtainMessage();
		msg.obj = tn;
		mHandler.sendMessage(msg);
	}

	public void exec(String tn)
	{
		ActivityProxy.activityProxyCallback = this;
		Intent intent = new Intent(mActivity, ActivityProxy.class);
		intent.putExtra("tn", tn);
		mActivity.startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		System.out.println("==UPPlugin==result_code==" + resultCode);

		// 得到结果后,关闭代理的activity
		finish();
		/*************************************************
		 * 
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 * 
		 ************************************************/
		if (data == null)
		{
			tianyiUppayCallback.handFail(orderid);
			return;
		}

		// String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		// System.out.println("==UPPlugin==str==" + str);
		if (str == null)
		{
			tianyiUppayCallback.handFail(orderid);
			return;
		}
		String code = SUCCESS;
		if (str.equalsIgnoreCase("success"))
		{
			// System.out.println("==UPPlugin==success==" + str);
			// msg = "支付成功！";
			code = SUCCESS;
			tianyiUppayCallback.handSuccess(orderid);
			return;
		}
		else if (str.equalsIgnoreCase("fail"))
		{
			// msg = "支付失败！";
			code = FAIL;
			tianyiUppayCallback.handFail(orderid);
			return;
		}
		else if (str.equalsIgnoreCase("cancel"))
		{
			// msg = "用户取消了支付";
			code = CANCEL;
			tianyiUppayCallback.handFail(orderid);
			return;
		}
		resultCode(code);
		tianyiUppayCallback.handFail(orderid);
	}

	private static final String	LOG_TAG					= "PayDemo";

	// private static final int PLUGIN_VALID = 0;
	private static final int	PLUGIN_NOT_INSTALLED	= -1;
	private static final int	PLUGIN_NEED_UPGRADE		= 2;

	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	private String				mMode					= "00";

	@Override
	public boolean handleMessage(Message msg)
	{
		Log.e(LOG_TAG, " " + "" + msg.obj);

		String tn = "";
		if (msg.obj == null || ((String) msg.obj).length() == 0)
		{
			// 没有得到订单流水号,关闭代理的activity
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle("错误提示");
			builder.setMessage("网络连接失败,请重试!");
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
							finish();
						}
					});
			builder.create().show();
		}
		else
		{
			tn = (String) msg.obj;
			/*************************************************
			 * 
			 * 步骤2：通过银联工具类启动支付插件
			 * 
			 ************************************************/
			// mMode参数解释：
			// 0 - 启动银联正式环境
			// 1 - 连接银联测试环境

			UPPayAssistEx.startPayByJAR(this.activity, PayActivity.class, null, null, tn,mMode);
			
			/*int ret = UPPayAssistEx.startPay(this.activity, null, null, tn,
					mMode);
			if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED)
			{
				// 需要重新安装控件
				Log.e(LOG_TAG, " plugin not found or need upgrade!!!");

				AlertDialog.Builder builder = new AlertDialog.Builder(
						this.activity);
				builder.setTitle("提示");
				builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

				builder.setNegativeButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								dialog.dismiss();
								finish();
								// installUPPayPlugin(activity);
								installUPPayPlugin(mActivity);
							}
						});

				builder.setPositiveButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								dialog.dismiss();
								finish();
							}
						});
				builder.create().show();

			}
			Log.e(LOG_TAG, "" + ret);*/
		}

		return false;
	}

	private void finish()
	{
		Log.i("","==UPpayPlugin==finish "+this.activity.getClass().getName());
		this.activity.finish();
	}

	private int installUPPayPlugin(Context paramContext)
	{
		int m = 0;
		try
		{
			InputStream is = paramContext.getAssets().open("UPPayPluginEx.apk");
			// System.out.println("==UPpayPlugin==is==" + is);
			FileOutputStream fos = paramContext.openFileOutput(
					"UPPayPluginEx.apk", 1);
			// System.out.println("==UPpayPlugin==fos==" + fos);
			byte[] arrayOfByte = new byte[1024];
			int n = 0;
			while ((n = is.read(arrayOfByte)) > 0)
			{
				fos.write(arrayOfByte, 0, n);
			}
			fos.close();
			is.close();
			String path = paramContext.getFilesDir().getAbsolutePath();
			String fullPath = path + File.separator + "UPPayPluginEx.apk";
			if (path != null)
			{
				Intent intent = new Intent("android.intent.action.VIEW")
						.setDataAndType(Uri.parse("file:" + fullPath),
								"application/vnd.android.package-archive");
				paramContext.startActivity(intent);
				m = 1;
			}
		}
		catch (IOException localIOException)
		{
			localIOException.printStackTrace();
		}
		return m;
	}

	@Override
	public void onActivityDestroy()
	{

	}

	void resultCode(String code)
	{
	}

}
