package com.tianyi.floatview;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tianyi.ui.UIResource;

public class CustomerActivity extends Activity
{
	private View	goback;
	private CustomerActivity activity;
	private TextView	emailAddress;
	private TextView	phonenum;
	private TextView	qq;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(UIResource.getLayoutResIDByName(getApplicationContext(), "tianyi_ccs"));
		
		activity = this;
		setOnclickGoback();
		
		emailAddress = (TextView)findViewById(UIResource.getIdResIDByName(getApplicationContext(), "emailaddress"));
		phonenum = (TextView)findViewById(UIResource.getIdResIDByName(getApplicationContext(), "phonenum"));
		qq = (TextView)findViewById(UIResource.getIdResIDByName(getApplicationContext(), "customer_qq"));
		
		LinearLayout customer_row_email = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(), "customer_row_email"));
		customer_row_email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				sendEmail(activity,emailAddress.getText().toString().replace(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_email")), ""));
			}
		});
		
		LinearLayout customer_row_phone = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(), "customer_row_phone"));
		customer_row_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				call(activity,phonenum.getText().toString().replace(getString(UIResource.getStringResIDByName(getApplicationContext(), "tianyi_phone")), ""));
			}
		});
		LinearLayout customer_row_qq = (LinearLayout)findViewById(UIResource.getIdResIDByName(getApplicationContext(), "customer_row_qq"));
		customer_row_qq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0)
			{
				qq(activity);
			}
		});
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	public static void sendEmail(Activity activity, String emailAddress)
	{
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setData(Uri.parse("mailto:"+emailAddress)); 
		
		try
		{
			activity.startActivity(Intent.createChooser(intent, activity.getString(UIResource.getStringResIDByName(activity, "tianyi_emailsending"))));
		}
		catch (Exception e)
		{
			final Dialog alertDialog = new AlertDialog.Builder(activity). 
	                setTitle(activity.getString(UIResource.getStringResIDByName(activity, "tianyi_emailsendingexception"))). 
	                setMessage(activity.getString(UIResource.getStringResIDByName(activity, "tianyi_emailsendingexception_noapp"))).setPositiveButton(activity.getString(UIResource.getStringResIDByName(activity, "tianyi_customercenter_confirm")), new DialogInterface.OnClickListener() { 
	                     
	                    @Override 
	                    public void onClick(DialogInterface dialog, int which) { 
	                    } 
	                }).create(); 
	        alertDialog.show(); 
			return;
		}
	}
	
	public static void call(Activity activity, String num)
	{
        try
		{
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
			activity.startActivity(intent);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void qq(Activity activity)
	{
		try
		{
			PackageManager packageManager = activity.getPackageManager();
			String packageName = getPkgName(activity);
			if(packageName==null)
			{
				final Dialog alertDialog = new AlertDialog.Builder(activity). 
		                setTitle(activity.getString(UIResource.getStringResIDByName(activity, "tianyi_qqexception"))). 
		                setMessage(activity.getString(UIResource.getStringResIDByName(activity, "tianyi_qqexception_noapp"))).setPositiveButton(activity.getString(UIResource.getStringResIDByName(activity, "tianyi_customercenter_confirm")), new DialogInterface.OnClickListener() { 
		                     
		                    @Override 
		                    public void onClick(DialogInterface dialog, int which) { 
		                    } 
		                }).create(); 
		        alertDialog.show(); 
				return;
			}
			else
			{
				Intent intent = new Intent();
				intent = packageManager.getLaunchIntentForPackage(packageName);
				activity.startActivity(intent); 
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getPkgName(Activity activity)
	{
		List<PackageInfo> packs = activity.getPackageManager().getInstalledPackages(0);
		if(packs != null)  
        {  
            String tempName = null;  
            for(int i = 0; i < packs.size(); i++)  
            {  
                tempName = packs.get(i).packageName;  
                if(tempName != null && tempName.contains("com.tencent")&&
                		tempName.endsWith("qq"))  
                {  
                	return tempName;
                }  
            }  
        }  
		return null;
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