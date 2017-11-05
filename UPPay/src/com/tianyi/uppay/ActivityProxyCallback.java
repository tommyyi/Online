package com.tianyi.uppay;

import android.app.Activity;
import android.content.Intent;

public interface ActivityProxyCallback
{
	void onActivityStart(Activity activity);

	void onActivityResult(int requestCode, int resultCode, Intent data);
}
