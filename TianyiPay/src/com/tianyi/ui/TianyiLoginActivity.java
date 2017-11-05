package com.tianyi.ui;

import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.tianyi.server.TianyiPay;
import com.tianyi.server.login.Login;
import com.tianyi.tool.UsernameSelectDialog;
import com.tianyi.tool.data.Tool;
import com.tianyi.userdb.UserDatabaseAdapater;
import com.tianyi.userdb.UserInfo;

public class TianyiLoginActivity extends Activity
{
	private View	goback;
	private Button	 login;
	private Button	 go2registerActivityButton;
	private Button	 selectButton;
	private AutoCompleteTextView	username_textview;
	private EditText	password_textview;
	private LoadingDialog loadingDialog;
	private TextView	tianyi_login_forget_password;
	private Handler	handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(UIResource.getLayoutResIDByName(getApplicationContext(),"tianyi_login"));
		setOnclickGoback();
		
		login = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"login"));
		go2registerActivityButton = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_go_to_register_btn"));
		
		selectButton = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"selectaccount"));
		username_textview = (AutoCompleteTextView)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"username"));
		password_textview = (EditText)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"password"));
		
		setonclickSelectusername();
		tianyi_login_forget_password = (TextView)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_login_forget_password"));
		
		setOnclick();
		handler = new Handler();
		loadingDialog = new LoadingDialog(TianyiLoginActivity.this,	UIResource.getStyleResIDByName(getApplicationContext(),"tianyi_loading_dialog"),handler);
	}
	
	private void setonclickSelectusername()
	{
		List<UserInfo> userinfolist=new UserDatabaseAdapater(getApplicationContext()).getUserList();
		if(userinfolist==null||userinfolist.size()==0)
		{
			selectButton.setVisibility(View.GONE);
			return;
		}
		
		UserInfo userInfo=new UserDatabaseAdapater(getApplicationContext()).getUserWhichIsLastestLogin();
		if(userInfo!=null)
		{
			username_textview.setText(userInfo.getUsername());
			password_textview.setText(userInfo.getPassword());
		}
		
		selectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				List<UserInfo> plist = new UserDatabaseAdapater(TianyiLoginActivity.this).getUserList();
				if(plist==null||plist.size()==0)
				{
					Tool.showToast(new Handler(), TianyiLoginActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_dialog_tips")));
					return ;
				}
				
				UsernameSelectDialog dialog =	new UsernameSelectDialog(TianyiLoginActivity.this,UIResource.getStyleResIDByName(TianyiLoginActivity.this, "tianyi_dialog"),username_textview,password_textview,plist);
				dialog.show();
			}
		});
	}

	private void setOnclick()
	{
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				final String username = username_textview.getText().toString();
				final String password = password_textview.getText().toString();
				
				loadingDialog.show(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_logining")));
				new Thread(){
					@Override
					public void run()
					{
						if(Login.login(TianyiPay.tianyiPay, getApplicationContext(),username,password))
						{
							String accesstoken=new UserDatabaseAdapater(TianyiLoginActivity.this).getUserWhichIsLastestLogin().getAccesstoken();
							TianyiPay.tianyiPay.islogin=true;
							TianyiPay.tianyiPay.getTianyiLoginCallback().loginSuccess(accesstoken);
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiLoginActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_login_success")));
							TianyiLoginActivity.this.finish();
						}
						else
						{
							TianyiPay.tianyiPay.islogin=false;
							TianyiPay.tianyiPay.getTianyiLoginCallback().loginFail(null);
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiLoginActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_login_fail")));
						}
						super.run();
					}
				}.start();
			}
		});
		
		go2registerActivityButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(TianyiLoginActivity.this,
						TianyiRegisterActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
		});
		
		tianyi_login_forget_password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				/*UserInfo userInfo=new UserDatabaseAdapater(TianyiLoginActivity.this).getUserWhichIsLastestLogin();
				if(userInfo==null)
				{
					Tool.showToast(handler, TianyiLoginActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_cannotbind_tip")));
					return;
				}
				String boundPhoneNumber = userInfo.getBindedPhoneNumber();
				if(boundPhoneNumber==null||boundPhoneNumber.equals(""))
				{
					Tool.showToast(handler, TianyiLoginActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_cannotbind_tip")));
					return;
				}*/
				
				Intent intent = new Intent(TianyiLoginActivity.this,
						TianyiResetPasswordActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
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
