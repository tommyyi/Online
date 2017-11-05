package com.tianyi.server;

public interface TianyiPayCallback
{
	public void handSuccess(String orderid);
	public void handFail(String orderid);
}
