package com.tianyidemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.tianyi.floatview.FloatView;
import com.tianyi.server.TianyiLoginCallback;
import com.tianyi.server.TianyiPay;
import com.tianyi.server.TianyiPayCallback;
import com.tianyi.server.TianyiPaySDK;
import com.tianyi.tool.data.Tool;
import com.tianyi.ui.UIResource;

public class MainActivity extends Activity
{
	private EditText amount_EditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(UIResource.getLayoutResIDByName(getApplicationContext(),"tianyipaylib_main"));
		Button tianyi_auto_register_btn = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_auto_register_btn"));
		setOnclick_tianyi_auto_register_btn(tianyi_auto_register_btn);
		Button tianyi_notice_bind_phone = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_notice_bind_phone"));
		setOnclick_tianyi_notice_bind_phone(tianyi_notice_bind_phone);
		Button tianyi_usercenter_landscape = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_usercenter_landscape"));
		setOnclick_tianyi_usercenter_landscape(tianyi_usercenter_landscape);
		Button tianyi_usercenter_portrait = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_usercenter_portrait"));
		setOnclick_tianyi_usercenter_portrait(tianyi_usercenter_portrait);
		Button tianyi_paytest = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_paytest"));
		setOnclick_tianyi_paytest(tianyi_paytest);
		
		TianyiLoginCallback tianyiLoginCallback = getTianyiLoginCallback();
		TianyiPaySDK.initiateSDK(this, tianyiLoginCallback);
		
		FloatView.show(this);
		
		login();
		
		amount_EditText = (EditText)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"amount"));
	}

	private TianyiLoginCallback getTianyiLoginCallback()
	{
		TianyiLoginCallback tianyiLoginCallback = new TianyiLoginCallback() {
			
			@Override
			public void loginSuccess(String token)
			{
				Log.i("login", "success");
			}
			
			@Override
			public void loginFail(String token)
			{
				Log.i("login", "failed");
			}
		};
		return tianyiLoginCallback;
	}

	private TianyiPayCallback getTianyiPayCallback()
	{
		TianyiPayCallback tianyiPayCallback = new TianyiPayCallback() {
			
			@Override
			public void handSuccess(String orderid)
			{
				Tool.showToast(TianyiPay.tianyiPay.getHandler(), MainActivity.this, "通知cp-支付成功");
			}
			
			@Override
			public void handFail(String orderid)
			{
				Tool.showToast(TianyiPay.tianyiPay.getHandler(), MainActivity.this, "通知cp-支付失败");
			}
		};
		return tianyiPayCallback;
	}

	private void setOnclick_tianyi_paytest(Button tianyi_paytest)
	{
		tianyi_paytest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				String amount = /*"0.01"*/amount_EditText.getText().toString();
				String notify_url = "http://www.baidu.com";//error
				String extend = "extend";
				String product_name = "30元宝";
				String server_name = "server1";
				String player_name = "soldier";
				String cpOrderID = "000000009";
				String remark = "remark";
				TianyiPayCallback tianyiPayCallback = getTianyiPayCallback();
				TianyiPaySDK.pay(amount,notify_url,extend,product_name,server_name,player_name,cpOrderID,remark,tianyiPayCallback);
			}
		});
	}

	private void setOnclick_tianyi_usercenter_portrait(
			Button tianyi_usercenter_portrait)
	{
		tianyi_usercenter_portrait.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				TianyiPaySDK.startUsercenter();
			}
		});
	}

	private void setOnclick_tianyi_usercenter_landscape(
			Button tianyi_usercenter_landscape)
	{
		
	}

	private void setOnclick_tianyi_notice_bind_phone(
			Button tianyi_notice_bind_phone)
	{
		
	}

	private void setOnclick_tianyi_auto_register_btn(Button tianyi_auto_register_btn)
	{
		tianyi_auto_register_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				login();
			}
		});
	}

	public static void login()
	{
		new Thread(){
			@Override
			public void run()
			{
//				TianyiPaySDK.autoLoginWithSelect();
             TianyiPaySDK.autoLogin();
//				TianyiPaySDK.loginByActivity();
				super.run();
			}
		}.start();
	}

	@Override
	protected void onPause()
	{
		FloatView.close();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		FloatView.show(this);
		super.onResume();
	}
}