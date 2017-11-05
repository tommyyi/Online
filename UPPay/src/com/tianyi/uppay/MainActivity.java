package com.tianyi.uppay;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		TianyiUppayCallback tianyiUppayCallback = new TianyiUppayCallback() {
			
			@Override
			public void handSuccess(String orderid)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void handFail(String orderid)
			{
				// TODO Auto-generated method stub
				
			}
		};
		String orderid = "2392049204";
		UPpayPlugin myPlugin = new UPpayPlugin(MainActivity.this, tianyiUppayCallback);
		myPlugin.setOrderid(orderid);
		String tn ="201403311425090089102";
		
		myPlugin.exec(tn);
	}
}