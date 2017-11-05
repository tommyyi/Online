package com.tianyi.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyi.server.changepassword.ChangePassword;
import com.tianyi.tool.data.Tool;
import com.tianyi.userdb.UserDatabaseAdapater;

public class TianyiChangePasswordActivity extends Activity
{
	private ImageView	goback;
	private EditText tianyi_password_old;
	private EditText tianyi_password_new;
	private Button cancel;
	private Button confirm;
	private TextView tianyi_login_forget_password;
	private Handler	handler;
	private LoadingDialog	loadingDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(UIResource.getLayoutResIDByName(getApplicationContext(),"tianyi_changepassword"));
		setOnclickGoback();
		
		tianyi_password_old = (EditText)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_password_old"));
		tianyi_password_new = (EditText)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_password_new"));
		confirm = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"confirm"));
		cancel = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"cancel"));
		tianyi_login_forget_password = (TextView)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_login_forget_password"));
		
		setOnClick();
		

		handler = new Handler();
		loadingDialog = new LoadingDialog(TianyiChangePasswordActivity.this,	UIResource.getStyleResIDByName(getApplicationContext(),"tianyi_loading_dialog"),handler);
	}
	
	private void setOnClick()
	{
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				TianyiChangePasswordActivity.this.finish();
			}
		});
		
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				final String username = new UserDatabaseAdapater(TianyiChangePasswordActivity.this).
						getUserWhichIsLastestLogin().getUsername();
				if(username==null)
					return;
				
				final String old_password = tianyi_password_old.getText().toString();
				final String new_password = tianyi_password_new.getText().toString();
				loadingDialog.show(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_lschangingpassword")));
				new Thread(){
					@Override
					public void run()
					{
						if(ChangePassword.changePassword(getApplicationContext(), username, old_password, new_password))
						{
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiChangePasswordActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_changepassword_ok")));
							TianyiChangePasswordActivity.this.finish();
						}
						else 
						{
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiChangePasswordActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_changepassword_fail")));
						}
						super.run();
					}
				}.start();
			}
		});
		
		tianyi_login_forget_password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				String boundPhoneNumber = new UserDatabaseAdapater(TianyiChangePasswordActivity.this).getUserWhichIsLastestLogin().getBindedPhoneNumber();
				if(boundPhoneNumber==null||boundPhoneNumber.equals(""))
				{
					Tool.showToast(handler, TianyiChangePasswordActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_cannotbind_tip")));
					return;
				}
				
				Intent intent = new Intent(TianyiChangePasswordActivity.this,
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
