package com.tianyi.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianyi.server.Constant;
import com.tianyi.server.TianyiPay;
import com.tianyi.tool.data.Tool;
import com.tianyi.userdb.UserDatabaseAdapater;

public class TianyiUserCenter extends Activity
{
	private Handler	handler;
	private ImageView	goback;
	private TextView	payment_center_username;
	private TextView	payment_center_bindedphone;
	private LinearLayout	payment_center_registernew;
	private LinearLayout	payment_center_relogin;
	private LinearLayout	payment_center_bindphone;
	private LinearLayout	payment_center_changepassword;
	private LinearLayout	payment_center_resetpassword;
	private LinearLayout	payment_center_history;
	private LinearLayout	payment_center_unbindphone;
	private LinearLayout	line_for_reset;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(UIResource.getLayoutResIDByName(getApplicationContext(),"tianyi_user_center"));
		
		handler = new Handler();
		
		setOnclickGoback();

		payment_center_username = (TextView)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"payment_center_username"));
		payment_center_bindedphone = (TextView)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"payment_center_bindedphone"));

		payment_center_registernew = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"payment_center_registernew"));
		setOnclick_payment_center_registernew();
		payment_center_relogin = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"payment_center_relogin"));
		setOnclick_payment_center_relogin();
		payment_center_bindphone = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"payment_center_bindphone"));
		setOnclick_payment_center_bindphone();
		payment_center_unbindphone = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"payment_center_unbindphone"));
		setOnclick_payment_center_unbindphone();
		line_for_reset = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"line_for_reset"));
		payment_center_changepassword = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"payment_center_changepassword"));
		setOnclick_payment_center_changepassword();
		payment_center_resetpassword = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"payment_center_resetpassword"));
		setOnclick_payment_center_resetpassword();
		payment_center_history = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(),"payment_center_history"));
		setOnclick_payment_center_history();
	}
	
	private void setOnclick_payment_center_unbindphone()
	{
		payment_center_unbindphone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				String boundNumber = new UserDatabaseAdapater(TianyiUserCenter.this).getUserWhichIsLastestLogin().getBindedPhoneNumber();
				if(boundNumber==null||boundNumber.equals(""))
				{
					Tool.showToast(handler, TianyiUserCenter.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_cannotbind_tip")));
					return;
				}
				
				Intent intent = new Intent(TianyiUserCenter.this,
						TianyiUnBindphoneActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume()
	{
		if(TianyiPay.tianyiPay.islogin)
		{
			String username1=new UserDatabaseAdapater(TianyiUserCenter.this).getUserWhichIsLastestLogin().getUsername();
			payment_center_username.setText(username1);
			
			String boundPhoneNumber =new UserDatabaseAdapater(TianyiUserCenter.this).getUserWhichIsLastestLogin().getBindedPhoneNumber(); 
			if(boundPhoneNumber==null||boundPhoneNumber.equals(""))
			{
				payment_center_unbindphone.setVisibility(View.GONE);
				payment_center_bindphone.setVisibility(View.VISIBLE);
				//line_for_reset.setVisibility(View.GONE);
				//payment_center_resetpassword.setVisibility(View.GONE);
				payment_center_bindedphone.setText(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_unbind")));
			}
			else 
			{
				payment_center_unbindphone.setVisibility(View.VISIBLE);
				payment_center_bindphone.setVisibility(View.GONE);
				//line_for_reset.setVisibility(View.VISIBLE);
				//payment_center_resetpassword.setVisibility(View.VISIBLE);
				payment_center_bindedphone.setText(boundPhoneNumber);
			}
		}
		else 
		{
			payment_center_username.setText("未登录");
			payment_center_bindedphone.setText("未登录");
			payment_center_unbindphone.setVisibility(View.GONE);
			payment_center_bindphone.setVisibility(View.GONE);
		}
		super.onResume();
	}

	private void setOnclick_payment_center_history()
	{
		payment_center_history.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				if(!TianyiPay.tianyiPay.islogin)
				{
					Tool.showToast(handler, TianyiUserCenter.this, "您未登录");
					return;
				}
				Intent intent = new Intent(TianyiUserCenter.this,
						TianyiPaymentHistoryActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	private void setOnclick_payment_center_resetpassword()
	{
		payment_center_resetpassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0)
			{
				/*String phoneNumber = Tool.getBindedPhoneNumber(TianyiUserCenter.this, Tool.getFromSharedpreference(TianyiUserCenter.this, Constant.username_fieldname));
				if(phoneNumber==null||phoneNumber.equals(""))
				{
					Tool.showToast(handler, TianyiUserCenter.this, getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_cannotbind_tip")));
					return;
				}*/
				Intent intent = new Intent(TianyiUserCenter.this,TianyiResetPasswordActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	private void setOnclick_payment_center_changepassword()
	{
		payment_center_changepassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				if(!TianyiPay.tianyiPay.islogin)
				{
					Tool.showToast(handler, TianyiUserCenter.this, "您未登录");
					return;
				}
				Intent intent = new Intent(TianyiUserCenter.this,
						TianyiChangePasswordActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	private void setOnclick_payment_center_bindphone()
	{
		payment_center_bindphone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(TianyiUserCenter.this,
						TianyiBindphoneActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	private void setOnclick_payment_center_relogin()
	{
		payment_center_relogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(TianyiUserCenter.this,
						TianyiLoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	private void setOnclick_payment_center_registernew()
	{
		payment_center_registernew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(TianyiUserCenter.this,
						TianyiRegisterActivity.class);
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
				TianyiUserCenter.this.finish();
			}
		});
	}
}