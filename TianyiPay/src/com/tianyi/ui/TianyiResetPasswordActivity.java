package com.tianyi.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tianyi.server.resetkey.ResetKey;
import com.tianyi.server.resetpassword.ResetPassword;
import com.tianyi.tool.UsernameSelectDialog;
import com.tianyi.tool.data.Tool;
import com.tianyi.userdb.UserDatabaseAdapater;
import com.tianyi.userdb.UserInfo;

public class TianyiResetPasswordActivity extends Activity
{
	private ImageView	goback;
	private EditText	 tianyi_reset_username_editext;
	private EditText	 tianyi_reset_code_edit;
	private EditText	 tianyi_reset_new_password;
	private Button tianyi_reset_getcode_button;
	private Button tianyi_reset_password_button;
	private Handler	handler;
	private LoadingDialog loadingDialog;
	private boolean isPermit2count = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(UIResource.getLayoutResIDByName(getApplicationContext(),"tianyi_reset_password"));
		setOnclickGoback();
		
		handler = new Handler();
		loadingDialog = new LoadingDialog(TianyiResetPasswordActivity.this,	UIResource.getStyleResIDByName(getApplicationContext(),"tianyi_loading_dialog"),handler);
		
		tianyi_reset_username_editext = (EditText)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_username_edit"));
		tianyi_reset_code_edit = (EditText)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_bind_code_edit"));
		tianyi_reset_new_password = (EditText)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_reset_new_password"));
		
		tianyi_reset_getcode_button = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_reset_getcode_button"));
		setOnclick_tianyi_reset_getcode_button();
		tianyi_reset_password_button = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_reset_password_button"));
		setOnclick_tianyi_reset_password_button();
		setonclickusername();
	}
	
	private void setonclickusername()
	{
		tianyi_reset_username_editext.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0)
			{
				List<UserInfo> plisttemp = new UserDatabaseAdapater(TianyiResetPasswordActivity.this).getUserList();
				List<UserInfo> plist = new ArrayList<UserInfo>();
				
				if(plisttemp==null||plisttemp.size()==0)
				{
					Tool.showToast(new Handler(), TianyiResetPasswordActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_dialog_tips")));
					return false;
				}
				
				int size = plisttemp.size();
				for(int index=0;index<size;index++)
				{
					if(!plisttemp.get(index).getBindedPhoneNumber().equals(""))
						plist.add(plisttemp.get(index));
				}
				
				if(plist==null||plist.size()==0)
				{
					Tool.showToast(new Handler(), TianyiResetPasswordActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_dialog_tips")));
					return false;
				}
				
				UsernameSelectDialog dialog =	new UsernameSelectDialog(TianyiResetPasswordActivity.this,UIResource.getStyleResIDByName(TianyiResetPasswordActivity.this, "tianyi_dialog"),tianyi_reset_username_editext,null,plist);
				dialog.show();
				return false;
			}
		});
	}
	
	private void setOnclick_tianyi_reset_password_button()
	{
		tianyi_reset_password_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				final String username = tianyi_reset_username_editext.getText().toString();
				final String resetKey = tianyi_reset_code_edit.getText().toString();
				final String newpassword = tianyi_reset_new_password.getText().toString();
				new Thread(){
					@Override
					public void run()
					{
						isPermit2count = false;
						handler.post(new Runnable() {
							@Override
							public void run()
							{
								tianyi_reset_getcode_button.setClickable(true);
								tianyi_reset_getcode_button.setText(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_number_gain_code")));
							}
						});
						
						loadingDialog.show(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_isresettingpassword")));
						if(ResetPassword.resetPassword(getApplicationContext(), username, newpassword, resetKey))
						{
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiResetPasswordActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_reset_ok")));
							TianyiResetPasswordActivity.this.finish();
						}
						else
						{
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiResetPasswordActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_reset_fail")));
						}
						
						super.run();
					}
				}.start();
			}
		});
	}

	private void setOnclick_tianyi_reset_getcode_button()
	{
		tianyi_reset_getcode_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				tianyi_reset_getcode_button.setClickable(false);
				final String username = tianyi_reset_username_editext.getText().toString();
				loadingDialog.show(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_isgettingcode")));
				new Thread(){
					private int	count = 60;
					@Override
					public void run()
					{
						if(ResetKey.requestResetkeyForUnloginStatus(TianyiResetPasswordActivity.this, username))
						{
							loadingDialog.dismiss();
							isPermit2count = true;
							do
							{
								if(count==0||!isPermit2count)
									break;

								handler.post(new Runnable() {
									@Override
									public void run()
									{
										tianyi_reset_getcode_button.setText(count +"");
									}
								});
								count--;
								Tool.sleep(1000);
							} while (true);
							
							handler.post(new Runnable() {
								@Override
								public void run()
								{
									tianyi_reset_getcode_button.setClickable(true);
									tianyi_reset_getcode_button.setText(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_number_gain_code")));
								}
							});
						}
						else
						{
							handler.post(new Runnable() {
								@Override
								public void run()
								{
									tianyi_reset_getcode_button.setClickable(true);
								}
							});
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiResetPasswordActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_getcode_fail")));
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