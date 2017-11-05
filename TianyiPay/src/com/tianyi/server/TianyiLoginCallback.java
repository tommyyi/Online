package com.tianyi.server;

public interface TianyiLoginCallback
{
	public void loginSuccess(String token);
	public void loginFail(String token);
}
