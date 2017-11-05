package com.tianyi.tool;

import com.tianyi.server.TianyiLoginCallback;
import com.tianyi.server.TianyiPayCallback;

public class CallbackGetter
{
	public static TianyiLoginCallback getTianyiLoginCallback()
	{
		TianyiLoginCallback tianyiLoginCallback = new TianyiLoginCallback() {
			@Override
			public void loginSuccess(String token)
			{
			}
			
			@Override
			public void loginFail(String token)
			{
			}
		};
		return tianyiLoginCallback;
	}

	public static TianyiPayCallback getTianyiPayCallback()
	{
		TianyiPayCallback tianyiPayCallback = new TianyiPayCallback() {
			@Override
			public void handSuccess(String orderid)
			{
			}
			
			@Override
			public void handFail(String orderid)
			{
			}
		};
		return tianyiPayCallback;
	}
}