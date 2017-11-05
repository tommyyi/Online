package com.tianyi.uppay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityProxy extends Activity
{
	public static ActivityProxyCallback	activityProxyCallback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (activityProxyCallback != null)
		{
			activityProxyCallback.onActivityStart(this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (activityProxyCallback != null)
		{
			activityProxyCallback.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onDestroy()
	{
		if (activityProxyCallback != null
				&& activityProxyCallback instanceof ActivityProxyCallback2)
		{
			((ActivityProxyCallback2) activityProxyCallback).onActivityDestroy();
		}
		super.onDestroy();
	}

}
