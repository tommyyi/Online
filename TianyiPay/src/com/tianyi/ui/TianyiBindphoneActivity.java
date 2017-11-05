package com.tianyi.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tianyi.server.bindphone.BindPhone;
import com.tianyi.server.resetkey.ResetKey;
import com.tianyi.tool.data.Tool;

public class TianyiBindphoneActivity extends Activity
{
	private ImageView	goback;
	private EditText	 tianyi_bind_phone_number_editext;
	private EditText	 tianyi_bind_code_edit;
	private Button tianyi_bind_getcode_button;
	private Button tianyi_bind_phone_button;
	private Handler	handler;
	private LoadingDialog loadingDialog;
	private boolean isPermit2count = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(UIResource.getLayoutResIDByName(getApplicationContext(),"tianyi_bind_phone"));
		
		setOnclickGoback();
		handler = new Handler();
		loadingDialog = new LoadingDialog(TianyiBindphoneActivity.this,	UIResource.getStyleResIDByName(getApplicationContext(),"tianyi_loading_dialog"),handler);
		
		tianyi_bind_phone_number_editext = (EditText)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_bind_phone_number_editext"));
		tianyi_bind_code_edit = (EditText)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_bind_code_edit"));
		
		tianyi_bind_getcode_button = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_reset_getcode_button"));
		setOnclick_tianyi_bind_getcode_button();
		tianyi_bind_phone_button = (Button)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"tianyi_bind_phone_button"));
		setOnclick_tianyi_bind_phone_button();
	}
	
	private void setOnclick_tianyi_bind_phone_button()
	{
		tianyi_bind_phone_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				final String resetKey = tianyi_bind_code_edit.getText().toString();
				final String phoneNumber = tianyi_bind_phone_number_editext.getText().toString();
				new Thread(){
					@Override
					public void run()
					{
						isPermit2count = false;
						handler.post(new Runnable() {
							@Override
							public void run()
							{
								tianyi_bind_getcode_button.setClickable(true);
								tianyi_bind_getcode_button.setText(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_number_gain_code")));
							}
						});
						
						loadingDialog.show(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_isbindingphone")));
						if(BindPhone.requestPhone(TianyiBindphoneActivity.this, phoneNumber, resetKey))
						{
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiBindphoneActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_isbindingok")));
							TianyiBindphoneActivity.this.finish();
						}
						else
						{
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiBindphoneActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_isbinding_notok")));
						}
						
						super.run();
					}
				}.start();
			}
		});
	}

	private void setOnclick_tianyi_bind_getcode_button()
	{
		tianyi_bind_getcode_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				tianyi_bind_getcode_button.setClickable(false);
				final String phoneNumber = tianyi_bind_phone_number_editext.getText().toString();
				loadingDialog.show(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_isgettingcode")));
				new Thread(){
					private int	count = 60;
					@Override
					public void run()
					{
						if(ResetKey.requestResetkeyForLoginStatus(TianyiBindphoneActivity.this, phoneNumber))
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
										tianyi_bind_getcode_button.setText(count +"");
									}
								});
								count--;
								Tool.sleep(1000);
							} while (true);
							
							handler.post(new Runnable() {
								@Override
								public void run()
								{
									tianyi_bind_getcode_button.setClickable(true);
									tianyi_bind_getcode_button.setText(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_number_gain_code")));
								}
							});
						}
						else
						{
							handler.post(new Runnable() {
								@Override
								public void run()
								{
									tianyi_bind_getcode_button.setClickable(true);
								}
							});
							loadingDialog.dismiss();
							Tool.showToast(handler, TianyiBindphoneActivity.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_bind_phone_getcode_fail")));
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
