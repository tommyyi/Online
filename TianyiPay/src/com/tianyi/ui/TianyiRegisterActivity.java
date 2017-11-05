package com.tianyi.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tianyi.server.TianyiPay;
import com.tianyi.server.login.Login;
import com.tianyi.server.register.Register;
import com.tianyi.tool.data.Tool;
import com.tianyi.userdb.UserDatabaseAdapater;

public class TianyiRegisterActivity extends Activity
{
	private ImageView	goback;
	private Button	 register;
	private Button	 tianyi_register_by_one_second_btn;
	private AutoCompleteTextView	username_textview;
	private EditText	password_textview;
	private LoadingDialog loadingDialog;
	private Handler	handler;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(UIResource.getLayoutResIDByName(getApplicationContext(),"tianyi_register"));
		setOnclickGoback();
		
		register = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"register"));
		tianyi_register_by_one_second_btn = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_register_by_one_second_btn"));
		username_textview = (AutoCompleteTextView)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"username"));
		password_textview = (EditText)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"password"));
		
		setOnclick();
		handler = new Handler();
		loadingDialog = new LoadingDialog(TianyiRegisterActivity.this,	UIResource.getStyleResIDByName(getApplicationContext(),"tianyi_loading_dialog"),handler);
	
		String username = Tool.getCurrentTimeString();
		username_textview.setText(username);
	}
	
	private void setOnclick()
	{
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				final String username = username_textview.getText().toString();
				final String password = password_textview.getText().toString();
				
				loadingDialog.show(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_registering")));
				new Thread(){
					@Override
					public void run()
					{
						if(Register.register(getApplicationContext(), username, password))
						{
							loadingDialog.dismiss();
							loadingDialog.show(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_logining")));
							if(Login.login(TianyiPay.tianyiPay, getApplicationContext(), username, password))
							{
								String accesstoken=new UserDatabaseAdapater(TianyiRegisterActivity.this).
										getUserWhichIsLastestLogin().getAccesstoken();
								TianyiPay.tianyiPay.islogin=true;
								TianyiPay.tianyiPay.getTianyiLoginCallback().loginSuccess(accesstoken );
								loadingDialog.dismiss();
								Tool.showToast(handler, TianyiRegisterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_login_success")));
								TianyiRegisterActivity.this.finish();
							}
							else
							{
								TianyiPay.tianyiPay.islogin=false;
								TianyiPay.tianyiPay.getTianyiLoginCallback().loginFail(null);
								loadingDialog.dismiss();
								Tool.showToast(handler, TianyiRegisterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_login_fail")));
							}
						}
						else
						{
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiRegisterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_register_fail")));
						}
						super.run();
					}
				}.start();
			}
		});
		
		tianyi_register_by_one_second_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				new Thread(){
					@Override
					public void run()
					{
						if(Tool.autoregister(loadingDialog,TianyiRegisterActivity.this)==true)
						{
							if(Tool.doLogin(loadingDialog, TianyiRegisterActivity.this))
							{
								TianyiRegisterActivity.this.finish();
							}
						}
						else
						{
							Tool.showToast(handler, TianyiRegisterActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_autoregister_fail")));
						}
						super.run();
					}
				}.start();
			}
		});
	}

	private void setOnclickGoback()
	{
		goback = (ImageView)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"goback"));
		goback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
}
